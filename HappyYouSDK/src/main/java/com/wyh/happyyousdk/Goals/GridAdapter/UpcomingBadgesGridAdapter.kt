package com.wyh.happyyousdk.Goals.GridAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.wyh.happyyousdk.Goals.ClickInterface.UpcomingBadgesItemClick
import com.wyh.happyyousdk.R

class UpcomingBadgesGridAdapter(val context: Context) : BaseAdapter() {


    override fun getCount(): Int {
        return 3
    }

    override fun getItem(p0: Int): Any {

        return ""
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        return  LayoutInflater.from(context).inflate(R.layout.upcoming_badges_layout,null)
    }




}