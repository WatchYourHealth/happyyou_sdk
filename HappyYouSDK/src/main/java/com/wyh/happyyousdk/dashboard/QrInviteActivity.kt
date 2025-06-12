package com.wyh.happyyousdk.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.ActivityQrInviteBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.Qrrequestmodel
import com.wyh.happyyousdk.model.response.QrResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
/*import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode*/
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

@Suppress("IMPLICIT_CAST_TO_ANY")
class QrInviteActivity : AppCompatActivity() {
    /*private lateinit var binding: ActivityQrInviteBinding
    var context: Context? = null
    var apiInterfaceWyh: ApiInterfaceWyh? = null

    var PERMISSION_ID = 44
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var latitude: String? = null
    var longitude: String? = null
    var city = ""

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->

        val text = when (result) {
            is QRResult.QRSuccess ->   SaveQRToken(result.content.rawValue)
            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_invite)
        context = this
        SharedPref.init(context)
        SharedPreference.init(context)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lastLocation
        apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context)).create(
            ApiInterfaceWyh::class.java
        )
        fetchToken()
        this.binding.welcometext.text = "Welcome to the event scan QR to enter event"
        binding.scanqrbtn.setOnClickListener { checkPermissioncamera() }
        binding.includeBack.llBack.setOnClickListener { view: View? -> finish() }
        binding.includeBack.tvBack.setTextColor(resources.getColor(R.color.white))
        binding.includeBack.ivBack.setColorFilter(resources.getColor(R.color.white))

    }
    val lastLocation: Unit
        get() {
            // check if permissions are given
            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled()) {

                    // getting last
                    // location from
                    // FusedLocationClient
                    // object
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    mFusedLocationClient!!.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                        val location: Location? = task.getResult()
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude = location.getLatitude().toString() + ""
                            longitude = location.getLongitude().toString() + ""
                            getCurrentCityName(latitude!!, longitude!!)
                        }
                    }
                } else {
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                // if permissions aren't available,
                // request for permissions
                requestPermissions()
            }
        }

    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation!!.latitude.toString() + ""
            longitude = mLastLocation.longitude.toString() + ""
            getCurrentCityName(latitude!!, longitude!!)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCurrentCityName(latitude: String, longitude: String) {
        val gcd = Geocoder(
            context,
            Locale.getDefault()
        )
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
            if (addresses.size > 0) city = addresses[0].locality
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@QrInviteActivity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }
    private fun checkPermissioncamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted
            // Do something with the camera
            scanQrCodeLauncher.launch(null)
        } else {
            // Permission is not granted
            // Request for the permission
            ActivityCompat.requestPermissions(
                this@QrInviteActivity,
                arrayOf(Manifest.permission.CAMERA),
                223
            )
        }
    }



    private fun SaveQRToken(result: String) {
       *//* Toast.makeText(this, result, Toast.LENGTH_LONG)
            .show()*//*
        val request = Qrrequestmodel(city, result)
        val call = apiInterfaceWyh!!.saveQRToken(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.body() != null && response.code() == 200 && response.body()!!.isSuccess) {
                    fetchToken()
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }// if permissions aren't available,
    var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            fetchToken()
        }
    }

    private fun fetchToken() {
        val call = apiInterfaceWyh!!.fetchQRToken(SharedPref.getAuthToken())
        call.enqueue(object : Callback<QrResponse?> {
            override fun onResponse(call: Call<QrResponse?>, response: Response<QrResponse?>) {
                *//*  if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();*//*
                if (response.code() == 200 && response.body() != null) {
                    //  Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_journal_upload_events_success));
                    val data = response.body()!!
                        .data
                    if (!data.isEmpty()) {
                      setData(data)
                    } else {
                        binding!!.scanqrlayout.visibility = View.VISIBLE
                        binding!!.congralayout.visibility = View.GONE
                    }
                } else {
                    Analytics.logEvent(
                        context,
                        context!!.javaClass.name,
                        context!!.getString(R.string.fetch_pending_journal_upload_events_failed)
                    )
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<QrResponse?>, t: Throwable) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setData(data: List<QrResponse.Datum>) {
        binding!!.congralayout.visibility = View.VISIBLE
        binding!!.scanqrlayout.visibility = View.GONE
        try {
            *//*  ClickableSpan token = new ClickableSpan() {
                @Override
                public void onClick(View view) {

                }
            };*//*
            val textCondition = "Your code is : " + data[0].tokenId

            *//*  SpannableString textCondition = new SpannableString("Your code is : "+data.get(0).getTokenId());
            int endPosition = textCondition.length();
            textCondition.setSpan(token, 15, endPosition, 0);*//*binding!!.couponcodetextview.text =
                textCondition
        } catch (e: Exception) {
            binding!!.scanqrlayout.visibility = View.VISIBLE
            Toast.makeText(
                context,
                "Sorry.Some thing went wrong.\n Please try again ! ",
                Toast.LENGTH_LONG
            ).show()
            binding!!.congralayout.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 223) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                // Do something with the camera
                checkPermissioncamera()
            } else {
                // Permission is not granted
                // Show an explanation to the user and request for the permission again
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    // Show an explanation
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Camera permission")
                    builder.setMessage("We need your permission to use the camera")
                    builder.setPositiveButton("OK") { dialog, which -> // Request for the permission again
                        ActivityCompat.requestPermissions(
                            this@QrInviteActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            223
                        )
                    }
                    builder.setNegativeButton("Cancel", null)
                    builder.show()
                } else {
                    // User denied the permission without showing an explanation
                    // Show a toast message or take appropriate action
                }
            }
        }
    }*/
}