package com.annevonwolffen.gallery_impl.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.data.remote.firebase.Image
import com.annevonwolffen.gallery_impl.databinding.FragmentAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class AddImageFragment : Fragment() {

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddImageViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddImageViewModel(getFeature(GalleryInternalApi::class.java).photosInteractor) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    private val args: AddImageFragmentArgs by navArgs() // todo: will need it later

    private val imageLoader: ImageLoader by lazy { getFeature(UiUtilsApi::class.java).imageLoader }

    private lateinit var addedImage: ShapeableImageView

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { viewModel.setFile(createFileFromUri(it)) }
            viewModel.dismissBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        _binding = FragmentAddImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectFlows()
    }

    private fun initViews() {
        addedImage = binding.ivAddedImage

        val addImageButton = binding.btnImage
        addImageButton.setOnClickListener {
            findNavController().navigate(AddImageFragmentDirections.actionToAddImageBottomSheet())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewModel.fileFlow.value?.let {
                // viewModel.saveImage(UploadImage(file = it))
                uploadImageToStorage(
                    Image(
                        "", it.name, "test description", "",
                        FileProvider.getUriForFile(
                            requireContext(),
                            "com.annevonwolffen.fileprovider",
                            it
                        ).toString()
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadImageToStorage(image: Image) {
        // load to db
        val dbReference = Firebase.database.reference
            .child("dailyphotosdiary")
            .child(Firebase.auth.currentUser?.uid.orEmpty())
            .child("testfolder")

        val generatedId = dbReference.push().key.orEmpty()
        dbReference
            .child(generatedId)
            .setValue(image)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Saving to db is successful: $generatedId")
                }
            }

        val storageReference = Firebase.storage.reference
            .child("dailyphotosdiary")
            .child(Firebase.auth.currentUser?.uid.orEmpty())
            .child("testfolder")
            .child(image.name.orEmpty())

        val uploadTask = storageReference.putFile(image.url.orEmpty().toUri())
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.d(TAG, "Ошибка при загрузке картинки на сервер: ${it.message}")
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Ссылка на картинку: ${task.result}")
                // update db entry
                dbReference
                    .child(generatedId)
                    .setValue(image.copy(id = generatedId, url = task.result.toString()))

                findNavController().popBackStack()
                viewModel.setFile(null)
            } else {
                Log.d(TAG, "Ошибка при получении ссылки на картинку: ${task.exception?.message}")
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchFlowCollection(viewModel.fileFlow) {
                    imageLoader.loadImage(addedImage, it)
                }

                launchFlowCollection(viewModel.uploadedImageFlow) {
                    when (it) {
                        is State.Success -> it.value.takeIf { images -> images.isNotEmpty() }
                            ?.let {
                                findNavController().popBackStack()
                                viewModel.setFile(null)
                            }
                        is State.Error -> Toast.makeText(
                            activity,
                            "Ошибка при загрузке картинки: ${it.errorMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                launchFlowCollection(viewModel.addImageEvent) { addImageCommand ->
                    when (addImageCommand) {
                        is AddImageBottomSheet.AddImage.FromCamera -> takePhotoFromCamera()
                        is AddImageBottomSheet.AddImage.FromGallery -> selectImageFromGallery()
                    }
                }
            }
        }
    }

    private fun selectImageFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(pickImageIntent)
    }

    private fun takePhotoFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
            takePhotoIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? =
                    kotlin.runCatching { createImageFile() }
                        .onFailure { Log.d(TAG, "Ошибка при создании файла.") }
                        .getOrNull()
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.annevonwolffen.fileprovider",
                        it
                    )
                    viewModel.setFile(it)
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePhotoIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat.getDateTimeInstance().format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "DailyPhotosDiary_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun createFileFromUri(uri: Uri): File {
        val outputFile = createImageFile()
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
        return outputFile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> CoroutineScope.launchFlowCollection(flow: Flow<T>, action: (T) -> Unit) {
        launch {
            flow.collect { value -> action.invoke(value) }
        }
    }

    private companion object {
        const val TAG = "AddImageFragment"
    }
}