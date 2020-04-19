package com.words.storageapp


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import timber.log.Timber
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), View.OnClickListener {

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
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        //progressBar = binding.loginProgressBar

        binding.login.setOnClickListener(this)
        binding.registerClicker2.setOnClickListener(this)


        return binding.root
    }

    private fun validate(view: View): Boolean {
        var valid = true

        val email = view.lgEmail.text.toString()
        val password = view.lgPass.text.toString()

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isValidPassword = password.isNotEmpty() && password.length > 7

        if (!isValidEmail) {
            view.lgEmail.error = "Email is not valid"
            valid = false
        } else {
            view.lgEmail.error = null
        }
        if (!isValidPassword) {
            view.lgPass.error = "Password is too short"
            valid = false
        } else {
            view.lgPass.error = null
        }
        return valid
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.login -> {

                if (validate(view)) {
                    signInUser(view.lgEmail.text.toString(), view.lgPass.text.toString())
                }
            }
            R.id.registerClicker2 -> {
                (activity as Authentication).moveToRegistrationFragment()
            }
        }
    }

    private fun signInUser(email: String, password: String) {

        (activity as Authentication).showProgressBar()

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.i("signInWithEmail:success")

                    Toast.makeText(activity, "Log In  Successfully", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e("signInWithEmail:failure ${task.exception}")
                    /* Toast.makeText(baseContext, "Authentication failed.",
                         Toast.LENGTH_SHORT).show()*/
                    showSnackBar { "Sign In Failed, Check your Internet Connection: ${task.exception}" }
                }
                (activity as Authentication).hideProgressBar()
                // [END_EXCLUDE]
            }
    }//end of SignIn User

    //if Snack bar does not show on screen try setting the id inside viewGroup in Authentication layout
    private fun showSnackBar(call: () -> String) {
        Snackbar.make(
            activity!!.findViewById<ConstraintLayout>(R.id.login_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(activity!!.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


}