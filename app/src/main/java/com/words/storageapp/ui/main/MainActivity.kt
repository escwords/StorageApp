package com.words.storageapp.ui.main

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.MyApplication
import com.words.storageapp.R
import com.words.storageapp.di.dagger.AppLevelComponent
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.util.utilities.ConnectivityChecker
import com.words.storageapp.util.utilities.Constants
import com.words.storageapp.util.utilities.CurrentLocation
import com.words.storageapp.util.utilities.FetchAddressIntentService
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_LOCATION_CODE = 10930
        const val ADDRESS_REQUESTED_KEY = "10323"
        const val LOCATION_ADDRESS_KEY = "10343"
        const val REQUEST_CHECK_SETTINGS = 10002
        const val LOCATION_REQUESTED_KEY = "1245"
    }

    var connectivityChecker: ConnectivityChecker? = null
    private var addressOutput: ArrayList<String>? = null

    private lateinit var resultReceiver: AddressResultReceiver
    lateinit var daggerAppLevelComponent: AppLevelComponent
    private lateinit var navController: NavController

    //this method initializes the main screen
    override fun onCreate(savedInstanceState: Bundle?) {
        daggerAppLevelComponent = (application as MyApplication).daggerComponent
        daggerAppLevelComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultReceiver = AddressResultReceiver(Handler())
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navController = findNavController(R.id.mainNavHost)

        setUpBottomNav(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.profileFragment) {
                bottomNav.visibility = View.VISIBLE
            } else {
                bottomNav.visibility = View.GONE
            }
        }
        connectivityChecker = connectivityChecker(this)
    }


    //we received the location here and perform an action on it
    private fun retrieveUserLocation() {
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener(
//            this,
//            OnSuccessListener { location ->
//                if (location == null) {
//                    Timber.i("Location is Null")
//                    return@OnSuccessListener
//                }

        //checking if GeoCoder is available
//                if (!Geocoder.isPresent()) {
//                    Timber.i("GeoCoder not available")
//                    return@OnSuccessListener
//                }

        // currentLocation = CurrentLocation(longitude = location.longitude, latitude = location.latitude)
        //if the user press reset buttons  it reset addressRequested to true
        //calling the function the decode the address
//                if (addressRequested) startIntentService()

//            }).addOnFailureListener(this) { e ->
//
//            Timber.e(e, "Unable to fetch last location")
//            //show navigate you to dialog that lead to setting screen
//            //show error can't fetch location Error
//            finish()
//        }
//    }
    }

//    private fun startIntentService(){
//        val intent = Intent(this,FetchAddressIntentService::class.java).apply {
//            putExtra(Constants.RECEIVER, resultReceiver)
//            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
//        }
//        startService(intent)
//    }


    //Receiver for data sent from FetchAddressIntent Service
    private inner class AddressResultReceiver internal constructor(
        handler: Handler
    ) : ResultReceiver(handler) {
        //receives data sent from Fetch Address Intent Service and updates the ui
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                // String cannot be cast to arraylist
                addressOutput = resultData!!.getStringArrayList(Constants.RESULT_DATA_KEY)
                Timber.i("Retrieved Address: $addressOutput")
                //Display Address Dialog
            }
        }
    }

    //A generic snackBar
    private fun showSnackBar(
        snackStrId: Int,
        actionStrId: Int,
        listener: View.OnClickListener? = null
    ) {
        //replace snackBar context to R.id.parent_layout
        val snackBar = Snackbar.make(
            findViewById(R.id.parent_layout), getString(snackStrId),
            Snackbar.LENGTH_INDEFINITE
        )

        if (actionStrId != 0 && listener != null) {
            snackBar.setAction(getString(actionStrId), listener)
        }
        snackBar.show()
    }

    /***********************************************************************************************/


    //setting up bottom navigation
    private fun setUpBottomNav(navController: NavController) {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)
    }

    private fun connectivityChecker(activity: Activity): ConnectivityChecker? {
        val connectivityManager = activity.getSystemService<ConnectivityManager>()
        return if (connectivityManager != null) {
            ConnectivityChecker(connectivityManager)
        } else {
            null
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun hidekeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
//if the location is off in the setting we navigate the user to setting screen to switch on the location
//    private fun navigateToSettings() {
//        //this intent displays the app setting screen
//        val intent = Intent()
//        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//        val uri = Uri.fromParts(
//            "package",
//            APPLICATION_ID,
//            null
//        )
//        intent.data = uri
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//    }
