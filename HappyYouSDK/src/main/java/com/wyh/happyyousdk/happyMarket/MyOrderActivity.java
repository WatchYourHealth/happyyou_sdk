package com.wyh.happyyousdk.happyMarket;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMyOrderBinding;
import com.wyh.happyyousdk.databinding.LayouHeartAgeInfoBinding;
import com.wyh.happyyousdk.databinding.OrderInfoCardPopupBinding;
import com.wyh.happyyousdk.happyMarket.Model.OrderInfoCardModel;
import com.wyh.happyyousdk.happyMarket.adapter.MyOrderAdapter;
import com.wyh.happyyousdk.happyMarket.adapter.MyOrderInfoCardAdapter;
import com.wyh.happyyousdk.model.MyOrderResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity {

    ActivityMyOrderBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    MyOrderAdapter adapter;
    List<MyOrderResponse.Data> myOrderDataList;
    String vendor;
    boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.commonToolBar.tvBack.setText("My Order");
        binding.commonToolBar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        vendor = getIntent().getStringExtra("vendor");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        getMyOrder();

        binding.edtMyOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isFirst){
                    filter(editable.toString().toLowerCase());
                }
            }
        });

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
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_success));
                    binding.llNoRecordFound.setVisibility(View.GONE);
                    binding.llHeading.setVisibility(View.VISIBLE);
                    binding.rvMyOrder.setVisibility(View.VISIBLE);
                    myOrderDataList = response.body().getData();
                    List<MyOrderResponse.Data> data = new ArrayList<>();
                   if(myOrderDataList.size()>0){


                       if(Objects.equals(vendor, "")){
                           data = myOrderDataList;
                       }else{
                           binding.edtMyOrder.setText(vendor);
                           data = searchData(vendor.toLowerCase());
                       }

                       if (data.size() == 0) {
                           binding.llNoRecordFound.setVisibility(View.VISIBLE);
                           binding.rvMyOrder.setVisibility(View.GONE);
                           binding.llHeading.setVisibility(View.GONE);
                       } else {
                           binding.llNoRecordFound.setVisibility(View.GONE);
                           binding.rvMyOrder.setVisibility(View.VISIBLE);
                           binding.llHeading.setVisibility(View.VISIBLE);
                           adapter = new MyOrderAdapter(context, data, data1 -> showInfoLayout(data1));
                           binding.rvMyOrder.setLayoutManager(new LinearLayoutManager(context));
                           binding.rvMyOrder.setAdapter(adapter);
                           isFirst = true;
                       }


                   }else{
                       binding.llNoRecordFound.setVisibility(View.VISIBLE);
                       binding.llHeading.setVisibility(View.GONE);
                       binding.rvMyOrder.setVisibility(View.GONE);
                   }


                   /* if(response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null ){
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(MyOrderActivity.this, response.body().getFeedbackDetails());
                    }*/
                }else{
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

    private void filter(String text) {
        List<MyOrderResponse.Data> temp = searchData(text);

        if (temp.size() == 0) {
            binding.llNoRecordFound.setVisibility(View.VISIBLE);
            binding.rvMyOrder.setVisibility(View.GONE);
            binding.llHeading.setVisibility(View.GONE);
        } else {
            binding.llNoRecordFound.setVisibility(View.GONE);
            binding.rvMyOrder.setVisibility(View.VISIBLE);
            binding.llHeading.setVisibility(View.VISIBLE);
            adapter.updateList(temp);
        }
    }

    private List<MyOrderResponse.Data> searchData(String text){
        List<MyOrderResponse.Data> temp = new ArrayList<>();
        for (MyOrderResponse.Data p : myOrderDataList) {
            if (p.getVendor().toLowerCase().contains(text) || p.getStatus().toLowerCase().contains(text)) {
                temp.add(p);
            }
        }

        return temp;
    }


    private void showInfoLayout(MyOrderResponse.Data data){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        OrderInfoCardPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_info_card_popup, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        List<OrderInfoCardModel> dataList = new ArrayList<>();
        String date = data.getDate().split("T")[0];
        dataList.add(new OrderInfoCardModel("Customer Name", (data.getCustomerName() != null && !Objects.equals(data.getCustomerName(), "")) ? data.getCustomerName() : "NA"));
        dataList.add(new OrderInfoCardModel("OrderID", (data.getOrderID() != null && !Objects.equals(data.getOrderID(), "")) ? data.getOrderID()  : "NA" ));
        dataList.add(new OrderInfoCardModel("Vendor", (data.getVendor() != null && !Objects.equals(data.getVendor(), "")) ? data.getVendor() : "NA"));
        dataList.add(new OrderInfoCardModel("Date", (data.getDate() != null && !Objects.equals(data.getDate(), "")) ? CommonUtils.formatDateFromString("yyyy-mm-dd", "dd/mm/yyyy", date) : "NA"));
        dataList.add(new OrderInfoCardModel("Date of Appointment", (data.getDateOfAppointment() != null && !Objects.equals(data.getDateOfAppointment(), "")) ? CommonUtils.formatDateFromString("yyyy-mm-dd", "dd/mm/yyyy", data.getDateOfAppointment()) : "NA"));
        dataList.add(new OrderInfoCardModel("Service", (data.getService() != null && !Objects.equals(data.getService(), "")) ? data.getService() : "NA"));
        dataList.add(new OrderInfoCardModel("Report Url", (data.getReportUrl() != null && !Objects.equals(data.getReportUrl(), "")) ? data.getReportUrl(): "NA"));
        //dataList.add(new OrderInfoCardModel("Report Url", "https://stackoverflow.com/questions/12013416/is-there-any-way-in-android-to-force-open-a-link-to-open-in-chrome"));
        dataList.add(new OrderInfoCardModel("Invoice Document", (data.getInvoiceDocument() != null && !Objects.equals(data.getInvoiceDocument(), "")) ? data.getInvoiceDocument() : "NA"));
        dataList.add(new OrderInfoCardModel("Status", (data.getStatus() != null && !Objects.equals(data.getStatus(), "")) ? data.getStatus() : "NA"));


        MyOrderInfoCardAdapter adapter = new MyOrderInfoCardAdapter(MyOrderActivity.this, dataList);
        binding.rv.setAdapter(adapter);

        binding.btnClosed.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}