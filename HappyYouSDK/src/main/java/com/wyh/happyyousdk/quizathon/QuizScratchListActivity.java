package com.wyh.happyyousdk.quizathon;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.databinding.ActivityQuizScrachListBinding;
import com.wyh.happyyousdk.model.response.quizathon.GetQuizScratchListResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.adapter.GridCardsScratchListAdapter;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.List;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizScratchListActivity extends AppCompatActivity implements onRewardClick, ScratchListener, rewardDialogCloseListener {
    ActivityQuizScrachListBinding binding;
    APIInterface apiInterface;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    ProgressDialog progressDialog;
    GetQuizScratchListResponse.QuizScratchItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterface = RetrofitHandler.apiInterface();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_scrach_list);
        binding.ivBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetQuizRewardList();
    }

    private void GetQuizRewardList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.getQuizRewardList(SharedPref.getAuthToken()).enqueue(new Callback<GetQuizScratchListResponse>() {
                @Override
                public void onResponse(Call<GetQuizScratchListResponse> call, Response<GetQuizScratchListResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetQuizScratchListResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    public void setData(GetQuizScratchListResponse rewardResponse) {
//        Type postLogRewardType = new TypeToken<EarnedRewardResponse>() {
//        }.getType();
//         rewardResponse = new Gson().fromJson(response, postLogRewardType);
        boolean isDataFound = false;
        binding.scrollRewardList.setVisibility(View.VISIBLE);
        binding.tvNDF.setVisibility(View.GONE);

        if (rewardResponse.getData() != null && !rewardResponse.getData().getActiveList().isEmpty()) {
            isDataFound = true;
            binding.rlActive.setVisibility(View.VISIBLE);
            binding.rvGridActive.setVisibility(View.VISIBLE);
            binding.rvGridActiveIndicator.setVisibility(View.VISIBLE);

            GridCardsScratchListAdapter gridCardsAdapter = new GridCardsScratchListAdapter(rewardResponse.getData().getActiveList(), "Active", this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvGridActive.setLayoutManager(linearLayoutManager);
            binding.rvGridActive.setAdapter(gridCardsAdapter);
            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
            binding.rvGridActive.setOnFlingListener(null);
            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridActive);
            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(this);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            int indicatorSize = 1;

            if (rewardResponse.getData().getActiveList().size() > 3) {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(rewardResponse.getData().getActiveList().size())) / 3.0);
            }

            if (indicatorSize > 1) {
                binding.rvGridActiveIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvGridActiveIndicator.setVisibility(View.GONE);
            }
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(this, indicatorSize, 0);
            binding.rvGridActiveIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvGridActiveIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvGridActiveIndicator.setHasFixedSize(true);

            binding.rvGridActive.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int position = 0;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            position = linearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                    }
                }
            });
        } else {
            binding.rlActive.setVisibility(View.GONE);
            binding.rvGridActive.setVisibility(View.GONE);
            binding.rvGridActiveIndicator.setVisibility(View.GONE);
        }


        if (rewardResponse.getData() != null && !rewardResponse.getData().getExpiredList().isEmpty()) {
            isDataFound = true;
            binding.tvExpired.setVisibility(View.VISIBLE);
            binding.rvGridExpiredIndicator.setVisibility(View.VISIBLE);

            GridCardsScratchListAdapter gridCardsAdapter = new GridCardsScratchListAdapter(rewardResponse.getData().getExpiredList(), "Expired", this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvGridExpired.setLayoutManager(linearLayoutManager);
            binding.rvGridExpired.setAdapter(gridCardsAdapter);
            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
            binding.rvGridExpired.setOnFlingListener(null);
            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridExpired);
            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(this);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            int indicatorSize = 1;

            if (rewardResponse.getData().getExpiredList().size() > 3) {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(rewardResponse.getData().getExpiredList().size())) / 3.0);
            }

            if (indicatorSize > 1) {
                binding.rvGridExpiredIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvGridExpiredIndicator.setVisibility(View.GONE);
            }
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(this, indicatorSize, 0);
            binding.rvGridExpiredIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvGridExpiredIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvGridExpiredIndicator.setHasFixedSize(true);

            binding.rvGridExpired.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int position = 0;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            position = linearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                    }
                }
            });
        } else {
            binding.tvExpired.setVisibility(View.GONE);
            binding.rvGridExpiredIndicator.setVisibility(View.GONE);
            binding.rvGridExpiredIndicator.setVisibility(View.GONE);
        }

        if (!isDataFound) {
            binding.scrollRewardList.setVisibility(View.GONE);
            binding.tvNDF.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onRewardClick(Object item, String type) {
        Log.e("onRewardClick", "onRewardClick");
        this.item = (GetQuizScratchListResponse.QuizScratchItem) item;
        getSpinRewardPopup((GetQuizScratchListResponse.QuizScratchItem) item);
    }

    private void getSpinRewardPopup(GetQuizScratchListResponse.QuizScratchItem data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    Log.e("onRewardClick", "onRewardClick");
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } /*else if (rewardtype.equalsIgnoreCase("Offers")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                }*/ else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardTitleIcon(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardTitleIcon(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                }
                else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context,data.getDialogModel(),data.getClaimDate());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onScratchComplete() {
        //QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            scratchCardLayout.onFullReveal();
            if (item != null) {
                QuizRewardDialog.INSTANCE.QuizScratchCard(this, item.getTransId(), true);

            }
        }
    }

    @Override
    public void onDialogDismiss() {
        GetQuizRewardList();
    }
}
