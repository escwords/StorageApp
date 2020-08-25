package com.words.storageapp.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import com.words.storageapp.R
import com.words.storageapp.adapters.AuthPagerAdapter
import com.words.storageapp.adapters.AuthPagerAdapter.Companion.SIGN_IN_INDEX
import com.words.storageapp.adapters.AuthPagerAdapter.Companion.SIGN_OUT_INDEX
import com.words.storageapp.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var documentRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAuthBinding.inflate(inflater, container, false)
        val viewPager = binding.authPager
        val tabLayout = binding.authTabLayout


        viewPager.adapter = AuthPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        binding.closeBtn.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = firebaseAuth.currentUser
        user?.uid?.let {
            val action = R.id.action_authFragment_to_profileFragment
            view.findNavController().navigate(action)
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            SIGN_IN_INDEX -> getString(R.string.tab_sign_in)
            SIGN_OUT_INDEX -> getString(R.string.tab_sign_up)
            else -> null
        }
    }

}
