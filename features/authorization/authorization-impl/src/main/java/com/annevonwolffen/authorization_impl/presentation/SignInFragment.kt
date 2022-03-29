package com.annevonwolffen.authorization_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import com.annevonwolffen.design_system.extensions.hideKeyboard
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.navigation.activityNavController
import com.annevonwolffen.navigation.navigateSafely
import com.annevonwolffen.ui_utils_api.extensions.setVisibility
import com.annevonwolffen.ui_utils_api.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.launch
import com.annevonwolffen.navigation.R as NavigationR

class SignInFragment : Fragment() {

    private lateinit var loadingScreen: FrameLayout

    private val viewModel: AuthorizationViewModel by viewModels {
        ViewModelProviderFactory {
            AuthorizationViewModel(getFeature(AuthorizationApi::class).authInteractor)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthorizationFlows()

        view.findViewById<Button>(R.id.btn_sign_in).setOnClickListener {
            signIn(view)
        }

        view.findViewById<Button>(R.id.btn_no_account).setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
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
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.signIn(email, password)
        } else {
            Toast.makeText(activity, "Не введён e-mail или пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAuth(authState: AuthState) {
        when (authState) {
            is SignedIn -> {
                activityNavController().navigateSafely(NavigationR.id.action_global_mainScreen)
                loadingScreen.setVisibility(false)
                requireActivity().hideKeyboard()
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