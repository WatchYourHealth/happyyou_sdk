package com.wyh.happyyousdk.qc;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.databinding.ActivityQcOrderListBinding;
import com.wyh.happyyousdk.databinding.QcOrderPopupBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.qc.QCOrderCard;
import com.wyh.happyyousdk.model.response.qc.QCOrderResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.qc.adapter.QCOrderListAdapter;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCOrderListActivity extends AppCompatActivity {
    ActivityQcOrderListBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    QCOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_qc_order_list);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Order List");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        getAllQCOrderList();

    }

    private void getAllQCOrderList() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<QCOrderResponse> call = apiInterfaceWyh.getQCOrderList(SharedPref.getAuthToken());
        call.enqueue(new Callback<QCOrderResponse>() {
            @Override
            public void onResponse(Call<QCOrderResponse> call, Response<QCOrderResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_order_list_success));

                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        binding.rvQcOrderList.setVisibility(View.VISIBLE);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        List<QCOrderResponse.Data> res = response.body().getData();

                        List<QCOrderCard> data = new ArrayList<>();
                        for (int i = 0; i < res.size(); i++) {
                            if (res.get(i).getCards() != null)
                                data.addAll(res.get(i).getCards());
                            else {
                                QCOrderCard qcOrderCard = new QCOrderCard();
                                qcOrderCard.setSku(null);
                                qcOrderCard.setProductName(res.get(i).getProductName());
                                qcOrderCard.setCardNumber("");
                                qcOrderCard.setCardPin("");
                                qcOrderCard.setAmount(res.get(i).getStatus());
                                qcOrderCard.setActivationUrl(null);
                                qcOrderCard.setActivationCode(null);
                                qcOrderCard.setValidity(null);
                                data.add(qcOrderCard);
                            }
                        }
                        adapter = new QCOrderListAdapter(context, data, new QCOrderListAdapter.ClickListenerInterface() {
                            @Override
                            public void onItemClickActivities(QCOrderCard card, int bgDrawable) {
                                if (card.getSku() != null)
                                    showQCPopUp(card, bgDrawable);
                            }
                        });

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        binding.rvQcOrderList.setAdapter(adapter);
                        binding.rvQcOrderList.setLayoutManager(gridLayoutManager);
                        binding.rvQcOrderList.setItemViewCacheSize(30);

                    } else {
                        binding.rvQcOrderList.setVisibility(View.GONE);
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_order_list_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QCOrderResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_order_list_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken() {
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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getAllQCOrderList();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
               /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MobileNumberActivity.class);
                startActivity(intent);
                SharedPref.clearSharedPref();
                finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    private void showQCPopUp(QCOrderCard cards, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        QcOrderPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.qc_order_popup, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.tvProductName.setText(cards.getProductName());
        binding.tvSku.setText(cards.getSku());

        if(cards.getActivationCode() != null){
            binding.llUBActivationCodeCopy.setVisibility(View.VISIBLE);
            binding.tvUBActivationCode.setVisibility(View.VISIBLE);
            binding.tvUBActivationCOdeVal.setText(cards.getActivationCode());
        } else {
            binding.llUBActivationCodeCopy.setVisibility(View.GONE);
            binding.tvUBActivationCode.setVisibility(View.GONE);
        }

        if (cards.getActivationUrl() != null) {
            SpannableString activationUrl = new SpannableString(cards.getActivationUrl());
            activationUrl.setSpan(new UnderlineSpan(), 0, activationUrl.length(), 0);
            binding.tvUBActivationUrlVal.setText(activationUrl);
            binding.tvUBActivationUrlVal.setOnClickListener(v -> {
                openWebView(cards.getActivationUrl(), "");
            });
            binding.tvCopy.setOnClickListener(view -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", cards.getActivationCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            });

            binding.tvUBActivationUrl.setVisibility(View.VISIBLE);
            binding.tvUBActivationUrlVal.setVisibility(View.VISIBLE);

        } else {
            binding.tvUBActivationUrl.setVisibility(View.GONE);
            binding.tvUBActivationUrlVal.setVisibility(View.GONE);
        }


        binding.llVoucherCode.setVisibility(View.VISIBLE);
        binding.tvCardAmountVal.setText(cards.getAmount());
        binding.tvCardNumberVal.setText(cards.getCardNumber());
        binding.tvCardPinVal.setText(cards.getCardPin());
        String date = CommonUtils.formatDateFromString("yyyy-mm-dd", "dd/mm/yyyy", cards.getValidity().split("T")[0]);
        binding.tvCardValidationVal.setText(date);


        binding.btnPositive.setOnClickListener(v -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.5f));
    }

    public void openWebView(String url, String loginURL) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("comingFrom","");
        intent.putExtra("Url", url);
        intent.putExtra("loginUrl", loginURL);
        startActivity(intent);
    }

}