package com.wyh.happyyousdk.Sonde.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Adapter.MentalWellnessScoreDetails
import com.wyh.happyyousdk.Sonde.Utilities.Helper

import com.wyh.happyyousdk.dashboard.NewDashboardActivity
import com.wyh.happyyousdk.databinding.ActivityMentalWellnessVoiceFeatureBinding
import com.wyh.happyyousdk.model.response.VoiceFeature
import com.wyh.happyyousdk.utils.SharedPref
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MentalWellnessVoiceFeature : AppCompatActivity() {

    lateinit var binding : ActivityMentalWellnessVoiceFeatureBinding
    var voiceFeature : ArrayList<VoiceFeature> = ArrayList()
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(this)
        context = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_mental_wellness_voice_feature)
        setContentView(binding.root)

        binding.voiceFeatureBackLayout.setOnClickListener {
            finish()
        }

        val customObj = JSONObject()
        try {
            customObj.put("PAGE_ID", "MentalWellnessAnalysisInfo")
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        


        binding.voiceFeatureHome.setOnClickListener {
            val intent = Intent(this, NewDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        try{
            val voice = intent.getStringExtra("voiceFeature")
            val jsonArray = JSONArray(voice)

            for(i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val voiceFeatureObj = VoiceFeature(jsonObject.getString("name")
                    ,jsonObject.getString("score"))
                voiceFeature.add(voiceFeatureObj)
            }

            binding.voiceFeatureRecyclerList.layoutManager = LinearLayoutManager(this)
            binding.voiceFeatureRecyclerList.hasFixedSize()
            val mentalWellnessScoreDetails = MentalWellnessScoreDetails(this, voiceFeature)
            binding.voiceFeatureRecyclerList.adapter = mentalWellnessScoreDetails
        }catch (e: Exception){
            e.toString()
        }


    }
}