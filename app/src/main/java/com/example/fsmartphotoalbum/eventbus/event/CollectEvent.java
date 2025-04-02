package com.example.fsmartphotoalbum.eventbus.event;

public class CollectEvent {
    private boolean updateCollect;

    public CollectEvent(boolean updateCollect) {
        this.updateCollect = updateCollect;
    }

    public boolean isUpdateCollect() {
        return updateCollect;
    }

    public void setUpdateCollect(boolean updateCollect) {
        this.updateCollect = updateCollect;
    }
}
