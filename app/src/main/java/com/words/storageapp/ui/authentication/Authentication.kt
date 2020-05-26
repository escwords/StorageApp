package com.words.storageapp.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.R
import com.words.storageapp.ui.main.MainActivity

class Authentication : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        progressBar = findViewById(R.id.login_ProgressBar)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, LoginFragment())
            .addToBackStack(LoginFragment::class.java.simpleName)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() //Check out the different b/w calling this function and implementing intent.Flag_activity_clear Task and others
        }
    }

    fun moveToRegistrationFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_holder,
                RegistrationFragment()
            )
            .commit()
    }

    //consider the change this function provides
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            finish()
        } else {
            super.onBackPressed()

        }
    }

    fun moveToLoginScreen() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_holder,
                LoginFragment()
            )
            .addToBackStack(LoginFragment::class.java.simpleName)
            .commit()
    }


    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }


}
