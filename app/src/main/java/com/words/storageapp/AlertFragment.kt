package com.words.storageapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * A simple [Fragment] subclass.
 */
class AlertFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the layout for this fragment
        return activity?.let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle("No Internet")
                setMessage("SkillHub needs Internet for Setup ")

            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}
