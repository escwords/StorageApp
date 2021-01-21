package com.words.storageapp.clientSession

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.words.storageapp.R
import com.words.storageapp.chat.ChatsListAdapter
import com.words.storageapp.chat.ItemClickListener
import com.words.storageapp.databinding.FragmentClientChatsBinding
import com.words.storageapp.domain.ChatData
import timber.log.Timber


class ClientChatsFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var labourerChatsRef: CollectionReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var chatsListAdapter: ChatsListAdapter
    private lateinit var chatsListener: ListenerRegistration
    private lateinit var clientId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Have not yet login", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.authFragment)
        } else {
            clientId = uid
            labourerChatsRef = firestore.collection("skills")
                .document(clientId).collection("labourers")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentClientChatsBinding.inflate(inflater, container, false)
        val chatsRecycler = binding.labourersChatList

        chatsListAdapter = ChatsListAdapter(ItemClickListener { labourerId ->
            val ids = arrayListOf<String>(labourerId, clientId)
            val bundle = bundleOf("Message" to ids)
        })
        chatsRecycler.adapter = chatsListAdapter
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        attachListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        detachListener()
    }

    private fun attachListener() {
        val chats = mutableListOf<ChatData>()
        chatsListener =
            labourerChatsRef.addSnapshotListener addSnapshotListener@{ querySnapshot, e ->
                if (e != null) {
                    Timber.i(e, "Listen failed")
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    it.forEach { document ->
                        val chat = document.toObject(ChatData::class.java)
                        chats.add(chat)
                    }
                } ?: Toast.makeText(requireContext(), "EmptyList", Toast.LENGTH_SHORT).show()
                chatsListAdapter.submitList(chats)
            }
    }

    private fun detachListener() {
        chatsListener.remove()
    }

}