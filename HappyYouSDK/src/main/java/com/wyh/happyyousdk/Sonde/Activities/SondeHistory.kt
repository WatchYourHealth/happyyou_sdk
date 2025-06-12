package com.wyh.happyyousdk.Sonde.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Adapter.SondeHistoryAdapter

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivitySondeHistoryBinding
import com.wyh.happyyousdk.model.response.MentalWellnessVoiceFeature
import com.wyh.happyyousdk.model.response.SondeHistoryData
import com.wyh.happyyousdk.model.response.SondeHistoryResponse
import com.wyh.happyyousdk.model.response.VoiceFeature
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SondeHistory : AppCompatActivity() {

    lateinit var binding : ActivitySondeHistoryBinding
    var comingFrom = ""
    var diaryConcern = false
    var mentalWellnesslist : ArrayList<SondeHistoryData> = ArrayList()
    var respiratoryList : ArrayList<SondeHistoryData> = ArrayList()
    var voiceFeature : ArrayList<VoiceFeature> = ArrayList()
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(this)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sonde_history)
        setContentView(binding.root)
        context = this

        comingFrom = intent.getStringExtra("comingFrom").toString()

        val customObj = JSONObject()
        try {
            customObj.put("PAGE_ID", "SondeHistory")
            customObj.put("type", comingFrom)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        
        getHistroy()

        binding.challengesBackLayout.setOnClickListener {
            finish()
        }


        binding.ivHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }


    private fun getHistroy() {
        try {
            CommonUtils.showProgressDialige(this)
            val apiIInterface = RetrofitHandler.apiInterface()
            apiIInterface.getSondeHistory(SharedPref.getAuthToken()).enqueue(object : Callback<SondeHistoryResponse> { override fun onResponse(
                    call: Call<SondeHistoryResponse>, response: Response<SondeHistoryResponse>) {
                        response.body().let {
                            CommonUtils.dismissDialoge()
                            if (response.code() == 200 && response.body()!!.success) {
                                try {
                                    if (response.body()!!.data != null) {
                                        if (comingFrom.equals("metalFitness", true)) {
                                            response.body()!!.data.forEachIndexed { i, sondeHistoryData ->
                                                if (sondeHistoryData.feature.equals("mental-fitness", true)) {
                                                    mentalWellnesslist.add(sondeHistoryData)
//                                                    val voice = sondeHistoryData.subFeatureScore
//                                                    val jsonArray = JSONArray(voice)
//
//                                                    for(i in 0 until jsonArray.length()){
//                                                        val jsonObject = jsonArray.getJSONObject(i)
//                                                        val voiceFeatureObj = VoiceFeature(jsonObject.getString("name")
//                                                            ,jsonObject.getString("score"))
//                                                        voiceFeature.add(voiceFeatureObj)
//                                                    }
                                                    binding.sondeHistoryRecyclerList.layoutManager =
                                                        LinearLayoutManager(this@SondeHistory)
                                                    binding.sondeHistoryRecyclerList.hasFixedSize()
                                                    val adapter = SondeHistoryAdapter(
                                                        this@SondeHistory,
                                                        mentalWellnesslist,
                                                        comingFrom
                                                    )
                                                    binding.sondeHistoryRecyclerList.adapter =
                                                        adapter
                                                }
                                            }
                                        } else {
                                            response.body()!!.data.forEachIndexed { _, sondeHistoryData ->
                                                if (!sondeHistoryData.feature.equals("mental-fitness", true)) {
                                                    respiratoryList.add(sondeHistoryData)
                                                    binding.sondeHistoryRecyclerList.layoutManager =
                                                        LinearLayoutManager(this@SondeHistory)
                                                    binding.sondeHistoryRecyclerList.hasFixedSize()
                                                    val adapter = SondeHistoryAdapter(
                                                        this@SondeHistory,
                                                        respiratoryList,
                                                        comingFrom
                                                    )
                                                    binding.sondeHistoryRecyclerList.adapter =
                                                        adapter
                                                }
                                            }
                                        }

                                    }
                                } catch (e: Exception) {
                                    e.message
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<SondeHistoryResponse>, t: Throwable) {
                        CommonUtils.dismissDialoge()
                    }

                })
        } catch (e: Exception) {
            CommonUtils.dismissDialoge()
            e.message
        }
    }
}