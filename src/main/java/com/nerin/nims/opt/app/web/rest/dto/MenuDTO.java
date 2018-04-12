package com.nerin.nims.opt.app.web.rest.dto;


/**
 * Created by Zach on 16/5/23.
 */
public class MenuDTO {
    private long id;
    private String name;
    private String description;
    private String url;
    private String icon;
    private String type;
    private Long parentId;
    private String parentName;

    private int orderNo;
    private int outsideUrl;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getOutsideUrl() {
        return outsideUrl;
    }

    public void setOutsideUrl(int outsideUrl) {
        this.outsideUrl = outsideUrl;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
