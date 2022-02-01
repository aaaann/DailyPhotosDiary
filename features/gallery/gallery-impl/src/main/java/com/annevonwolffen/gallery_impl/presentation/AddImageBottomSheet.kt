package com.annevonwolffen.gallery_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annevonwolffen.gallery_impl.databinding.BottomsheetAddImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddImageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomsheetAddImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromCamera.setOnClickListener {
            // send event onAddImageFromCamera
            dismiss()
        }
        binding.fromGallery.setOnClickListener {
            // send event onAddImageFromGallery
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}