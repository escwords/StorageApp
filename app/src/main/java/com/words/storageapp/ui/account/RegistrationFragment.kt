package com.words.storageapp.ui.account


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.words.storageapp.R
import com.words.storageapp.databinding.FragmentRegistrationBinding
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.domain.toLoggedInUser
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.account.viewProfile.ProfileViewModel
import com.words.storageapp.ui.main.MainActivity
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.setLocation
import timber.log.Timber
import javax.inject.Inject


class RegistrationFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var regEmail: TextInputEditText
    private lateinit var regPassword: TextInputEditText
    private lateinit var firestore: FirebaseFirestore

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

        binding.signUp.setOnClickListener {
            (activity as MainActivity).hidekeyboard(requireView())
            if (isValidForm()) {
                when (userManager.uLatitude) {
                    null -> createAccount(regEmail.text.toString(), regPassword.text.toString())

                    else -> {
                        val geoPoint = GeoPoint(userManager.uLatitude!!, userManager.uLongitude!!)
                        createAccount(
                            geoPoint,
                            regEmail.text.toString(),
                            regPassword.text.toString()
                        )
                    }
                }
            }
        }
        return binding.root
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


    private fun createAccount(email: String, password: String) {

        val firstName = binding.firstNameText.text.toString()
        val lastName = binding.lastNameText.text.toString()
        val emailAddress = binding.emailNameText.text.toString()
        val mobileNumber = binding.mobileNumberText.text.toString()
        val skills = binding.skillNameText.text.toString()
        val locality = binding.localityNameText.text.toString()


        val collectionRef = firestore.collection(getString(R.string.fireStore_node))
        val geoFirestore = GeoFirestore(collectionRef)


        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity(),
            OnSuccessListener { location ->
                if (location == null) {
                    Timber.i("Location is Null")
                    //Error Either Location is turned Off or not Internet Connection
                    return@OnSuccessListener
                }
                val latitude = location.latitude
                val longitude = location.longitude
                val geoPoint = GeoPoint(latitude, longitude)

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.i("createUserWithEmail:success")
                            val userId = firebaseAuth.currentUser!!.uid

                            val register = RegisterUser(
                                firstName = firstName,
                                lastName = lastName,
                                email = emailAddress,
                                phone = mobileNumber,
                                skills = skills,
                                skillId = userId,
                                locality = locality
                            )

                            val documentRef =
                                firestore.collection(getString(R.string.fireStore_node))
                                    .document(userId)

                            documentRef.set(register).addOnSuccessListener {
                                geoFirestore.setLocation(userId, geoPoint) { exception ->
                                    if (exception == null) {
                                        //Display Dialog welcoming User to the profile Screen
                                        Timber.i("Location saved on server successfully")
                                        val logged = register.toLoggedInUser()
                                        Timber.i("logged: $logged")
                                        profileViewModel.initializeAccount(logged)
                                        showDialog()
                                    } else {
                                        Timber.e(exception, " Unable to save Location")
                                    }
                                }
                            }.addOnFailureListener { e ->
                                Timber.e(e, "Failed to save User Document")
                                //display Error Dialog telling user to check internet
                                displayErrorDialog()
                                firebaseAuth.currentUser!!.delete()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            //display error  dialog screen telling user of no internet connection
                            showSnackBar { "Account Creation failed: The email address is already in Use" }
                        } //end of else

                    }//end of CreateAccount
            })
    }


    private fun createAccount(geoPoint: GeoPoint, email: String, password: String) {

        val firstName = binding.firstNameText.text.toString()
        val lastName = binding.lastNameText.text.toString()
        val emailAddress = binding.emailNameText.text.toString()
        val mobileNumber = binding.mobileNumberText.text.toString()
        val skills = binding.skillNameText.text.toString()
        val locality = binding.localityNameText.text.toString()
        val collectionRef = firestore.collection(getString(R.string.fireStore_node))
        val geoFirestore = GeoFirestore(collectionRef)

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Timber.i("createUserWithEmail:success")
                val userId = firebaseAuth.currentUser!!.uid

                val register = RegisterUser(
                    accountStatus = 0,
                    firstName = firstName,
                    lastName = lastName,
                    email = emailAddress,
                    accountActive = false,
                    phone = mobileNumber,
                    skills = skills,
                    skillId = userId,
                    locality = locality
                )

                val documentRef = collectionRef.document(userId)

                documentRef.set(register).addOnSuccessListener {
                    geoFirestore.setLocation(userId, geoPoint) { exception ->
                        if (exception == null) {
                            //Display Dialog welcoming User to the profile Screen
                            Timber.i("Location saved on server successfully")
                            showDialog()
                        } else {
                            Timber.e(exception, " Unable to save Location")
                        }
                    }
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


    private fun showDialog() {
        val alertDialog: AlertDialog? = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Welcome")
                setMessage("Account Creation was Successful Processed to DashBoard")
                setPositiveButton("Continue") { _, _ ->
                    //Navigate to Profile Screen
                    val action = R.id.action_authFragment_to_profileFragment
                    findNavController().navigate(action) //remember ot implement pop in the navHost xml
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


    private fun showSnackBar(call: () -> String) {
        Snackbar.make(
            requireActivity().findViewById<ConstraintLayout>(R.id.register_layout)
            , call.invoke(), Snackbar.LENGTH_LONG
        ).show()
    }

}
