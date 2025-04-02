package com.example.fsmartphotoalbum.eventbus.event;

public class PhotoEvent {
    private boolean isVisible;

    public PhotoEvent(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
