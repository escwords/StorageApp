package com.words.storageapp.ui.authentication


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.words.storageapp.R
import com.words.storageapp.databinding.FragmentRegistrationBinding
import com.words.storageapp.domain.User
import com.words.storageapp.ui.account.createProfile.CreateFragment
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */


class RegistrationFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var regEmail: TextInputEditText
    private lateinit var regPassword: TextInputEditText

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
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_registration, container, false
            )

        regEmail = binding.regEmail
        regPassword = binding.regPass

        binding.registerBtn.setOnClickListener {
            if (isValidForm()) {
                createAccount(regEmail.text.toString(), regPassword.text.toString())
            }
        }
        binding.backLoginBtn.setOnClickListener {
            (activity as Authentication).moveToLoginScreen()
        }
        return binding.root
    }


    private fun isValidForm(): Boolean {
        var valid = true

        val email = regEmail.text.toString()
        val password = regPassword.text.toString()

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isValidPassword = password.isNotEmpty() && password.length > 7

        if (!isValidEmail) {
            regEmail.error = "Email is not valid"
            valid = false
        } else {
            regEmail.error = null
        }
        if (!isValidPassword) {
            regPassword.error = "Password is too short"
            valid = false
        } else {
            regPassword.error = null
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

                //Create user object
//                val user = User(
//                    "This is first Description",
//                    null,
//                    firebaseAuth.currentUser!!.email,
//                    "Chinonso",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    firebaseAuth.currentUser!!.uid
//                )
                //firebaseAuth.signOut()
                navigationToCreateProfile()

            } else {
                // If sign in fails, display a message to the user.
                showSnackBar { "Account Creation failed: The email address is already in Use" }
            }
            (activity as Authentication).hideProgressBar()
        }
    }//end of CreateAccount

    //move the screen to Create Profile screen
    private fun navigationToCreateProfile() {
        (activity as Authentication).supportFragmentManager.commit {
            replace(R.id.fragment_holder, CreateFragment())
        }
    }


    private fun showSnackBar(call: () -> String) {
        Snackbar.make(
            requireActivity().findViewById<ConstraintLayout>(R.id.register_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }

//    private fun setDatabaseUser(user: User){
//        FirebaseDatabase.getInstance().reference.child(getString(R.string.db_node))
//            .child(firebaseAuth.currentUser!!.uid)
//            .setValue(user)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful){
//                    Toast.makeText(activity,"User set Up was successful",Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(activity,"Failed to Create User Account",Toast.LENGTH_SHORT).show()
//                    Timber.e("Failed to Create User: ${task.exception}")
//                }
//            }
//    }


}
