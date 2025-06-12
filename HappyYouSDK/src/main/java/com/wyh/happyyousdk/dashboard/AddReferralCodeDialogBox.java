package com.wyh.happyyousdk.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.CryptoHelper;
import com.wyh.happyyousdk.databinding.RefferelCodeDialogeLayoutBinding;
import com.wyh.happyyousdk.model.request.ReferalContestRequest;
import com.wyh.happyyousdk.model.request.UpdateCoopCodeRequest;
import com.wyh.happyyousdk.model.response.ReferalContestResponse;
import com.wyh.happyyousdk.model.response.UpdateCoopResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReferralCodeDialogBox {

    private static AddReferralCodeDialogBox instance = null;

    private AddReferralCodeDialogBox(){}


    public static synchronized AddReferralCodeDialogBox getInstance(){
        if(instance == null){
            instance = new AddReferralCodeDialogBox();
        }

        return instance;
    }

    public void addReferralCode(String comingFrom, Activity activity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        RefferelCodeDialogeLayoutBinding bindingNickName = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.refferel_code_dialoge_layout, null, false);
        alertBuilder.setView(bindingNickName.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        if(comingFrom.equalsIgnoreCase("referralCode")){
            bindingNickName.tvName.setText(activity.getString(R.string.add_referral_code));
            bindingNickName.edtRefferal.setHint("Enter referral code");
            if (SharedPref.getCopReffralCode() != null) {
                bindingNickName.edtRefferal.setText(SharedPref.getCopReffralCode());
            }

        }else{
            bindingNickName.tvName.setText(activity.getString(R.string.enter_mobile_num));
            bindingNickName.edtRefferal.setHint("Enter mobile number");
            bindingNickName.edtRefferal.setInputType(InputType.TYPE_CLASS_PHONE);
            bindingNickName.edtRefferal.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
        }


        bindingNickName.btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        bindingNickName.btnSubmit.setOnClickListener(v -> {


            if(comingFrom.equalsIgnoreCase("referralCode")){
                if (!bindingNickName.edtRefferal.getText().toString().equals("")) {
                    updateCorporateCode(bindingNickName.edtRefferal.getText().toString(), activity, alertDialog, bindingNickName.edtRefferal);
                } else {
                    Toast.makeText(activity, "Please enter referral code", Toast.LENGTH_SHORT).show();
                }
            }

        });


        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }



    private void updateCorporateCode(String refferalCode, Activity activity, AlertDialog alertDialog, EditText edtName) {
        try {
            CommonUtils.showProgressDialige(activity);
            UpdateCoopCodeRequest updateCoopCodeRequest = new UpdateCoopCodeRequest(refferalCode);
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.updateCoopCode(SharedPref.getAuthToken(), updateCoopCodeRequest).enqueue(new Callback<UpdateCoopResponse>() {
                @Override
                public void onResponse(Call<UpdateCoopResponse> call, Response<UpdateCoopResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData().size() != 0) {
                            if (!response.body().getData().get(0).getMsg().equals("") && response.body().getData() != null) {
                                if (!response.body().getData().get(0).getStatus().equalsIgnoreCase("false")) {
                                    SharedPref.putCopReferralCode(refferalCode);
                                    alertDialog.dismiss();
                                }
                                Toast.makeText(activity, response.body().getData().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<UpdateCoopResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
