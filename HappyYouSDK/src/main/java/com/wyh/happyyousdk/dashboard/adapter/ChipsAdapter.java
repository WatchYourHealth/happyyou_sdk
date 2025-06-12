package com.wyh.happyyousdk.dashboard.adapter;

import android.util.Log;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.model.response.ChipsModel;

import java.util.ArrayList;

public class ChipsAdapter extends ArrayAdapter<ChipsModel> {

    Context context;
    OnItemClickListener listener;

    public ChipsAdapter(@NonNull Context context, ArrayList<ChipsModel> chipsModel, OnItemClickListener listener) {
        super(context, 0, chipsModel);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.chips_layout, parent, false);
        }

        ChipsModel chipsModel = getItem(position);
        TextView chips = listitemView.findViewById(R.id.tvChips);
        LinearLayout linearLayout = listitemView.findViewById(R.id.llChips);

        try {
            if (chipsModel.isSelected()) {
                linearLayout.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
                chips.setTextColor(context.getColor(R.color.white));
            } else {
                linearLayout.setBackgroundResource(R.drawable.wyh_btn_grey_border);
                chips.setTextColor(context.getColor(R.color.black));
            }
        } catch (Exception e) {
            //
        }

        linearLayout.setOnClickListener(v -> {

            if (chipsModel.isSelected()) {
                chipsModel.setSelected(false);
                listener.onCLick(chipsModel.getName(), false);
            } else {
                chipsModel.setSelected(true);
                listener.onCLick(chipsModel.getName(), true);
            }


            notifyDataSetChanged();


        });

        if (chipsModel.getName() != null) {
            chips.setText(chipsModel.getName());
        }

        return listitemView;
    }

    public interface OnItemClickListener {
        void onCLick(String name, boolean isAdd);
    }


}
