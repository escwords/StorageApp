package com.words.storageapp.ui.authentication


import android.content.Intent
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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.R
import com.words.storageapp.ui.account.AccountActivity
import com.words.storageapp.databinding.FragmentLoginBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var logEmail: TextInputEditText
    private lateinit var logPassWord: TextInputEditText

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
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login, container, false
            )

        //progressBar = binding.loginProgressBar

        logEmail = binding.lgEmail
        logPassWord = binding.lgPass

        binding.login.setOnClickListener(this)
        binding.registerClicker2.setOnClickListener(this)


        return binding.root
    }
    private fun validate(): Boolean {
        var valid = true

        val email = logEmail.text.toString()
        val password = logPassWord.text.toString()

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isValidPassword = password.isNotEmpty() && password.length > 7

        if (!isValidEmail) {
            logEmail.error = "Email is not valid"
            valid = false
        } else {
            logEmail.error = null
        }
        if (!isValidPassword) {
            logPassWord.error = "Password is too short"
            valid = false
        } else {
            logPassWord.error = null
        }
        return valid
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.login -> {

                if (validate()) {
                    signInUser(logEmail.text.toString(), logPassWord.text.toString())
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

                    navigateToAccountActivity()

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
            requireActivity().findViewById<ConstraintLayout>(R.id.login_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }

//    private fun navigationToAccountActivity(){
//        val intent = Intent(activity,AccountActivity::class.java)
//        startActivity(intent)
//    }

    //After the user has log In navigate the user to account Activity
    private fun navigateToAccountActivity() {
        val intent = Intent(requireActivity().applicationContext, AccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

}
