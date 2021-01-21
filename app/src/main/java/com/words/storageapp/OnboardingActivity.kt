package com.words.storageapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import com.words.storageapp.ui.main.MainActivity

class OnboardingActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        this.getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val nextBtn = findViewById<MaterialButton>(R.id.continueBtn)

        val hasShown = sharedPref.getBoolean(SHOWN, false)

        if (hasShown) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        nextBtn.setOnClickListener {
            cachedState()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!checkLocationPermissionApproved()) {
            startLocationPermission()
        }
    }

    private fun checkLocationPermissionApproved() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun startLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            , MainActivity.REQUEST_LOCATION_CODE
        )
    }

    private fun cachedState() {
        with(sharedPref.edit()) {
            putBoolean(SHOWN, true)
            commit()
        }
    }

    companion object {
        const val SHOWN = "showned"
    }
}