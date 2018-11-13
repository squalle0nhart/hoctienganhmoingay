package com.squalle0nhart.hoctienganh.model;

/**
 * Created by squalleonhart on 3/17/2017.
 */

public class PharseCategoryInfo {
    String text;
    int id;

    public PharseCategoryInfo(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
