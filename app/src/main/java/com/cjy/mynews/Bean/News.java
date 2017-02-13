package com.cjy.mynews.Bean;

/**
 * Created by CJY on 2016/11/26.
 */

public class News {
    private String title;
    private String thumbnail;
    private String url;

    public News() {
    }

    public News(String thumbnail, String url, String title) {
        this.thumbnail = thumbnail;
        this.url = url;
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "News{" +
                "thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
