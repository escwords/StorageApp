package com.words.storageapp.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.BuildConfig
import com.words.storageapp.MyApplication
import com.words.storageapp.R
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.account.AccountActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_LOCATION_CODE = 10930
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        userManager = (application as MyApplication).appContainer.userManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()
    }

    private fun checkLocationPermissionApproved() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        val providePermission = checkLocationPermissionApproved()

        if (providePermission) {
            Snackbar.make(
                findViewById(R.id.parent_layout),
                R.string.request_permission_string, Snackbar.LENGTH_LONG
            )
                .setAction("ok") {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                        , REQUEST_LOCATION_CODE
                    )
                }.show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                , REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> when {
                grantResults.isEmpty() ->
                    Timber.i("User Interaction was Cancelled")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    retrieveUserLocation()
            }
            else -> {
                Snackbar.make(
                    findViewById(R.id.parent_layout),
                    R.string.denied_permission_string,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        navigateToSettings()
                    }
                    .show()
            }
        }
    }

    private fun retrieveUserLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { task ->
            if (task != null) {
                initializeLocation(task)
            }
        }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Failed to Fetch  last Location")
                //navigate the user to settings screen to confirm location settings
            }
    }

    private fun initializeLocation(location: Location) {
        userManager.setUpLocation(location)
        val geoCoder: Geocoder = Geocoder(this, Locale.getDefault())

        lifecycleScope.launch {
            try {
                val addresses = computeAddress(location, geoCoder)
                if (addresses != null && addresses.isNotEmpty()) {
                    parseAddress(addresses)
                } else {
                    Timber.i("Address is null")
                }
            } catch (e: Throwable) {
                Timber.e(e, "Unable to fetch addresses")
            }
        }
    }

    private fun parseAddress(addresses: List<Address>) {
        val parsedAddress = arrayListOf<String>()
        val currentAddress = addresses[0]
        for (i in 0..currentAddress.maxAddressLineIndex) {
            parsedAddress.add(currentAddress.getAddressLine(i))
        }
        userManager.setUpAddress(TextUtils.join("\n", parsedAddress))
    }

    private suspend fun computeAddress(location: Location, geoCoder: Geocoder): List<Address>? {
        return withContext(Dispatchers.Default) {
            geoCoder.getFromLocation(location.latitude, location.longitude, 4)
        }
    }

    private fun navigateToSettings() {
        //this intent displays the app setting screen
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID,
            null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                signOut()
                true
            }
            R.id.account -> {
                navigateToAccountScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            firebaseAuth.signOut()
            Toast.makeText(applicationContext, "User has Signed Out", Toast.LENGTH_SHORT).show()
        }

    }

    private fun navigateToAccountScreen() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    //we want to hide the logIn when user is already Logged in.
    // hiding the menu item in the overflow menu
    /*private fun setUpOverflowMenu(){
        val user: FirebaseUser? = firebaseAuth.currentUser

        if(user != null ){
        }
    }*/
}
