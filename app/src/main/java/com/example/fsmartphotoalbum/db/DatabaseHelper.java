package com.example.fsmartphotoalbum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fsmartphotoalbum.constants.Constants;


/**
 * @author Feng Zhaohao
 * Created on 2021/3/7
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // 创建收藏信息表
    private static final String CREATE_TABLE_COLLECT_PHOTO = "create table " + Constants.TABLE_COLLECT_PHOTO
            + " (" + Constants.TABLE_COLLECT_PHOTO_ID + " text primary key, "
            + Constants.TABLE_COLLECT_PHOTO_NAME + " text, "
            + Constants.TABLE_COLLECT_PHOTO_PATH + " text, "
            + Constants.TABLE_COLLECT_PHOTO_TYPE + " text, "
            + Constants.TABLE_COLLECT_PHOTO_TIME + " text, "
            + Constants.TABLE_COLLECT_PHOTO_SIZE + " text, "
            + Constants.TABLE_COLLECT_PHOTO_WIDTH + " int, "
            + Constants.TABLE_COLLECT_PHOTO_HEIGHT + " int)";

    // 创建智能相册信息表
    private static final String CREATE_TABLE_SMART_PHOTO = "create table " + Constants.TABLE_SMART_PHOTO
            + " (" + Constants.TABLE_SMART_PHOTO_ID + " text primary key, "
            + Constants.TABLE_SMART_PHOTO_TITLE + " text)";


    DatabaseHelper(Context context, String name,
                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COLLECT_PHOTO);
        db.execSQL(CREATE_TABLE_SMART_PHOTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
