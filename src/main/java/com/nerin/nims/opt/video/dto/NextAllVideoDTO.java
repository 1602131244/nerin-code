package com.nerin.nims.opt.video.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class NextAllVideoDTO {
    private long id;
    private String title;
    private String description;
    private String format;
    private long collectCount;
    private long playCount;
    private String url;
    private long fId;

    public long getfId() {
        return fId;
    }

    public void setfId(long fId) {
        this.fId = fId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public long getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(long collectCount) {
        this.collectCount = collectCount;
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
}
