package com.wyh.happyyousdk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.wyh.happyyousdk.APIEncryption.APIInterface
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.Activities.SpinWheelActivity
import com.wyh.happyyousdk.SpinWheel.Utilities.DownloadImage
import com.wyh.happyyousdk.login.MobileNumberActivity
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyh.happyyousdk.utils.wheelview.WheelItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.Serializable
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Master {

    const val PACKAGE_ID = "packageID"
    const val PACKAGE_NAME = "packageName"
    const val PATIENT_NAME = "patientName"
    const val PATIENT_EMAIL = "patientEmail"
    const val PATIENT_AGE = "patientAge"
    const val PATIENT_MOBILE = "patientMobile"
    const val PATIENT_GENDER = "patientGender"
    const val PATIENT_ADDRESS = "patientAddress"
    const val LAB_ID = "labID"
    const val APPIONTMENT_DATE = "appointmentDate"
    const val APPIONTMENT_TIME = "appointmentTime"

    val tempToken =
        "S5C/CBK71i9u35w6zuQhpY+gR3ZfQ2Ov+VWZe99G5Cemzmgdf/ub8Tw9eiY0miPBJpzS0E0cYWoTqMCYKV96svXmJ8Gk5bu0m2u5TaaG3ghyTKYLOD8Z+5apyw8EG5rCribJmSVfDRRsRd0Z7VCl/bvfWoy6UhJ6Jcj88M+Rivk="

    fun openCamera(context: Activity) {
        ImagePicker.cameraOnly().imageDirectory("Camera").start(context)
    }

    @SuppressLint("Range")
    fun getRealPathFromURI(uri: Uri, uriString: String, myFile: File, context: Activity): String? {
        var displayName: String? = null
        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.name
        }
        return displayName
    }

    @Throws(IllegalArgumentException::class)
    fun getFileName(uri: Uri, context: Activity): String? {
        // Obtain a cursor with information regarding this uri
        val cursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        if (cursor.count <= 0) {
            cursor.close()
            throw IllegalArgumentException("Can't obtain file name, cursor is empty")
        }
        cursor.moveToFirst()
        val fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor.close()
        return fileName
    }


    fun logOut(context: Context) {
        Toast.makeText(context, context.resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
        getQuadrants(context)
     /*   val intent = Intent(context, MobileNumberActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        SharedPref.clearSharedPref()
        (context as Activity).finish()*/
    }

    private fun getQuadrants(context: Context) {
        try {
//            CommonUtils.showProgressDialige(context)
            val apiInterface: APIInterface = RetrofitHandler.apiInterface()
            apiInterface.getQuadrant().enqueue(object : Callback<GetQuadrantsResponse?> {
                override fun onResponse(call: Call<GetQuadrantsResponse?>, response: Response<GetQuadrantsResponse?>) {
                    if (response.body() != null && response.code() == 200) {
                        if (response.body()!!.getData() != null && !response.body()!!.getData().getQuadrantData().isEmpty()) {
                            CommonUtils.wheelItems.clear()
                            for (i in response.body()!!.getData().getQuadrantData().indices) {
                                val finalI = i
                                val imageBitmap = arrayOf<Bitmap?>(null)
                                DownloadImage { bitmap: Bitmap? ->
                                    if (bitmap != null) {
                                        imageBitmap[0] = bitmap
                                        val scaledBitmap = Bitmap.createScaledBitmap(imageBitmap[0]!!, 80, 80, true)

                                        val wheelItem = WheelItem(
                                            Color.parseColor(response.body()!!.getData().getQuadrantData()[finalI].getQuadrantColor()),
                                            scaledBitmap,
                                            response.body()!!.getData().getQuadrantData()[finalI]
                                                .getRewardName()
                                        )
                                        if (CommonUtils.wheelItems.size < response.body()!!.getData().getQuadrantData().size) {
                                            CommonUtils.wheelItems.add(wheelItem)
                                            Log.d("AuthToken", CommonUtils.wheelItems.size.toString())
                                        }


                                        if (CommonUtils.wheelItems.size == response.body()!!.getData().getQuadrantData().size) {
                                            CommonUtils.dismissDialoge()
                                            val intent = Intent(context, SpinWheelActivity::class.java)
                                            intent.putExtra("quadrantData", response.body()!!.getData().getQuadrantData() as Serializable)
                                            intent.putExtra("description", response.body()!!.getData().getDescription())
                                            intent.putExtra("timer", response.body()!!.getData().getExpiryInMinutes())
                                            context.startActivity(intent)
                                            SharedPref.clearSharedPref()
                                            (context as? Activity)?.finishAffinity()
                                        }
                                    } else {
//                                        CommonUtils.dismissDialoge()
                                        //Log.e("DownloadError", "Failed to download image.");
                                    }
                                }.execute(
                                    response.body()!!.getData().getQuadrantData()[i].getRewardIcon()
                                )
                            }
                        } else {
                            val intent = Intent(context, MobileNumberActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context.startActivity(intent)
                            SharedPref.clearSharedPref()
                            (context as? Activity)?.finishAffinity()
                        }
                    } else {
                        val intent = Intent(context, MobileNumberActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        SharedPref.clearSharedPref()
                        (context as? Activity)?.finishAffinity()
                    }
                }

                override fun onFailure(call: Call<GetQuadrantsResponse?>, t: Throwable) {
//                    CommonUtils.dismissDialoge()
                    val intent = Intent(context, MobileNumberActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                    SharedPref.clearSharedPref()
                    (context as? Activity)?.finishAffinity()
                }
            })
        } catch (e: java.lang.Exception) {
//            CommonUtils.dismissDialoge()
            val intent = Intent(context, MobileNumberActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            SharedPref.clearSharedPref()
            (context as? Activity)?.finishAffinity()
            e.printStackTrace()
        }
    }

    fun numberFormatConverter(amount: String): String {
        val format = NumberFormat.getNumberInstance(Locale("en", "in"))
        format.minimumFractionDigits = 2
        format.maximumFractionDigits = 2
        return format.format(amount.toDouble())
    }

    fun isPolicyExpired(endDate: String): Boolean {
        val currentTime = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val policyEndDate: Date? = dateFormat.parse(endDate)
        return currentTime.after(policyEndDate)
    }

    fun dateDifference(date1str: String, date2str: String): Boolean {
        val c = Calendar.getInstance()
        var difference = 0
        var returnValue = false
        val df: SimpleDateFormat = if (date1str.contains("/")) SimpleDateFormat("dd/MM/yyyy") else SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        try {
            val date2 = df.parse(date1str)
            val date1 = df.parse(df.format(c.time))
            difference = printDifference(date1!!, date2!!)
            returnValue = difference >= 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return returnValue
    }


    private fun printDifference(startDate: Date, endDate: Date): Int {
        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        )
        return elapsedDays.toInt()
    }
}