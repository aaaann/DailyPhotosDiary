package com.annevonwolffen.gallery_impl.presentation

import android.net.Uri
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
import com.annevonwolffen.gallery_impl.data.remote.firebase.Image
import com.annevonwolffen.gallery_impl.databinding.FragmentGalleryBinding
import com.annevonwolffen.gallery_impl.di.GalleryInternalApi
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.presentation.viewmodels.GalleryViewModel
import com.annevonwolffen.ui_utils_api.UiUtilsApi
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
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
                return GalleryViewModel(getFeature(GalleryInternalApi::class.java).photosInteractor) as T
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
        // viewModel.loadPhotos()
        listenForImages()
        collectPhotos()
    }

    private fun initViews() {
        errorBanner = binding.errorBanner
        addImageButton = binding.btnAddImage
        addImageButton.setOnClickListener {
            findNavController().navigate(
                GalleryFragmentDirections.actionToAddImage(
                    resources.getString(R.string.add_image_title)
                )
            )
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.rvPhotos
        adapter = PhotosListAdapter(getFeature(UiUtilsApi::class.java).imageLoader)
        recyclerView.adapter = adapter
    }

    private fun listenForImages() {
        val dbReference = Firebase.database.reference
            .child("dailyphotosdiary")
            .child(Firebase.auth.currentUser?.uid.orEmpty())
            .child("testfolder")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.children.map { "${it.key}" }}")
                val photos: List<Photo> = dataSnapshot.children
                    .map {
                        val key = it.key
                        val image = it.getValue(Image::class.java)
                        Photo(
                            key.orEmpty(),
                            image?.name.orEmpty(),
                            image?.description.orEmpty(),
                            image?.createdAt.orEmpty(),
                            image?.url.orEmpty()
                        )
                    }
                render(State.Success(photos))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Ошибка при выгрузке картинок из Firebase Database: ", databaseError.toException())
                render(State.Error(databaseError.toException().message))
            }
        })
    }

    private fun collectPhotos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photos.collect { render(it) }
            }
        }
    }

    private fun render(state: State<List<Photo>>) {
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