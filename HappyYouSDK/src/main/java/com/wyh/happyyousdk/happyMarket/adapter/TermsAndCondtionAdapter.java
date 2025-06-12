package com.wyh.happyyousdk.happyMarket.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.TermsNConditionModel;

import java.util.List;

public class TermsAndCondtionAdapter extends RecyclerView.Adapter<TermsAndCondtionAdapter.MyViewHolder> {

    List<TermsNConditionModel> termsList;
    Context context;


    public TermsAndCondtionAdapter(List<TermsNConditionModel> termsList, Context context) {
        this.termsList = termsList;
        this.context = context;
    }

    @NonNull
    @Override
    public TermsAndCondtionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.terms_condition_layout,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TermsAndCondtionAdapter.MyViewHolder holder, int position) {
            holder.terms_tv.setText(termsList.get(position).getCount()+". "+termsList.get(position).getTermsName());
    }

    @Override
    public int getItemCount() {
        return termsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView terms_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            terms_tv = itemView.findViewById(R.id.terms_tv);
        }
    }
}
