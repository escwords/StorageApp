package com.words.storageapp.clientSession

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.words.storageapp.R
import com.words.storageapp.databinding.FragmentClientProfileBinding
import com.words.storageapp.ui.account.viewProfile.ProfileViewModel
import javax.inject.Inject


class ClientProfileFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var clientRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        //val userId = firebaseAuth.currentUser?.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentClientProfileBinding.inflate(inflater, container, false)

        binding.chatBtn.setOnClickListener {
        }

        binding.settingBtn.setOnClickListener {

        }

        binding.editBtn.setOnClickListener {

        }
        return binding.root
    }

}