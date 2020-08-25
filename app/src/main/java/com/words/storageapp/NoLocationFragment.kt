package com.words.storageapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.words.storageapp.databinding.FragmentNoLocationBinding
import com.words.storageapp.ui.main.MainActivity

class NoLocationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentNoLocationBinding.inflate(inflater, container, false)

        binding.retryTxt.setOnClickListener {
            navigateToMain()
        }
        return binding.root
    }

    private fun navigateToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}
