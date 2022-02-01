package com.annevonwolffen.gallery_impl.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.FragmentAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class AddImageFragment : Fragment() {

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddImageViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddImageViewModel(getFeature(GalleryInternalApi::class.java).photosInteractor) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    private val args: AddImageFragmentArgs by navArgs() // todo: will need it later

    private val imageLoader: ImageLoader by lazy { getFeature(UiUtilsApi::class.java).imageLoader }

    private lateinit var addedImage: ShapeableImageView
    private var imageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri?.let { viewModel.setUri(it) }
            // result.data?.data.also { viewModel.setUri(it) }
            // val file = getTempFile()
            // result.data?.data.also { viewModel.setUri(Uri.fromFile(file)) }
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
    }

    private fun initViews() {
        addedImage = binding.ivAddedImage
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uriFlow.collect {
                    imageUri = it
                    imageLoader.loadImage(addedImage, it)
                }
            }
        }

        val addImageButton = binding.btnImage
        addImageButton.setOnClickListener {
            findNavController().navigate(AddImageFragmentDirections.actionToAddImageBottomSheet())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uploadedImageFlow.collect {
                    when (it) {
                        is State.Success -> it.value.takeIf { images -> images.isNotEmpty() }
                            ?.let { findNavController().popBackStack() }
                        is State.Error -> Toast.makeText(
                            activity,
                            "Ошибка при загрузке картинки: ${it.errorMessage}",
                            Toast.LENGTH_LONG
                        ).show()
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
                    kotlin.runCatching { createImageFile() }.onFailure { Log.d(TAG, "Ошибка при создании файла.") }
                        .getOrNull()
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.annevonwolffen.fileprovider",
                        it
                    )
                    imageUri = photoURI
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            imageUri?.let {
                viewModel.saveImage(UploadImage(file = createFileFromUri(it)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createFileFromUri(uri: Uri): File {
        val outputFile = getTempFile()
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

    private fun getTempFile(): File {
        val imageFile = File(context?.externalCacheDir, "temp_file")
        imageFile.parentFile?.mkdirs()
        return imageFile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val TAG = "AddImageFragment"
    }
}