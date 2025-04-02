package com.example.fsmartphotoalbum.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.fsmartphotoalbum.app.App;
import com.example.fsmartphotoalbum.constants.Constants;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.util.PictureUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Feng Zhaohao
 * Created on 2021/3/7
 */
public class DatabaseManager {
    private static final String TAG = "DatabaseManager";

    private static DatabaseManager mManager;
    private SQLiteDatabase mDb;

    private DatabaseManager() {
        SQLiteOpenHelper helper = new DatabaseHelper(
                App.getContext(), Constants.DB_NAME, null, 1);
        mDb = helper.getWritableDatabase();
    }

    public static DatabaseManager getInstance() {
        if (mManager == null) {
            mManager = new DatabaseManager();
        }
        return mManager;
    }

    /**
     * 插入一条新的收藏记录
     */
    public void insertCollect(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLLECT_PHOTO_ID, photo.getId());
        values.put(Constants.TABLE_COLLECT_PHOTO_NAME, photo.getName());
        values.put(Constants.TABLE_COLLECT_PHOTO_PATH, photo.getPath());
        values.put(Constants.TABLE_COLLECT_PHOTO_TYPE, photo.getType());
        values.put(Constants.TABLE_COLLECT_PHOTO_TIME, String.valueOf(photo.getTime()));
        values.put(Constants.TABLE_COLLECT_PHOTO_SIZE, String.valueOf(photo.getSize()));
        values.put(Constants.TABLE_COLLECT_PHOTO_WIDTH, photo.getWidth());
        values.put(Constants.TABLE_COLLECT_PHOTO_HEIGHT, photo.getHeight());
        mDb.insert(Constants.TABLE_COLLECT_PHOTO, null, values);
    }

    /**
     * 删除一条收藏记录
     */
    public void deleteCollect(String id) {
        mDb.delete(Constants.TABLE_COLLECT_PHOTO, Constants.TABLE_COLLECT_PHOTO_ID + " = ?", new String[]{id});
    }

    /**
     * 查询所有收藏记录（较新的记录排前面）
     */
    public List<Photo> queryAllCollect() {
        List<Photo> res = new ArrayList<>();
        Cursor cursor = mDb.query(Constants.TABLE_COLLECT_PHOTO, null, null,
                null, null, null,null);
        if (cursor.moveToLast()) {
            int idIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_ID);
            int nameIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_NAME);
            int pathIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_PATH);
            int typeIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_TYPE);
            int timeIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_TIME);
            int sizeIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_SIZE);
            int widthIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_WIDTH);
            int heightIndex = cursor.getColumnIndex(Constants.TABLE_COLLECT_PHOTO_HEIGHT);
            do {
                String id = cursor.getString(idIndex);
                Uri uri = PictureUtil.getUri(id);
                String name = cursor.getString(nameIndex);
                String path = cursor.getString(pathIndex);
                String type = cursor.getString(typeIndex);
                long time = Long.parseLong(cursor.getString(timeIndex));
                long size = Long.parseLong(cursor.getString(sizeIndex));
                int width = cursor.getInt(widthIndex);
                int height = cursor.getInt(heightIndex);
                res.add(new Photo(id, name, uri, path, time, width, height, size, type));
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }

    /**
     * 查询收藏表是否存在主键为 id 的记录
     */
    public boolean isExistInCollect(String id) {
        Cursor cursor = mDb.query(Constants.TABLE_COLLECT_PHOTO, null,
                Constants.TABLE_COLLECT_PHOTO_ID+ " = ?", new String[]{id},
                null,null, null ,null);
        boolean res = false;
        if (cursor.moveToLast()) {
            do {
                res = true;
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }

    /**
     * 插入一条新的智能相册记录
     */
    public void insertSmart(String id, String title) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_SMART_PHOTO_ID, id);
        values.put(Constants.TABLE_SMART_PHOTO_TITLE, title);
        mDb.insert(Constants.TABLE_SMART_PHOTO, null, values);
    }

    /**
     * 查询所有智能相册记录（较新的记录排前面）
     */
    public Map<String, String> queryAllSmart() {
        Map<String, String> res = new HashMap<>();
        Cursor cursor = mDb.query(Constants.TABLE_SMART_PHOTO, null, null,
                null, null, null,null);
        if (cursor.moveToLast()) {
            int idIndex = cursor.getColumnIndex(Constants.TABLE_SMART_PHOTO_ID);
            int titleIndex = cursor.getColumnIndex(Constants.TABLE_SMART_PHOTO_TITLE);
            do {
                String id = cursor.getString(idIndex);
                String title = cursor.getString(titleIndex);
                res.put(id, title);
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return res;
    }

}
