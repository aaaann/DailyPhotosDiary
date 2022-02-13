package com.annevonwolffen.gallery_impl.presentation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.annevonwolffen.coroutine_utils_api.extension.launchFlowCollection
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.FragmentAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.presentation.utils.createFileFromUri
import com.annevonwolffen.gallery_impl.presentation.utils.createImageFile
import com.annevonwolffen.gallery_impl.presentation.utils.getUriForFile
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

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

    private val args: AddImageFragmentArgs by navArgs()
    private val image: Image? by lazy { args.image }

    private val imageLoader: ImageLoader by lazy { getFeature(UiUtilsApi::class.java).imageLoader }

    private lateinit var addedImage: ShapeableImageView
    private lateinit var description: EditText
    private lateinit var dateTextView: TextView
    private lateinit var progressLoader: FrameLayout

    private var selectedCalendar: Calendar = Calendar.getInstance()

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { viewModel.setFile(createFileFromUri(it, requireContext())) }
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
        selectedCalendar = savedInstanceState?.getSerializable(SELECTED_CALENDAR) as? Calendar
            ?: image?.date?.toCalendar()
                ?: Calendar.getInstance()
        initViews()
        collectFlows()
    }

    private fun initViews() {
        progressLoader = binding.progressLayout
        addedImage = binding.ivAddedImage
        description = binding.etImageDescr
        image?.let {
            description.setText(it.description)
        }
        setupDateField()

        val addImageButton = binding.btnImage
        addImageButton.setOnClickListener {
            findNavController().navigate(AddImageFragmentDirections.actionToAddImageBottomSheet())
        }
    }

    private fun setupDateField() {
        val todayCalendar = Calendar.getInstance()
        dateTextView = binding.tvDate
        if (dateTextView.text.isEmpty()) {
            dateTextView.text =
                selectedCalendar.takeIf { !it.isEqualByDate(todayCalendar) }?.toString(resources)
                    ?: getString(R.string.today)
        }
        dateTextView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedCalendar = Calendar.getInstance().also { it.set(year, month, dayOfMonth) }
                    dateTextView.text =
                        if (selectedCalendar.isEqualByDate(todayCalendar)) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(SELECTED_CALENDAR, selectedCalendar)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            val file = viewModel.fileFlow.value
            viewModel.saveImage(
                Image(
                    id = image?.id,
                    name = file?.name ?: image?.name.orEmpty(),
                    description = description.text.toString(),
                    date = selectedCalendar.timeInMillis,
                    url = file?.getUriForFile(requireContext())?.toString() ?: image?.url.orEmpty()
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchFlowCollection(viewModel.fileFlow) { file ->
                    file?.let {
                        imageLoader.loadImage(addedImage, file)
                    } ?: imageLoader.loadImage(addedImage, image?.url)
                }

                launchFlowCollection(viewModel.progressLoaderState) { isLoading ->
                    progressLoader.setVisibility(isLoading)
                }

                launchFlowCollection(viewModel.uploadedImageEvent) {
                    when (it) {
                        is State.Success -> {
                            findNavController().popBackStack()
                            viewModel.setFile(null)
                        }
                        is State.Error -> {
                            Toast.makeText(
                                activity,
                                "Ошибка при загрузке картинки: ${it.errorMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
                    kotlin.runCatching { createImageFile(requireContext()) }
                        .onFailure { Log.d(TAG, "Ошибка при создании файла.") }
                        .getOrNull()
                photoFile?.also {
                    val photoURI: Uri = it.getUriForFile(requireContext())
                    viewModel.setFile(it)
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePhotoIntent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val TAG = "AddImageFragment"

        private const val SELECTED_CALENDAR = "SELECTED_CALENDAR"
    }
}