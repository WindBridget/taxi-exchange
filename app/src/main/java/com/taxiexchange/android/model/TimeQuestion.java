package com.taxiexchange.android.model;

/**
 * Created by Nhahv on 10/12/2016.
 * <></>
 */

public class TimeQuestion {

    private int hour;
    private int minute;
    private int second;



    private int day;
    private String date;
    private String dateHour;

    private String time;


    public TimeQuestion(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public TimeQuestion(int hour, int minute, int second, int day) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.day = day;
    }

    public TimeQuestion(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public TimeQuestion(int hour, int minute, int day, String date, String dateHour) {
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.date = date;
        this.dateHour = dateHour;
    }

    public TimeQuestion(int hour, int minute, int day, String date) {
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateHour() {
        return dateHour;
    }

    public void setDateHour(String dateHour) {
        this.dateHour = dateHour;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
