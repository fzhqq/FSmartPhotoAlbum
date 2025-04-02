package com.example.fsmartphotoalbum.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 图片实体类
 *
 * @author Feng Zhaohao
 * Created on 2021/3/2
 */
public class Photo implements Parcelable {

    private String id; // 图片id，可以获取uri
    private Uri uri;//图片Uri
    private String name;//图片名称
    private String path;//图片全路径
    private String type;//图片类型
    private int width;//图片宽度
    private int height;//图片高度
    private long size;//图片文件大小，单位：Bytes
    private long time;//图片拍摄的时间戳,单位：毫秒

    public Photo(String id, String name, Uri uri, String path, long time, int width, int height, long size, String type) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.time = time;
        this.width = width;
        this.height = height;
        this.type = type;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.type);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeLong(this.size);
        dest.writeLong(this.time);
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.path = in.readString();
        this.type = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.size = in.readLong();
        this.time = in.readLong();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
