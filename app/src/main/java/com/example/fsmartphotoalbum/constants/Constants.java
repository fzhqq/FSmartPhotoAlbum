package com.example.fsmartphotoalbum.constants;

public class Constants {

    public static final int REQUEST_CHOOSE_PHOTO_FROM_ALBUM = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;


    /* 数据库相关 */
    // 数据库名
    public static final String DB_NAME = "FSmartPhotoAlbum.db";
    // 收藏图片表
    public static final String TABLE_COLLECT_PHOTO = "table_collect_info";
    // 收藏图片表的记录
    public static final String TABLE_COLLECT_PHOTO_ID = "collect_photo_id";       // 图片id，可以获取uri，主键
    public static final String TABLE_COLLECT_PHOTO_NAME = "collect_photo_name";
    public static final String TABLE_COLLECT_PHOTO_PATH = "collect_photo_path";
    public static final String TABLE_COLLECT_PHOTO_TYPE = "collect_photo_type";
    public static final String TABLE_COLLECT_PHOTO_WIDTH = "collect_photo_width";
    public static final String TABLE_COLLECT_PHOTO_HEIGHT = "collect_photo_height";
    public static final String TABLE_COLLECT_PHOTO_SIZE = "collect_photo_size";
    public static final String TABLE_COLLECT_PHOTO_TIME = "collect_photo_time";

    // 智能相册表
    public static final String TABLE_SMART_PHOTO = "table_smart_info";
    // 智能相册表的记录
    public static final String TABLE_SMART_PHOTO_ID = "smart_photo_id";       // 图片id，可以获取uri，主键
    public static final String TABLE_SMART_PHOTO_TITLE = "smart_photo_type";    // 图片分类
}
