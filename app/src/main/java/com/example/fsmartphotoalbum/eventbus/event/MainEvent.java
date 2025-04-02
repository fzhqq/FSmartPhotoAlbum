package com.example.fsmartphotoalbum.eventbus.event;

public class MainEvent {
    private boolean updateList;

    public MainEvent(boolean updateList) {
        this.updateList = updateList;
    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
