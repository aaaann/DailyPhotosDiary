package com.annevonwolffen.gallery_impl.presentation

import android.app.Activity
import android.app.DatePickerDialog
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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import com.annevonwolffen.gallery_impl.databinding.FragmentAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddImageFragment : Fragment() {

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddImageViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddImageViewModel(getFeature(GalleryInternalApi::class.java).imagesInteractor) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    private val args: AddImageFragmentArgs by navArgs() // todo: will need it later

    private val imageLoader: ImageLoader by lazy { getFeature(UiUtilsApi::class.java).imageLoader }

    private lateinit var addedImage: ShapeableImageView
    private lateinit var description: EditText
    private lateinit var dateTextView: TextView

    private var selectedCalendar: Calendar = Calendar.getInstance()

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
        description = binding.etImageDescr
        setupDateField()

        val addImageButton = binding.btnImage
        addImageButton.setOnClickListener {
            findNavController().navigate(AddImageFragmentDirections.actionToAddImageBottomSheet())
        }
    }

    private fun setupDateField() {
        val initialCalendar = Calendar.getInstance()
        dateTextView = binding.tvDate
        if (dateTextView.text.isEmpty()) {
            dateTextView.text = getString(R.string.today)
        }
        dateTextView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedCalendar = Calendar.getInstance().also { it.set(year, month, dayOfMonth) }
                    dateTextView.text =
                        if (selectedCalendar.isEqualByDate(initialCalendar)) {
                            getString(R.string.today)
                        } else {
                            selectedCalendar.toString(resources)
                        }
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        selectedCalendar = savedInstanceState?.getSerializable(SELECTED_CALENDAR) as? Calendar
            ?: Calendar.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(SELECTED_CALENDAR, selectedCalendar)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewModel.fileFlow.value?.let {
                viewModel.saveImage(
                    Image(
                        name = it.name,
                        description = description.text.toString(),
                        createdAt = selectedCalendar.timeInMillis,
                        url = FileProvider.getUriForFile(
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

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchFlowCollection(viewModel.fileFlow) {
                    imageLoader.loadImage(addedImage, it)
                }

                launchFlowCollection(viewModel.uploadedImageEvent) {
                    when (it) {
                        is State.Success -> {
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

        private const val SELECTED_CALENDAR = "SELECTED_CALENDAR"
    }
}