package com.nerin.nims.opt.video.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MyFavoriteDTO {
    private long id;
    private String name;
    private String description;
    private String format;
    private long favoriteCount;
    private long playCount;
    private String url;
    private long fId;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getfId() {
        return fId;
    }

    public void setfId(long fId) {
        this.fId = fId;
    }
}
