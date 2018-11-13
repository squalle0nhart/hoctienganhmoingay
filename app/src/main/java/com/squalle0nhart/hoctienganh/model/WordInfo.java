package com.squalle0nhart.hoctienganh.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThangBK on 13/3/2017.
 */

public class WordInfo implements Parent<WordExpandInfo>{
    String name;
    String read;
    String category;
    String meaning;
    int isRead;
    String fullMean;
    List<WordExpandInfo> listExpand;

    public WordInfo(String name, String read, String category, String translated, int isRead, String fullMean) {
        this.name = name;
        this.read = read;
        this.category = category;
        this.meaning = translated;
        this.isRead = isRead;
        this.fullMean = fullMean;
        listExpand = new ArrayList<>();
        listExpand.add(new WordExpandInfo(fullMean));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getFullMean() {
        return fullMean;
    }

    public void setFullMean(String fullMean) {
        this.fullMean = fullMean;
    }

    @Override
    public List<WordExpandInfo> getChildList() {
        return listExpand;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
