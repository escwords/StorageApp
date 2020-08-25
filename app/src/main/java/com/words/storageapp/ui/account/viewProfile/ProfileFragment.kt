package com.words.storageapp.ui.account.viewProfile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.words.storageapp.R
import com.words.storageapp.databinding.FragmentProfile2Binding
import com.words.storageapp.domain.Photo
import com.words.storageapp.ui.main.MainActivity
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var collectionRef: CollectionReference

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    private lateinit var progressTop: ProgressBar
    private lateinit var toolbar: MaterialToolbar
    private lateinit var warningDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        val userId = firebaseAuth.currentUser!!.uid
        collectionRef = fireStore.collection(getString(R.string.db_node)).document(userId)
            .collection("photos")
        Timber.i("userId:$userId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfile2Binding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
        progressTop = binding.progressTop
        toolbar = binding.actionBar
        binding.profile = profileViewModel

        profileViewModel.userData.observe(viewLifecycleOwner, Observer { data ->
            if (data == null) {
                Timber.i("User data is empty")
            }
        })

        profileViewModel.isAcctActive.observe(viewLifecycleOwner, Observer { active ->
            active.getContentIfNotHandled()?.let {
                if (!it) {
                    noNetworkDialog()
                }
            }
        })

        binding.profilePics.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_profileFragment_to_profileImageFragment)
            Timber.i("image icon clicked")
        }

        binding.contactDetail.setOnClickListener { view ->
            val action = R.id.action_profileFragment_to_profileImageFragment
            view.findNavController().navigate(action)
        }

        //click listener that takes you to work album
        binding.skillDetail.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_profileFragment_to_editAlbumFragment)
        }

        binding.notification.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_profileFragment_to_notificationFragment)

        }

        binding.settings.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_profileFragment_to_configureFragment)
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sign_out -> {
                    firebaseAuth.signOut()
                    val action = R.id.action_profileFragment_to_authFragment
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).daggerAppLevelComponent.inject(this)
    }

    /*private fun initializePhotoData() {

        val photos = mutableListOf<Photo>()

        collectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { documents ->
                    Timber.i("returning photos")
                    documents.forEach { document ->
                        Timber.i("Am inside for each loop")
                        val photo = document.toObject(Photo::class.java)
                        photos.add(photo)
                        Timber.i("photoList: $photo")
                    }
                    Timber.i("List photos: $photos")
                }
                Timber.i("List photos: $photos")

            } else {
                Timber.i("Error fetching photos: ${task.exception}")
            }
        }
    }*/


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.profilePics -> {
                val action = R.id.action_profileFragment_to_profileImageFragment
                view.findNavController().navigate(action)
            }
        }
    }

    private fun noNetworkDialog() {
        warningDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle("Notice!!")
                setMessage("Complete Profile details to Activate Account.")
                setPositiveButton("close") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.create()
        }
        warningDialog.show()
    }


}