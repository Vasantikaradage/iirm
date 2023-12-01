package com.indiainsure.android.MB360.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.indiainsure.android.MB360.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WindowPeriodCounter {
    String endDate;
    Context context;
    Activity activity;
    private static final String FORMAT = "%02d:%02d:%02d:%02d";
    private static final String FORMAT_DAYS = "%02d";

    public WindowPeriodCounter(String endDate, Context context, Activity activity) {
        this.endDate = endDate;
        this.context = context;
        this.activity = activity;
    }

    public CountDownTimer countDownTimer(boolean showDialog) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
        //end date in millis
        long millis = calendar.getTimeInMillis();

        //current date in millis
        long now = Calendar.getInstance().getTimeInMillis();

        //difference of millis
        long milisInFuture = millis - now;

        if (milisInFuture > 0) {


            //dialog box
            final Dialog alertDialog = new Dialog(context);
            alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.nhborder));
            LayoutInflater mLayoutInflater = activity.getLayoutInflater();
            View alertLayout = mLayoutInflater.inflate(R.layout.dialog_counter, null);
            alertDialog.setContentView(alertLayout);
            Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
            btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
            TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);

            if (showDialog) {
                alertDialog.show();
            }

            //timer
            CountDownTimer timer = new CountDownTimer(milisInFuture, 1000) {

                @SuppressLint("DefaultLocale")
                public void onTick(long millisUntilFinished) {

                    alertMessage.setText(String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toDays(millisUntilFinished), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                            ), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }


                public void onFinish() {
                    alertMessage.setText("00:00:00:00");

                }
            };

            alertDialog.setOnDismissListener(dialogInterface -> {
                timer.cancel();
            });

            return timer;
        } else {
            return null;
        }
    }


    public CountDownTimer getTimer(TextView timerTextView) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
        //end date in millis
        long millis = calendar.getTimeInMillis();

        //current date in millis
        long now = Calendar.getInstance().getTimeInMillis();

        //difference of millis
        long milisInFuture = millis - now;

        if (milisInFuture > 0) {
            //timer
            CountDownTimer timer = new CountDownTimer(milisInFuture, 1000) {

                @SuppressLint("DefaultLocale")
                public void onTick(long millisUntilFinished) {

                    timerTextView.setText(String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toDays(millisUntilFinished), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                            ), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }


                public void onFinish() {
                    timerTextView.setText("00:00:00:00");

                }
            };

            return timer;
        } else {
            return null;
        }
    }


    public CountDownTimer showDays(TextView enrollmentTextView) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
        //end date in millis
        long millis = calendar.getTimeInMillis();

        //current date in millis
        long now = Calendar.getInstance().getTimeInMillis();

        //difference of millis
        long milisInFuture = millis - now;

        if (milisInFuture > 0) {
            //timer
            CountDownTimer timer = new CountDownTimer(milisInFuture, 1000) {

                @SuppressLint("DefaultLocale")
                public void onTick(long millisUntilFinished) {

                    enrollmentTextView.setText(String.valueOf(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
                }


                public void onFinish() {
                    enrollmentTextView.setText("0");

                }
            };

            return timer;
        } else {
            return null;
        }
    }


}
