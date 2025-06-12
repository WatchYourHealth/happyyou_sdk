package com.wyh.happyyousdk.dashboard.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.model.response.QrResponse;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.ArrayList;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ViewAdapter> {

    Context context;
    ArrayList<QrResponse.Datum> scanQrList;

    public ScanListAdapter(Context context, ArrayList<QrResponse.Datum> scanQrList) {
        this.context = context;
        this.scanQrList = scanQrList;
    }

    @NonNull
    @Override
    public ScanListAdapter.ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scan_list_layout,parent,false);
        return new ViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanListAdapter.ViewAdapter holder, int position) {
        holder.scan_activity_name.setText(scanQrList.get(position).getActivityName());
        holder.scan_date.setText(CommonUtils.DateFromString("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyyy hh:mm a", scanQrList.get(position).getDate()));
        holder.scan_reedem_code.setText(scanQrList.get(position).getTokenId());
    }

    @Override
    public int getItemCount() {
        return scanQrList.size();
    }

    public class ViewAdapter extends RecyclerView.ViewHolder {

        TextView scan_activity_name,scan_date,scan_reedem_code;

        public ViewAdapter(@NonNull View itemView) {
            super(itemView);

            scan_activity_name = itemView.findViewById(R.id.scan_activity_name);
            scan_date = itemView.findViewById(R.id.scan_date);
            scan_reedem_code = itemView.findViewById(R.id.scan_reedem_code);
        }
    }
}
