package com.wyh.happyyousdk.happyMarket;

import static com.wyh.happyyousdk.happyMarket.HappyMartActivity.totalPoints;
import static com.wyh.happyyousdk.rewards.fragment.LevelFragment.equivalentAmount;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;
import static com.wyh.happyyousdk.utils.Constants.*;
import static com.wyh.happyyousdk.utils.Constants.HappyMartDevice;
import static com.wyh.happyyousdk.utils.Constants.HappyMartDiagnostics;
import static com.wyh.happyyousdk.utils.Constants.HappyMartFitness;
import static com.wyh.happyyousdk.utils.Constants.HappyMartHobby;
import static com.wyh.happyyousdk.utils.Constants.HappyMartPharmacy;
import static com.wyh.happyyousdk.utils.Constants.HappyMentalWellbeing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.Sonde.Activities.RespiratoryCheck;
import com.wyh.happyyousdk.TermsAndConditionActivity;
import com.wyh.happyyousdk.WebActivity;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityHappyMartDisclaimerBinding;
import com.wyh.happyyousdk.databinding.EditNickNameLayoutBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.databinding.TermsLayoutBinding;
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartPartnerAdapter;
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartPartnerOtherAdapter;
import com.wyh.happyyousdk.happyMarket.adapter.TermsAndCondtionAdapter;
import com.wyh.happyyousdk.model.CHCustomerRegistrationModel;
import com.wyh.happyyousdk.model.HACustomerRegistrationRequest;
import com.wyh.happyyousdk.model.HACustomerRegistrationResponse;
import com.wyh.happyyousdk.model.HappyMartModel;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.AllocateVoucherRequest;
import com.wyh.happyyousdk.model.request.GetUserTypemodel;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.request.rewards.RedeemRewardsRequest;
import com.wyh.happyyousdk.model.response.AllocateVoucherData;
import com.wyh.happyyousdk.model.response.AllocateVoucherResponse;
import com.wyh.happyyousdk.model.response.AmahaKeyResponse;
import com.wyh.happyyousdk.model.response.GetKliActivitiesResponse;
import com.wyh.happyyousdk.model.response.dashboard.UsertypeDashboardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.qc.QCProductsActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HappyMartDisclaimerActivity extends AppCompatActivity implements ScratchListener {
    ActivityHappyMartDisclaimerBinding binding;
    Context context;
    HappyMartPartnerAdapter adapter;
    HappyMartPartnerOtherAdapter adapterOthers;
    String cameFrom, url;

    String toolbarname;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    String selectedPartner = "";
    double totalPoint;
    String enterAmount = "0", bannerID;
    DecimalFormat format = new DecimalFormat("0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_happy_mart_disclaimer);
        context = this;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        SharedPref.init(context);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cameFrom = extras.getString("came_from");
            toolbarname = (extras.getString("toolbarname"))!=null?extras.getString("toolbarname"):"Back";
            if(extras.getString("selectedPartner") != null){
                selectedPartner = extras.getString("selectedPartner");
            }

        }

        if (cameFrom.equals(Constants.DASHBOARD_BANNER) || cameFrom.equals(KLI_OTHER)) {
            binding.rlBottomLayout.setVisibility(View.VISIBLE);
            binding.happyMartRecyclerView.setVisibility(View.GONE);
            binding.llAmount.setVisibility(View.GONE);
            binding.tvInfo.setVisibility(View.GONE);
            toolbarname = (extras.getString("toolbarname")) != null ? extras.getString("toolbarname") : "Back";
            bannerID = extras.getString("bannerID", "");
            if (extras != null) {
                Glide.with(context)
                        .load(extras.getString("disclaimerURL"))
                        .into(binding.partnerImage);
                url = extras.getString("url");
                binding.tvDisclaimer.loadData(extras.getString("disclaimer"), "text/html", "utf-8");
            } else {
                binding.tvDisclaimer.loadData(getResources().getString(R.string.disclaimer), "text/html", "utf-8");
            }
        } else {
            binding.rlBottomLayout.setVisibility(View.GONE);
            binding.happyMartRecyclerView.setVisibility(View.VISIBLE);
            binding.tvDisclaimer.loadData(getResources().getString(R.string.disclaimer), "text/html", "utf-8");

        }
        binding.includeBack.tvBack.setText(toolbarname);
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));


