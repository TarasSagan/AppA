package com.bigdig.appabigdig;

public class HistoryModel {
    public long id;
    public String url;
    public int status;
    public long openTime;

    public HistoryModel(long id, String url, int status, long openTime) {
        this.id = id;
        this.url = url;
        this.status = status;
        this.openTime = openTime;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }
}
