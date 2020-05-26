//package com.words.storageapp.ui.account
//
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.activityViewModels
//import com.bumptech.glide.Glide
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import com.words.storageapp.R
//import com.words.storageapp.databinding.FragmentViewProfileBinding
//import com.words.storageapp.domain.User
//import timber.log.Timber
//
///**
// * A simple [Fragment] subclass.
// */
//class ViewProfileFragment : Fragment() {
//
//    private val sharedViewModel: SharedDetailViewModel by activityViewModels()
//
//    private lateinit var binding : FragmentViewProfileBinding
//
//    private lateinit var reference: DatabaseReference
//
//    private lateinit var profileImage: ImageView
//
//    //private lateinit var userDataListener: ValueEventListener
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val user = FirebaseAuth.getInstance().currentUser
//        if(user != null){
//            reference = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node)).child(user.uid)
//        }else{
//
//            (activity as AccountActivity).navigateToRegisterScreen()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_profile,container,false)
//        //binding.lifecycleOwner = this
//        profileImage = binding.profilePicture
//       /* binding.sharedDetailViewModel = sharedViewModel.also { accountViewModel ->
//            accountViewModel.user.observe(this, Observer { user ->
//                user?.let{
//                    binding.user = it
//                }
//            })
//        }*/
//        setUpValueListener(binding)
//
//        binding.editBtn.setOnClickListener {
//            navigateToEditScreen()
//        }
//        return binding.root
//    }
//
//    /*override fun onStart() {
//        super.onStart()
//        //here we read data from database
//        databaseRef.addValueEventListener(userDataListener)
//    }
//*/
//    /************************************************************************
//     * setting listener for database user data node
//     ********************************************************/
//    /*private fun basicUserDataListener(){
//        userDataListener = object : ValueEventListener{
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                    val user: User? = dataSnapshot.getValue(User::class.java)
//                    //UpdateUi here
//                    sharedViewModel.setUpUserData(user!!)
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                  Timber.e("Unable to Modify user data")
//            }
//
//        }
//    }*/
//
//    private fun setUpValueListener(view: FragmentViewProfileBinding){
//
//        reference.addValueEventListener( object : ValueEventListener{
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                val user = dataSnapshot.getValue(User::class.java)
//                updateUi(view,user!!)
//
//                Timber.i("Retrieved User data $user")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Timber.e("Unable to fetch User: ${databaseError.toException()}")
//            }
//        })
//    }
//    private fun updateUi(view: FragmentViewProfileBinding, user: User) {
//           view.emailTxt.text = user.email.toString()
//            view.addressTxt.text = user.address.toString()
//           view.mobileTxt.text = user.phoneNumber
//           view.fname.text = user.fname.toString()
//           view.lname.text = user.lname
//        loadImageWithGlide(user.photoUrl)
//    }
//
//    private fun navigateToEditScreen() {
//        (activity as AccountActivity).supportFragmentManager.beginTransaction()
//            .replace(R.id.accountFragment_holder,CreateProfileFragment())
//            .addToBackStack(ViewProfileFragment::class.java.simpleName)
//            .commit()
//    }
//
////    override fun onStop() {
////        super.onStop()
////        databaseRef.removeEventListener(userDataListener)
////    }
//private fun loadImageWithGlide(uri: String?){
//     Glide.with(this)
//        .load(uri)
//        .centerInside()
//        .into(profileImage)
//}
//
//}
