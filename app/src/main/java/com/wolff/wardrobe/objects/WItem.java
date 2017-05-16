package com.wolff.wardrobe.objects;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wolff on 03.04.2017.
 */

public class WItem {
    private UUID mId;
    private String mTitle;
    private String mSeason;
    private int mMinTemp;
    private int mMaxTemp;
    private Date mAddDate;//дата добавления
    public WItem(UUID id){
        mId = id;
    }
    public WItem(){
        mId = UUID.randomUUID();
        mAddDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season) {
        mSeason = season;
    }

    public int getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(int minTemp) {
        this.mMinTemp = minTemp;
    }

    public int getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.mMaxTemp = maxTemp;
    }

    public Date getAddDate() {
        return mAddDate;
    }

    public void setAddDate(Date addDate) {
        mAddDate = addDate;
    }

    public String getPhotoFileName(){
        return "IMG_"+getId().toString()+".jpg";
    }
}
