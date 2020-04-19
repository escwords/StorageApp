package com.words.storageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar

class Authentication : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        progressBar = findViewById(R.id.login_ProgressBar)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

    fun moveToRegistrationFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, RegistrationFragment())
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
