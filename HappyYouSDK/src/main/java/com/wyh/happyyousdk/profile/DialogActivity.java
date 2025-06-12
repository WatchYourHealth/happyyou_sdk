package com.wyh.happyyousdk.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.material.tabs.TabLayout;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityDialogBinding;
import com.wyh.happyyousdk.profile.Fragments.DareReferralFragment;
import com.wyh.happyyousdk.profile.Fragments.ReferenceFragment;
import com.wyh.happyyousdk.profile.Fragments.TribeFragment;
import com.wyh.happyyousdk.rewards.SampleFragmentPagerAdapter;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    ArrayList<ConnectionListModel> tribeModelList = new ArrayList<>();
    ArrayList<ConnectionListModel> referalModelList = new ArrayList<>();
    ArrayList<ConnectionListModel> dareReferalModelList = new ArrayList<>();
    ActivityDialogBinding binding;

    static String categoryCountText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            categoryCountText = extras.getString("categoryCountText");
        }
        tribeModelList = ProfileActivity.tribeListModel;
        referalModelList = ProfileActivity.referenceListModel;
        dareReferalModelList = ProfileActivity.dareListModel;
        setupViewPager(binding.viewpager);
        binding.profileTabs.setupWithViewPager(binding.viewpager);


        binding.profileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getText().equals("Tribe")){
                    APILogs.INSTANCE.activityTracker("A_DB_HM_Profile_Connections_Tribe",DialogActivity.this);

                }else if(tab.getText().equals("Referral code")){
                    APILogs.INSTANCE.activityTracker("A_DB_HM_Profile_Connections_Referral",DialogActivity.this);
                }else{
                    APILogs.INSTANCE.activityTracker("A_DB_HM_Profile_Connections_DareRfl",DialogActivity.this);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("A_DB_HM_Profile_Connections_Close",DialogActivity.this);
                finish();
            }
        });


        binding.rlMain.setOnClickListener(view -> finish());
        binding.llDialog.setOnClickListener(view -> {});

    }


    private void setupViewPager(ViewPager viewPager) {
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TribeFragment(DialogActivity.this,categoryCountText,tribeModelList), "Tribe");
        adapter.addFragment(new ReferenceFragment(DialogActivity.this,referalModelList), "Referral code");
        adapter.addFragment(new DareReferralFragment(DialogActivity.this,dareReferalModelList), "Referral tile");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

}