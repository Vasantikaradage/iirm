package com.indiainsure.android.MB360.insurance.hospitalnetwork.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.indiainsure.android.MB360.utilities.LogMyBenefits;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    public static void getAddressFromLocation(String locationAddress, Context context, Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                Address address = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address = (Address) addressList.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(address.getLatitude()).append("\n");
                        stringBuilder.append(address.getLongitude()).append("\n");
                        result = stringBuilder.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address   :   " + locationAddress +
                                "\n\n\nLatitude and longitude\n" + result;
                        bundle.putString("address", result);
                        bundle.putDouble("lat", address.getLatitude());
                        bundle.putDouble("lng", address.getLongitude());
                        LogMyBenefits.d("LOCATION", "lATLNG" + result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }


    public static void getAddressFromLocation(String lat, String lang, Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {

                String result = null;
                Address address = null;
                try {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 2;
                    Bundle bundle = new Bundle();

                    bundle.putDouble("lat", Double.parseDouble(lat));
                    bundle.putDouble("lng", Double.parseDouble(lang));
                    LogMyBenefits.d("LOCATION", "lATLNG" + result);
                    message.setData(bundle);
                    message.sendToTarget();

                    LogMyBenefits.d("LAT-LNG", "" + lat + " " + lang);

                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 2;
                    Bundle bundle = new Bundle();

                    bundle.putDouble("lat", 0.0);
                    bundle.putDouble("lng", 0.0);
                    LogMyBenefits.d("LAT-LNG", "" + lat + " " + lang);
                    message.setData(bundle);
                }
            }
        };
        thread.start();
    }

}
