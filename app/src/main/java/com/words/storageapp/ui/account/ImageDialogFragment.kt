package com.words.storageapp.ui.account
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.DialogFragment
//import com.words.storageapp.R
//import timber.log.Timber
//
//const val MEMORY_REQUEST_CODE = 180
//const val CAMERA_REQUEST_CODE = 181
//const val BITMAP_REQUEST = "image_bitmap"
//
//class ImageDialogFragment : DialogFragment() {
//
//    private lateinit var camera: TextView
//    private lateinit var memory: TextView
//
//
//    interface OnPhotoSelectedListener {
//        fun getImageBitmap(bitmap: Bitmap?)
///*
//        fun getImageBitMap(uri: Uri?)
//*/
////    }
//
//    private var callback: OnPhotoSelectedListener? = null
//
//    internal fun setOnPhotoSelectedCallback(callback: OnPhotoSelectedListener) {
//        this.callback = callback
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        val editProfileFragmentRef = (activity as AccountActivity).supportFragmentManager
////            .findFragmentById(R.id.accountFragment_holder) as CreateProfileFragment
////        editProfileFragmentRef.setOnPhotoSelectedListener(this)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_imagedialog, container, false)
//        camera = view.findViewById(R.id.take_photo)
//        memory = view.findViewById(R.id.select_photo)
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        camera.setOnClickListener {
//            openCameraIntent()
//        }
//        memory.setOnClickListener {
//            openStorageIntent()
//        }
//    }
//
//    private fun openStorageIntent() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "image/*"
//            addCategory(Intent.CATEGORY_OPENABLE)
//        }
//        if (intent.resolveActivity(requireActivity().packageManager) != null) {
//            startActivityForResult(intent, MEMORY_REQUEST_CODE)
//        }
//    }
//
//    private fun openCameraIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
//            takePhotoIntent.resolveActivity(requireActivity().packageManager)?.also {
//                startActivityForResult(takePhotoIntent, CAMERA_REQUEST_CODE)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == MEMORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Timber.i("Done Selecting photo from external storage")
//            //   val fullPhotoUri: Uri? = data!!.data
//            val thumbnailBitmap: Bitmap? = data!!.getParcelableExtra("data")
//            callback!!.getImageBitmap(thumbnailBitmap)
//            dismiss()
//            //callback!!.getImageBitMap(fullPhotoUri)
//
//        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Timber.i("Done taking picture")
//            val imageBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
//            callback!!.getImageBitmap(imageBitmap)
//            dismiss()
//        }
//    }
//
//
//    /* private fun navigateToEditProfileScreen(bitmap: Bitmap){
//
//         val fragment = CreateProfileFragment()
//         val bundle1 = Bundle()
//         bundle1.putParcelable("bitmapObject",bitmap)
//         fragment.arguments = bundle1
//
//         parentFragmentManager.commit {
//             add(R.id.accountFragment_holder,fragment)
//         }
//     }*/
//
//
//}