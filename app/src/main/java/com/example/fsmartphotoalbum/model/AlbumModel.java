package com.example.fsmartphotoalbum.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.fsmartphotoalbum.entity.Album;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.entity.SmartPhotoItem;
import com.example.fsmartphotoalbum.util.PictureUtil;
import com.example.fsmartphotoalbum.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumModel {

    private static AlbumModel instance;
    private Album album;
    private List<Photo> allPhotos;
    private HashMap<String, SmartPhotoItem> smartPhotoItemMap;

    private AlbumModel() {
        album = new Album();
        allPhotos = new ArrayList<>();
        smartPhotoItemMap = new HashMap<>();
    }

    public static AlbumModel getInstance() {
        if (null == instance) {
            synchronized (AlbumModel.class) {
                if (null == instance) {
                    instance = new AlbumModel();
                }
            }
        }
        return instance;
    }

    /**
     * 得到本地相册列表
     *
     * 耗时操作，需在子线程进行
     */
    public void updateAlbum(Context context) {
        album.clear();

        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        String[] selectionAllArgs =
                {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};

        ContentResolver contentResolver = context.getContentResolver();
        String[] projections;
        projections = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH, MediaStore.MediaColumns.HEIGHT,
                MediaStore.MediaColumns.SIZE};

        Cursor cursor = contentResolver.query(contentUri, projections, selection,
                selectionAllArgs, sortOrder);

        if (cursor != null) {

            if (!cursor.moveToFirst()) {
                return;
            }

            int idCol = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
            int pathCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
            int mimeType = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeCol = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
            int widthCol = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH);
            int heightCol = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT);

            do {
                String id = cursor.getString(idCol);
                String path = cursor.getString(pathCol);
                String name = cursor.getString(nameCol);
                long dateTime = cursor.getLong(DateCol);
                String type = cursor.getString(mimeType);
                long size = cursor.getLong(sizeCol);
                int width = cursor.getInt(widthCol);
                int height = cursor.getInt(heightCol);

                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                    continue;
                }

                Uri uri = PictureUtil.getUri(id);

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    continue;
                }

                Photo photo = new Photo(id, name, uri, path, dateTime, width, height, size, type);

                // 初始化“全部”专辑
                if (album.isEmpty()) {
                    // 用第一个图片作为专辑的封面
                    album.addAlbumItem("全部图片", path,uri);
                }
                // 把图片全部放进“全部”专辑
                album.getAlbumItem("全部图片").addImageItem(photo);

                // 添加当前图片的专辑到专辑模型实体中
                File parentFile = new File(path).getParentFile();
                if (null == parentFile) {
                    continue;
                }
                String folderPath = parentFile.getAbsolutePath();
                String albumName = StringUtil.getLastPathSegment(folderPath);
                album.addAlbumItem(albumName, path,uri);
                album.getAlbumItem(albumName).addImageItem(photo);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    /**
     * 得到本地所有图片列表
     *
     * 耗时操作，需在子线程进行
     */
    public void updateAllPhotos(Context context) {
        allPhotos.clear();

        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        String[] selectionAllArgs =
                {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};

        ContentResolver contentResolver = context.getContentResolver();
        String[] projections;
        projections = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH, MediaStore.MediaColumns.HEIGHT,
                MediaStore.MediaColumns.SIZE};

        Cursor cursor = contentResolver.query(contentUri, projections, selection,
                selectionAllArgs, sortOrder);

        if (cursor != null) {

            if (!cursor.moveToFirst()) {
                return;
            }

            int idCol = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
            int pathCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
            int mimeType = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeCol = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
            int widthCol = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH);
            int heightCol = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT);

            do {
                String id = cursor.getString(idCol);
                String path = cursor.getString(pathCol);
                String name = cursor.getString(nameCol);
                long dateTime = cursor.getLong(DateCol);
                String type = cursor.getString(mimeType);
                long size = cursor.getLong(sizeCol);
                int width = cursor.getInt(widthCol);
                int height = cursor.getInt(heightCol);

                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                    continue;
                }

                Uri uri = PictureUtil.getUri(id);

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    continue;
                }

                allPhotos.add(new Photo(id, name, uri, path, dateTime, width, height, size, type));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public Album getAlbum() {
        return album;
    }

    public void delete(String albumItemName, String filePath) {
        album.delete(albumItemName, filePath);
    }

    public List<Photo> getAllPhotos() {
        return allPhotos;
    }

    public void addSmartPhotoItem(SmartPhotoItem smartPhotoItem) {
        if (smartPhotoItem == null) {
            return;
        }

        smartPhotoItemMap.put(smartPhotoItem.getTitle(), smartPhotoItem);
    }

    public SmartPhotoItem getSmartPhotoItem(String title) {
        return smartPhotoItemMap.get(title);
    }
}
