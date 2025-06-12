package com.wyh.happyyousdk.profile.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ConnectionListLayoutBinding;
import com.wyh.happyyousdk.profile.ConnectionListModel;
import com.wyh.happyyousdk.profile.DialogActivity;
import com.wyh.happyyousdk.profile.ProfileActivity;
import com.wyh.happyyousdk.profile.adapter.ConnectionListAdapter;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;

import java.util.ArrayList;

public class ReferenceFragment extends Fragment {

    ConnectionListLayoutBinding binding;
    Context context;

    ArrayList<ConnectionListModel> referenceList;


    public ReferenceFragment() {
    }

    public ReferenceFragment(Context context, ArrayList<ConnectionListModel> referenceList) {
        this.context = context;
        this.referenceList = referenceList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.connection_list_layout,container,false);
        context = getActivity();
        binding.infoImg.setVisibility(View.INVISIBLE);
        if(referenceList != null && referenceList.size() == 0){
            binding.totalCountTv.setText("Your Referral code count is "+referenceList.size());
            binding.noDataFoundTv.setVisibility(View.VISIBLE);
        }else{
            binding.noDataFoundTv.setVisibility(View.GONE);
            binding.totalCountTv.setText("Your Referral code count is "+referenceList.size());
            binding.connectionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            binding.connectionRecyclerView.hasFixedSize();
            ConnectionListAdapter connectionListAdapter = new ConnectionListAdapter(context,referenceList);
            binding.connectionRecyclerView.setAdapter(connectionListAdapter);
        }

        binding.infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
                customYesNoDialog.show();
                customYesNoDialog.setCancelable(false);
                customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
                customYesNoDialog.binding.llActionRequired.setVisibility(View.VISIBLE);
                customYesNoDialog.binding.popupIcon.setVisibility(View.GONE);
                customYesNoDialog.binding.popupHeading.setTextColor(getResources().getColor(R.color.black));
                customYesNoDialog.binding.popupHeading.setText("Referral count");
                customYesNoDialog.binding.txtInfoPopUpDesc.setText("Your referral Count is "+ ProfileActivity.referralCount);
                customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
                customYesNoDialog.binding.btnYes.setText("Close");
                customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));customYesNoDialog.binding.btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        APILogs.INSTANCE.activityTracker("A_DB_HM_Profile_Connections_IButton_Cl",context);
                        customYesNoDialog.dismiss();
                    }
                });
            }
        });


        return binding.getRoot();
    }


}
