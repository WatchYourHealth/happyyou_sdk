package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel

class SearchGridViewAdapter(private val lifeStyleList: ArrayList<HappyMartListModel?>, private val context: Context): BaseAdapter() {

    var res: IntArray = intArrayOf(
        R.drawable.ic_light_pink_button_bg,
        R.drawable.ic_light_blue_button_bg,
        R.drawable.ic_light_orange_button_bg,
    )
    var bgCount = -1


    override fun getCount(): Int = lifeStyleList.size

    override fun getItem(position: Int): Any = ""

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view =  LayoutInflater.from(context).inflate(R.layout.life_style_list_horizontal_layout,null)
        val parentLayout = view!!.findViewById<RelativeLayout>(R.id.life_style_list_parent_layout)
        val image = view.findViewById<ImageView>(R.id.life_style_list_img)
        val text = view.findViewById<TextView>(R.id.life_style_list_tv)
        if (bgCount == res.size - 1) bgCount = 0 else bgCount++
        lifeStyleList[position]?.let { image.setImageResource(it.happyMartImg) }
        text.text = lifeStyleList[position]?.happyMartTile ?: ""
        parentLayout.background = ContextCompat.getDrawable(context, res[position])

        return view
    }

}