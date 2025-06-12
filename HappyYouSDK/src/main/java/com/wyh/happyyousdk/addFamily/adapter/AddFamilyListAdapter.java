package com.wyh.happyyousdk.addFamily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.FamilyListAdapterBinding;
import com.wyh.happyyousdk.databinding.QuizOptionItemBinding;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;
import com.wyh.happyyousdk.model.response.AddFamilyResponse;

import java.util.List;


public class AddFamilyListAdapter extends RecyclerView.Adapter<AddFamilyListAdapter.MyViewHolder> {

    Context context;
    List<AddFamilyRequest> list;
    private final AddFamilyListAdapter.OnItemClickListener listener;

    public AddFamilyListAdapter(Context context, List<AddFamilyRequest> list, AddFamilyListAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddFamilyListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FamilyListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.family_list_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFamilyListAdapter.MyViewHolder holder, int position) {
        AddFamilyRequest data = list.get(position);



        holder.binding.name.setText(data.getName());
        holder.binding.relationship.setText(data.getRelationship());
        holder.binding.mobileNumber.setText(data.getMobileNo());
        holder.binding.ivDelete.setOnClickListener(v->{
            listener.onClick(position, data);
        });

        if(data.getRelationship().equalsIgnoreCase("You")){
            holder.binding.ivDelete.setVisibility(View.GONE);
        }else{
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        FamilyListAdapterBinding binding;

        public MyViewHolder(@NonNull FamilyListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, AddFamilyRequest data);
    }

}
