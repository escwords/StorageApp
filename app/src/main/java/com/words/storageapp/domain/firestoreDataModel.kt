package com.words.storageapp.domain

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.words.storageapp.database.model.LoggedInUser
import java.util.*

//Define our Remote data structure here
//retrieved data model
//@IgnoreExtraProperties
//data class SelfSkilled(
//    val uId: String? = null,
//    val firstName: String? = null,
//    val lastName: String? = null,
//    val email: String? = null,
//    val phone: String? = null,
//    val address: String? = null,
//    val location: String? = null,
//    val state: String? = null,
//    val lga: String? = null,
//    val dob: String? = null,
//    val gender: String? = null,
//    val skills: String? = null,
//    val experience: String? = null,
//    val imageUrl: String? = null,
//    val description: String? = null
//)

fun RegisterUser.toLoggedInUser(): LoggedInUser {
    return LoggedInUser(
        id = userId!!,
        first_name = firstName,
        last_name = lastName,
        email = email,
        image_url = imageUrl,
        mobile = phone,
        address = address,
        state = state,
        lga = lga,
        dob = dob,
        gender = gender,
        wageRate = wageRate,
        skills = skills,
        experience = experience,
        description = serviceOffered,
        date_joined = date_joined

    )
}

//register user data model
@IgnoreExtraProperties
data class RegisterUser(
    var address: String? = null,
    @ServerTimestamp var date_joined: Date? = null,
    var dob: String? = null,
    var education: String? = null,
    var email: String? = null,
    var experience: String? = null,
    var firstName: String? = null,
    var gender: String? = null,
    var imageUrl: String? = null,
    var lastName: String? = null,
    var lga: String? = null,
    var phone: String? = null,
    var serviceOffered: String? = null, // this field contains more of what the user can do
    var skillId: String? = null,
    var skills: String? = null,
    var state: String? = null,
    var userId: String? = null,
    var wageRate: String? = null
)