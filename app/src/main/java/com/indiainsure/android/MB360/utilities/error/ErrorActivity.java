package com.indiainsure.android.MB360.utilities.error;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.indiainsure.android.MB360.databinding.ActivityErrorBinding;
import com.indiainsure.android.MB360.utilities.UtilMethods;


public class ErrorActivity extends AppCompatActivity {

    ActivityErrorBinding binding;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.tryAgainButton.setOnClickListener(v -> {
            tryAgain();
        });
    }


    private void tryAgain() {
        //here we try again using the same creds...
        UtilMethods.logout(this);
       /* Intent tryAgainIntent = new Intent(this, SplashScreenActivity.class);
        finishAffinity();
        startActivity(tryAgainIntent);*/
    }

}