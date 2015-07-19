package com.potseluev.server;

import com.potseluev.common.NotificationType;

import java.util.Date;

public class Notification {
    private int id;
    private String message;
    private String externalId;
    private Date time;
    private NotificationType notificationType;
    private String email;
    private String url;

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {

        return message;
    }

    public int getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public Date getTime() {
        return time;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }
}