//        binding.tvDisclaimer.setText(Html.fromHtml(getResources().getString(R.string.disclaimer)));


        SpannableString textCondition = new SpannableString("I accept the Terms of Use");
        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermsAndConditionActivity.class);
                startActivity(intent);
            }
        };
        textCondition.setSpan(termsAndCondition, 13, 25, 0);

        binding.tvTandc.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE);

        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.enterWalletAmount.setText(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    try {
                        double amount = Integer.parseInt(charSequence.toString()) * 0.2;
                        enterAmount = String.format("%.2f", amount);
                        binding.enterWalletAmount.setText(String.format("%.2f", amount));
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    //binding.enterWalletAmount.setText("00.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        binding.rlQuiksilver.setOnClickListener(view -> {
            /*if (SharedPref.getCurrentLevel() < 6) {
                showPopUp();
                return;
            }*/
            binding.llOthers.setVisibility(View.GONE);
            binding.rlBottomLayout.setVisibility(View.VISIBLE);
            binding.partnerImage.setVisibility(View.GONE);
            binding.tvInfo.setVisibility(View.GONE);
            binding.llAmount.setVisibility(View.GONE);
            selectedPartner = "other";
        });

        if (Objects.equals(cameFrom, HappyMartOther)) {
            binding.tvTitle.setText("You are being guided to\n\nVouchers");
            binding.tvTitle.setText("You are being guided to\n\nVouchers");
            getKliActivities();
        } else {
            binding.tvTitle.setText("You are being guided to");
        }

        List<HappyMartModel> list = getLogo(cameFrom);


        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.tvCouponCode.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        binding.ivInfoPharmeasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConcernInfoLayout();
            }
        });

        if (cameFrom.equals(HappyMartOther)) {
            binding.rlBottomLayout.setVisibility(View.GONE);
            binding.happyMartRecyclerView.setVisibility(View.GONE);
            list.clear();
            binding.llOthers.setVisibility(View.VISIBLE);
        }

        adapter = new HappyMartPartnerAdapter(context, list, new HappyMartPartnerAdapter.OnItemClickListener() {
            @Override
            public void onClick(HappyMartModel data) {
                binding.happyMartRecyclerView.setVisibility(View.GONE);
                binding.tvInfo.setVisibility(View.GONE);
                binding.rlBottomLayout.setVisibility(View.VISIBLE);
                binding.partnerImage.setVisibility(View.VISIBLE);
                binding.partnerImage.setImageResource(data.getImage());
                selectedPartner = data.getName();
                if (Objects.equals(data.getName(), "pharmeasy")) {
                    binding.llCoupon.setVisibility(View.VISIBLE);
                } else {
                    binding.llCoupon.setVisibility(View.GONE);
                }


                if (Objects.equals(cameFrom, HappyMartDiagnostics)) {
                    if (SharedPref.getCurrentLevel() >= 6) {
                        if (!selectedPartner.equalsIgnoreCase("visitor")) {
                            binding.llAmount.setVisibility(View.VISIBLE);
                            getRewardsDashboardData();
                        } else {
                            binding.llAmount.setVisibility(View.GONE);
                        }
                    } else {
                        binding.llAmount.setVisibility(View.GONE);
                    }
                } else {
                    binding.llAmount.setVisibility(View.GONE);
                }
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.happyMartRecyclerView.setLayoutManager(layoutManager);
        binding.happyMartRecyclerView.setAdapter(adapter);

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        binding.includeBack.llBack.setOnClickListener(view -> finish());
        binding.btnProceed.setOnClickListener(v -> {
            try {

            } catch (Exception e) {

            }

            if (binding.checkBox.isChecked()) {
                if (cameFrom.equals(DASHBOARD_BANNER)) {
                    goToNextScreen(cameFrom);
                } else {
                    if (selectedPartner.equals("Amaha")) {
                        getAmahaKey(this);
                    } else {
                        goToNextScreen(cameFrom);
                    }
                }

            } else {
                Toast.makeText(context, "Please accept the Terms of Use", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void  getAmahaKey(Context context) {
        try {
            CommonUtils.showProgressDialige(context);
            APIInterface apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface.class);
            apiInterface.amahaGetKey(SharedPref.getAuthToken()).enqueue(new Callback<AmahaKeyResponse>() {
                @Override
                public void onResponse(Call<AmahaKeyResponse> call, Response<AmahaKeyResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.isSuccessful()) {
                        if (response.body() != null && response.body().getData() != null) {
                            String url = "https://kotak-life-insurance.integrations.amahahealth.com?CRNID=" + SharedPref.getAesUuid() + "&code=" + response.body().getData().getCode();
                            Intent i = new Intent(context, AmahaWebView.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("Url", url);
                            context.startActivity(i);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AmahaKeyResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();

                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }
    }

    private void showConcernInfoLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        TermsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.terms_layout, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        List<TermsNConditionModel> data = new ArrayList<>();

        data.add(new TermsNConditionModel("Offer valid once per user.", "1"));
        data.add(new TermsNConditionModel("Offer valid for all users.", "2"));
        data.add(new TermsNConditionModel("A user will get a flat 15% off PLD + Extra 5% Cashback on the medicine order.", "3"));
        data.add(new TermsNConditionModel("Coupon code valid on a minimum MRP cart value of 1500 and the order must have a minimum medicine cart value of Rs.750.", "4"));
        data.add(new TermsNConditionModel("The Extra cashback is capped to Rs.500.", "5"));
        data.add(new TermsNConditionModel("Once the order is successfully delivered you will receive the PE Cashback within 48 hours after the order is\n" +
                "delivered and fulfilled.", "6"));
        data.add(new TermsNConditionModel("Credited cashback is valid only for 60 days.", "7"));
        data.add(new TermsNConditionModel("This offer cannot be combined with any other offers.", "8"));
        data.add(new TermsNConditionModel("Offer applicable only on Medicine orders.", "9"));
        data.add(new TermsNConditionModel("Coupon code not applicable on healthcare products.", "10"));
        data.add(new TermsNConditionModel("For coupon code issues, please reach out to care@pharmeasy.in / 766100300.", "11"));
        data.add(new TermsNConditionModel("PharmEasy has the right to amend the terms & conditions, end the offer, or call back any or all of its offers\n" +
                "without prior notice.", "12"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        TermsAndCondtionAdapter termsAndCondtionAdapter = new TermsAndCondtionAdapter(data, this);
        binding.rvTermsCondition.setAdapter(termsAndCondtionAdapter);
        binding.rvTermsCondition.setLayoutManager(linearLayoutManager);
        binding.rvTermsCondition.setHasFixedSize(true);

        binding.btnOK.setOnClickListener(view -> alertDialog.dismiss());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }

        binding.btnOK.setOnClickListener(v-> {
            alertDialog.dismiss();
        });
*/
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cameFrom.equals(DASHBOARD_BANNER) || cameFrom.equals(KLI_OTHER)) {
            binding.happyMartRecyclerView.setVisibility(View.GONE);
            binding.llAmount.setVisibility(View.GONE);
            binding.rlBottomLayout.setVisibility(View.VISIBLE);
            binding.tvInfo.setVisibility(View.GONE);
        } else {
            binding.happyMartRecyclerView.setVisibility(View.VISIBLE);
            binding.rlBottomLayout.setVisibility(View.GONE);
        }

        binding.checkBox.setChecked(false);
        binding.edtAmount.getText().clear();
        if (cameFrom.equals(HappyMartOther)) {
            binding.rlBottomLayout.setVisibility(View.GONE);
            binding.happyMartRecyclerView.setVisibility(View.GONE);
            binding.llOthers.setVisibility(View.VISIBLE);
            binding.llComingSoon.setVisibility(View.GONE);
        }
        if (selectedPartner != null && selectedPartner != "") {
            if (selectedPartner.equals("other")) {
                binding.llOthers.setVisibility(View.GONE);
                binding.rlBottomLayout.setVisibility(View.VISIBLE);
                binding.partnerImage.setVisibility(View.GONE);
                binding.tvInfo.setVisibility(View.GONE);
                binding.llAmount.setVisibility(View.GONE);
            } else if (selectedPartner.equals("AdvantageClub")) {
                binding.llOthers.setVisibility(View.GONE);
                binding.rlBottomLayout.setVisibility(View.VISIBLE);
                binding.partnerImage.setVisibility(View.GONE);
                binding.tvInfo.setVisibility(View.GONE);
                binding.llAmount.setVisibility(View.GONE);
            } else if (selectedPartner.equalsIgnoreCase("visitor")) {
                binding.llAmount.setVisibility(View.GONE);
            } else {
                binding.happyMartRecyclerView.setVisibility(View.GONE);
                binding.rlBottomLayout.setVisibility(View.VISIBLE);
                if (Objects.equals(selectedPartner, "pharmeasy")) {
                    binding.llCoupon.setVisibility(View.VISIBLE);
                } else {
                    binding.llCoupon.setVisibility(View.GONE);
                }
                if (Objects.equals(cameFrom, HappyMartDiagnostics)) {
                    if (SharedPref.getCurrentLevel() >= 6) {
                        if (selectedPartner.equals("visitor")) {
                            binding.llAmount.setVisibility(View.GONE);
                        } else {
                            binding.llAmount.setVisibility(View.VISIBLE);
                            getRewardsDashboardData();
                        }

                    } else {
                        binding.llAmount.setVisibility(View.GONE);
                    }
                } else {
                    binding.llAmount.setVisibility(View.GONE);
                }
            }
        }

        if (cameFrom.equals(HappyMartFitness)) {
            binding.rlBottomLayout.setVisibility(View.GONE);
            binding.happyMartRecyclerView.setVisibility(View.GONE);
            binding.llComingSoon.setVisibility(View.VISIBLE);
            binding.tvInfo.setVisibility(View.GONE);
        }

    }

    private List<HappyMartModel> getLogo(String cameFrom) {
        List<HappyMartModel> list = new ArrayList<>();
        if (HappyMartOPD.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.ic_medpay_logo, "medpay"));
            return list;
        } else if (HappyMartPharmacy.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.ic_medpay_logo, "medpay"));
            list.add(new HappyMartModel(R.drawable.ic_pharmeasy_logo, "pharmeasy"));
            return list;
        } else if (HappyMartDiagnostics.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.ha_logo, "health_assure"));
            list.add(new HappyMartModel(R.drawable.connected_logo, "connected"));
            list.add(new HappyMartModel(R.drawable.vistorlogo, "visitor"));
            return list;
        } else if (HappyMartFitness.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.coach_pro_logo, "coach_pro"));
            return list;
        } else if (HappyMentalWellbeing.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.mrf_logo_light, "mrf"));
            list.add(new HappyMartModel(R.drawable.amaha_logo, "Amaha"));
            return list;
        } else if (HappyMartHobby.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.hobby_tribe_logo, "fitter"));
            return list;
        } else if (HappyMartDevice.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.actofit_logo, "actofit"));
            return list;
        } else if (DASHBOARD_BANNER.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.actofit_logo, "actofit"));
            return list;
        } else if (HappyMartTravel.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.trip_stacc_logo, "Tripstacc"));
            return list;
        } else if (HappyMartMerchandise.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.adavnatageclub, "Advantage Club"));
            list.add(new HappyMartModel(R.drawable.shopstacc_transparent, "Shopsta"));
            return list;
        } else if (HappyMartTeleconsulatation.equals(cameFrom)) {
            list.add(new HappyMartModel(R.drawable.vistorlogo, "visitor"));
            return list;
        } else {
            list.add(new HappyMartModel(R.drawable.doconline_logo, "docoline"));
            return list;
        }
    }

    private void goToNextScreen(String cameFrom) {
        Analytics.logEvent(context, "Third_party_vender_name", "A_" + cameFrom + "_" + bannerID);
        Analytics.logEvent(context, "Third_party_UUID", "A_" + SharedPref.getAesUuid());
        Intent intent;
        if (HappyMartOPD.equals(cameFrom)) {
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + getResources().getString(R.string.medpayOPD));
            openWebView(getResources().getString(R.string.medpayOPD), "");
            openWebView(getResources().getString(R.string.medpayOPD), "");
        } else if (HappyMartPharmacy.equals(cameFrom)) {
            if (selectedPartner.equals("medpay")) {
                Analytics.logEvent(context, "Third_party_vender_url", "A_" + getResources().getString(R.string.medpayPharmacy));
                openWebView(getResources().getString(R.string.medpayPharmacy), "");
            } else {
                Analytics.logEvent(context, "Third_party_vender_url", "A_" + getResources().getString(R.string.medpayPharmacy));
                openWebView("https://pharmeasy.in/online-medicine-order?utm_source=alz-watchyourhealth&utm_medium=alliance&utm_campaign=alz-medicine-18july2023", "");
            }
        } else if (HappyMartTeleconsulatation.equals(cameFrom)) {
            getUserType(context,context.getString(R.string.getVisitURL));
        }
        else if (HappyMartDiagnostics.equals(cameFrom)) {
           diagonistics();

        } else if (HappyMartFitness.equals(cameFrom)) {
            String url = "https://www.coachprokotak.in/auth?UID=" + SharedPref.getAesUuid();
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
            openWebView(url, "");
//            openWebView("https://wellness.futuregenerali.in/UI/WellnessContent/List?clid=W0PhJ0R1ASdnp9HDCSiN0A%3D%3D&policyNo=Gx%2BNQaIVoRyHN3mauEqDOA%3D%3D", "");
        } else if (HappyMentalWellbeing.equals(cameFrom)) {
            String url = "https://themindresearchfoundation.com/wtpd-authenticate?UID=" + SharedPref.getAesUuid();
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
            openWebView(url, "");
        } else if (HappyMartHobby.equals(cameFrom)) {
            String url = "https://www.thehobbytribe.com/kotak-bts/employee/authenticate?corporate=kotak_life&UID=" + SharedPref.getAesUuid();
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
            openWebView(url, "");
        } else if (HappyMartDevice.equals(cameFrom)) {
            String url = "https://actofit.com/collections/kotak?user_id=" + SharedPref.getAesUuid();
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
            openWebView(url, "");
        } else if (HappyMartTravel.equals(cameFrom)) {
            String url = "https://uat-sso.ai-loyalty.com/landing?UUID=" + SharedPref.getAesUuid() + "&piid=61JFRQ4B-JIEF-JM65-A2F0-KNL57ED456MC";
            Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
            openWebView(url, "");
        } else if (HappyMartMerchandise.equals(cameFrom)) {
            if (selectedPartner.equalsIgnoreCase("Shopsta")) {
                String url = "https://sso.ai-loyalty.com/landing?UUID=" + SharedPref.getAesUuid() + "&piid=JHTG56MRDKT-UYTD-HG90-KJYJ-PRDKT030124";
                Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                openWebView(url, "");
            } else if (selectedPartner.equalsIgnoreCase("Advantage Club")) {
                String url = "https://codetest.advantageclub.co/advantageclub_login_otp?ku" + SharedPref.getAesUuid();
                Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                openWebView(url, "");
            }


        }
        else if(cameFrom.equals(DASHBOARD_BANNER)){
            try {
                if(selectedPartner != ""){
                    if(selectedPartner.equalsIgnoreCase("health_assure") || selectedPartner.equalsIgnoreCase("connected")
                    || selectedPartner.equalsIgnoreCase("visitor")){
                        diagonistics();
                    }else if (selectedPartner.equalsIgnoreCase("GetVisit")){
                       getUserType(context,context.getString(R.string.getVisitURL));
                    }else if (selectedPartner.equalsIgnoreCase("medpay")){
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + getResources().getString(R.string.medpayPharmacy));
                        openWebView(getResources().getString(R.string.medpayPharmacy), "");
                    } else if(selectedPartner.equalsIgnoreCase("pharmeasy")){
                        binding.llCoupon.setVisibility(View.VISIBLE);
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + getResources().getString(R.string.medpayPharmacy));
                        openWebView("https://pharmeasy.in/online-medicine-order?utm_source=alz-watchyourhealth&utm_medium=alliance&utm_campaign=alz-medicine-18july2023", "");
                    } else if(selectedPartner.equalsIgnoreCase("mrf")){
                        String url = "https://themindresearchfoundation.com/wtpd-authenticate?UID=" + SharedPref.getAesUuid();
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    } else if(selectedPartner.equalsIgnoreCase("Amaha")){
                        getAmahaKey(this);
                    } else if(selectedPartner.equalsIgnoreCase("fitter") || selectedPartner.equalsIgnoreCase("hobby")){
                        String url = "https://www.thehobbytribe.com/kotak-bts/employee/authenticate?corporate=kotak_life&UID=" + SharedPref.getAesUuid();
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    } else if(selectedPartner.equalsIgnoreCase("actofit")){
                        String url = "https://actofit.com/collections/kotak?user_id=" + SharedPref.getAesUuid();
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    } else if(selectedPartner.equalsIgnoreCase("Tripstacc")){
                        String url = "https://uat-sso.ai-loyalty.com/landing?UUID=" + SharedPref.getAesUuid() +"&piid=61JFRQ4B-JIEF-JM65-A2F0-KNL57ED456MC";
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    } else if(selectedPartner.equalsIgnoreCase("Advantage Club")){
                        String url = "https://ac-react.advantageclub.co?ku=" + SharedPref.getAesUuid();
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    } else if(selectedPartner.equalsIgnoreCase("Shopsta")){
                        String url = "https://sso.ai-loyalty.com/landing?UUID="+SharedPref.getAesUuid()+"&piid=JHTG56MRDKT-UYTD-HG90-KJYJ-PRDKT030124";
                        Analytics.logEvent(context, "Third_party_vender_url", "A_" + url);
                        openWebView(url, "");
                    }
                }
                if(url.contains("watchyourhealth.com")){
                    openWebView(url, "");
                } else {
                    if(url.contains("zoom")){
                        Uri uri = Uri.parse(url.replace("zoomus://","https://"));
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(sendIntent);
                    }else{
                        if(url.contains("{userid}")){
                            openWebView(url.replace("{userid}",SharedPref.getAesUuid()), "");
                        }else{
                            openWebView(url,"");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(cameFrom.equals(KLI_OTHER)){
            openWebView(url, "");
        } else if (HappyMartTeleconsulatation.equals(cameFrom)) {
           getUserType(context,context.getString(R.string.getVisitURL));
        } else if (cameFrom.equals(HappyMartOther)) {
            intent = new Intent(context, QCProductsActivity.class);
            intent.putExtra("points", totalPoints);
            intent.putExtra("amount", equivalentAmount);
            startActivity(intent);
        }
    }

    private void diagonistics(){
        try{
            if(SharedPref.getCurrentLevel() < 6){
                if (selectedPartner.equals("health_assure")) {
                    getHACustomerRegistration(context,enterAmount);
                } else if(selectedPartner.equalsIgnoreCase("visitor")){
                   getUserType(context,context.getString(R.string.getVisitURL));
                }
                else {
                    cHCustomerRegistration(context);
                }
            }else{
                if(!binding.enterWalletAmount.getText().toString().equals("00.00")){
                    if(checkAmount()){
                        if((int) Double.parseDouble(binding.enterWalletAmount.getText().toString()) > 0){
                            updateUserRewardBalance(context,selectedPartner,"0");
                        }else{
                            Toast.makeText(context, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (selectedPartner.equals("health_assure")) {
                        getHACustomerRegistration(context,enterAmount);
                    } else if(selectedPartner.equalsIgnoreCase("visitor")){
                       getUserType(context,context.getString(R.string.getVisitURL));
                    } else {
                        cHCustomerRegistration(context);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getUserType(Context context, String redirectionUrl){
        try{
            CommonUtils.showProgressDialige(context);
            GetUserTypemodel request = new GetUserTypemodel(SharedPref.getEncryptedMobileNo());
            ApiInterfaceWyh apiInterface = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
            Call<UsertypeDashboardData> call = apiInterface.getUserType(SharedPref.getAuthToken(), request);
            call.enqueue(new Callback<UsertypeDashboardData>() {
                @Override
                public void onResponse(Call<UsertypeDashboardData> call, Response<UsertypeDashboardData> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getDashboardData() != null) {
                            String url = redirectionUrl + SharedPref.getAesUuid() + "&userType=" + response.body().getDashboardData().get(0).getUserType();
                            Intent i = new Intent(context, WebActivity.class);
                            i.putExtra("comingFrom","");
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("Url", url);
                            context.startActivity(i);
                        } else {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UsertypeDashboardData> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }
    }

    public void openWebView(String url, String loginURL) {
        Log.e("URL1",url);
        Log.e("URL2",loginURL);
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("Url", url);
        intent.putExtra("comingFrom","");
        intent.putExtra("loginUrl", loginURL);
        startActivity(intent);
    }

    public void getRewardsDashboardData() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.show();
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest("");
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {

            @Override
            public void onResponse(@NonNull Call<LevelDashboardResponse> call, @NonNull Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_success));
                    Double totalAmount = response.body().getData().getEquivalentAmount();
                    totalPoint = Math.round(totalAmount / 0.2);
                    binding.walletAmount.setText("Your available points: " + format.format(totalPoint));
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LevelDashboardResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
            }
        });
    }

    public void updateUserRewardBalance(Context context, String selectedPartner,String enterAmount) {
        CommonUtils.showProgressDialige(context);
        Double amount = Double.valueOf(enterAmount);
        String des;
        if (selectedPartner.equals("health_assure")) {
            des = "Health Assure";
        } else {
            des = "Connect & Heal";
        }
        RedeemRewardsRequest request = new RedeemRewardsRequest(SharedPref.getAesUuid(), todayDateInFormat("yyyy-MM-dd"),
                (int) Math.round(amount), des);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateUserRewards(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_success));
                    CommonUtils.dismissDialoge();
                    if (selectedPartner.equals("health_assure") || selectedPartner.equals("Health assure")) {
                        getHACustomerRegistration(context,enterAmount);
                    } else {
                        cHCustomerRegistration(context);
                    }
                } else {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                CommonUtils.dismissDialoge();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getHACustomerRegistration(Context context, String amt) {
        CommonUtils.showProgressDialige(context);

        double amount;
        if (SharedPref.getCurrentLevel() >= 6) {
            amount = Double.parseDouble(amt);
        } else {
            amount = 0.0;
        }

        HACustomerRegistrationRequest request = new HACustomerRegistrationRequest((int) Math.round(amount));
        Log.d("AuthToken", new Gson().toJson(request));
        ApiInterfaceWyh apiInterface = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<HACustomerRegistrationResponse> call = apiInterface.getHACustomerRegistration(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<HACustomerRegistrationResponse>() {

            @Override
            public void onResponse(@NonNull Call<HACustomerRegistrationResponse> call, @NonNull Response<HACustomerRegistrationResponse> response) {
                CommonUtils.dismissDialoge();

                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ha_customer_registration_success));
                    Analytics.logEvent(context, "Third_party_vender_url", "A_" + response.body().getData().get(0).getLoginURL());
                    Intent i = new Intent(context, WebActivity.class);
                    i.putExtra("comingFrom","");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("Url", response.body().getData().get(0).getLoginURL());
                    context.startActivity(i);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ha_customer_registration_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<HACustomerRegistrationResponse> call, @NonNull Throwable t) {
                CommonUtils.dismissDialoge();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ha_customer_registration_failed));
            }
        });

    }

    public void cHCustomerRegistration(Context context) {
        CommonUtils.showProgressDialige(context);
        double amount;
        if (SharedPref.getCurrentLevel() >= 6) {
            amount = Double.parseDouble(enterAmount);
        } else {
            amount = 0.0;
        }

        HACustomerRegistrationRequest request = new HACustomerRegistrationRequest((int) Math.round(amount));
        ApiInterfaceWyh apiInterface = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<CHCustomerRegistrationModel> call = apiInterface.cHCustomerRegistration(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CHCustomerRegistrationModel>() {

            @Override
            public void onResponse(@NonNull Call<CHCustomerRegistrationModel> call, @NonNull Response<CHCustomerRegistrationModel> response) {
                CommonUtils.dismissDialoge();

                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ch_customer_registration_success));
                    Analytics.logEvent(context, "Third_party_vender_url", "A_" + response.body().getData());

                    Log.d("URL", response.body().getData());
                    Intent i = new Intent(context, WebActivity.class);
                    i.putExtra("comingFrom","");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("Url", response.body().getData());
                    context.startActivity(i);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ch_customer_registration_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CHCustomerRegistrationModel> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.ch_customer_registration_failed));
            }
        });

    }

    private boolean checkAmount() {
        if (!(binding.edtAmount.getText().toString().isEmpty())) {
            return !(totalPoint < Double.valueOf(binding.edtAmount.getText().toString()));
        } else {
            return false;
        }
    }

    private void showPopUp() {
        CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
        customYesNoDialog.show();
        customYesNoDialog.setCancelable(false);
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
        customYesNoDialog.binding.txtInfoPopUpDesc.setText("Please reach up to level 6 to redeem your points");
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.btnYes.setText("OK");
        customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
        customYesNoDialog.binding.btnYes.setOnClickListener(view1 -> customYesNoDialog.dismiss());
    }

    private void showPopupOthers(GetKliActivitiesResponse.Data data) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        EditNickNameLayoutBinding bindingVoucher = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.edit_nick_name_layout, null, false);
        alertBuilder.setView(bindingVoucher.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        bindingVoucher.tvName.setText("Coupon");
        bindingVoucher.edtName.setVisibility(View.GONE);
        bindingVoucher.edtVoucher.setVisibility(View.VISIBLE);

        bindingVoucher.btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        bindingVoucher.btnSubmit.setOnClickListener(v -> {
            if (!bindingVoucher.edtVoucher.getText().toString().equals("") && !bindingVoucher.edtVoucher.getText().toString().isEmpty()) {
//                hideKeyboard(HappyMartDisclaimerActivity.this);
                // Check if no view has focus:
                View view = binding.getRoot();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                allocateVoucher(data.getActivityId(), bindingVoucher.edtVoucher.getText().toString().trim(), alertDialog);
            } else {
                Toast.makeText(context, "Please enter coupon code", Toast.LENGTH_SHORT).show();
            }
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void allocateVoucher(int activityId, String voucher, AlertDialog alertDialog) {
        progressDialog.show();
        AllocateVoucherRequest request = new AllocateVoucherRequest(voucher, activityId, "");
        Call<AllocateVoucherResponse> call = apiInterfaceWyh.allocateVoucher(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AllocateVoucherResponse>() {
            @Override
            public void onResponse(Call<AllocateVoucherResponse> call, Response<AllocateVoucherResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("res", new Gson().toJson(response.code()));
                Log.d("res", new Gson().toJson(response.body()));
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_success));
                    if (response.body().getSuccess()) {
                        alertDialog.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showScratchCard(response.body().getData(), R.drawable.scratch_card_orange_new);
                            }
                        }, 500);
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_failed));
                }
            }

            @Override
            public void onFailure(Call<AllocateVoucherResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_failed));
            }
        });
    }

    private void showScratchCard(AllocateVoucherData allocateVoucherData, int drawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialogScratched = alertBuilder.create();
        alertDialogScratched.setCancelable(true);
        if (!alertDialogScratched.isShowing())
            alertDialogScratched.show();

        binding.scratchView.onFullReveal();
        binding.tvEarned.setVisibility(View.VISIBLE);

        if (allocateVoucherData.getVoucherCode() != null) {
            binding.llCopy.setVisibility(View.VISIBLE);
            binding.btnRedeem.setVisibility(View.GONE);
            binding.llScratchview.setVisibility(View.GONE);
        } else {
            binding.llCopy.setVisibility(View.GONE);
            binding.btnRedeem.setVisibility(View.VISIBLE);
            binding.llScratchview.setVisibility(View.VISIBLE);
        }

        /*if (!freeVoucher.isScratched())
            voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());*/

        if (!SharedPref.getIsScratchedFirstCard())
            SharedPref.putIsScratchedFirstCard(true);

        binding.scratchView.setScratchListener(HappyMartDisclaimerActivity.this);
//        binding.llScratchview.setVisibility(View.VISIBLE);
        binding.scratchView.setScratchDrawable(ContextCompat.getDrawable(context, drawable));
        binding.tvTitle.setText(allocateVoucherData.getVoucherName());
        binding.tvValue.setText(allocateVoucherData.getVoucherValue() + " off");
        binding.tvDescription.setText(allocateVoucherData.getVoucherDesc());
        binding.tvCouponCode.setText(allocateVoucherData.getVoucherCode());
        binding.tvAmount.setText("₹" + allocateVoucherData.getVoucherValue());


        /*alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                alertDialog.dismiss();
            }
        });*/

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogScratched.dismiss();
        });

        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", allocateVoucherData.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogScratched.dismiss();
        });

        Glide.with(context)
                .load(allocateVoucherData.getVoucherLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivVendorLogo);

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogScratched.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogScratched.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void getKliActivities() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<GetKliActivitiesResponse> call = apiInterfaceWyh.getKliActivities(SharedPref.getAuthToken());
        call.enqueue(new Callback<GetKliActivitiesResponse>() {
            @Override
            public void onResponse(Call<GetKliActivitiesResponse> call, Response<GetKliActivitiesResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("res", new Gson().toJson(response.code()));
                Log.d("res", new Gson().toJson(response.body()));
                if (response.code() == 200 && response.body() != null) {

                    if (response.body().isSuccess()) {
                        List<GetKliActivitiesResponse.Data> list = new ArrayList<>();
                        GetKliActivitiesResponse.Data voucher = new GetKliActivitiesResponse.Data();
                        GetKliActivitiesResponse.Data voucher2 = new GetKliActivitiesResponse.Data();
                        GetKliActivitiesResponse.Data watchYourHealth = new GetKliActivitiesResponse.Data();
                        voucher.setActivityName("Vouchers");
                        watchYourHealth.setActivityName("Happy Friendship Day");
                        Log.d("Authtoken", list.toString());
                        list.add(voucher);
                        list.addAll(response.body().getData());



                        binding.rvOther.setAdapter(adapterOthers);



                    }
                } else if (response.code() == 401) {
//                    refreshAuthToken();
                } else {
//                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_bookmark_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetKliActivitiesResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_bookmark_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {

    }

    @Override
    public void onScratchStarted() {

    }
}