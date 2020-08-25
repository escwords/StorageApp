package com.words.storageapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.words.storageapp.R
import com.words.storageapp.adapters.PhotosAdapter
import com.words.storageapp.databinding.FragmentDetail2Binding
import com.words.storageapp.domain.Photo
import com.words.storageapp.ui.search.SearchRepository
import com.words.storageapp.ui.search.SearchResultActivity
import com.words.storageapp.util.InjectorUtil
import com.words.storageapp.util.utilities.USERID
import kotlinx.android.synthetic.main.fragment_about.*
import timber.log.Timber

class SkilledFragment : Fragment() {

    private val skillId: String by lazy {
        arguments?.get(USERID) as String
    }

    private val searchRepository: SearchRepository by lazy {
        (activity as SearchResultActivity).repository
    }

    private val detailViewModel: DetailViewModel by viewModels {
        InjectorUtil.provideDetailViewModelFactory(skillId, searchRepository)
    }

    private lateinit var collectionRef: CollectionReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotosAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registration: ListenerRegistration
    private lateinit var messageBtn: MaterialButton
    private lateinit var callBtn: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        collectionRef =
            fireStore.collection(getString(R.string.db_node)).document(skillId.trimIndent())
                .collection("photos")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetail2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.detailModel = detailViewModel
        callBtn = binding.callBtn
        messageBtn = binding.messageBtn
        recyclerView = binding.photoRecycler
        adapter = PhotosAdapter()
        // recyclerView.adapter = adapter
        attachPhotoListener()

        callBtn.setOnClickListener {

            detailViewModel.mobile.observe(viewLifecycleOwner, Observer { phoneNo ->
                if (phoneNo != null) {
                    Timber.i("mobile: $phoneNo")
                    dialPhoneNumber(phoneNo)
                }
            })
        }

        messageBtn.setOnClickListener {
            detailViewModel.mobile.observe(viewLifecycleOwner, Observer { phoneNo ->
                if (phoneNo != null) {
                    Timber.i("mobile: $phoneNo")
                    composeSmsMessage(phoneNo)
                }
            })
        }

        detailViewModel.detailData.observe(viewLifecycleOwner, Observer { data ->
            Timber.i("Photos observer is called")
            Timber.i("PhotoList: $data")
            if (data == null) {
                Timber.i("userData is null")
            } else {
                Timber.i("userData $data")
            }
        })
        return binding.root
    }


    override fun onStop() {
        super.onStop()
        detachListener()
    }


//    private fun initializePhotoData() {
//        Timber.i("OnViewCreated is called: $skillId")
//
//        val photos = mutableListOf<Photo>()
//
//        collectionRef.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                task.result?.let { documents ->
//                    Timber.i("returning photos")
//                    documents.forEach { document ->
//                        Timber.i("Am inside for each loop")
//                        val photo = document.toObject(Photo::class.java)
//                        photos.add(photo)
//                        Timber.i("photoList: $photo")
//                        detailViewModel.addPhotoToAlbum(photos)
//                    }
//                    Timber.i("List photos: $photos")
//                }
//                adapter.submitList(photos)
//                Timber.i("List photos: $photos")
//
//            } else {
//                Timber.i("Error fetching photos: ${task.exception}")
//            }
//        }
//    }

    private fun attachPhotoListener() {
        val list = mutableListOf<Photo>()
        Timber.i("Listener attached")
        registration = collectionRef.addSnapshotListener addSnapShotListener@{ querySnapshot, e ->
            if (e != null) {
                Timber.i(e, "Listen Failed")
                return@addSnapShotListener
            }
            for (document in querySnapshot!!) {
                val photo = document.toObject(Photo::class.java)
                list.add(photo)
                adapter.submitList(list)
            }
        }
        recyclerView.adapter = adapter
    }


    private fun detachListener() {
        registration.remove()
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber") //or use Uri.fromParts()
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun composeSmsMessage(address: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("smsto:$address")  // This ensures only SMS apps respond
            putExtra("sms_body", getString(R.string.messageHeader))
            //putExtra("address", address)
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

}