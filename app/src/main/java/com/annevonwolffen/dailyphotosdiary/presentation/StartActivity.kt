package com.annevonwolffen.dailyphotosdiary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import com.annevonwolffen.dailyphotosdiary.databinding.ActivityStartBinding
import com.annevonwolffen.design_system.extensions.removeNavBarInset

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        removeNavBarInset { _: Int, navigationBarSize: Int ->
            binding.root.updatePadding(bottom = navigationBarSize)
        }
    }
}