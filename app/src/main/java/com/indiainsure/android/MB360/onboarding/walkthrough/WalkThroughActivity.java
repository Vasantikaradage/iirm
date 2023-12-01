package com.indiainsure.android.MB360.onboarding.walkthrough;


import static com.indiainsure.android.MB360.BuildConfig.AUTH_FIRST_TIME_USER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ActivityWalkThroughBinding;
import com.indiainsure.android.MB360.onboarding.authentication.LoginActivity;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;


public class WalkThroughActivity extends AppCompatActivity {
    ActivityWalkThroughBinding binding;
    View view;
    private int[] layouts;

    private AppCompatTextView[] dots;
    EncryptionPreference encryptionPreference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalkThroughBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        //encryption preference
        encryptionPreference = new EncryptionPreference(this);

        layouts = new int[]{
                R.layout.screen_walkthrough,
                R.layout.screen_walkthrough,
                R.layout.screen_walkthrough
        };


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        //adapters
        WalkThroughViewPagerAdapter adapter = new WalkThroughViewPagerAdapter(getApplicationContext(), this);
        binding.viewPager.setOffscreenPageLimit(0);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
                    binding.btnNext.setVisibility(View.GONE);
                    binding.btnStart.setVisibility(View.VISIBLE);
                    binding.btnStart.setOnClickListener(v -> {
                        //save the preference of getting the walk throught
                        userOnboarded();
                        Intent loginIntent = new Intent(WalkThroughActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    });
                } else {
                    // still pages are left
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.btnStart.setVisibility(View.GONE);
                }
         /*   if(position == 1){
                btn_prev.setVisibility(View.VISIBLE);
            }*/


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        binding.btnNext.setOnClickListener(v -> {
            int current = getItem(+1);
            if (current < layouts.length) {
                binding.viewPager.setCurrentItem(current);
            }
        });

        binding.btnPrev.setOnClickListener(v -> {
            int current = getItem(-1);
            if (current < layouts.length) {
                // move to next screen
                binding.viewPager.setCurrentItem(current);
            }
        });

        binding.btnStart.setOnClickListener(v -> {
            Intent i = new Intent(WalkThroughActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_close, R.anim.slide_out_close);
            startActivity(i);
            finish();
        });

    }

    private void userOnboarded() {
        SharedPreferences settings = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Store false value to say that user already saw the walk-through to SharedPreference
        encryptionPreference.setEncryptedBoolString(AUTH_FIRST_TIME_USER, false);
        editor.putBoolean(AUTH_FIRST_TIME_USER, false);
        editor.apply();
    }


    private void addBottomDots(int currentPage) {

        dots = new AppCompatTextView[layouts.length];

        binding.layoutDots.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(4, 0, 4, 0);

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new AppCompatTextView(this);
            dots[i].setGravity(Gravity.CENTER_VERTICAL);
            dots[i].setHeight(10);
            dots[i].setWidth(50);
            dots[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.walkthough_notselected));
            binding.layoutDots.addView(dots[i], layoutParams);
        }

        if (dots.length > 0)
            dots[currentPage].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.walkthough_selected));
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * returns the current page of the viewpager
     */
    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    /**
     * View pager adapter
     */
    public class WalkThroughViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public AppCompatTextView title, detailtxt;
        public AppCompatImageView imgtitle;
        Context context;
        Activity activity;

        public WalkThroughViewPagerAdapter(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            title = view.findViewById(R.id.title);
            detailtxt = view.findViewById(R.id.detailtxt);
            imgtitle = view.findViewById(R.id.imgTitle);


            switch (position) {
                case 0:
                    title.setText("ENROLL");
                    detailtxt.setText(R.string.enroll);
                    imgtitle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_enroll));
                    break;

                case 1:
                    title.setText("PERSONALIZE");
                    detailtxt.setText(R.string.personalize);
                    imgtitle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_personalize));
                    break;
                case 2:
                    title.setText("MANAGE");
                    detailtxt.setText(R.string.manage);
                    imgtitle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_manage));
                    break;

            }
            container.addView(view);
            return view;
        }


        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}