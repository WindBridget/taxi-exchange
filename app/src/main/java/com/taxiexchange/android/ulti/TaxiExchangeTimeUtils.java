package com.taxiexchange.android.ulti;

import android.util.Log;

import com.taxiexchange.android.model.TimeQuestion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Nhahv on 10/25/2016.
 * <></>
 */

public class TaxiExchangeTimeUtils {

    public static TimeQuestion convertTime(String time) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String temp = time.replace("T", " ").replace("Z", "");
            Date date = format.parse(temp);

            long timeMilliSecond = date.getTime();


            // current time
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            temp = nowAsISO.replace("T", " ").replace("Z", "");

            date = format.parse(temp);


            long timeCurrent = date.getTime();
            long timeSub = timeMilliSecond - timeCurrent;

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            String strDate = formatter.format(timeMilliSecond);

            timeSub = timeSub / 1000;

            int day = (int) (timeSub / (24 * 60 * 60));
            timeSub -= day * (24 * 60 * 60);
            int hour = (int) timeSub / (60 * 60);
            timeSub -= hour * (60 * 60);
            int minute = (int) timeSub / 60;
            timeSub -= minute * 60;
            int second = (int)timeSub;

            System.out.println("" + hour);
            System.out.println("" + minute);
            System.out.println("" + second);
            System.out.println("" + day);
            System.out.println("" + strDate);
            return new TimeQuestion(hour, minute, second, day);
        } catch (Exception e) {

            System.out.println(e.toString());
        }

        return null;


    }

    public static TimeQuestion convertInputTime(String time) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String temp = time.replace("T", " ").replace("Z", "");
            Date date = format.parse(temp);
            long timeMilliSecond = date.getTime();
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String strDate = formatterDate.format(timeMilliSecond);
            String strTime = formatterTime.format(timeMilliSecond);
            return new TimeQuestion(strDate, strTime);
        } catch (Exception e) {

            System.out.println(e.toString());
        }

        return null;


    }

    public static TimeQuestion convertTime(long time) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("HH, dd/MM/yyyy", Locale.getDefault());

            String timeString = formatter.format(time);
            int index = timeString.indexOf(",");

            String dateHour = timeString.substring(0, index);
            String result2 = timeString.substring(index);
            dateHour = dateHour + " hour" + result2;

            long timeSub = System.currentTimeMillis() - time;
            formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String strDate = formatter.format(timeSub);

            timeSub = timeSub / 1000;
            int day = (int) (timeSub / (3600 * 24));
            int hour = (int) timeSub / (3600) - 24 * day;
            int minute = (int) timeSub / 60 - (int) (timeSub / 3600) * 60;

            System.out.println("" + hour);
            System.out.println("" + minute);
            System.out.println("" + day);
            System.out.println("" + strDate);
            return new TimeQuestion(hour, minute, day, strDate, dateHour);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static String getCurrentDay() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(new Date());
    }

    public static String getDayYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(calendar.getTime());
    }

    public static long getTimeRemaining(String time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String temp = time.replace("T", " ").replace("Z", "");
        Date date = null;
        try {
            date = format.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeMilliSecond = date.getTime();
        // current time
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        temp = nowAsISO.replace("T", " ").replace("Z", "");
        try {
            date = format.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeCurrent = date.getTime();
        long timeSub = timeMilliSecond - timeCurrent;
        return timeSub;
    }

    public static long getTime(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String temp = time.replace("T", " ").replace("Z", "");
        Date date = null;
        try {
            date = format.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeMilliSecond = date.getTime();
        return timeMilliSecond;
    }

}
