package com.nerin.nims.opt.dsin.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class SpecAttrDTO {
    private long id;
    private long counter;
    private String jsonStr ="";
    private String color ="";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
