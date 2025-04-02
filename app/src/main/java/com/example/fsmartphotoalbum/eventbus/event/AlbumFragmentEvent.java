package com.example.fsmartphotoalbum.eventbus.event;

public class AlbumFragmentEvent {
    private boolean updateList;

    public AlbumFragmentEvent(boolean updateList) {
        this.updateList = updateList;
    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
