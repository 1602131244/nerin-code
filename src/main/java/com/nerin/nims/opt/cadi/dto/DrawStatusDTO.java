package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class DrawStatusDTO {
    public String getDrawStatusCode() {
        return DrawStatusCode;
    }

    public void setDrawStatusCode(String drawStatusCode) {
        DrawStatusCode = drawStatusCode;
    }

    public String getDrawStatusName() {
        return DrawStatusName;
    }

    public void setDrawStatusName(String drawStatusName) {
        DrawStatusName = drawStatusName;
    }

    private String DrawStatusCode;
    private String DrawStatusName;

}
