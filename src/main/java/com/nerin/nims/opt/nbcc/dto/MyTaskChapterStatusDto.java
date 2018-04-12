package com.nerin.nims.opt.nbcc.dto;

/**
 * Created by Administrator on 2016/7/14.
 */
public class MyTaskChapterStatusDto {

    private String chapterStatus; // 状态编码
    private String chapterStatusName;// 状态名称

    public String getChapterStatus() {
        return chapterStatus;
    }

    public void setChapterStatus(String chapterStatus) {
        this.chapterStatus = chapterStatus;
    }

    public String getChapterStatusName() {
        return chapterStatusName;
    }

    public void setChapterStatusName(String chapterStatusName) {
        this.chapterStatusName = chapterStatusName;
    }
}
