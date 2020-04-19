package com.words.storageapp


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.databinding.FragmentRegistrationBinding
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */


class RegistrationFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRegistrationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        binding.registerBtn.setOnClickListener { view ->
            if (isValidateForms(view)) {
                createAccount(view.regEmail.text.toString(), view.regPass.text.toString())
            }
        }
        return binding.root
    }

    private fun isValidateForms(view: View): Boolean {
        var valid = true

        val email = view.regEmail.text.toString()
        val password = view.regPass.text.toString()

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isValidPassword = password.isNotEmpty() && password.length > 7

        if (!isValidEmail) {
            view.regEmail.error = "Email is not valid"
            valid = false
        } else {
            view.regEmail.error = null
        }
        if (!isValidPassword) {
            view.regPass.error = "Password is too short"
            valid = false
        } else {
            view.regPass.error = null
        }
        return valid
    }

    private fun createAccount(email: String, password: String) {
        /*Clear the cached user details here */
        (activity as Authentication).showProgressBar()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Timber.i("createUserWithEmail:success")
                Toast.makeText(activity, "Account created Successfully", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
                (activity as Authentication).moveToLoginScreen()
            } else {
                // If sign in fails, display a message to the user.
                showSnackBar { "Account Creation failed: The email address is already in Use" }
            }
            (activity as Authentication).hideProgressBar()
        }
    }//end of CreateAccount

    private fun showSnackBar(call: () -> String) {
        Snackbar.make(
            activity!!.findViewById<ConstraintLayout>(R.id.register_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }


}
