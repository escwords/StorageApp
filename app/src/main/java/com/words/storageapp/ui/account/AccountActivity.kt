package com.words.storageapp.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.*
import com.words.storageapp.ui.authentication.Authentication
import com.words.storageapp.di.AppDiContainer
import com.words.storageapp.di.UserFlowContainer
import com.words.storageapp.ui.account.user.UserRepository
import com.words.storageapp.ui.account.user.UserViewModel
import com.words.storageapp.ui.account.viewProfile.ProfileFragment
import com.words.storageapp.ui.main.MainActivity

class AccountActivity : AppCompatActivity() {

    //lateinit var userManager: UserManager
    lateinit var userRepository: UserRepository
    lateinit var appDiContainer: AppDiContainer

    //by viewModel enables us to implement the userViewModel lazily
    val userViewModel: UserViewModel by viewModels {
        appDiContainer.userFlowContainer?.userViewModelFactory!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        //userManager = (application as MyApplication).appContainer.userManager
        appDiContainer = (application as MyApplication).appContainer

        appDiContainer.userFlowContainer = with(appDiContainer) {
            UserFlowContainer(appDatabase, fireStore, firebaseStorage, userManager)
        }

        //userRepository created from appSubContainer userFlowContainer and is scoped to accountActivity
        userRepository = appDiContainer.userFlowContainer?.userRepository!!

        checkIfUserExist()

        supportFragmentManager.beginTransaction()
            .add(R.id.accountFragment_holder, ProfileFragment())
            .commit()
    }

    /* override fun onResume() {
         super.onResume()
         checkIsUserExist()
     }*/

    override fun onDestroy() {
        appDiContainer.userFlowContainer = null
        super.onDestroy()

    }

    private fun checkIfUserExist() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navigateToAuthScreen()
        }
    }

    private fun navigateToAuthScreen() {
        val intent = Intent(this, Authentication::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (supportFragmentManager.backStackEntryCount == 0) {
            navigateToMainActivity()
        } else {
            super.onBackPressed()

        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
