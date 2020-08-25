package com.words.storageapp.util.utilities

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.ResultReceiver
import android.os.Bundle
import timber.log.Timber
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList


class FetchAddressIntentService : IntentService("FetchAddress") {

    private var receiver: ResultReceiver? = null

    @SuppressLint("TimberExceptionLogging")
    override fun onHandleIntent(intent: Intent?) {
        val errorMessage = arrayListOf<String>()
        receiver = intent?.getParcelableExtra(Constants.RECEIVER)

        if (intent == null || receiver == null) {
            Timber.i("No Receiver Received")
            return
        }

        val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

        if (location == null) {
            errorMessage[0] = "no location data provider"
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
            return
        }

        val geoCoder = Geocoder(this, Locale.getDefault())

        var addresses: List<Address> = emptyList()

        try {
            addresses = geoCoder.getFromLocation(
                location.latitude,
                location.longitude,
                5
            )
        } catch (ioException: IOException) {
            //catch network or other I/O problems.
            errorMessage[0] = "Service not Available"
            Timber.e(ioException, errorMessage[0])
        } catch (illegalArgumentException: IllegalArgumentException) {
            //catch invalid latitude or longitude
            errorMessage.add(" Invalid latitude and Longitude")
            Timber.e(illegalArgumentException, errorMessage[0])
        }

        //handle where no address is found
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage[0] = "No address found"
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)

        } else {
//            val address  = addresses[0]
//            val addressFragments = with(address){
//                (0..maxAddressLineIndex).map {
//                    getAddressLine(it) }
//            }

            val finalAddress = arrayListOf<String>()

            val address = addresses
            for (ad in address) {
                val resultAddress = with(ad) {
                    (0..maxAddressLineIndex).map { getAddressLine(it) }
                }
                finalAddress.add(resultAddress.joinToString("\n"))
            }
            Timber.i("Address Found")
            deliverResultToReceiver(
                Constants.SUCCESS_RESULT,
                finalAddress
            )
        }
    }

    private fun deliverResultToReceiver(resultCode: Int, message: ArrayList<String>) {
        val bundle = Bundle().apply { putStringArrayList(Constants.RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }


}