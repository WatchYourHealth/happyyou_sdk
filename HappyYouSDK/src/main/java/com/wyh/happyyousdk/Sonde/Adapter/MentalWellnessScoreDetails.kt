package com.wyh.happyyousdk.Sonde.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.model.response.VoiceFeature

class MentalWellnessScoreDetails(val context : Context, val voiceFeatureList: ArrayList<VoiceFeature> ) : RecyclerView.Adapter<MentalWellnessScoreDetails.ViewHolder>() {


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val featureName = itemView.findViewById<TextView>(R.id.feature_name)
        val featureScore = itemView.findViewById<TextView>(R.id.feature_score)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentalWellnessScoreDetails.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.wellness_score_detail_item_layout,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MentalWellnessScoreDetails.ViewHolder, position: Int) {
        holder.featureName.text = voiceFeatureList[position].name

        if(voiceFeatureList[position].name.equals("Smoothness",true) || voiceFeatureList[position].name.equals("Control",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} %"
        }else if(voiceFeatureList[position].name.equals("Liveliness",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} octaves"
        }else if(voiceFeatureList[position].name.equals("Energy range",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} dB"
        }else if(voiceFeatureList[position].name.equals("Clarity",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} kHz2"
        }else if(voiceFeatureList[position].name.equals("Crispness",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} ms"
        }else if(voiceFeatureList[position].name.equals("Speech rate",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} words/min"
        }else if(voiceFeatureList[position].name.equals("Pause duration",true)){
            holder.featureScore.text = "${voiceFeatureList[position].score} sec"
        }

    }

    override fun getItemCount(): Int {
        return voiceFeatureList.size
    }
}