package com.wyh.happyyousdk.Sonde.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.Sonde.Activities.MentalWellnessVoiceFeature
import com.wyh.happyyousdk.model.response.SondeHistoryData
import com.wyh.happyyousdk.model.response.SondeHistoryResponse
import com.wyh.happyyousdk.utils.CommonUtils

class SondeHistoryAdapter(val context: Context, val historyList : ArrayList<SondeHistoryData>, val comingFrom: String) : RecyclerView.Adapter<SondeHistoryAdapter.ViewHolder>() {


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val scoreTv = itemView.findViewById<TextView>(R.id.sonde_history_score)
        val scoreDate = itemView.findViewById<TextView>(R.id.sonde_history_date)
        val scoreTime = itemView.findViewById<TextView>(R.id.sonde_history_time)
        val scoreQuality = itemView.findViewById<TextView>(R.id.sonde_history_quality)
        val viewMoreButton = itemView.findViewById<Button>(R.id.btn_view_more)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SondeHistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sonde_history_item_layout,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: SondeHistoryAdapter.ViewHolder, position: Int) {


        holder.scoreDate.text = CommonUtils.formatDateFromString("yyyy-MM-dd HH:mm:ss","dd/MM/yyyy",historyList[position].TestDate)
        holder.scoreTime.text = CommonUtils.formatDateFromString("yyyy-MM-dd HH:mm:ss","hh:mm a",historyList[position].TestDate)
       try{

           if(comingFrom.equals("metalFitness",true)){
               holder.viewMoreButton.visibility = View.VISIBLE
               if(historyList[position].score.toInt() in (0..69)){
                   holder.scoreQuality.text = context.getString(R.string.pay_attention)
               }else if(historyList[position].score.toInt() in 70..79){
                   holder.scoreQuality.text = context.getString(R.string.sonde_good)
               }else{
                   holder.scoreQuality.text = context.getString(R.string.sinde_excellent)
               }

           }else{
               holder.viewMoreButton.visibility = View.GONE
               if(historyList[position].score.toInt() in (0..64)){
                   holder.scoreQuality.text = context.getString(R.string.low_risk)
               }else if(historyList[position].score.toInt() in 65..79){
                   holder.scoreQuality.text = context.getString(R.string.high_risk)
               }else{
                   holder.scoreQuality.text = context.getString(R.string.high_risk)
               }
           }

           if(historyList[position].score != "" || historyList[position].score != null){
               holder.scoreTv.text = historyList[position].score
           }else{
               holder.scoreTv.text = "0"
           }

           holder.viewMoreButton.setOnClickListener {
               context.startActivity(Intent(context, MentalWellnessVoiceFeature::class.java)
                   .putExtra("voiceFeature",historyList[position].subFeatureScore))
           }
       }catch (e: Exception){
           e.toString()
       }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}