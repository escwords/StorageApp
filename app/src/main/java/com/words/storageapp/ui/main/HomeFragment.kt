package com.words.storageapp.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.words.storageapp.R
import com.words.storageapp.database.AppDatabase
import com.words.storageapp.databinding.FragmentHomeBinding
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.domain.toWokrData
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.search.SearchResultActivity
import com.words.storageapp.util.utilities.ConnectivityChecker
import com.words.storageapp.util.utilities.CurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.getAtLocation
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : Fragment() {

    companion object {
        const val LOCATION_REQUESTED = "initialized"
        const val REQUEST_LOCATION_CODE = 10930

    }

    private val connectionChecker: ConnectivityChecker? by lazy {
        (activity as MainActivity).connectivityChecker
    }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var collectionRef: CollectionReference
    private lateinit var geoFirestore: GeoFirestore
    private var initialized = false
    private var locationRequested: Boolean = false
    private lateinit var errorDialog: AlertDialog

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var db: AppDatabase
    private lateinit var progressBar: ProgressBar

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val latitude: Double? by lazy {
        userManager.uLatitude
    }

    val longitude: Double? by lazy {
        userManager.uLongitude
    }

    private val locationRequest by lazy {
        userManager.locationState
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        collectionRef = FirebaseFirestore.getInstance().collection("skills")
        geoFirestore = GeoFirestore(collectionRef)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onAttach(context: Context) {
        (activity as MainActivity).daggerAppLevelComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.searchBtn.setOnClickListener { startSearchActivity() }
        progressBar = binding.homeProgress

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeNetwork()
    }


    private fun initializeNetwork() {
        connectionChecker?.apply {
            lifecycle.addObserver(this)
            connectedStatus.observe(viewLifecycleOwner, Observer<Boolean> { active ->
                if (active) {
                    if (!checkLocationPermissionApproved()) {
                        Timber.i("location is not yet approved")
                        startLocationPermission()
                    } else if (!locationRequest) {
                        if (::errorDialog.isInitialized) {
                            errorDialog.dismiss()
                        }
                        Timber.i("location have not been requested")
                        createLocationRequest()

                    } else {
                        Timber.i("location has been requested, location has been approved")
//                        val latitude = userManager.uLatitude
//                        val longitude = userManager.uLongitude
                        Timber.i("$latitude,$longitude")
                        if (latitude != null && longitude != null) {
                            fetchFromLocation(latitude!!, longitude!!)
                        }
                    }
                } else {
                    if (!locationRequest) {
                        Timber.i("No locationRequested, No network")
                        noNetworkDialog()
                    }
                }
            })
        }
    }

    //we call this function to check if location permission has been approved
    private fun checkLocationPermissionApproved() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun startLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            , MainActivity.REQUEST_LOCATION_CODE
        )
    }

    private fun createLocationRequest() {
        showProgressBar()

        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
        }

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder?.build())

        task.addOnSuccessListener {
            retrieveUserLocation()
        }

        task.addOnFailureListener { e ->
            hideProgressBar()

            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        requireActivity(),
                        MainActivity.REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.i("Cannot resolve location Error")
                }
            }
        }
    }

    private fun retrieveUserLocation() {
        Timber.i("Retrieve Location object")
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity(),
            OnSuccessListener { location ->
                if (location == null) {
                    Timber.i("Location is Null")
                    return@OnSuccessListener
                }
                hideProgressBar()
                userManager.setLocationState(true)
                //locationRequested = true
                val currentLocation = CurrentLocation(location.longitude, location.latitude)
                userManager.setUpLocation(currentLocation)
                latitude?.let { longitude?.let { it1 -> fetchFromLocation(it, it1) } }
                Toast.makeText(
                    requireActivity(),
                    "Location Retrieval was successful: ${latitude}, $longitude",
                    Toast.LENGTH_SHORT
                ).show()

            }).addOnFailureListener(requireActivity()) { e ->
            Timber.e(e, "Unable to fetch last location")
            //show navigate you to dialog that lead to setting screen
            //show error can't fetch location Error
        }
    }

    //handle the permission intent
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> when {
                grantResults.isEmpty() ->
                    Timber.i("User Interaction was Cancelled")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    createLocationRequest()
                }
            }
            else -> {
                showSnackBar(
                    R.string.denied_permission_string,
                    R.string.settings,
                    View.OnClickListener {
                        requestLocationPermission()
                    })
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) { //first time running of the app
            showSnackBar(R.string.request_permission_string, R.string.ok,
                View.OnClickListener {
                    //check if location is turned on
                    createLocationRequest()
                })
        }
    }

    private fun showSnackBar(
        snackStrId: Int,
        actionStrId: Int,
        listener: View.OnClickListener? = null
    ) {
        //replace snackBar context to R.id.parent_layout
        val snackBar = Snackbar.make(
            requireActivity().findViewById(R.id.parent_layout), getString(snackStrId),
            Snackbar.LENGTH_INDEFINITE
        )

        if (actionStrId != 0 && listener != null) {
            snackBar.setAction(getString(actionStrId), listener)
        }
        snackBar.show()
    }

    private fun startSearchActivity() {
        val intent = Intent(requireContext(), SearchResultActivity::class.java)
        startActivity(intent)
    }


    //queries the backend with user location and cache it in the local database
    private fun fetchFromLocation(lat: Double, long: Double) {
        val queryCenter = GeoPoint(lat, long)

        Timber.i("HomeFragment FetchFromLocation called:")
        geoFirestore.getAtLocation(queryCenter, 600.0) { docs, ex ->
            if (ex != null) {
                hideProgressBar()
                Timber.e(ex, "Unable to fetch document")
                return@getAtLocation
            } else {
                docs?.let { skills ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        //db.wokrDataDao().deleteAllUsers()
                        for (result in skills) {
                            val registerUser = result.toObject(RegisterUser::class.java)
                            db.wokrDataDao().insert(registerUser!!.toWokrData())
                            Timber.i("Users : $registerUser")
                        }
                        progressBar.visibility = View.GONE
                    }
                    hideProgressBar()
                    initialized = true
                }
            }
        }
    }

    private fun noNetworkDialog() {
        errorDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle("You're Offline")
                val inflater = it.layoutInflater

                setView(inflater.inflate(R.layout.fragment_no_connection, null))
                setPositiveButton("Retry") { dialog, _ ->
                    dialog.dismiss()
                    createLocationRequest()
                }
                setNegativeButton("Exit") { dialog, _ ->
                    dialog.dismiss()
                    requireActivity().finish()
                }
            }
            builder.create()
        }
        errorDialog.show()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putBoolean(LOCATION_REQUESTED,locationRequested)
//    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
