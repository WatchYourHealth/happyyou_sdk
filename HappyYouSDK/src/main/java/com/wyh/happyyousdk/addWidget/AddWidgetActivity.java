package com.wyh.happyyousdk.addWidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityAddWidgetBinding;
import com.wyh.happyyousdk.databinding.ActivityQuizBinding;

public class AddWidgetActivity extends AppCompatActivity {

    ActivityAddWidgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_widget);
//        this.setFinishOnTouchOutside(false);

        binding.calCount.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(1));
        });
        binding.activeHr.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(2));
        });
        binding.pillowTime.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(3));
        });
        binding.zenzone.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(4));
        });
        binding.footprint.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(5));
        });
        binding.h2o.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(6));
        });
        binding.healthvault.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(7));
        });
        binding.tribe.setOnClickListener(v->{
            binding.ivAddWidgetMenuImage.setImageResource(getImage(8));
        });
    }

    private int getImage(int id){
        switch (id){
            case 1:
                return R.drawable.ic_widget_2;
            case 2:
                return R.drawable.ic_widget_3;
            case 3:
                return R.drawable.ic_widget_4;
            case 4:
                return R.drawable.ic_widget_5;
            case 5:
                return R.drawable.ic_widget_6;
            case 6:
                return R.drawable.ic_widget_7;
            case 7:
                return R.drawable.ic_widget_8;
            case 8:
                return R.drawable.ic_widget_9;
        }

        return R.drawable.ic_widget_1;
    }
}