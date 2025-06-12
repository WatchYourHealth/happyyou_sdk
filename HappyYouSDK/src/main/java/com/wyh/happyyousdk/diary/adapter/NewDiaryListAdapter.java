package com.wyh.happyyousdk.diary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DiaryListItemBinding;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.model.response.NewMyDiaryResponse;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.List;


public class NewDiaryListAdapter extends RecyclerView.Adapter<NewDiaryListAdapter.MyViewHolder> {

    Context context;
    List<NewMyDiaryResponse.NewDiaryData> list;
    private final NewDiaryListAdapter.OnItemClickListener listener;

    public NewDiaryListAdapter(Context context, List<NewMyDiaryResponse.NewDiaryData> list, NewDiaryListAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewDiaryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiaryListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.diary_list_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewDiaryListAdapter.MyViewHolder holder, int position) {
        NewMyDiaryResponse.NewDiaryData data = list.get(position);
        String newDate = CommonUtils.formatDateFromString("yyyy-MM-dd'T'HH:mm", "dd/MM/yyyy", data.getJournalDate());
        holder.binding.tvDate.setText(newDate);
        holder.binding.tvDocumentName.setText(data.getJournalName());
        boolean isUnwind = data.getJournalSource().equalsIgnoreCase("unwind");

        holder.binding.ivDelete.setOnClickListener(v-> {
            if(isUnwind){
                listener.onDelete(data.getJournalid());
            }
        });
        holder.binding.ivEdit.setOnClickListener(v-> {
            if (isUnwind){
                listener.onEdit(data.getJournalid(), data.getJournalName(), data.getJournalContent(), newDate, data.getImagePath(), data.getImageId());
            }
        });
        holder.binding.llInfo.setOnClickListener(v-> listener.onView(data.getJournalid(), data.getJournalName(), data.getJournalContent(), newDate, data.getImagePath(), isUnwind, data.getImageId()));

        if(isUnwind){
            holder.binding.ivEdit.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
            holder.binding.ivEdit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_edit_diary_active));
            holder.binding.ivDelete.setBackgroundResource(R.drawable.pink_rc_bg_8dp);
            holder.binding.ivDelete.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete_diary_white));
        }else{
            holder.binding.ivEdit.setBackgroundResource(R.drawable.btn_bg);
            holder.binding.ivEdit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_edit_diary_gray));
            holder.binding.ivDelete.setBackgroundResource(R.drawable.btn_bg);
            holder.binding.ivDelete.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete_diary_gray));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        DiaryListItemBinding binding;

        public MyViewHolder(@NonNull DiaryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onEdit(int id, String titleName, String content, String date, String imagePath, int imageId);
        void onView(int id, String titleName, String content, String date, String imagePath, boolean journalSource, int imageId);
        void onDelete(int id);
    }
}
