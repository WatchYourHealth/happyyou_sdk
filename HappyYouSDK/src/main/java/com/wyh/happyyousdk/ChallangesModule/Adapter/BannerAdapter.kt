package com.wyh.happyyousdk.ChallangesModule.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.model.response.ChallengeBanners
import com.wyh.happyyousdk.R

class BannerAdapter(val context: Context, val bannerList: ArrayList<ChallengeBanners>) : RecyclerView.Adapter<BannerAdapter.Viewholder>() {


    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.banner_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.banner_layout,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: BannerAdapter.Viewholder, position: Int) {
        Glide.with(context)
            .load(bannerList[position].bannerLogo)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }
}