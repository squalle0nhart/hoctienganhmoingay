package com.squalle0nhart.hoctienganh.model;

/**
 * Created by squalleonhart on 3/17/2017.
 */

public class PharseInfo {
    String text;
    String mean;

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    int isRead;

    public PharseInfo(String text, String mean, int isLearn) {
        this.text = text;
        this.mean = mean;
        this.isRead = isLearn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
