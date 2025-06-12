package com.wyh.happyyousdk.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.databinding.VoucherItemLayoutBinding
import com.wyh.happyyousdk.model.response.voucherListData
import com.wyh.happyyousdk.utils.CommonUtils

class VoucherAdapter(val context: Context,val list: ArrayList<voucherListData>, val click : voucherClick) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

    lateinit var binding : VoucherItemLayoutBinding

    class ViewHolder(binding: VoucherItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding : VoucherItemLayoutBinding
        init {
            this.binding = binding
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherAdapter.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.voucher_item_layout,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherAdapter.ViewHolder, position: Int) {
        holder.binding.voucherName.text = list[position].voucherName

        if(list[position].validity != null){
            val date = list[position].validity.split(" ")
            if(date.isNotEmpty()){
                holder.binding.voucherCode.text = CommonUtils.formatDateFromString("MM/dd/yyyy","dd/MM/yyyy",date[0])
            }
        }

        if (list[position].status != null){
            holder.binding.voucherStatus.text = list[position].status
        }

        holder.binding.voucherDate.text = CommonUtils.formatDateFromString("yyyy-MM-dd","dd/MM/yyyy",list[position].transactionDate.split("T")[0])
        //Glide.with(context).load(list[position].voucherLogo).into(holder.binding.voucherLogo)

        holder.binding.viewVoucher.setOnClickListener {
            click.voucherClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return  list.size
    }
}


interface voucherClick{

    fun voucherClick(data : voucherListData)
}