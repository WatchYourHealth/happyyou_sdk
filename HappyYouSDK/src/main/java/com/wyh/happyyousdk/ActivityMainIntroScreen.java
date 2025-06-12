package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.Constants.HappyYou;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

;
import com.wyh.happyyousdk.databinding.ActivityMainIntroScreenBinding;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.utils.IntroVideosActivity;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityMainIntroScreen extends AppCompatActivity {
    ActivityMainIntroScreenBinding binding;
    SharedPref preferenceManager;
    ImageView[] bottomBars;
    int[] screens;
    MyViewPagerAdapter myvpAdapter;
    int currentPage;
    String referrerUrl;
    Context context;
//    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_intro_screen);
        myvpAdapter = new MyViewPagerAdapter();
        screens = new int[]{
                R.layout.intro_screen_one,
                R.layout.intro_screen_two,
                R.layout.intro_screen_three,
                R.layout.intro_screen_four,
                R.layout.intro_screen_five
        };

        binding.viewPager.setAdapter(myvpAdapter);
        SharedPref.init(getApplicationContext());
        bottomBars = new ImageView[screens.length];
        ColoredBars(0);

        referrerUrl = getIntent().getStringExtra("referrerUrl");

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AppIntroduction");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

//        setTimer();

        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        binding.tvSkip.setOnClickListener(view -> {
            if (!SharedPref.getIsHappyYouIntroShown() && SharedPref.getNewUser()) {
                Intent intent = new Intent(ActivityMainIntroScreen.this,
                        VideoReaderActivity.class);
                //intent.putExtra("url", "android.resource://" + getPackageName() + "/" + R.raw.happy_you_intro);
                intent.putExtra("redirectTo", HappyYou);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(ActivityMainIntroScreen.this, MobileNumberActivity.class);
                intent.putExtra("referrerUrl", referrerUrl);
                startActivity(intent);
            }
        });
    }

    private void setTimer() {
        Handler handler = new Handler();

        Runnable update = () -> {
            if (currentPage != bottomBars.length) {
                ColoredBars(currentPage);
                binding.viewPager.setCurrentItem(currentPage++, true);
            } else {
//                timer.cancel();
                binding.tvSkip.setVisibility(View.VISIBLE);
            }
        };
//        timer = new Timer();
        /*timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, 6000);*/
    }

    private void ColoredBars(int thisScreen) {
        TypedArray drawableActive = getResources().obtainTypedArray(R.array.dot_on_page_active1);
        TypedArray drawableNonActive = getResources().obtainTypedArray(R.array.dot_on_page_not_active1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 100, 10, 10);
        binding.layoutBars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new ImageView(this);
            bottomBars[i].setLayoutParams(params);
            binding.layoutBars.addView(bottomBars[i]);
            bottomBars[i].setImageResource(drawableNonActive.getResourceId(thisScreen, -1));
        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setImageResource(drawableActive.getResourceId(thisScreen, -1));
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            ColoredBars(position);
//            setTimer();

            if (position == screens.length - 1) {
            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }

    public void next() {
        int i = getItem(+1);
        if (i < screens.length) {
            binding.viewPager.setCurrentItem(i);
        } else {
            Intent intent = new Intent(ActivityMainIntroScreen.this, MobileNumberActivity.class);
            startActivity(intent);
        }
    }
}


