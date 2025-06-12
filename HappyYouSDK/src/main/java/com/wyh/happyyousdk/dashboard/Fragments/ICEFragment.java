package com.wyh.happyyousdk.dashboard.Fragments;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.databinding.IceFragmenrtLayoutBinding;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.ice.AddEmergencyContactActivity;
import com.wyh.happyyousdk.ice.CPRAndFirstAddActivity;
import com.wyh.happyyousdk.ice.SOSActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ICEFragment extends Fragment {
    
    Context context;
    IceFragmenrtLayoutBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    FetchEmergencyDetailsResp fetchEmergencyDetailsResp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        binding = DataBindingUtil.inflate(inflater, R.layout.ice_fragmenrt_layout,container,false);
        context = getActivity();

        binding.includeBack.ivBack.setVisibility(View.GONE);
        binding.includeBack.tvBack.setText("ICE (In Case of Emergency)");

        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.rlEmergencyContact.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddEmergencyContactActivity.class);
            intent.putExtra("data", new Gson().toJson(fetchEmergencyDetailsResp));
            startActivity(intent);
        });

        binding.rlFirstAid.setOnClickListener(view -> {
            Intent intent = new Intent(context, CPRAndFirstAddActivity.class);
            startActivity(intent);
        });

        binding.rlDrOnCall.setOnClickListener(view -> {
            /*Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:01141132843"));
            startActivity(intent);*/
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", Constants.HappyMartTeleconsulatation);
            intent.putExtra("toolbarname", "Tele-Consultation");
            startActivity(intent);
        });

        binding.llAmbulance.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:9111891118"));
            startActivity(intent);
        });

        binding.llPolice.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:100"));
            startActivity(intent);
        });

        binding.llFirebrigade.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:101"));
            startActivity(intent);
        });
        
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchEmergencyContactDetails(context,"ice");

    }

    public void fetchEmergencyContactDetails(Context context, String comingFrom) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        ApiInterfaceWyh apiInterfaceWyh1 = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<FetchEmergencyDetailsResp> call = apiInterfaceWyh1.fetchEmergencyDetails(SharedPref.getAuthToken());
        call.enqueue(new Callback<FetchEmergencyDetailsResp>() {
            @Override
            public void onResponse(Call<FetchEmergencyDetailsResp> call, Response<FetchEmergencyDetailsResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ice_fetch_emergency_details_success));
                    if(comingFrom.equalsIgnoreCase("redirection")){
                        fetchEmergencyDetailsResp = response.body();
                        SharedPref.putEmergencyContact(response.body().getData().get(0).getPrimaryContactMobile());
                        Intent intent = new Intent(context, AddEmergencyContactActivity.class);
                        intent.putExtra("data", new Gson().toJson(fetchEmergencyDetailsResp));
                        startActivity(intent);
                    }else{
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            fetchEmergencyDetailsResp = response.body();
                            SharedPref.putEmergencyContact(response.body().getData().get(0).getPrimaryContactMobile());
                            binding.llSOs.setOnClickListener(view -> {
                                Intent intent = new Intent(context, SOSActivity.class);
                                context.startActivity(intent);
                            });
                        } else {
                            binding.llSOs.setOnClickListener(view -> {
                                Intent intent = new Intent(context, SOSActivity.class);
                                context.startActivity(intent);
                            });
                        }
                    }

                } else if (response.code() == 401) {
                    refreshAuthToken(context);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ice_fetch_emergency_details_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchEmergencyDetailsResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ice_fetch_emergency_details_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken(Context context) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    fetchEmergencyContactDetails(context,"ice");
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
                   /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
                /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }
}
