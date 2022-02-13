package com.annevonwolffen.authorization_impl

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthorizationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSignedIn()

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
    }

    private fun isSignedIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "Not signed in", Toast.LENGTH_LONG).show() // make ui visible instead of toast
        } else {
            onSignedIn()
        }
    }

    private fun signIn(view: View) {
        val email = view.findViewById<EditText>(R.id.et_email).text.toString()
        val password = view.findViewById<EditText>(R.id.et_password).text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    onSignedIn()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(view: View) {
        val email = view.findViewById<EditText>(R.id.et_email).text.toString()
        val password = view.findViewById<EditText>(R.id.et_password).text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    onSignedIn()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onSignedIn() {
        Log.d(TAG, "Signed in, email - ${auth.currentUser?.email.toString()}, uid - ${auth.currentUser?.uid}")
        findNavController().navigate(AuthorizationFragmentDirections.actionToMainScreen())
        requireActivity().finish()
    }

    private companion object {
        const val TAG = "AuthorizationFragment"
    }
}