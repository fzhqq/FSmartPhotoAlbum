package com.example.fsmartphotoalbum.entity;

import android.net.Uri;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 相册实体类
 *
 * @author Feng Zhaohao
 * Created on 2021/3/2
 */
public class Album {
    public List<AlbumItem> albumItems;
    private LinkedHashMap<String, AlbumItem> albumItemMap;//用于记录专辑项目

    public Album() {
        albumItems = new ArrayList<>();
        albumItemMap = new LinkedHashMap<>();
    }

    private void addAlbumItem(AlbumItem albumItem) {
        this.albumItemMap.put(albumItem.name, albumItem);
        this.albumItems.add(albumItem);
    }

    public void addAlbumItem(String name, String coverImagePath, Uri coverImageUri) {
        if (null == albumItemMap.get(name)) {
            addAlbumItem(new AlbumItem(name, coverImagePath,coverImageUri));
        }
    }

    public AlbumItem getAlbumItem(String name) {
        return albumItemMap.get(name);
    }

    public AlbumItem getAlbumItem(int currIndex) {
        return albumItems.get(currIndex);
    }

    public boolean isEmpty() {
        return albumItems.isEmpty();
    }

    public void clear() {
        albumItems.clear();
        albumItemMap.clear();
    }

    public void delete(String albumItemName, String filePath) {
        AlbumItem albumItem = albumItemMap.get(albumItemName);
        if (albumItem != null) {
            boolean delete = albumItem.delete(filePath);
            if (delete) {
                albumItemMap.remove(albumItemName);
                for (int i = 0; i < albumItems.size(); i++) {
                    AlbumItem ai = albumItems.get(i);
                    if (albumItemName.equals(ai.name)) {
                        albumItems.remove(i);
                        break;
                    }
                }
            }
        }
    }
}
