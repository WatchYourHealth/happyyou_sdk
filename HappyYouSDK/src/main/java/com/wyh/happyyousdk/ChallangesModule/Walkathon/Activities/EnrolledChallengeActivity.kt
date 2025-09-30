package com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.wyh.happyyousdk.APIEncryption.APILogs.activityTracker
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Adapters.ChallengeEnrolledAdapter
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Helper.Master
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.crypto.RSAEncryption
import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.dashboard.ScannerIntegrate
import com.wyh.happyyousdk.databinding.ActivityEnrolledChallengeBinding
import com.wyh.happyyousdk.model.request.BranchData
import com.wyh.happyyousdk.model.request.EnrollUserRequest
import com.wyh.happyyousdk.model.request.EnrolledHistoryResponse
import com.wyh.happyyousdk.model.response.EnrollUserResponse
import com.wyh.happyyousdk.model.response.EnrolledQRModel
import com.wyh.happyyousdk.syncDevice.ConnectApp
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EnrolledChallengeActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnrolledChallengeBinding
    lateinit var enrolledAdapter: ChallengeEnrolledAdapter
    var enrolledList: ArrayList<BranchData> = arrayListOf()
    var validationList: ArrayList<BranchData> = arrayListOf()
    var startDate = ""
    var scanningTime = ""
    var isStarted = 0
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_enrolled_challenge)
        binding.includeBack.tvBack.text = resources.getString(R.string.walkathon)
        SharedPref.init(this)
        SharedPreference.init(this)
        context = this
        binding.includeBack.llBack.setOnClickListener {
            finish()
        }

        binding.walkingBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_walking.json")

        startDate = intent.getStringExtra("startDate").toString()
        isStarted = intent.getIntExtra("isStarted", 0)



        binding.tvStartDate.text = getFormatedDate(startDate)


        binding.syncDeviceLayout.setOnClickListener {
            activityTracker("A_ENROLLMENT_SYNC_DEVICE", this@EnrolledChallengeActivity)
            startActivity(Intent(this, ConnectApp::class.java))
        }

        binding.infoImg.setOnClickListener {
            activityTracker("A_ENROLLMENT_INFO", this@EnrolledChallengeActivity)
            if (binding.branchEnrollmentTv.text.toString() == "NA" && binding.branchCountTv.text.toString() == "NA") {
                Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show()
            } else {
                Master.getBranchUser(this)
            }
        }

        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }



        getEnrolledUserHistroy()

        binding.scanqrbtn.setOnClickListener {
            scanningTime = "firstTime"

            val intentIntegrator = IntentIntegrator(this@EnrolledChallengeActivity)
            intentIntegrator.setPrompt("Place a barcode/QR Code inside the viewfinder rectangle to scan it.")
            intentIntegrator.setCameraId(0)
            intentIntegrator.captureActivity = ScannerIntegrate::class.java
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES)
            activityTracker("A_ENROLLMENT_QR_SCAN", this@EnrolledChallengeActivity)

        }

        binding.rescanqrbtn.setOnClickListener {
            scanningTime = "retry"
            activityTracker("A_ENROLLMENT_QR_RESCAN", this@EnrolledChallengeActivity)
            Master.enrolledDialoge(this, startDate, "retry", isStarted)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {

            }
            else {
                Log.d("AuthToken", "QR ${intentResult.contents}")
                val type = object : TypeToken<EnrolledQRModel?>() {}.type
                val enrolledData: EnrolledQRModel = Gson().fromJson(intentResult.contents, type)
                val branchCode = RSAEncryption.callDecryptionMethod(enrolledData.BranchCode)
                Log.d("AuthToken", "Decrypted QR $branchCode")

                if (validationList.isNotEmpty()) {
                    if (validationList[0].branchCode == branchCode) {
                        Toast.makeText(
                            this,
                            "You are already enrolled with the same branch",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        enrolledUser(branchCode)

                    }
                } else {
                    enrolledUser(branchCode)

                }
            }
        }

    }

    fun enrolledUser(branchCode: String) {
        try {
            CommonUtils.showProgressDialige(this)
            val enrolledUserRequest = EnrollUserRequest(branchCode)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.enrollUser(SharedPref.getAuthToken(), enrolledUserRequest)
                .enqueue(object : Callback<EnrollUserResponse> {
                    override fun onResponse(
                        call: Call<EnrollUserResponse>,
                        response: Response<EnrollUserResponse>
                    ) {
                        CommonUtils.dismissDialoge()

                        if (response.code() == 200 && response.isSuccessful) {
                            getEnrolledUserHistroy()
                            Master.enrolledDialoge(
                                this@EnrolledChallengeActivity,
                                startDate,
                                "firstTime",
                                isStarted
                            )

                            /*if(scanningTime.equals("firstTime",true)){
                            }*/
                        }
                        /*sendLogs(
                            call.request().url().toString(),
                            if(response.body() != null) response.body()!!.msg else "success",
                            response.code().toString(),
                            this@EnrolledChallengeActivity
                        )*/
                    }

                    override fun onFailure(call: Call<EnrollUserResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                       /* t.message?.let {
                            sendLogs(
                                call.request().url().toString(),
                                it,
                                "onFailure",
                                this@EnrolledChallengeActivity
                            )
                        }*/

                    }

                })

        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
            /*e.message?.let {
                sendLogs(
                    Constants.EXCEPTION, it, "Exception",
                    this@EnrolledChallengeActivity
                )
            }*/
        }
    }

    override fun onResume() {
        super.onResume()
        if (!SharedPreference.getGoogleFitConnection()) {
            binding.syncDeviceLayout.visibility = View.VISIBLE
        } else {
            binding.syncDeviceLayout.visibility = View.GONE
        }
    }

    fun getEnrolledUserHistroy() {
        try {
            CommonUtils.showProgressDialige(this)
            val apiInterface = RetrofitHandler.apiInterface()
            apiInterface.getEnrolledHistory(SharedPref.getAuthToken())
                .enqueue(object : Callback<EnrolledHistoryResponse> {
                    override fun onResponse(
                        call: Call<EnrolledHistoryResponse>,
                        response: Response<EnrolledHistoryResponse>
                    ) {
                        CommonUtils.dismissDialoge()

                        /*Log.d("response", Gson().toJson(call.request().url()))
                        Log.d("response", Gson().toJson(response.code()))
                        Log.d("response", Gson().toJson(response.body()))
                        Log.d("response", Gson().toJson(response.body()!!.data.branchDetails.size))*/

                        if (response.body() != null && response.code() == 200 && response.isSuccessful) {

                            if (response.body()!!.data.branchDetails.isEmpty()) {
                                binding.newEnrolledLayout.visibility = View.GONE
                                binding.enrolledHistoryLayout.visibility = View.GONE
                                binding.congratsTv.visibility = View.GONE
                                binding.scanqrbtn.visibility = View.VISIBLE
                                binding.eventTv.text =
                                    resources.getString(R.string.walkathon_event_text)
                                binding.rescanqrbtn.visibility = View.GONE
                                if (response.body()!!.data.enrollStats.isNotEmpty()) {
                                    val allIndiaCount: List<String>  = response.body()!!.data.enrollStats[0].allIndiaEnrollmentCount.split(" ")
                                    binding.allCountTv.text = allIndiaCount[0]
                                    binding.allTotalCountTv.text = allIndiaCount.drop(1).joinToString(separator = " ")
                                   /* if(response.body()!!.data.enrollStats[0].myBranchEnrollmentPercentage){

                                    }
                                    binding.branchEnrollmentTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentPercentage
                                    binding.branchCountTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentCount*/
                                }
                                //binding.enrollmentDetailLayout.visibility = View.GONE
                                //binding.countLayout.visibility = View.GONE
                            } else if (response.body()!!.data.branchDetails.size == 1) {
                                binding.newEnrolledLayout.visibility = View.VISIBLE
                                binding.enrolledHistoryLayout.visibility = View.GONE
                                binding.scanqrbtn.visibility = View.GONE
                                binding.congratsTv.visibility = View.VISIBLE
                                binding.rescanqrbtn.visibility = View.VISIBLE
                                binding.enrollmentDetailLayout.visibility = View.VISIBLE
                                binding.countLayout.visibility = View.VISIBLE
                                binding.eventTv.text =
                                    resources.getString(R.string.walkathon_congrats_text)


                                validationList.clear()
                                response.body()!!.data.branchDetails.forEachIndexed { index, historyData ->
                                    validationList.add(historyData)
                                }


                                binding.branchId.text =
                                    response.body()!!.data.branchDetails[0].branchCode
                                binding.branchName.text =
                                    response.body()!!.data.branchDetails[0].branchName
                                binding.enrollDate.text =
                                    response.body()!!.data.branchDetails[0].scannedDate
                                binding.enrollAddress.text =
                                    response.body()!!.data.branchDetails[0].participationCode

                                if (response.body()!!.data.enrollStats.isNotEmpty()) {
                                    val allIndiaCount: List<String>  = response.body()!!.data.enrollStats[0].allIndiaEnrollmentCount.split(" ")
                                    binding.allCountTv.text = allIndiaCount[0]
                                    binding.allTotalCountTv.text = allIndiaCount.drop(1).joinToString(separator = " ")
                                    binding.branchEnrollmentTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentPercentage
                                    binding.branchCountTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentCount
                                }

                            } else {
                                enrolledList.clear()
                                validationList.clear()
                                response.body()!!.data.branchDetails.forEachIndexed { index, historyData ->
                                    validationList.add(historyData)
                                    if (index > 0) {
                                        enrolledList.add(historyData)
                                    }
                                }
                                binding.enrolledHistoryLayout.visibility = View.VISIBLE
                                binding.newEnrolledLayout.visibility = View.VISIBLE
                                binding.congratsTv.visibility = View.VISIBLE
                                binding.rescanqrbtn.visibility = View.VISIBLE
                                binding.scanqrbtn.visibility = View.GONE
                                binding.enrollmentDetailLayout.visibility = View.VISIBLE
                                binding.countLayout.visibility = View.VISIBLE
                                binding.eventTv.text =
                                    resources.getString(R.string.walkathon_congrats_text)


                                binding.branchId.text =
                                    response.body()!!.data.branchDetails[0].branchCode
                                binding.branchName.text =
                                    response.body()!!.data.branchDetails[0].branchName
                                binding.enrollDate.text =
                                    response.body()!!.data.branchDetails[0].scannedDate
                                binding.enrollAddress.text =
                                    response.body()!!.data.branchDetails[0].participationCode

                                if (response.body()!!.data.enrollStats.isNotEmpty()) {
                                    val allIndiaCount: List<String>  = response.body()!!.data.enrollStats[0].allIndiaEnrollmentCount.split(" ")
                                    binding.allCountTv.text = allIndiaCount[0]
                                    binding.allTotalCountTv.text = allIndiaCount.drop(1).joinToString(separator = " ")
                                    binding.branchEnrollmentTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentPercentage
                                    binding.branchCountTv.text =
                                        response.body()!!.data.enrollStats[0].myBranchEnrollmentCount
                                }


                                binding.challengeEnrolledList.layoutManager =
                                    LinearLayoutManager(this@EnrolledChallengeActivity)
                                enrolledAdapter = ChallengeEnrolledAdapter(
                                    this@EnrolledChallengeActivity,
                                    enrolledList
                                )
                                binding.challengeEnrolledList.adapter = enrolledAdapter
                            }


                        }

                    }

                    override fun onFailure(call: Call<EnrolledHistoryResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()

                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }


    fun getFormatedDate(inputDate: String): String {
        var formattedDate = ""
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date: Date = inputFormat.parse(inputDate)
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            formattedDate = outputFormat.format(date)
        } catch (e: Exception) {
            e.toString()
        }

        return formattedDate

    }

}


