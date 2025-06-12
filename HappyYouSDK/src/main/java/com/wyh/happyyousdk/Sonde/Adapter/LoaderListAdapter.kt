package com.wyh.happyyousdk.Sonde.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.R

class LoaderListAdapter(val context: Context, val stringList: Array<String>) : RecyclerView.Adapter<LoaderListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var loaderText = itemView.findViewById<TextView>(R.id.loader_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoaderListAdapter.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.loader_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoaderListAdapter.ViewHolder, position: Int) {
        holder.loaderText.text = stringList[position]
    }

    override fun getItemCount(): Int {
        return stringList.size
    }
}