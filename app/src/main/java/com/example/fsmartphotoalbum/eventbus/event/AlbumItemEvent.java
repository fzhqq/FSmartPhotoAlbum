package com.example.fsmartphotoalbum.eventbus.event;

public class AlbumItemEvent {
    private boolean updateList;

    public AlbumItemEvent(boolean updateList) {
        this.updateList = updateList;
    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
