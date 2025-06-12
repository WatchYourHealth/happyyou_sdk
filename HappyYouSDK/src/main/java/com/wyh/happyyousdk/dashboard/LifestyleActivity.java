package com.wyh.happyyousdk.dashboard;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.ACTIVEHOURS;
import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.SearchKey;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityLifestyleBinding;
import com.wyh.happyyousdk.databinding.LayoutLifeStyleWelcomeBinding;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LifestyleActivity extends AppCompatActivity implements LifestyleAdapter.LifeStyleClick {

    ActivityLifestyleBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;

    ArrayList<HappyMartListModel> lifeStyleListModel = new ArrayList<>();
    ArrayList<HappyMartListModel> searchHappyMartList = new ArrayList<>();
    LifestyleAdapter lifestyleAdapter;
    String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lifestyle);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Lifestyle");
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivMenu.setVisibility(View.VISIBLE);
        binding.includeBack.ivMenu.setImageResource(R.drawable.ic_info);
        binding.includeBack.ivMenu.setColorFilter(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeBack.llBack.setOnClickListener(view -> finish());
        binding.ivHome.setOnClickListener(view -> finish());
        binding.includeBack.ivMenu.setOnClickListener(view -> {
            showLifestyleWelcomeLayout();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "LifestyleDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        searchKey = getIntent().getStringExtra(SearchKey);


        boolean isFirst = SharedPref.getIsFirstInstall();
        if (!isFirst) {
            SharedPref.putIsFirstInstall(true);
            showLifestyleWelcomeLayout();
        }


        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.searchBarLayout.getVisibility() == View.VISIBLE){
                    binding.lifeStyleRecyler.setVisibility(View.VISIBLE);
                    binding.noResultTv.setVisibility(View.GONE);
                    binding.lifeStyleRecyler.setLayoutManager(new GridLayoutManager(LifestyleActivity.this,3));
                    binding.lifeStyleRecyler.hasFixedSize();
                    lifestyleAdapter = new LifestyleAdapter(LifestyleActivity.this,lifeStyleListModel,LifestyleActivity.this);
                    binding.lifeStyleRecyler.setAdapter(lifestyleAdapter);
                    binding.lifeStyleSearchEt.setText("");
                    binding.lifeStyleSearchEt.setEnabled(false);
                    binding.searchBarLayout.hide();
                }else{
                    binding.lifeStyleSearchEt.setEnabled(true);
                    binding.searchBarLayout.show();
                }
            }
        });

        binding.lifestyleSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lifeStyleSearchEt.setText("");
            }
        });




        binding.lifeStyleSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.chars().count() == 0){
                    binding.lifestyleSearchCancel.setVisibility(View.GONE);
                    binding.lifeStyleRecyler.setVisibility(View.VISIBLE);
                    binding.noResultTv.setVisibility(View.GONE);
                    binding.lifeStyleRecyler.setLayoutManager(new GridLayoutManager(LifestyleActivity.this,3));
                    binding.lifeStyleRecyler.hasFixedSize();
                    lifestyleAdapter = new LifestyleAdapter(LifestyleActivity.this,lifeStyleListModel,LifestyleActivity.this);
                    binding.lifeStyleRecyler.setAdapter(lifestyleAdapter);

                }else{
                    binding.lifestyleSearchCancel.setVisibility(View.VISIBLE);
                    searchHappyMartList.clear();
                    for(int j=0;j < lifeStyleListModel.size();j++){
                        if(lifeStyleListModel.get(j).getHappyMartTile().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            searchHappyMartList.add(lifeStyleListModel.get(j));
                        }

                    }
                    if(searchHappyMartList.size() == 0){
                        binding.lifeStyleRecyler.setVisibility(View.GONE);
                        binding.noResultTv.setVisibility(View.VISIBLE);
                    }else{
                        binding.lifeStyleRecyler.setVisibility(View.VISIBLE);
                        binding.noResultTv.setVisibility(View.GONE);
                        binding.lifeStyleRecyler.setLayoutManager(new GridLayoutManager(LifestyleActivity.this,3));
                        binding.lifeStyleRecyler.hasFixedSize();
                        lifestyleAdapter = new LifestyleAdapter(LifestyleActivity.this,searchHappyMartList,LifestyleActivity.this);
                        binding.lifeStyleRecyler.setAdapter(lifestyleAdapter);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setUpListItem();


    }

    private void setUpListItem(){
        try{
            HappyMartListModel model = new HappyMartListModel(R.drawable.ic_h2o_intake,"H2O");
            lifeStyleListModel.add(model);

            model = new HappyMartListModel(R.drawable.ic_sleep_star,"Pillow Time");
            lifeStyleListModel.add(model);

            model = new HappyMartListModel(R.drawable.ic_foot_prints,"Happy Footprints");
            lifeStyleListModel.add(model);

            model = new HappyMartListModel(R.drawable.ic_cal_burned,"Cal - Count");
            lifeStyleListModel.add(model);


            model = new HappyMartListModel(R.drawable.ic_stand_time,"Active Hours");
            lifeStyleListModel.add(model);

            model = new HappyMartListModel(R.drawable.ic_ideal_weight,"Weight Watcher");
            lifeStyleListModel.add(model);

            binding.lifeStyleRecyler.setLayoutManager(new GridLayoutManager(this,3));
            binding.lifeStyleRecyler.hasFixedSize();
            lifestyleAdapter = new LifestyleAdapter(this,lifeStyleListModel,this);
            binding.lifeStyleRecyler.setAdapter(lifestyleAdapter);


            //Log.d("search Key", searchKey);
            if(searchKey != null && !searchKey.isEmpty() && searchKey.length() >= 3) {
                binding.lifeStyleSearchEt.setEnabled(true);
                binding.searchBarLayout.show();
                binding.lifeStyleSearchEt.setText(searchKey);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(String lifeStyleTitle) {
        if(lifeStyleTitle.equalsIgnoreCase("H2O")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", WATER);
            startActivity(intent);
        }
        if(lifeStyleTitle.equalsIgnoreCase("Pillow Time")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", SLEEP);
            startActivity(intent);
        }
        if(lifeStyleTitle.equalsIgnoreCase("Happy Footprints")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", STEPS);
            startActivity(intent);

        }
        if(lifeStyleTitle.equalsIgnoreCase("Cal - Count")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", CALORIE);
            startActivity(intent);
        }
        if(lifeStyleTitle.equalsIgnoreCase("Active Hours")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", ACTIVEHOURS);
            startActivity(intent);
        }
        if(lifeStyleTitle.equalsIgnoreCase("Weight Watcher")){
            Intent intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", WEIGHT);
            startActivity(intent);
        }

    }

    private void showLifestyleWelcomeLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        LayoutLifeStyleWelcomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_life_style_welcome, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.content.setVisibility(View.GONE);
        binding.tvContent.loadData(getResources().getString(R.string.life_style_welcome), "text/html", "utf-8");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        } else {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }*/

        binding.btnOK.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }


}