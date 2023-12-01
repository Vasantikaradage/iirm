package com.indiainsure.android.MB360.utilities;

import com.indiainsure.android.MB360.BuildConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AppServerConstants {


    //for core app

    public static final String WebURL = "https://www.mybenefits360.in/appservice/";
    public static final String Weburl = "https://portal.mybenefits360.com/mb360apiV1/api/";

    public static final String baseURL = "https://core.mybenefits360.com";//For Production

//   public static final String JSONURL = "http://www.mybenefits360.in/mb360api/api/";
//   public static final String JSONURL = "http://demo.mybenefits360.com/mb360api/api/";

    /*=====================TCL Login=============================*/

    //      public static final String baseURL = "http://www.mybenefits360.in";//Local URl
    //public static final String baseURL = "http://core.mybenefits360.com";//For Production
    //     public static final String baseURL = "http://15.206.179.89:81";//Local URl

    //public static final String WebURL = baseURL + "/appservice/";
//   public static final String WebURL = "http://www.mybenefits360.com/appservice/";

    public static final String JSONURL = baseURL + "/mb360api/api/";

    public static final String JSONURL2 = baseURL + "/mbapi/api/v1/";

    public static final String JSONURL3 = "https://wellness.mybenefits360.com/mbapiv2/api/v1/";

    public static final String adminsettings = "adminPref";

    public static final String staticValues = "StaticValues";

    public static String NumberFormatter(String amt) {
        String Amount = "";
      /*  NumberFormat nsf = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

        if(amt.length() < 8)
        Amount = nsf.format(Integer.valueOf(amt)).replace(".00", "")
                .substring(2).replaceAll("[$]", "");
        else  Amount = nsf.format(Long.valueOf(amt)).replace(".00", "")
                .substring(2).replaceAll("[$]", "");
        return Amount;*/

        String newPrice = null;
        try {
            Amount = amt.replace(",", "");

            DecimalFormat format = new DecimalFormat("##,##,##,###");
            newPrice = format.format(Double.parseDouble(Amount));
        } catch (Exception e) {
            newPrice = amt;
        }
        return newPrice;

    }

    public static String DecimalNumberFormatter(String amt) {
        String Amount = "";
        NumberFormat nsf = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        nsf.setMinimumFractionDigits(2);
        Amount = nsf.format(Double.parseDouble(amt))
                .substring(2).replaceAll("[$]", "");
        return Amount;
    }


    // Define a list of trusted certificates
    public static String trustedCertificates =
            BuildConfig.CERT;

}
