package com.dhkdw.androidopencv;

public class AlarmList {
    private int alarmImg;
    private String alarmName;
    private String alarmContent;

    public AlarmList(int alarmImg, String alarmName, String alarmContent) {
        this.alarmImg = alarmImg;
        this.alarmName = alarmName;
        this.alarmContent = alarmContent;
    }

    public int getAlarmImg() {
        return alarmImg;
    }

    public void setAlarmImg(int alarmImg) {
        this.alarmImg = alarmImg;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }
}
