package com.example.fsmartphotoalbum.eventbus.event;

public class SmartPhotoEvent {
    private boolean updateAllPhotos;

    public SmartPhotoEvent(boolean updateAllPhotos) {
        this.updateAllPhotos = updateAllPhotos;
    }

    public boolean isUpdateAllPhotos() {
        return updateAllPhotos;
    }

    public void setUpdateAllPhotos(boolean updateAllPhotos) {
        this.updateAllPhotos = updateAllPhotos;
    }
}
