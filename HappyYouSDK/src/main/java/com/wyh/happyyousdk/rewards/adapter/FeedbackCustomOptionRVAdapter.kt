package com.wyh.happyyousdk.rewards.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.FeedbackOptionItemBinding
import com.wyh.happyyousdk.model.response.FeedbackOption

class FeedbackCustomOptionRVAdapter(val context: Context, private var selectedPosition: MutableList<String>, private var type: String, val list: List<FeedbackOption>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<ViewHolder>() {



    interface OnItemClickListener {
        fun onSelected(name: String, position: Int)
        fun onMultiSelected(name: String, position: Int)
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(FeedbackOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = list[position].option

        holder.binding.apply {
            tvOption.text = item

            try {
                if (selectedPosition.contains(item)) {
                    if(item.equals("no", ignoreCase = true) || item.equals("none", ignoreCase = true)){
                        llOption.setBackgroundResource(R.drawable.light_red_border_rc_bg_8dp)
                    }else{
                        llOption.setBackgroundResource(R.drawable.light_blue_border_rc_bg_8dp)
                    }
                } else {
                    llOption.setBackgroundResource(R.drawable.wyh_btn_grey_border)
                }
            } catch (e: Exception) {
                //
            }


            llOption.setOnClickListener {
                if(type.equals("multiChoice", ignoreCase = true)){
                    listener.onMultiSelected(item, position)
                    if(selectedPosition.contains(item)){
                        selectedPosition.remove(item)
                    }else{
                        selectedPosition.add(item)
                    }
                }else{
                    listener.onSelected(item, position)
                    selectedPosition.clear()
                    selectedPosition.add(item)
                }
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount(): Int = list.size

}

class ViewHolder(val binding: FeedbackOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

}