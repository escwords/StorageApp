package com.words.storageapp.ui.detail

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.words.storageapp.R
import com.words.storageapp.adapters.PhotosAdapter
import com.words.storageapp.databinding.FragmentAlbumBinding
import com.words.storageapp.domain.Photo
import com.words.storageapp.util.InjectorUtil
import com.words.storageapp.util.USERID
import timber.log.Timber


class AlbumFragment : Fragment() {

    private val skillId: String by lazy {
        arguments?.get(USERID) as String
    }

    private lateinit var collectionRef: CollectionReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotosAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var noConnection: ImageView
    private lateinit var imageProgress: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var collectionReference: CollectionReference
    private lateinit var listenerRef: ListenerRegistration

    private val albumViewModel: AlbumViewModel by viewModels {
        InjectorUtil.provideAlbumViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        collectionRef = fireStore.collection(getString(R.string.db_node)).document(skillId)
            .collection("photos")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlbumBinding.inflate(inflater, container, false)
        imageProgress = binding.loadingProgress
        recyclerView = binding.listAlbumItems
        noConnection = binding.imgNetworkOff

        adapter = PhotosAdapter()

        albumViewModel.photos.observe(viewLifecycleOwner, Observer { photoItems ->
            Timber.i("photos observer is called")
            if (photoItems.isNotEmpty()) {
                adapter.submitList(photoItems)
                imageProgress.visibility = View.GONE
            } else {
                Timber.i("Empty photos list")
            }
        })
        recyclerView.adapter = adapter

        Timber.i("AlbumFragment was called")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initializeData()
    }
//
//    override fun onStop() {
//        super.onStop()
//        listenerRef.remove()
//    }

    private fun initializeData() {
        Timber.i("OnViewCreated is called: $skillId")

        imageProgress.visibility = View.VISIBLE
        val photos = mutableListOf<Photo>()

        collectionRef.get().addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Timber.i("Photo request called ${task.result}")

                task.result?.let { documents ->
                    Timber.i("returning photos")

                    documents.forEach { document ->
                        Timber.i("Am inside for each loop")
                        val photo = document.toObject(Photo::class.java)
                        photos.add(photo)
                        albumViewModel.addPhotoToAlbum(photos)
                    }
                }
                adapter.submitList(photos)

            } else {
                Timber.i("Error fetching photos: ${task.exception}")
            }

        }
    }
}


//        listenerRef = collectionRef.addSnapshotListener listener@ { querySnapshot, e ->
//             Timber.i("Photos listener called:")
//            if(e != null){
//                Timber.i(e, "listen failed")
//                return@listener
//            }
//            for(document in querySnapshot!!){
//                val photo = document.toObject(Photo::class.java)
//                photos.add(photo)
//                Timber.i(photo.toString())
//
//                albumViewModel.addPhotoToAlbum(photos)
//            }
//
//            if(photos.isEmpty()){
//                noConnection.visibility = View.VISIBLE
//            }
//        }


