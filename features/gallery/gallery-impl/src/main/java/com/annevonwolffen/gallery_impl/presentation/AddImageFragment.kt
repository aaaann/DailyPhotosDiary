package com.annevonwolffen.gallery_impl.presentation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.annevonwolffen.coroutine_utils_api.extension.launchFlowCollection
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.di.FeatureProvider.getInnerFeature
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.FragmentAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.presentation.models.Image
import com.annevonwolffen.gallery_impl.presentation.models.toDomain
import com.annevonwolffen.gallery_impl.presentation.utils.createFileFromUri
import com.annevonwolffen.gallery_impl.presentation.utils.createImageFile
import com.annevonwolffen.gallery_impl.presentation.utils.getUriForFile
import com.annevonwolffen.gallery_impl.presentation.utils.isEqualByDate
import com.annevonwolffen.gallery_impl.presentation.utils.toCalendar
import com.annevonwolffen.gallery_impl.presentation.utils.toDateString
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.annevonwolffen.mainscreen_api.ToolbarFragment
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.annevonwolffen.ui_utils_api.image.ImageLoader
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import com.annevonwolffen.navigation.R as NavR

class AddImageFragment : Fragment() {

    private var _binding: FragmentAddImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddImageViewModel by navGraphViewModels(NavR.id.gallery_graph) {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddImageViewModel(
                    getInnerFeature(
                        GalleryExternalApi::class,
                        GalleryInternalApi::class
                    ).imagesInteractor
                ) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    private val args: AddImageFragmentArgs by navArgs()
    private val image: Image? by lazy { args.image }

    private val imageLoader: ImageLoader by lazy { getFeature(UiUtilsApi::class).imageLoader }

    private lateinit var addedImage: ShapeableImageView
    private lateinit var description: EditText
    private lateinit var dateTextView: TextView
    private lateinit var progressLoader: FrameLayout
    private lateinit var deleteButton: MaterialButton

    private var selectedCalendar: Calendar = Calendar.getInstance()

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { viewModel.setFile(createFileFromUri(it, requireContext())) }
            viewModel.dismissBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        (parentFragment?.parentFragment as? ToolbarFragment)
            ?.inflateToolbarMenu(R.menu.menu_add_image) { onMenuItemSelected(it) }
    }

    private fun initViews() {
        progressLoader = binding.progressLayout
        addedImage = binding.ivAddedImage
        description = binding.etImageDescr
        image?.let {
            description.setText(it.description)
        }
        viewModel.setFile(null)
        setupDateField()

        val addImageButton = binding.btnImage
        addImageButton.setOnClickListener {
            findNavController().navigate(AddImageFragmentDirections.actionToAddImageBottomSheet())
        }

        setupDeleteButton()
    }

    private fun setupDateField() {
        val todayCalendar = Calendar.getInstance()
        dateTextView = binding.tvDate
        if (dateTextView.text.isEmpty()) {
            dateTextView.text =
                selectedCalendar.takeIf { !it.isEqualByDate(todayCalendar) }?.toDateString(resources)
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
                            selectedCalendar.toDateString(resources)
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

    private fun setupDeleteButton() {
        deleteButton = binding.btnDelete
        deleteButton.setVisibility(image?.id != null)
        image?.let { im -> deleteButton.setOnClickListener { viewModel.deleteImage(im.toDomain()) } }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(SELECTED_CALENDAR, selectedCalendar)
        super.onSaveInstanceState(outState)
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            val file = viewModel.fileFlow.value
            viewModel.saveImage(
                Image(
                    id = image?.id,
                    name = file?.name ?: image?.name.orEmpty(),
                    description = description.text.toString(),
                    date = selectedCalendar.timeInMillis,
                    url = file?.getUriForFile(requireContext())?.toString() ?: image?.url.orEmpty()
                ).toDomain()
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

                launchFlowCollection(viewModel.imageUploadedEvent) {
                    processImageEvent(it, "Ошибка при сохранении изображения")
                }

                launchFlowCollection(viewModel.imageDeletedEvent) {
                    processImageEvent(it, "Ошибка при удалении изображения")
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

    private fun processImageEvent(state: State<Unit>, errorMessage: String) {
        when (state) {
            is State.Success -> {
                findNavController().popBackStack()
                viewModel.setFile(null)
            }
            is State.Error -> {
                Toast.makeText(
                    activity,
                    "$errorMessage: ${state.errorMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    activity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
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