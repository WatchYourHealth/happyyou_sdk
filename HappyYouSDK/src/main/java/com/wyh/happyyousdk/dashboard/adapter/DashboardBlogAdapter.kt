package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.APIEncryption.APILogs
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.absorb.QuickReadActivity
import com.wyh.happyyousdk.dashboard.AddBookMark
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper
import com.wyh.happyyousdk.model.response.Blogs

class DashboardBlogAdapter(val context: Context,val list: ArrayList<Blogs>,val addBookMark: AddBookMark) : RecyclerView.Adapter<DashboardBlogAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val blogPoster = itemView.findViewById<ImageView>(R.id.blog_poster)
        val likeBlog = itemView.findViewById<ImageView>(R.id.like_blog)
        val readBlog = itemView.findViewById<ImageView>(R.id.read_blog)
        val blogHeading = itemView.findViewById<TextView>(R.id.blog_heading)
        val blogMin = itemView.findViewById<TextView>(R.id.blog_min)
        val blogSec = itemView.findViewById<TextView>(R.id.sec_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardBlogAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dashboard_blogs_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardBlogAdapter.ViewHolder, position: Int) {
        Glide.with(context).load(list[position].imgPath).into(holder.blogPoster)
        holder.blogHeading.text = list[position].articleName


        if(list[position].isBookMarked!!){
            holder.likeBlog.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
        }else{
            holder.likeBlog.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24)
        }

        holder.blogPoster.setOnClickListener {
            val intent = Intent(context, QuickReadActivity::class.java)
            intent.putExtra("article_code", list[position].articleCode)
            context.startActivity(intent)
        }

        holder.likeBlog.setOnClickListener {
            addBookMark.clickToBookMark(list[position].articleCode,!list[position].isBookMarked!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}