package com.squalle0nhart.hoctienganh.model;

/**
 * Created by squalleonhart on 3/19/2017.
 */

public class ConversationInfo {
    String text;
    String mean;
    String person;

    public ConversationInfo(String person, String text, String mean) {
        this.text = text;
        this.mean = mean;
        this.person = person;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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
