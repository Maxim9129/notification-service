package com.potseluev.common;

public enum NotificationType {
    EMAIL(0), HTTP_REQUEST(1);
    private int id;

    public int getId() {
        return id;
    }

    public static NotificationType valueOf(int id) {
        for(NotificationType c : NotificationType.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    NotificationType(int id) {
        this.id = id;
    }
}
