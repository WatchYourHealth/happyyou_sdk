package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> contactList;
    ContactAdapter.OnItemClickListener listener;


    public ContactAdapter(Context context, ArrayList<String> contactList,OnItemClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        holder.contact_list_tv.setText(contactList.get(position));
        holder.contact_list_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickOnContact(contactList.get(holder.getAdapterPosition()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public interface OnItemClickListener {
        void clickOnContact(String contact);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contact_list_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contact_list_tv = itemView.findViewById(R.id.contact_num_tv);
        }
    }
}
