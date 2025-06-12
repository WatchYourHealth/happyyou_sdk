package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MyOrderItemBinding;
import com.wyh.happyyousdk.databinding.OrderInfoCardPopupBinding;
import com.wyh.happyyousdk.databinding.OrderItemInfoBinding;
import com.wyh.happyyousdk.happyMarket.AmahaWebView;
import com.wyh.happyyousdk.happyMarket.Model.OrderInfoCardModel;
import com.wyh.happyyousdk.model.MyOrderResponse;
import com.wyh.happyyousdk.model.response.AmahaKeyResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrderInfoCardAdapter extends RecyclerView.Adapter<MyOrderInfoCardAdapter.MyViewHolder> {

    Context context;
    List<OrderInfoCardModel> myOrderDataList;

    public MyOrderInfoCardAdapter(Context context, List<OrderInfoCardModel> myOrderDataList) {
        this.context = context;
        this.myOrderDataList = myOrderDataList;
    }

    @NonNull
    @Override
    public MyOrderInfoCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_item_info, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderInfoCardAdapter.MyViewHolder holder, int position) {
        OrderInfoCardModel data = myOrderDataList.get(position);
        holder.binding.tvKey.setText(data.getKey());

        if((myOrderDataList.size() - 1) == position) {
            holder.binding.view.setVisibility(View.GONE);
        } else {
            holder.binding.view.setVisibility(View.VISIBLE);
        }


        if(data.getKey().equals("Report Url") && !Objects.equals(data.getValue(), "NA")){
            SpannableString str = new SpannableString(data.getValue());
            str.setSpan(new UnderlineSpan(), 0, data.getValue().length(), 0);
            holder.binding.downloadFile.setVisibility(View.VISIBLE);
            holder.binding.tvValue.setVisibility(View.INVISIBLE);
            holder.binding.tvValue.setText(str);
            holder.binding.tvValue.setTextColor(context.getColor(R.color.blue));
        }else if(data.getKey().equals("Invoice Document") && !Objects.equals(data.getValue(), "NA")){
            SpannableString str = new SpannableString(data.getValue());
            str.setSpan(new UnderlineSpan(), 0, data.getValue().length(), 0);
            holder.binding.downloadFile.setVisibility(View.VISIBLE);
            holder.binding.tvValue.setVisibility(View.INVISIBLE);
            holder.binding.tvValue.setText(str);
            holder.binding.tvValue.setTextColor(context.getColor(R.color.blue));
        }
        else{
            holder.binding.downloadFile.setVisibility(View.GONE);
            holder.binding.tvValue.setVisibility(View.VISIBLE);
            holder.binding.tvValue.setText(data.getValue());
            holder.binding.tvValue.setTextColor(context.getColor(R.color.black));
        }

        holder.binding.downloadFile.setOnClickListener(v->{
            try{
                if(myOrderDataList.get(2).getValue().equalsIgnoreCase("AmahaHealth")){
                    if(data.getKey().equals("Report Url") && !Objects.equals(data.getValue(), "NA")){
                        getAmahaKey(context);
                    }else if(data.getKey().equals("Invoice Document") && !Objects.equals(data.getValue(), "NA")){
                        String url = data.getValue();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }
                }else{
                    if(data.getKey().equals("Report Url") && !Objects.equals(data.getValue(), "NA")){
                        String url = data.getValue();
                        if(!url.contains("https://")){
                            url = "https://"+data.getValue();
                        }
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }else if(data.getKey().equals("Invoice Document") && !Objects.equals(data.getValue(), "NA")){
                        String url = data.getValue();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(i);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });

    }

    @Override
    public int getItemCount() {
        return myOrderDataList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        OrderItemInfoBinding binding;

        public MyViewHolder(@NonNull OrderItemInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(int id);
    }

    public void getAmahaKey(Context context) {
        try {
            CommonUtils.showProgressDialige(context);
            APIInterface apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface.class);
            apiInterface.amahaGetKey(SharedPref.getAuthToken()).enqueue(new Callback<AmahaKeyResponse>() {
                @Override
                public void onResponse(Call<AmahaKeyResponse> call, Response<AmahaKeyResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.isSuccessful()) {
                        if (response.body() != null && response.body().getData() != null) {
                            String url = "https://kotak-life-insurance.integrations.amahahealth.com?CRNID=" + SharedPref.getAesUuid() + "&code=" + response.body().getData().getCode() + "&redirectPath=%2Fdashboard%2Fpsychiatrist%2Fprescriptions%2F";
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
}
