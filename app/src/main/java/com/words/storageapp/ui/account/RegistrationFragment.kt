package com.words.storageapp.ui.account


import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.words.storageapp.R
import com.words.storageapp.databinding.FragmentRegistrationBinding
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.account.viewProfile.ProfileViewModel
import com.words.storageapp.ui.main.MainActivity
import com.words.storageapp.util.AccountType
import kotlinx.coroutines.launch
import org.imperiumlabs.geofirestore.GeoFirestore
import timber.log.Timber
import javax.inject.Inject


class RegistrationFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var regEmail: TextInputEditText
    private lateinit var regPassword: TextInputEditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collectionRef: CollectionReference
    private lateinit var skillsSpinner: Spinner
    private lateinit var genderSpinner: Spinner
    private lateinit var accountSpinner: Spinner
    private lateinit var addressSpinner: Spinner
    private lateinit var skillsSpinnerCallback: AdapterView.OnItemSelectedListener
    private lateinit var genderSpinnerCallback: AdapterView.OnItemSelectedListener
    private lateinit var addressSpinnerCallback: AdapterView.OnItemSelectedListener
    var skills: String? = null
    var address: String? = null
    var gender: String? = null
    var account: String? = null

    //firebase database
    private lateinit var fireDatabase: DatabaseReference


    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var profileViewModel: ProfileViewModel


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        collectionRef = firestore.collection(getString(R.string.fireStore_node))

        fireDatabase = Firebase.database.reference
    }

    override fun onAttach(context: Context) {
        (activity as MainActivity).daggerAppLevelComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration, container, false
        )
        regEmail = binding.emailNameText
        regPassword = binding.passwordNameText

        addressSpinner = binding.addressSpinner
        genderSpinner = binding.genderSpinner
        skillsSpinner = binding.skillsSpinner

        addressSpinnerCallBack()
        genderSpinnerCallBack()
        skillsSpinnerCallBack()

        binding.signUp.setOnClickListener {
            (activity as MainActivity).hideKeyBoard(requireView())

            if (isValidForm()) {

                val geoPoint = GeoPoint(userManager.uLatitude!!, userManager.uLongitude!!)
                createLabourerAccount(
                    geoPoint,
                    regEmail.text.toString(),
                    regPassword.text.toString()
                )
            }

        }

        profileViewModel.createSuccess.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
                    showDialog()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error has occurred, try again", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpinnerAdapters()
    }


    private fun isValidForm(): Boolean {
        var valid = true

        val email = regEmail.text.toString()
        val password = regPassword.text.toString()

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val isValidPassword = password.isNotEmpty() && password.length > 7

        if (!isValidEmail) {
            regEmail.error = "Email is not valid"
            valid = false
        } else {
            regEmail.error = null
        }
        if (!isValidPassword) {
            regPassword.error = "Password is too short"
            valid = false
        } else {
            regPassword.error = null
        }
        return valid
    }


    private fun createLabourerAccount(geoPoint: GeoPoint, email: String, password: String) {

        val firstName = binding.firstNameText.text.toString()
        val lastName = binding.lastNameText.text.toString()
        val emailAddress = binding.emailNameText.text.toString()
        val mobileNumber = binding.mobileNumberText.text.toString()
        val skills = skills //change this line of code
        //val locality = binding.localityNameText.text.toString()

        showLoadingProgress()

        val collectionRef = firestore.collection(getString(R.string.fireStore_node))
        val geoFirestore = GeoFirestore(collectionRef)

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Timber.i("createUserWithEmail:success")
                val userId = firebaseAuth.currentUser!!.uid

                val labourer = RegisterUser(
                    accountStatus = 0,
                    firstName = firstName,
                    lastName = lastName,
                    imageUrl = "",
                    email = emailAddress,
                    accountActive = false,
                    phone = mobileNumber,
                    skills = skills,
                    serviceOffered = "Short Description of what you do!!",
                    skillId = userId
                )

                val documentRef = fireDatabase.child("skills").child(userId)
                val geoFire = GeoFire(documentRef)
                val geoLocation = GeoLocation(1.345664, 1.454583)

                //val documentRef = collectionRef.document(userId)

//                documentRef.set(labourer).addOnSuccessListener {
//                    geoFirestore.setLocation(userId, geoPoint) { exception ->
//                        if (exception == null) {
//                            //Display Dialog welcoming User to the profile Screen
//                            Timber.i("Location saved on server successfully")
//                            showDialog()
//                        } else {
//                            Timber.e(exception, " Unable to save Location")
//                        }
//                    }
//                }
                documentRef.setValue(labourer).addOnSuccessListener {
                    Timber.i("Account has been Created")
                    geoFire.setLocation(
                        userId, geoLocation
                    ) { _, error ->
                        if (error != null) {
                            Timber.i("Error saving location to database")
                            Toast.makeText(
                                requireContext(),
                                "Error has occurred, Try again", Toast.LENGTH_SHORT
                            ).show()
                            deleteAccount() //try email and password if error occurred.
                            hideLoadingProgress()
                        } else {
                            Timber.i("Location saved successfully")
                            showDialog()
                            hideLoadingProgress()
                        }
                    }
                    //profileViewModel.createClient(client.toClientDbModel()) //choose to populate db using online Data //showDialog()
                }.addOnFailureListener { e ->
                    Timber.e(e, "Failed to save User Document")
                    //display Error Dialog telling user to check internet
                    displayErrorDialog()
                    firebaseAuth.currentUser!!.delete()
                    Timber.i("Delete account:Success, Account deleted")
                }
            } else {
                // If sign in fails, display a message to the user.
                //display error  dialog screen telling user of no internet connection
                showSnackBar { "Account Creation failed: The email address is already in Use" }
            } //end of else
        }//end of CreateAccount
    }


    private fun createClientAccount(email: String, password: String) {
        val firstName = binding.firstNameText.text.toString()
        val lastName = binding.lastNameText.text.toString()
        // val locality = binding.localityNameText.text.toString()
        val mobile = binding.mobileNumberText.text.toString()
        //val gender = binding.genderNameText.text.toString()
        showLoadingProgress()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("Create Client User::Success")
                val userId = firebaseAuth.currentUser?.uid
                val client = RegisterUser(
                    skillId = userId!!,
                    firstName = firstName,
                    lastName = lastName,
                    accountType = account,
                    address = address,
                    phone = mobile
                )
                //val documentRef = collectionRef.document(userId)
                val documentRef = fireDatabase.child("skills").child(userId)
                val geoFire = GeoFire(documentRef)
                val geoLocation = GeoLocation(1.345664, 1.454583)

                documentRef.setValue(client).addOnSuccessListener {
                    Timber.i("Account has been Created")
                    geoFire.setLocation(
                        userId, geoLocation
                    ) { _, error ->
                        if (error != null) {
                            Timber.i("Error saving location to database")
                            Toast.makeText(
                                requireContext(),
                                "Error has occurred, Try again", Toast.LENGTH_SHORT
                            ).show()
                            deleteAccount() //try email and password if error occurred.
                            hideLoadingProgress()
                        } else {
                            Timber.i("Location saved successfully")
                            showDialog()
                            hideLoadingProgress()
                        }
                    }
                    //profileViewModel.createClient(client.toClientDbModel()) //choose to populate db using online Data //showDialog()
                }.addOnFailureListener {
                    Timber.e(it, "Failed to save User Document")
                    //display Error Dialog telling user to check internet
                    displayErrorDialog()
                    deleteAccount()
                    Timber.i("Delete account:Success, Account deleted")
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Account already in Use", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setUpSpinnerAdapters() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.skills_array
            , android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            skillsSpinner.adapter = adapter
        }
        skillsSpinner.onItemSelectedListener = skillsSpinnerCallback

        ArrayAdapter.createFromResource(
            requireContext(), R.array.gender_array
            , android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
        genderSpinner.onItemSelectedListener = genderSpinnerCallback
    }

    private fun genderSpinnerCallBack() {
        genderSpinnerCallback = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val genderString = parent?.getItemAtPosition(position) as String
                gender = genderString
            }
        }
    }

    private fun skillsSpinnerCallBack() {
        skillsSpinnerCallback = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val skillString = parent?.getItemAtPosition(position) as String
                skills = skillString
            }
        }
    }

    private fun addressSpinnerCallBack() {
        addressSpinnerCallback = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val addressString = parent?.getItemAtPosition(position) as String
                address = addressString
            }
        }
    }

    //change this dialog to material component dialog
    private fun showDialog() {
        val alertDialog: AlertDialog? = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Welcome")
                setMessage("Account Creation was Successful Processed to DashBoard")
                setPositiveButton("Continue") { _, _ ->
                    cacheAccountType() //this store the accountType in the local cache
                    val action = R.id.action_authFragment_to_profileFragment
                    findNavController().navigate(action) //remember to implement pop in the navHost xml
                }
            }
            //create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    private fun displayErrorDialog() {
        val alertDialog: AlertDialog? = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Error")
                setMessage(
                    "Unable to Create Account \n check your Location settings" +
                            "and Internet connection before Retry"
                )
                setPositiveButton("close") { dialog, _ ->
                    //Navigate to Profile Screen
                    dialog.dismiss()
                }
            }
            //create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    private fun cacheAccountType() {
        val sharedPref = (activity as MainActivity).sharedPref
        with(sharedPref.edit()) {
            putString(AccountType, account)
            commit()
        }
    }

    private fun showLoadingProgress() {
        lifecycleScope.launch {
            binding.loadingCard.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingProgress() {
        lifecycleScope.launch {
            binding.loadingCard.visibility = View.GONE
        }
    }

    private fun deleteAccount() {
        firebaseAuth.currentUser!!.delete()
    }

    private fun showSnackBar(call: () -> String) {
        Snackbar.make(
            requireActivity().findViewById<ConstraintLayout>(R.id.register_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }

}
