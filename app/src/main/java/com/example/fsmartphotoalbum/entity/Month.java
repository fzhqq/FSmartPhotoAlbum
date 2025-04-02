package com.example.fsmartphotoalbum.entity;

import java.util.List;

public class Month {
    private String title;       // 时间
    private List<Photo> photoList;      // 图片列表

    public Month(String title, List<Photo> photoList) {
        this.title = title;
        this.photoList = photoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
