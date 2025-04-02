package com.example.fsmartphotoalbum.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SmartPhotoItem implements Parcelable {
    private String title;
    private String coverImagePath;
    private Uri coverImageUri;
    private List<Photo> photoList = new ArrayList<>();

    public SmartPhotoItem(String title, String coverImagePath, Uri coverImageUri) {
        this.title = title;
        this.coverImagePath = coverImagePath;
        this.coverImageUri = coverImageUri;
    }

    public void addPhoto(Photo photo) {
        photoList.add(photo);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public Uri getCoverImageUri() {
        return coverImageUri;
    }

    public void setCoverImageUri(Uri coverImageUri) {
        this.coverImageUri = coverImageUri;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.coverImagePath);
        dest.writeParcelable(this.coverImageUri, flags);
        dest.writeTypedList(this.photoList);
    }

    protected SmartPhotoItem(Parcel in) {
        this.title = in.readString();
        this.coverImagePath = in.readString();
        this.coverImageUri = in.readParcelable(Uri.class.getClassLoader());
        this.photoList = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Parcelable.Creator<SmartPhotoItem> CREATOR = new Parcelable.Creator<SmartPhotoItem>() {
        @Override
        public SmartPhotoItem createFromParcel(Parcel source) {
            return new SmartPhotoItem(source);
        }

        @Override
        public SmartPhotoItem[] newArray(int size) {
            return new SmartPhotoItem[size];
        }
    };
}
