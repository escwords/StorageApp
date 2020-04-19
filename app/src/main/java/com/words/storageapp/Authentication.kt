package com.words.storageapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth

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
        }
    }

    fun moveToRegistrationFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, RegistrationFragment())
            .addToBackStack(RegistrationFragment::class.java.simpleName)
            .commit()
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

    fun moveToLoginScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, LoginFragment())
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
