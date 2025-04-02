package com.example.fsmartphotoalbum.entity;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 专辑管理类（指一个分类，例如"贴吧"相册）
 *
 * @author Feng Zhaohao
 * Created on 2021/3/2
 */
public class AlbumItem {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat mDataFormatOfMonth = new SimpleDateFormat("yyyy年MM月");

    public String name;
    public String coverImagePath;
    public Uri coverImageUri;
    public Map<String, List<Photo>> photoMap = new LinkedHashMap<>(); // 按照月份对同一专辑的照片分类
    private boolean needUpdate = false;
    private List<Photo> photoList = new ArrayList<>();   // 全部专辑照片

    AlbumItem(String name, String coverImagePath, Uri coverImageUri) {
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.coverImageUri = coverImageUri;
    }

    public void addImageItem(Photo photo) {
        sortPhotosByMonth(photo);
    }

    private void sortPhotosByMonth(Photo photo) {
        Date date = new Date(photo.getTime() * 1000);
        String millisecond = mDataFormatOfMonth.format(date);
        if (!photoMap.containsKey(millisecond)) {
            List<Photo> section = new ArrayList<>();
            section.add(photo);
            photoMap.put(millisecond, section);
        } else {
            List<Photo> section = photoMap.get(millisecond);
            if (section != null) {
                section.add(photo);
            }
        }
        needUpdate = true;
    }

    public int getNum() {
        int num = 0;
        for (List<Photo> list : photoMap.values()) {
            num += list.size();
        }

        return num;
    }

    public List<Photo> getPhotoList() {
        if (needUpdate) {
            photoList.clear();
            for (List<Photo> photos : photoMap.values()) {
                photoList.addAll(photos);
            }
            needUpdate = false;
        }

        return photoList;
    }

    /**
     * 删除照片
     *
     * @param path 删除照片的路径
     * @return true：删除相册，false：不删除相册
     */
    public boolean delete(String path) {
        for (String key : photoMap.keySet()) {
            List<Photo> photos = photoMap.get(key);
            if (photos == null) {
                continue;
            }
            for (int i = 0; i < photos.size(); i++) {
                Photo photo = photos.get(i);
                if (path.equals(photo.getPath())) {
                    if (photos.size() == 1) {
                        photoMap.remove(key);
                    } else {
                        photos.remove(i);
                    }
                    break;
                }
             }
        }

        for (int i = 0; i < photoList.size(); i++) {
            Photo photo = photoList.get(i);
            if (path.equals(photo.getPath())) {
                photoList.remove(i);
                break;
            }
        }

        return photoList.isEmpty();
    }
}
