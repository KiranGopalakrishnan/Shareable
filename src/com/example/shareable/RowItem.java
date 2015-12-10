package com.example.shareable;

import android.graphics.Typeface;

public class RowItem {
    private int imageId;
    private String title;
    private String desc;
    private Boolean defaultIcon_nf;
     
    public RowItem(int imageId, String title, String desc,Boolean dfin) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        this.defaultIcon_nf=dfin;
    }
    public void setDefault()
    {
    	
    }
    public int getImageId() {
        return imageId;
    }
    
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Boolean getDefault() {
        return this.defaultIcon_nf;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }  
}