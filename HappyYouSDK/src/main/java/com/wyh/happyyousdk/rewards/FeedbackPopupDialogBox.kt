package com.wyh.happyyousdk.rewards

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.gson.Gson
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler.apiInterface
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack
import com.wyh.happyyousdk.dashboard.adapter.ChipsRecyclerViewAdapter
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.featureName
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper.Companion.trasactionId
import com.wyh.happyyousdk.databinding.CustomFeedbackLayoutBinding
import com.wyh.happyyousdk.databinding.FeedbackOverallPopupLayoutBinding
import com.wyh.happyyousdk.model.CommonSuccessResponse
import com.wyh.happyyousdk.model.request.AddUserFeedbackRequest
import com.wyh.happyyousdk.model.request.FeedbackQuestionModelRequest
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest
import com.wyh.happyyousdk.model.response.ChipsModel
import com.wyh.happyyousdk.model.response.CustomFeedbackRespModel
import com.wyh.happyyousdk.model.response.FeedbackQuestionModel
import com.wyh.happyyousdk.model.response.FeedbackQuestionModelData
import com.wyh.happyyousdk.model.response.FeedbackResponseData
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.rewards.adapter.FeedbackCustomOptionRVAdapter
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedbackPopupDialogBox private constructor() :
    FeedbackCustomOptionRVAdapter.OnItemClickListener {

    var answer = ""

    companion object {
        @Volatile
        private var instance: FeedbackPopupDialogBox? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FeedbackPopupDialogBox().also { instance = it }
        }
    }


    val data: ArrayList<FeedbackQuestionModelData> = ArrayList()
    var currentIndex = 0
    var currentType = ""


    fun showPopUpFeedback(activity: Activity, feedbackResponseData: FeedbackResponseData) {
        if (feedbackResponseData.customFeedbackRespModel != null && feedbackResponseData.customFeedbackRespModel.status == 1 && feedbackResponseData.customFeedbackRespModel.questionJSON != null) {
            showCustomFeedback(
                activity,
                feedbackResponseData.customFeedbackRespModel,
                if (feedbackResponseData.feedbackModel != null) feedbackResponseData.feedbackModel.featureModuleName else "Custom"
            )
        } else {
            showPopUpFeedbackOverall(activity, feedbackResponseData)
        }
    }

    fun showPopUpFeedbackCallback(activity: Activity, feedbackResponseData: FeedbackResponseData, spinRewardRecieved: spinRewardCallBack) {

        showCustomFeedbackCallBack(
            activity,
            feedbackResponseData.customFeedbackRespModel,
            if (feedbackResponseData.feedbackModel != null) feedbackResponseData.feedbackModel.featureModuleName else "Custom",spinRewardRecieved
        )

    }

    private fun showPopUpFeedbackOverall(
        activity: Context,
        feedbackResponseData: FeedbackResponseData
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(activity)
            val binding: FeedbackOverallPopupLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.feedback_overall_popup_layout, null, false
            )
            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)
            val selectedChips = ArrayList<String>()


            binding.tvFeedbackDes.text = feedbackResponseData.feedbackModel.feedbackDesc

            alertDialog.setOnDismissListener {
                NewDashboardHelper.isShowUserPopup = false
                alertDialog.dismiss()
            }
            binding.ratingBar.setOnRatingBarChangeListener { _, _, _ ->
                val rating: Int = binding.ratingBar.rating.toInt()
                feedbackResponseData.starConfig.forEach {
                    if (rating == it.starCount) {
                        selectedChips.clear();
                        val chipsData = ArrayList<ChipsModel>()
                        val keywordList: List<String> = it.keywords.split(",")
                        keywordList.forEach { key ->
                            chipsData.add(ChipsModel(key, false))
                        }
                        val adapter = ChipsRecyclerViewAdapter(
                            activity, chipsData
                        ) { name, isAdd ->
                            if (isAdd) {
                                if (!name.isNullOrEmpty()) {
                                    selectedChips.add(name)
                                }
                            } else {
                                selectedChips.remove(name)
                            }
                        }
                        val chipsLayoutManager =
                            ChipsLayoutManager.newBuilder(activity)
                                .setChildGravity(Gravity.TOP)
                                .setScrollingEnabled(true)
                                .setMaxViewsInRow(4)
                                .setGravityResolver { Gravity.CENTER }
                                .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
                                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                                .build()
                        binding.rvChips.visibility = View.VISIBLE
                        binding.rvChips.adapter = adapter
                        binding.rvChips.layoutManager = chipsLayoutManager
                    }
                }
            }


            binding.btnSubmit.setOnClickListener {
                if (binding.ratingBar.rating.toInt() == 0) {
                    Toast.makeText(activity, "Please select ratings ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (selectedChips.isEmpty()) {
                    Toast.makeText(activity, "Please select rating keywords", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (binding.edtFeedback.text.toString().isEmpty()) {
                    Toast.makeText(activity, "Please enter your feedback", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                NewDashboardHelper.isShowUserPopup = true
                val rating: Int = binding.ratingBar.rating.toInt()
                val keywords = selectedChips.joinToString(separator = ",")
                val request = AddUserFeedbackRequest(
                    feedbackResponseData.feedbackModel.featureModuleName,
                    rating,
                    keywords,
                    binding.edtFeedback.text.toString(),
                    feedbackResponseData.feedbackModel.moduleConfigId
                )

                saveFeedbackOverAll(activity as Activity, request, alertDialog)
            }
            binding.btnClosed.setOnClickListener { alertDialog.dismiss() }
            Log.d("call api", "inside dialog")
            val displayRectangle = Rect()
            val window: Window = (activity as Activity).window
            if (!alertDialog.isShowing && window.decorView.rootView.isShown) alertDialog.show()
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: Exception) {
            Log.d("error", e.message ?: "catch error")
        }
    }


    private fun showCustomFeedback(
        activity: Activity,
        feedbackData: CustomFeedbackRespModel,
        featureName: String
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(activity)
            val binding: CustomFeedbackLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.custom_feedback_layout, null, false
            )
            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)

            data.clear()
            currentIndex = 0

            val question =
                Gson().fromJson(feedbackData.questionJSON, FeedbackQuestionModel::class.java)
            data.addAll(question.customFeedback)

            if (question.description != null && question.description != "") {
                binding.tvFeedbackDes.text = question.description
            }

            setData(activity, binding, data[0].type)

            alertDialog.setOnDismissListener { alertDialog.dismiss() }



            binding.btnNext.setOnClickListener {
                if (currentIndex < (data.size - 1)) {

                    //only execute if question type is userInput
                    if (currentType.equals(
                            "userInput",
                            ignoreCase = true
                        ) || currentType.equals("text", ignoreCase = true)
                    ) {
                        data[currentIndex].ans = binding.tvFeedbackAnswer.text.toString()
                        binding.tvFeedbackAnswer.text.clear()
                    }

                    if (currentType.equals("Both", true)) {
                        data[currentIndex].ans =
                            "$answer,${binding.tvFeedbackAnswer.text.toString()}"
                        binding.tvFeedbackAnswer.text.clear()

                    }

                    //without answer user not able to go next
                    if (data[currentIndex].ans.isNotEmpty()) {
                        currentIndex++
                        setData(activity, binding, data[currentIndex].type)
                    }

                } else {
                    if (data[currentIndex].type.equals("Both", true)) {
                        data[currentIndex].ans =
                            "$answer,${binding.tvFeedbackAnswer.text.toString()}"
                        if (data[currentIndex].ans.isNotEmpty()) {
                            NewDashboardHelper.isShowUserPopup = true
                            val request = FeedbackQuestionModelRequest(
                                feedbackData.ccmmId,
                                featureName,
                                Gson().toJson(FeedbackQuestionModel(data, ""))
                            )
                            saveFeedbackCustom(activity, request, alertDialog)
                        }

                    } else {
                        if (data[currentIndex].ans.isNotEmpty()) {
                            NewDashboardHelper.isShowUserPopup = true
                            val request = FeedbackQuestionModelRequest(
                                feedbackData.ccmmId,
                                featureName,
                                Gson().toJson(FeedbackQuestionModel(data, ""))
                            )
                            saveFeedbackCustom(activity, request, alertDialog)
                        }
                    }

                }
            }

            binding.btnPrevious.setOnClickListener {
                if (currentIndex > 0) {
                    currentIndex--
                    setData(activity, binding, data[currentIndex].type)
                }
            }


            binding.btnClosed.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = false
                alertDialog.dismiss()
            }


            if (!alertDialog.isShowing) alertDialog.show()
            val displayRectangle = Rect()
            val window: Window = activity.window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: Exception) {
            Log.d("AuthToken", e.message ?: "catch error")
        }
    }

    private fun showCustomFeedbackCallBack(
        activity: Activity,
        feedbackData: CustomFeedbackRespModel,
        featureName: String,spinRewardRecieved: spinRewardCallBack
    ) {
        try {
            val alertBuilder = AlertDialog.Builder(activity)
            val binding: CustomFeedbackLayoutBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.custom_feedback_layout, null, false
            )
            alertBuilder.setView(binding.root)
            val alertDialog = alertBuilder.create()
            alertDialog.setCancelable(false)

            data.clear()
            currentIndex = 0

            val question =
                Gson().fromJson(feedbackData.questionJSON, FeedbackQuestionModel::class.java)
            data.addAll(question.customFeedback)

            if (question.description != null && question.description != "") {
                binding.tvFeedbackDes.text = question.description
            }

            setData(activity, binding, data[0].type)

            alertDialog.setOnDismissListener { alertDialog.dismiss() }



            binding.btnNext.setOnClickListener {
                if (currentIndex < (data.size - 1)) {

                    //only execute if question type is userInput
                    if (currentType.equals(
                            "userInput",
                            ignoreCase = true
                        ) || currentType.equals("text", ignoreCase = true)
                    ) {
                        data[currentIndex].ans = binding.tvFeedbackAnswer.text.toString()
                        binding.tvFeedbackAnswer.text.clear()
                    }

                    if (currentType.equals("Both", true)) {
                        data[currentIndex].ans =
                            "$answer,${binding.tvFeedbackAnswer.text.toString()}"
                        binding.tvFeedbackAnswer.text.clear()

                    }

                    //without answer user not able to go next
                    if (data[currentIndex].ans.isNotEmpty()) {
                        currentIndex++
                        setData(activity, binding, data[currentIndex].type)
                    }

                } else {
                    if (data[currentIndex].type.equals("Both", true)) {
                        data[currentIndex].ans =
                            "$answer,${binding.tvFeedbackAnswer.text.toString()}"
                        if (data[currentIndex].ans.isNotEmpty()) {
                            NewDashboardHelper.isShowUserPopup = true
                            val request = FeedbackQuestionModelRequest(
                                feedbackData.ccmmId,
                                featureName,
                                Gson().toJson(FeedbackQuestionModel(data, ""))
                            )
                            saveFeedbackCustomCallBack(activity, request, alertDialog,spinRewardRecieved)
                        }

                    } else {
                        if (data[currentIndex].ans.isNotEmpty()) {
                            NewDashboardHelper.isShowUserPopup = true
                            val request = FeedbackQuestionModelRequest(
                                feedbackData.ccmmId,
                                featureName,
                                Gson().toJson(FeedbackQuestionModel(data, ""))
                            )
                            saveFeedbackCustomCallBack(activity, request, alertDialog,spinRewardRecieved)
                        }
                    }

                }
            }

            binding.btnPrevious.setOnClickListener {
                if (currentIndex > 0) {
                    currentIndex--
                    setData(activity, binding, data[currentIndex].type)
                }
            }


            binding.btnClosed.setOnClickListener {
                NewDashboardHelper.isShowUserPopup = false
                alertDialog.dismiss()
            }


            if (!alertDialog.isShowing) alertDialog.show()
            val displayRectangle = Rect()
            val window: Window = activity.window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window!!.setLayout(
                (displayRectangle.width() *
                        0.8f).toInt(), RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } catch (e: Exception) {
            Log.d("AuthToken", e.message ?: "catch error")
        }
    }


    private fun setData(activity: Activity, binding: CustomFeedbackLayoutBinding, type: String) {
        binding.tvFeedbackQuestion.text = "Q.${currentIndex + 1} ${data[currentIndex].question}"

        if (currentIndex == (data.size - 1)) {
            binding.btnNext.text = activity.getString(R.string.submit)
        } else {
            binding.btnNext.text = activity.getString(R.string.next)
        }

        if(currentIndex == 0){
            binding.btnPrevious.isEnabled = false
            binding.btnPrevious.alpha = 0.1f
        }

        val ans = ArrayList<String>()
        currentType = type

        if (type.equals("userInput", ignoreCase = true) || type.equals("text", ignoreCase = true)) {
            binding.tvFeedbackAnswer.visibility = View.VISIBLE
            binding.rvOption.visibility = View.GONE

            if (data[currentIndex].ans.isNotEmpty()) {
                binding.tvFeedbackAnswer.setText(data[currentIndex].ans)
            }

        } else if (type.equals("Both", true)) {
            binding.rvOption.visibility = View.VISIBLE
            binding.tvFeedbackAnswer.visibility = View.VISIBLE
            if (data[currentIndex].ans.isNotEmpty()) {
                binding.tvFeedbackAnswer.setText(data[currentIndex].ans)
            }

            ans.addAll(data[currentIndex].ans.split(","))
            val adapter = FeedbackCustomOptionRVAdapter(
                activity,
                ans,
                data[currentIndex].type,
                data[currentIndex].option,
                this
            )
            binding.rvOption.adapter = adapter
        } else {
            if (type.equals(
                    "multiChoice",
                    ignoreCase = true
                ) && data[currentIndex].ans.isNotEmpty() && data[currentIndex].ans.contains(",")
            ) {
                ans.addAll(data[currentIndex].ans.split(","))
            } else {
                ans.add(data[currentIndex].ans);
            }

            binding.tvFeedbackAnswer.visibility = View.GONE
            binding.rvOption.visibility = View.VISIBLE

            val adapter = FeedbackCustomOptionRVAdapter(
                activity,
                ans,
                data[currentIndex].type,
                data[currentIndex].option,
                this
            )
            binding.rvOption.adapter = adapter
        }
    }


    private fun saveFeedbackOverAll(
        activity: Activity,
        request: AddUserFeedbackRequest,
        alertDialog: AlertDialog
    ) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterfaceWyh =
            ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity)).create<ApiInterfaceWyh>(
                ApiInterfaceWyh::class.java
            )
        val call = apiInterfaceWyh.addUserFeedback(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse> {
            override fun onResponse(
                call: Call<CommonSuccessResponse>,
                response: Response<CommonSuccessResponse>
            ) {
                /*Log.d("Feedback request", Gson().toJson(call.request()))
                Log.d("Feedback response", Gson().toJson(response.body()))
                Log.d("Feedback response", Gson().toJson(response.code()))*/
                progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                    alertDialog.dismiss()
                    Toast.makeText(activity, "Feedback added successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    activity,
                    activity.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun saveFeedbackCustom(
        activity: Activity,
        request: FeedbackQuestionModelRequest,
        alertDialog: AlertDialog
    ) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity))
            .create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
        val call = apiInterfaceWyh.customFeedback(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse> {
            override fun onResponse(
                call: Call<CommonSuccessResponse>,
                response: Response<CommonSuccessResponse>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                    alertDialog.dismiss()
                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Please something wend wrong", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    activity,
                    activity.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun saveFeedbackCustomCallBack(
        activity: Activity,
        request: FeedbackQuestionModelRequest,
        alertDialog: AlertDialog,spinRewardRecieved: spinRewardCallBack
    ) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(activity))
            .create<ApiInterfaceWyh>(ApiInterfaceWyh::class.java)
        val call = apiInterfaceWyh.customFeedback(SharedPref.getAuthToken(), request)
        call.enqueue(object : Callback<CommonSuccessResponse> {
            override fun onResponse(
                call: Call<CommonSuccessResponse>,
                response: Response<CommonSuccessResponse>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess) {
                    alertDialog.dismiss()
                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                    if(response.body() != null && response.body()!!.spinTheWheelRewardsModel != null) {
                        spinRewardRecieved.onRewardRecieved(response.body()!!.spinTheWheelRewardsModel)
                    }else if(checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward(spinRewardRecieved);

                    }
                } else {
                    Toast.makeText(activity, "Please something wend wrong", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onFailure(call: Call<CommonSuccessResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(
                    activity,
                    activity.getString(R.string.error_string),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun FetchQuizReward(spinRewardRecieved: spinRewardCallBack) {
        val activityRewardRequest = ActivityRewardRequest(trasactionId, featureName)
        val call = apiInterface().FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest)

        call.enqueue(object : Callback<CommonSuccessResponse?> {
            override fun onResponse(
                call: Call<CommonSuccessResponse?>,
                response: Response<CommonSuccessResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trasactionId = null
                    featureName = null
                    if (response.body()!!.quizathonRewardData != null) {
                        spinRewardRecieved.onQuizRewardRecieved(response.body()!!.quizathonRewardData)
                    }
                }
            }

            override fun onFailure(call: Call<CommonSuccessResponse?>, t: Throwable) {
            }
        })
    }
    fun checkIsFromQuizqathon(): Boolean {
        if (trasactionId != null && !trasactionId!!.isEmpty()) {
            return true
        }
        return false
    }

    override fun onSelected(name: String, position: Int) {
        if (data[currentIndex].type.equals("Both", true)) {
            if (name.equals("no", true)) {
                answer = "no"
            } else {
                answer = "yes"
            }
        } else {
            data[currentIndex].ans = name
        }
    }

    override fun onMultiSelected(name: String, position: Int) {
        if (data[currentIndex].ans.isNotEmpty()) {
            val ans: List<String> = data[currentIndex].ans.split(",")
            val a: MutableList<String> = mutableListOf()
            ans.forEach { item ->
                a.add(item)
            }

            if (a.contains(name)) {
                a.remove(name)
            } else {
                a.add(name)
            }

            data[currentIndex].ans = a.joinToString(separator = ",")
        } else {
            data[currentIndex].ans = name
        }
    }
}