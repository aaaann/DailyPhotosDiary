package com.annevonwolffen.gallery_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.annevonwolffen.di.FeatureProvider
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.BottomsheetAddImageBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.presentation.viewmodels.AddImageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddImageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddImageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddImageViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddImageViewModel(FeatureProvider.getFeature(GalleryInternalApi::class.java).photosInteractor) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomsheetAddImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpItemsClick()
        observerDismissEvents()
    }

    private fun setUpItemsClick() {
        binding.fromCamera.setOnClickListener {
            viewModel.addImage(AddImage.FromCamera)
        }
        binding.fromGallery.setOnClickListener {
            viewModel.addImage(AddImage.FromGallery)
        }
    }

    private fun observerDismissEvents() {
        viewModel.dismissBottomSheetEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { dismiss() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun getTheme(): Int {
        return R.style.ThemeOverlay_BottomSheetDialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    sealed class AddImage {
        object FromCamera : AddImage()
        object FromGallery : AddImage()
    }
}