package com.annevonwolffen.gallery_impl.presentation

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.di.FeatureProvider.getInnerFeature
import com.annevonwolffen.gallery_api.di.GalleryExternalApi
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.FragmentGalleryBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.presentation.models.Image
import com.annevonwolffen.gallery_impl.presentation.models.ImagesGroup
import com.annevonwolffen.gallery_impl.presentation.viewmodels.GalleryViewModel
import com.annevonwolffen.mainscreen_api.ToolbarFragment
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.annevonwolffen.ui_utils_api.viewmodel.ViewModelProviderFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val galleryInternalApi: GalleryInternalApi by lazy {
        getInnerFeature(
            GalleryExternalApi::class,
            GalleryInternalApi::class
        )
    }

    private lateinit var adapter: ImagesGroupListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterDataObserver: RecyclerView.AdapterDataObserver
    private lateinit var errorBanner: View
    private lateinit var addImageButton: FloatingActionButton
    private lateinit var shimmerLayout: LinearLayout

    private val viewModel: GalleryViewModel by viewModels {
        ViewModelProviderFactory {
            GalleryViewModel(
                galleryInternalApi.imagesInteractor,
                galleryInternalApi.imagesAggregator,
                galleryInternalApi.settingsInteractor
            )
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

        (parentFragment?.parentFragment as? ToolbarFragment)
            ?.inflateToolbarMenu(R.menu.menu_gallery, { prepareOptionsMenu(it) }, { onMenuItemSelected(it) })
    }

    private fun initViews() {
        errorBanner = binding.errorBanner
        addImageButton = binding.btnAddImage
        addImageButton.setOnClickListener { addOrEditImage(null) }
        shimmerLayout = binding.shimmerLayout.root
    }

    private fun setupRecyclerView() {
        recyclerView = binding.rvPhotos
        adapter =
            ImagesGroupListAdapter(getFeature(UiUtilsApi::class).imageLoader) { image -> addOrEditImage(image) }
        recyclerView.adapter = adapter
        adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                recyclerView.scrollToPosition(positionStart)
            }
        }
        adapter.registerAdapterDataObserver(adapterDataObserver)
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

    private fun render(state: State<List<ImagesGroup>>) {
        Log.d(TAG, "Rendering: $state")
        when (state) {
            is State.Loading -> {
                errorBanner.setVisibility(false)
                recyclerView.setVisibility(false)
                shimmerLayout.setVisibility(true)
                updateAddImageButtonState(false)
            }
            is State.Success -> {
                errorBanner.setVisibility(false)
                recyclerView.setVisibility(true)
                adapter.submitList(state.value)
                shimmerLayout.setVisibility(false)
                updateAddImageButtonState(true)
            }
            is State.Error -> {
                errorBanner.setVisibility(true)
                recyclerView.setVisibility(false)
                shimmerLayout.setVisibility(false)
                updateAddImageButtonState(false)
            }
        }
    }

    private fun updateAddImageButtonState(isEnabled: Boolean) {
        addImageButton.isEnabled = isEnabled
        addImageButton.setBackgroundColor(
            if (isEnabled) {
                com.annevonwolffen.design_system.R.color.color_green_300_dark
            } else {
                R.color.gray_500
            }
        )
    }

    private fun prepareOptionsMenu(menu: Menu) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.initialSortOrder.collect { sortOrder ->
                    sortOrder?.let { it ->
                        val sortItem = menu.findItem(R.id.sort)
                        sortItem.icon = ContextCompat.getDrawable(
                            requireContext(),
                            selectSortIcon(it)
                        )
                    }
                }
            }
        }
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            item.icon = ContextCompat.getDrawable(
                requireContext(),
                selectSortIcon(viewModel.sortOrder)
            )

            val menuIcon = item.icon
            if (menuIcon is Animatable) {
                menuIcon.start()
            }
            viewModel.toggleImagesSort()
        }
        return true
    }

    @DrawableRes
    private fun selectSortIcon(sortOrder: SortOrder): Int {
        return if (sortOrder == SortOrder.BY_DATE_DESCENDING)
            R.drawable.anim_sort_by_date_ascending
        else
            R.drawable.anim_sort_by_date_descending
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        _binding = null
    }

    private companion object {
        const val TAG = "GalleryFragment"
    }
}