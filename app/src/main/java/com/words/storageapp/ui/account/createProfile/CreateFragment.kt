package com.words.storageapp.ui.account.createProfile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.words.storageapp.R
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.databinding.FragmentCreateBinding
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.ui.account.AccountActivity
import com.words.storageapp.util.InjectorUtil
import com.words.storageapp.util.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateFragment : Fragment() {

    //add this field's to the already created field on create fragment
    lateinit var binding: FragmentCreateBinding

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var emailAddr: String
    private lateinit var phone: String
    private lateinit var addres: String
    private lateinit var state: String
    private lateinit var lga: String
    private lateinit var dob: String
    private lateinit var gender: String
    private lateinit var skills: String
    private lateinit var exper: String
    private lateinit var desc: String


    private val createViewModel: CreateViewModel by viewModels {
        val repository = (activity as AccountActivity).userRepository
        InjectorUtil.provideCreateViewModelFactory(repository)
    }

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userManager = (activity as AccountActivity).appDiContainer.userManager
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            cviewModel = createViewModel

            clickListener = View.OnClickListener { buildProfile() }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstName = binding.fname.editText?.text.toString()
        lastName = binding.lname.editText?.text.toString()
        emailAddr = binding.email.editText?.text.toString()
        phone = binding.mobile.editText?.text.toString()
        addres = binding.address.editText?.text.toString()
        state = binding.state.editText?.text.toString()
        lga = binding.lgaArea.editText?.text.toString()
        dob = binding.dob.editText?.text.toString()
        gender = binding.gender.editText?.text.toString()
        skills = binding.Skills.editText?.text.toString()
        exper = binding.expr.editText?.text.toString()
        desc = binding.description.editText?.text.toString()
    }

    val skillsItem = listOf(
        "Plumber", "Technician", "Make-Up Artist", "Computer Technician"
    )

    val adapter = ArrayAdapter(requireContext(), R.layout.skills_list, skillsItem)


    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    fun buildProfile() {
        val loggedInUser = RegisterUser(
            addres, null, dob, null, emailAddr, exper, firstName, gender, null,
            lastName, lga, phone, null, null, skills, state, null, null
        )
        lifecycleScope.launch {

            createViewModel.addProfile(loggedInUser).collect { state ->
                when (state) {
                    is State.Loading -> {
                        //Show loading icon
                        //showLoading
                    }
                    is State.Success -> {
                        //Navigate to the next fragment
                        navigateToAccountScreen()
                    }
                    is State.Failed -> {
                        Timber.e("Failed to Build Profile")
                        Toast.makeText(activity, "Failed:${state.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun navigateToAccountScreen() {
        val intent = Intent(activity, AccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}