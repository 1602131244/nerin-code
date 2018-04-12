package com.nerin.nims.opt.dsin.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ApproveIdDTO {
    private Long id;
    private Long tabId;
    private String url;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
