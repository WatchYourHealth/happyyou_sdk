package com.wyh.happyyousdk.happyMarket;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.FeedbackPOPUP;
import static com.wyh.happyyousdk.utils.Constants.SearchKey;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.Sonde.Activities.SondeDashboard;
import com.wyh.happyyousdk.VideoReaderActivity;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.model.request.HappyMartSearchData;
import com.wyh.happyyousdk.model.request.SearchRequest;
import com.wyh.happyyousdk.model.request.SearchResponse;
import com.wyh.happyyousdk.databinding.ActivityHappyMartBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartListAdapter;
import com.wyh.happyyousdk.happyMarket.adapter.MyPurchasesAdapter;
import com.wyh.happyyousdk.model.MyOrderResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.GetUserTypemodel;
import com.wyh.happyyousdk.model.response.HappyMartTilesData;
import com.wyh.happyyousdk.model.response.dashboard.UsertypeDashboardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HappyMartActivity extends AppCompatActivity implements MyPurchasesAdapter.ClickListenerInterface, HappyMartListAdapter.HappyMartClick, ScratchListener {

    ActivityHappyMartBinding binding;
    Context context;
    int positionCurrentActivities = 0;
    public static int totalPoints = 0;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<MyOrderResponse.Data> myOrderDataList;
    List<String> myPurchasesList;
    int stampId = -1;
    boolean isPositiveBtn = false;
    AlertDialog alertDialogStamp, alertDialogBonusStamp;
    ArrayList<HappyMartListModel> happyMartListModels = new ArrayList<>();
    ArrayList<HappyMartListModel> searchHappyMartList = new ArrayList<>();
    ArrayList<String> purchaseArray = new ArrayList<>();

    List<String> newPurchaseList = new ArrayList<>();

    ArrayList<HappyMartListModel> baseModel = new ArrayList<>();

    HappyMartListAdapter happyMartListAdapter;
    MyPurchasesAdapter myPurchasesAdapter;

    String searchKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_happy_mart);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Glide.with(context)
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_bear_mart.json")
                .into(binding.laBear);

        binding.includeToolbar.tvBack.setText("Happy Mart");
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);
        binding.includeToolbar.ivMenu.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        totalPoints = getIntent().getIntExtra("points", 0);

        searchKey = getIntent().getStringExtra(SearchKey);

        setUpHappyMartList();
        requestCallPermission();


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        binding.happyMartSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.happyMartSearchLayout.getVisibility() == View.VISIBLE) {
                    binding.happyMartRecycler.setVisibility(View.VISIBLE);
                    binding.happyMartNoResultTv.setVisibility(View.GONE);
                   // happyMartListAdapter = new HappyMartListAdapter(context, happyMartListModels, HappyMartActivity.this);
                    binding.happyMartRecycler.setAdapter(happyMartListAdapter);
                    myPurchasesAdapter = new MyPurchasesAdapter(context, myPurchasesList, HappyMartActivity.this);
                    binding.rvMyPurchases.setAdapter(myPurchasesAdapter);
                    binding.happyMartSearchEt.setText("");
                    binding.happyMartSearchEt.setEnabled(false);
                    binding.happyMartSearchLayout.hide();
                } else {
                    binding.happyMartSearchEt.setEnabled(true);
                    binding.happyMartSearchLayout.show();
                }
            }
        });

        getMyOrder();

        binding.searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.happyMartSearchEt.setText("");
            }
        });

        binding.happyMartSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(charSequence.chars().count() == 0){
                   binding.happyMartRecycler.setVisibility(View.VISIBLE);
                   binding.searchCancel.setVisibility(View.GONE);
                   binding.happyMartNoResultTv.setVisibility(View.GONE);
                   //happyMartListAdapter = new HappyMartListAdapter(context,happyMartListModels,HappyMartActivity.this);
                   binding.happyMartRecycler.setAdapter(happyMartListAdapter);

                   myPurchasesAdapter = new MyPurchasesAdapter(context, myPurchasesList, HappyMartActivity.this);
                   binding.myPurchaseNoResultTv.setVisibility(View.GONE);
                   binding.rvMyPurchases.setVisibility(View.VISIBLE);
                   binding.rvMyPurchasesIndicator.setVisibility(View.VISIBLE);
                   binding.rvMyPurchases.setAdapter(myPurchasesAdapter);
               }else{
                   binding.searchCancel.setVisibility(View.VISIBLE);
                   newPurchaseList.clear();
                   if(binding.rlMyPurchases.getVisibility() == View.VISIBLE){
                       for(int j=0; j < myPurchasesList.size() ; j++){
                           if(purchaseArray.get(j).toLowerCase().contains(charSequence.toString().toLowerCase())){
                               newPurchaseList.add(myPurchasesList.get(j));
                           }
                       }
                   }
                   if(newPurchaseList.size() == 0){
                       binding.myPurchaseNoResultTv.setVisibility(View.VISIBLE);
                       binding.rvMyPurchases.setVisibility(View.GONE);
                       binding.rvMyPurchasesIndicator.setVisibility(View.GONE);
                   }else{
                       binding.myPurchaseNoResultTv.setVisibility(View.GONE);
                       binding.rvMyPurchases.setVisibility(View.VISIBLE);
                       binding.rvMyPurchasesIndicator.setVisibility(View.VISIBLE);

                       myPurchasesAdapter = new MyPurchasesAdapter(context, newPurchaseList, HappyMartActivity.this);
                       binding.rvMyPurchases.setAdapter(myPurchasesAdapter);
                   }
                   searchCall(charSequence.toString().toLowerCase());
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.rlMyPurchases.setOnClickListener(view -> {
            Intent intent = new Intent(context, MoreMyPurchasesActivity.class);
            startActivity(intent);
        });

        binding.includeToolbar.ivMenu.setOnClickListener(view -> {
            showPopup(binding.includeToolbar.ivMenu);
        });

        LinearSnapHelper linearSnapHelper1 = new SnapHelperOneByOne();
        linearSnapHelper1.attachToRecyclerView(binding.rvMyPurchases);

        boolean isHappyMartIntroShown = SharedPref.getIsHappyMartIntroShown();

    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_happy_mart, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_my_order) {
                Intent intent = new Intent(context, MyOrderActivity.class);
                intent.putExtra("vendor", "");
                startActivity(intent);
            }
            return false;
        });
    }

    private void requestCallPermission() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE
            }, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("AuthToken", "Permission granted");
            } else {
                Toast.makeText(this, "Permission denied",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getUserType() {
        progressDialog.show();
        GetUserTypemodel request = new GetUserTypemodel(SharedPref.getAesUuid());
        Call<UsertypeDashboardData> call = apiInterfaceWyh.getUserType(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<UsertypeDashboardData>() {
            @Override
            public void onResponse(Call<UsertypeDashboardData> call, Response<UsertypeDashboardData> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getDashboardData() != null) {
                        String url = "https://kli.getvisitapp.com/kli/sso?userId=" + SharedPref.getAesUuid() + "&userType=" + response.body().getDashboardData().get(0).getUserType();
                        Intent i = new Intent(context, WebActivity.class);
                        i.putExtra("comingFrom","");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("Url", url);
                        startActivity(i);
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsertypeDashboardData> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpHappyMartList() {
        try {
            HappyMartListModel model = new HappyMartListModel(R.drawable.ic_tele_consultation, "Tele-Consultation");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_diagnostic, "Diagnostics and LAB Tests");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_pharmacy, "Pharmacy");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_physical_consultation, "Physical Consultation");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_fitness_and_nutrition, "Fitness & Nutrition");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_mental_welbeing, "Mental Wellbeing");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_hobby, "Hobby");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_device_electronics, "Devices and Electronics");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.airport__1_,"Travel and Hotels");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.merchandise,"Merchandise");
            happyMartListModels.add(model);

            model = new HappyMartListModel(R.drawable.ic_stamp, "Others");
            happyMartListModels.add(model);

            baseModel = happyMartListModels;


            binding.happyMartRecycler.setLayoutManager(new GridLayoutManager(this, 3));
            binding.happyMartRecycler.hasFixedSize();
            //happyMartListAdapter = new HappyMartListAdapter(this, happyMartListModels, this);
            binding.happyMartRecycler.setAdapter(happyMartListAdapter);


            //Log.d("search Key", searchKey);
            if (searchKey != null && !searchKey.isEmpty() && searchKey.length() >= 3) {
                binding.happyMartSearchEt.setEnabled(true);
                binding.happyMartSearchLayout.show();
                binding.happyMartSearchEt.setText(searchKey);
                searchCall(searchKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchCall(String searchKey) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        SearchRequest request = new SearchRequest(searchKey);
        Call<SearchResponse> call = apiInterfaceWyh.dashboardSearch(SharedPref.getAuthToken(), request);
        Log.d("search request", new Gson().toJson(call.request().url()));
        Log.d("search request", new Gson().toJson(call.request()));
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                Log.d("search response", new Gson().toJson(response.body()));
                List<HappyMartSearchData> hm = new ArrayList<>();
                if (response.code() == 200 && response.body() != null) {
                    hm = response.body().getData().getHappyMartSearchData();
                    if (hm.size() > 0) {
                        binding.happyMartRecycler.setVisibility(View.VISIBLE);
                        binding.happyMartNoResultTv.setVisibility(View.GONE);
                        setUpHappyMart(hm);
                    } else {
                        binding.happyMartRecycler.setVisibility(View.GONE);
                        binding.happyMartNoResultTv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(
                        context,
                        getString(R.string.error_string),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void setUpHappyMart(List<HappyMartSearchData> happyMartSearchData) {
        searchHappyMartList.clear();

        try {
            for (int i = 0; i < happyMartSearchData.size(); i++) {
                HappyMartSearchData item = happyMartSearchData.get(i);
                int img;

                if (item.getResult().equalsIgnoreCase("Tele-Consultation")) {
                    img = R.drawable.ic_tele_consultation;
                } else if (item.getResult().equalsIgnoreCase("Diagnostics and LAB Tests")) {
                    img = R.drawable.ic_diagnostic;
                } else if (item.getResult().equalsIgnoreCase("Pharmacy")) {
                    img = R.drawable.ic_pharmacy;
                } else if (item.getResult().equalsIgnoreCase("Physical Consultation")) {
                    img = R.drawable.ic_physical_consultation;
                } else if (item.getResult().equalsIgnoreCase("Fitness & Nutrition")) {
                    img = R.drawable.ic_fitness_and_nutrition;
                } else if (item.getResult().equalsIgnoreCase("Mental Wellbeing")) {
                    img = R.drawable.ic_mental_welbeing;
                } else if (item.getResult().equalsIgnoreCase("Hobby")) {
                    img = R.drawable.ic_hobby;
                } else if (item.getResult().equalsIgnoreCase("Devices and Electronics")) {
                    img = R.drawable.ic_device_electronics;
                } else {
                    img = R.drawable.ic_device_electronics;
                }
                searchHappyMartList.add(new HappyMartListModel(img, item.getResult()));
            }

            binding.happyMartRecycler.setLayoutManager(new GridLayoutManager(this, 3));
            binding.happyMartRecycler.hasFixedSize();
            //happyMartListAdapter = new HappyMartListAdapter(this, searchHappyMartList, this);
            binding.happyMartRecycler.setAdapter(happyMartListAdapter);

            //Log.d("search list", ""+searchHappyMartList.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(HappyMartTilesData happyMartTilesData) {
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Tele-Consultation")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartTeleconsulatation);
            intent.putExtra("toolbarname", "Tele-Consultation");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Diagnostics and LAB Tests")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartDiagnostics);
            intent.putExtra("toolbarname", "Diagnostics and LAB Tests");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Pharmacy")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartPharmacy);
            intent.putExtra("toolbarname", "Pharmacy");
            startActivity(intent);

        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Physical Consultation")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartOPD);
            intent.putExtra("toolbarname", "Physical Consultation");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Fitness & Nutrition")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartFitness);
            intent.putExtra("toolbarname", "Fitness & Nutrition");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Mental Wellbeing")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMentalWellbeing);
            intent.putExtra("toolbarname", "Mental Wellbeing");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Hobby")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartHobby);
            intent.putExtra("toolbarname", "Hobby");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Devices and Electronics")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartDevice);
            intent.putExtra("toolbarname", "Devices and Electronics");
            startActivity(intent);
        }
        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Travel and Hotels")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartTravel);
            intent.putExtra("toolbarname", "Travel and Hotels");
            startActivity(intent);
        }

        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Merchandise")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartMerchandise);
            intent.putExtra("toolbarname", "Merchandise");
            startActivity(intent);
        }


        if (happyMartTilesData.getCategoryName().equalsIgnoreCase("Others")) {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartOther);
            intent.putExtra("toolbarname", "Others");
            startActivity(intent);
        }

    }

    private void setActivitiesAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        myPurchasesAdapter = new MyPurchasesAdapter(context, myPurchasesList, this);
        binding.rvMyPurchases.setAdapter(myPurchasesAdapter);
        binding.rvMyPurchases.setLayoutManager(linearLayoutManager);

        if (myPurchasesList.size() > 3) {
            binding.rvMyPurchasesIndicator.setVisibility(View.VISIBLE);
            int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(myPurchasesList.size())) / 3.0);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvMyPurchasesIndicator.setAdapter(indicatorsAdapter);
            binding.rvMyPurchasesIndicator.setLayoutManager(linearLayoutManager1);
            binding.rvMyPurchasesIndicator.setHasFixedSize(true);
            binding.rvMyPurchases.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionCurrentActivities = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionCurrentActivities = linearLayoutManager.findFirstVisibleItemPosition();
                        indicatorsAdapter.updateSelectedIndex(positionCurrentActivities);
                    }
                }
            });
        } else {
            binding.rvMyPurchasesIndicator.setVisibility(View.GONE);
        }
    }

    private void getMyOrder() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<MyOrderResponse> call = apiInterfaceWyh.getAllOrders(SharedPref.getAuthToken());
        call.enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderResponse> call, @NonNull Response<MyOrderResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("request", new Gson().toJson(call.request()));
                Log.d("response", new Gson().toJson(response.body()));
                Log.d("response", new Gson().toJson(response.code()));
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_success));

                    binding.rlMyPurchases.setVisibility(View.VISIBLE);
                    myOrderDataList = response.body().getData();
                    myPurchasesList = new ArrayList<>();
                    if (myOrderDataList.size() > 0) {
                        for (MyOrderResponse.Data data : myOrderDataList) {
                            if (!(myPurchasesList.contains(data.getVendor()))) {
                                myPurchasesList.add(data.getVendor());
                            }
                        }
                        getOriginalValues();
                        setActivitiesAdapter();
                    } else {
                        binding.rlMyPurchases.setVisibility(View.GONE);
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    if (response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(FeedbackPOPUP, ""));
                        NewDashboardHelper.Companion.setFeedbackResponseData(response.body().getFeedbackDetails());
                    }
                    showRewardsPopupDialogBox(context);

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_failed));
//                Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(String vendor) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("vendor", vendor);
        startActivity(intent);
    }

    private void getOriginalValues(){
        try{
            for (int i=0; i < myPurchasesList.size() ; i++){
                if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("medpayopd")){
                    purchaseArray.add("Physical Consultation");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("pharmeasy")){
                    purchaseArray.add("Pharmacy");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("healthassure") || myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("connectandheal")){
                    purchaseArray.add("Diagnostics and LAB Tests");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("coachpro")){
                    purchaseArray.add("Fitness n Nutrition");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("mentalWellbeing")){
                    purchaseArray.add("Mental Wellbeing");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("hobbytribe")){
                    purchaseArray.add("Hobby");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("actofit")){
                    purchaseArray.add("Devices and Electronics");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("vouchers")){
                    purchaseArray.add("Others");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("medpaypharmacy")){
                    purchaseArray.add("Medpay Pharmacy");
                }
                else if(myPurchasesList.get(i).replace(" ","").equalsIgnoreCase("GetVisit")){
                    purchaseArray.add("Tele-Consultation");
                } else {
                    purchaseArray.add("Others");
                }
            }


        }catch (Exception e){

        }
    }


    private void showStampsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox(context);
            }
        });

        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox(context);
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });

        alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox(context);
            }
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox(context);
        });
        binding.scratchView.setScratchListener(HappyMartActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((HappyMartActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showRewardsPopupDialogBox(Context context) {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            try{
                switch (firstData.getKey()) {
                    case "TokenStamp":
                        showStampsPopup(firstData.getValue(), context);
                        break;
                    case "TokenStampBounce":
                        showBonusStampPopup(firstData.getValue(), context);
                        break;
                    case "FeedbackPOPUP":
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback((Activity) context, NewDashboardHelper.Companion.getFeedbackResponseData());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + firstData.getKey());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchTokenReward();
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((alertDialogBonusStamp != null && alertDialogBonusStamp.isShowing())) {
                        alertDialogBonusStamp.dismiss();
                    }
                    if ((alertDialogStamp != null && alertDialogStamp.isShowing())) {
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }


}