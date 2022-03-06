package com.annevonwolffen.authorization_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.authorization_api.domain.AuthState
import com.annevonwolffen.authorization_api.domain.Loading
import com.annevonwolffen.authorization_api.domain.NotSignedIn
import com.annevonwolffen.authorization_api.domain.SignedIn
import com.annevonwolffen.authorization_impl.R
import com.annevonwolffen.coroutine_utils_api.extension.launchFlowCollection
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import kotlinx.coroutines.launch

class AuthorizationFragment : Fragment() {

    private lateinit var loadingScreen: FrameLayout

    private val viewModel: AuthorizationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AuthorizationViewModel(getFeature(AuthorizationApi::class.java).authInteractor) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthorizationFlows()

        view.findViewById<Button>(R.id.btn_sign_in).setOnClickListener {
            signIn(view)
        }

        view.findViewById<TextView>(R.id.tv_no_account).setOnClickListener {
            it.visibility = View.GONE
            view.findViewById<Button>(R.id.btn_sign_in).visibility = View.GONE
            view.findViewById<Button>(R.id.btn_sign_up).visibility = View.VISIBLE
        }

        view.findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
            signUp(view)
        }

        loadingScreen = view.findViewById(R.id.progress_layout)
    }

    private fun observeAuthorizationFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchFlowCollection(viewModel.authFlow) { handleAuth(it) }

                launchFlowCollection(viewModel.authorizationFailure) {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signIn(view: View) {
        val email = view.findViewById<EditText>(R.id.et_email).text.toString()
        val password = view.findViewById<EditText>(R.id.et_password).text.toString()
        viewModel.signIn(email, password)
    }

    private fun signUp(view: View) {
        val email = view.findViewById<EditText>(R.id.et_email).text.toString()
        val password = view.findViewById<EditText>(R.id.et_password).text.toString()
        viewModel.signUp(email, password)
    }

    private fun handleAuth(authState: AuthState) {
        when (authState) {
            is SignedIn -> {
                findNavController().navigate(AuthorizationFragmentDirections.actionToMainScreen())
                requireActivity().finish()
                loadingScreen.setVisibility(false)
            }
            is NotSignedIn -> {
                loadingScreen.setVisibility(false)
            }
            is Loading -> {
                loadingScreen.setVisibility(true)
            }
        }
    }
}