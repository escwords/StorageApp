package com.words.storageapp.ui.account
//
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.net.toUri
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Observer
//import androidx.lifecycle.lifecycleScope
//import com.google.android.material.textfield.TextInputEditText
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageMetadata
//import com.google.firebase.storage.StorageReference
//import com.words.storageapp.R
//import com.words.storageapp.domain.User
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import timber.log.Timber
//import java.io.ByteArrayOutputStream
//
///**
// * A simple [Fragment] subclass.
// */
//const val REQUEST_PERMISSION_CODE = 1245
//const val MB = 1000000.0
//const val MB_THRESHOLD = 5.0
//
//class CreateProfileFragment : Fragment(), ImageDialogFragment.OnPhotoSelectedListener {
//
//    fun setOnPhotoSelectedListener(fragment: Fragment) {
//        if (fragment is ImageDialogFragment) {
//            fragment.setOnPhotoSelectedCallback(this)
//        }
//    }
//
//    private lateinit var query: Query
//
//    var user: User? = null
//
//    /*val imageBitmap: Bitmap? by lazy {
//        requireArguments().getParcelable("bitmapObject")
//       }*/
//    private lateinit var profileImageThumbnail: ImageView
//    private lateinit var mImageByte: ByteArray
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        val database = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node))
////        query = database.child(FirebaseAuth.getInstance().currentUser!!.uid)
//        // requireActivity().setResult()
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
//
//        //profileImageThumbnail = view.findViewById(R.id.profile_img)
//
//        setCurrentUser(query, view)
//        return view
//    }
//
//    private fun setCurrentUser(query: Query, view: View) {
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                user = dataSnapshot.getValue(User::class.java)
//                setUpUi(view, user!!)
//                if (user == null) {
//                    // User is null, error out
//                    Timber.e("User is unexpectedly null")
//                    Toast.makeText(
//                        activity,
//                        "Error: could not fetch user.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onCancelled(dataSnapshot: DatabaseError) {
//                Timber.e("getUser::onCancelled ${dataSnapshot.toException()}")
//            }
//        })
//    }
//
//    private fun setUpUi(view: View, user: User) {
//        val fname = view.findViewById<TextInputEditText>(R.id.fname)
//        val lname = view.findViewById<TextInputEditText>(R.id.lname)
//        fname.setText(user.fname)
//        lname.setText(user.lname)
//
//    }
//
//    private fun loadImageOptionDialog() {
//        if (readPermissionAccessApproved()) {
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                , REQUEST_PERMISSION_CODE
//            )
//        }
//    }
//
//    private fun readPermissionAccessApproved(): Boolean {
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            REQUEST_PERMISSION_CODE -> when {
//                grantResults.isEmpty() ->
//                    Timber.i("User interaction was cancelled.")
//
//                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
//                    // Permission was granted.
//                    Timber.i("Access Granted")
//                }
//                else -> {
//                    Timber.i("Access was Denied")
//                    return
//                }
//            }
//        }
//    }
//
//    override fun getImageBitmap(bitmap: Bitmap?) {
//        // mImageBitmap = bitmap
//        Timber.i("mImageBitMap in EditFragment Set $bitmap")
//        if (bitmap != null) {
//            compressImage(bitmap)
//        }
//    }
//    /*override fun getImageBitMap(uri: Uri?) {
//        TODO("Not yet implemented")
//    }*/
//
//    private fun compressImage(bitmap: Bitmap) {
//        viewLifecycleOwner.lifecycleScope.launch {
//            //show imageView widget loading dialog
//            Toast.makeText(activity, "Compressing Image", Toast.LENGTH_SHORT).show()
//            Timber.i("Compressing image Started")
//
//            val imageByte: ByteArray? = startCompressing(bitmap)
//            if (imageByte != null) {
//                //backup up this Uri
//                mImageByte = imageByte
//                uploadStarted(mImageByte)
//            } else {
//                Timber.e("ImageByte is Null")
//            }
//        }
//    }
//
//    //THIS METHOD IS COMPLETED
//    private suspend fun startCompressing(bitmap: Bitmap): ByteArray? {
//        return withContext(Dispatchers.Default) byte@{
//            var bytes: ByteArray? = null
//            for (i in 1 until 11) {
//
//                if (i == 10) {
//                    Timber.i("Image is too large")
//                    Toast.makeText(activity, "Image is Too Large", Toast.LENGTH_LONG)
//                        .show()
//                    break
//                }
//                bytes = getBytesFromBitmap(bitmap, 100 / i)
//                if (bytes.size / MB < MB_THRESHOLD) {
//                    return@byte bytes
//                }
//            }
//            bytes
//        }
//    }
//
//    private suspend fun getBytesFromBitmap(bitmap: Bitmap, quality: Int): ByteArray {
//        return coroutineScope {
//            val stream: ByteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
//            stream.toByteArray()
//        }
//    }
//
//    //    THIS METHOD HAS BEEN COMPLETED
//    private fun uploadStarted(mImageByte: ByteArray) {
//        val storage = FirebaseStorage.getInstance()
//        val storageRef: StorageReference =
//            storage.reference.child(
//                "images/users/${FirebaseAuth.getInstance()
//                    .currentUser!!.uid}/"
//            )
//
//        val metadata = StorageMetadata.Builder()
//            .setContentType("image/jpg")
//            .setContentLanguage("en")
//            .build()
//
//        storageRef.putBytes(mImageByte, metadata)
//            .addOnSuccessListener {
//                val url: Uri? = storageRef.downloadUrl.result
//
//                FirebaseDatabase.getInstance()
//                    .getReference(getString(R.string.db_node))
//                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
//                    .child("photoUrl")
//                    .setValue(url.toString())
//
//                Timber.i("Upload is Successful")
//                //set image to profile image imageView
//
//            }.addOnFailureListener { exception ->
//                Timber.e(exception, "Failed To Upload image")
//                Toast.makeText(activity, "Failed to Upload Image", Toast.LENGTH_SHORT)
//                    .show()
//
//            }.addOnProgressListener { taskSnapshot ->
//                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
//                Toast.makeText(activity, "Upload is $progress% done", Toast.LENGTH_SHORT).show()
//                Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()
//            }
//    }
////}
