package com.wyh.happyyousdk.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.QuizCategoryItemBinding;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.QuestionModel;

import java.util.List;


public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.MyViewHolder> {

    Context context;
    List<GetQuizQuestions> list;
    private final QuizCategoryAdapter.OnItemClickListener listener;

    public QuizCategoryAdapter(Context context, List<GetQuizQuestions> list, QuizCategoryAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizCategoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quiz_category_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizCategoryAdapter.MyViewHolder holder, int position) {
        GetQuizQuestions data = list.get(position);

        holder.binding.btnCategoryItem.setText(data.getCategoryName());

        holder.binding.btnCategoryItem.setOnClickListener(v->{
            listener.onClick(data.getCategoryName(), data.getQuestions());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        QuizCategoryItemBinding binding;

        public MyViewHolder(@NonNull QuizCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(String name, List<QuestionModel> question);
    }
}
