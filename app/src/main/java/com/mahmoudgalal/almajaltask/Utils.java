/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask;

import android.content.Context;
import android.icu.text.TimeZoneNames;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static final String NEWS_BASE_URL = "http://lowcost-env.pmtiunacvu.us-east-1.elasticbeanstalk.com";

    private static final String SHARED_PREF_NAME = "My_APP_SHARED_PREF";
    public static final String LAST_TOKEN_KEY = "com.mahmoudgalal.almajaltask_LAST_TOKEN_KEY";
    public static final String LAST_TOKEN_TIME_KEY = "com.mahmoudgalal.almajaltask_LAST_TOKEN_TIME_KEY";
    public static final String LAST_TOKEN_VALIDITY_PERIOD_KEY =
            "com.mahmoudgalal.almajaltask_LAST_TOKEN_VALIDITY_PERIOD_KEY";

    public static String getSavedToken(Context context){
        return isSavedTokenStillValid(context)?context.getSharedPreferences(SHARED_PREF_NAME,0).
                getString(LAST_TOKEN_KEY,null):null;
    }

    public static void saveLatestToken(Context context,String token,long tokenTime,long tokenPeriod){
        context.getSharedPreferences(SHARED_PREF_NAME,0).edit().
                putString(LAST_TOKEN_KEY,token).putLong(LAST_TOKEN_TIME_KEY,tokenTime).
                putLong(LAST_TOKEN_VALIDITY_PERIOD_KEY,tokenPeriod).apply();
    }

    public static boolean isSavedTokenStillValid(Context context){
        long time = context.getSharedPreferences(SHARED_PREF_NAME,0).
                getLong(LAST_TOKEN_TIME_KEY,-1);
        long val = context.getSharedPreferences(SHARED_PREF_NAME,0).
                getLong(LAST_TOKEN_VALIDITY_PERIOD_KEY,-1);

        Log.d("Utils",String.format("Time = %s ,TTL = %s",time,val));
// Assuming that ttl values are in 10 * Seconds units
        return val > 0 && (((new Date().getTime()-time)/100)<val);
    }
    public static final String getTimeDatePart(String date){
        char[] charArray = date.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c:charArray ) {
            if(c == ' ' || c == '.' || c == ':'
                    ||c == '-'
                    || Character.isDigit(c)){
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getLocalizedDateTime(String dateTime){
        String temp = getTimeDatePart(dateTime).trim();
            String tempDate = "13:13 - 2016.7.27";
            // all dates are in this format "2016.9.2 - 17:37"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d - HH:mm");
        //Abu Dhabi time Zone (GMT+4)
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        Date date ;
        Calendar localCal = Calendar.getInstance();
        try {
            date = sdf.parse(temp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            localCal.setTime(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat(" HH:mm - yyyy.M.d");
        return sdf.format(localCal.getTime());
    }

    /**
     * Returns whether there is a valid internet connection or no connection
     *
     * @param context
     * @return true if there is internet connection false otherwise
     */
    public static boolean isInternetConnectionExist(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
