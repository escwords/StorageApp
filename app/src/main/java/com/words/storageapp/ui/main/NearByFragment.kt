package com.words.storageapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

import com.words.storageapp.R
import com.words.storageapp.adapters.NearByAdapter
import com.words.storageapp.adapters.NearByAdapter2
import com.words.storageapp.databinding.FragmentNearByBinding
import com.words.storageapp.domain.NearBySkill
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.util.utilities.ConnectivityChecker
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener
import timber.log.Timber

class NearByFragment : Fragment() {

    private lateinit var collectionRef: CollectionReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var geoFirestore: GeoFirestore

    private lateinit var recyclerView: RecyclerView

    private var connectivityChecker: ConnectivityChecker? = null

    private lateinit var progressBar: ProgressBar

    private lateinit var noConnection: ViewGroup

    private lateinit var adapter: NearByAdapter2

    private lateinit var nearbyLayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        collectionRef =
            FirebaseFirestore.getInstance().collection(getString(R.string.fireStore_node))
        geoFirestore = GeoFirestore(collectionRef)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentNearByBinding.inflate(inflater, container, false)

        recyclerView = binding.nearbyRecycler

        progressBar = binding.nearbyProgress

        noConnection = binding.noConnectionHome

        nearbyLayout = binding.nearLayout

        adapter = NearByAdapter2(NearByAdapter2.ClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = adapter
        return binding.root
    }

//    override fun onStart() {
//        super.onStart()
//        //check internet connection
//        connectivityChecker = (activity as MainActivity).connectivityChecker
//        connectivityChecker?.apply {
//            lifecycle.addObserver(this)
//            connectedStatus.observe(viewLifecycleOwner, Observer<Boolean> {
//                if(it) {
//                    showProgressBar()
//                    when (userManager.uLongitude) {
//                        null -> {
//                            Toast.makeText(
//                                requireContext(),
//                                "No location available",
//                                Toast.LENGTH_LONG
//                            ).show()
//                             hideProgressBar()
//                            //Navigate user to switch on location settings
//                            findNavController().navigate(R.id.noLocationFragment)
//                            findNavController().popBackStack(R.id.homeFragment,true)
//                        }
//                        else -> {
//
//                            Timber.i("Location is available")
//                            nearbyLayout.visibility = View.VISIBLE
//                            noConnection.visibility = View.GONE
//
//                            val latitude = userManager.uLatitude
//                            val longitude = userManager.uLongitude
//                            fetchFromLocation(latitude!!, longitude!!)
//                        }
//                    }
//                }else{
//                    nearbyLayout.visibility = View.GONE
//                    noConnection.visibility = View.VISIBLE
//                }
//            })
//        }
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.homeFragment, true)
        }
    }

    private fun fetchFromLocation(lat: Double, long: Double) {
        Timber.i("fetchFromLocation Initial log")

        val queryCenter = GeoPoint(lat, long)
        val qeoQuery = geoFirestore.queryAtLocation(queryCenter, 600.0)

        val nearBySkills = mutableListOf<NearBySkill>()
        qeoQuery.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {

            override fun onDocumentChanged(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
                Timber.i("onDocument Changed Called")
            }

            override fun onDocumentEntered(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
                Timber.i("OnDocumentEntered")

                val nearBySkill = documentSnapshot["DESCRIPTION"] as? NearBySkill
                Timber.i("$nearBySkill")

                if (nearBySkill != null) {
                    nearBySkills.add(nearBySkill)
                }
            }

            override fun onDocumentExited(documentSnapshot: DocumentSnapshot) {
                Timber.i("onDocumentExited")
                TODO("Not yet implemented")
            }

            override fun onDocumentMoved(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
                TODO("Not yet implemented")
            }

            override fun onGeoQueryError(exception: Exception) {
                TODO("Not yet implemented")
            }

            override fun onGeoQueryReady() {
                Timber.i("OnGeoQuery Called")
            }

        })

        hideProgressBar()

        if (nearBySkills.isEmpty()) {
            Timber.i("Empty list")
            noConnection.visibility = View.VISIBLE
        }
        Timber.i("List passed to Adapter: $nearBySkills")

        adapter.submitList(nearBySkills)
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
