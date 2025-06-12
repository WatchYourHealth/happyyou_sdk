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
import com.wyh.happyyousdk.profile.ProfileActivity;
import com.wyh.happyyousdk.profile.adapter.ConnectionListAdapter;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;

import java.util.ArrayList;

public class TribeFragment extends Fragment {

    ConnectionListLayoutBinding binding;
    Context context;
    String countText;

    ArrayList<ConnectionListModel> tribeList;

    public TribeFragment() {
    }

    public TribeFragment(Context context, String countText, ArrayList<ConnectionListModel> tribeList) {
        this.context = context;
        this.countText = countText;
        this.tribeList = tribeList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       binding = DataBindingUtil.inflate(inflater, R.layout.connection_list_layout, container, false);
       context = getActivity();
       if(tribeList != null && tribeList.size() == 0){
           binding.noDataFoundTv.setVisibility(View.VISIBLE);
           binding.totalCountTv.setText("Your Tribe count is " +ProfileActivity.tribeCount);

       }else{
           binding.noDataFoundTv.setVisibility(View.GONE);
           binding.totalCountTv.setText("Your Tribe count is " +ProfileActivity.totalCount);
           binding.connectionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
           binding.connectionRecyclerView.hasFixedSize();
           ConnectionListAdapter connectionListAdapter = new ConnectionListAdapter(context,tribeList);
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
               customYesNoDialog.binding.popupHeading.setText("Tribe count");
               customYesNoDialog.binding.txtInfoPopUpDesc.setText(ProfileActivity.categoryCountText);
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
