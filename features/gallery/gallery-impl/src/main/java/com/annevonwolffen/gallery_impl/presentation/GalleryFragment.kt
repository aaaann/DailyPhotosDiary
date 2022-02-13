package com.annevonwolffen.gallery_impl.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.FragmentGalleryBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.presentation.viewmodels.GalleryViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PhotosListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorBanner: View
    private lateinit var addImageButton: FloatingActionButton

    private val viewModel: GalleryViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GalleryViewModel(getFeature(GalleryInternalApi::class.java).imagesInteractor) as T
            } // TODO: create base ViewModelProviderFactory in some core module
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupRecyclerView()
        collectImages()
    }

    private fun initViews() {
        errorBanner = binding.errorBanner
        addImageButton = binding.btnAddImage
        addImageButton.setOnClickListener { addOrEditImage(null) }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.rvPhotos
        adapter = PhotosListAdapter(getFeature(UiUtilsApi::class.java).imageLoader) { image -> addOrEditImage(image) }
        recyclerView.adapter = adapter
    }

    private fun addOrEditImage(image: Image?) {
        findNavController().navigate(
            GalleryFragmentDirections.actionToAddImage(
                resources.getString(
                    if (image == null) R.string.add_image_title else R.string.edit_image_title
                ),
                image
            )
        )
    }

    private fun collectImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.images.collect { render(it) }
            }
        }
    }

    private fun render(state: State<List<Image>>) {
        Log.d(TAG, "Rendering: $state")
        when (state) {
            is State.Loading -> {
                errorBanner.setVisibility(false)
                recyclerView.setVisibility(false)
                // TODO: set shimmers visibility to true
            }
            is State.Success -> {
                errorBanner.setVisibility(false)
                recyclerView.setVisibility(true)
                adapter.submitList(state.value)
                // TODO: set shimmers visibility to false
            }
            is State.Error -> {
                errorBanner.setVisibility(true)
                recyclerView.setVisibility(false)
                // TODO: set shimmers visibility to false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val TAG = "GalleryFragment"
    }
}