package com.nerin.nims.opt.cadi.dto;
import java.util.Date;
import java.util.List;
/**
 * Created by user on 16/7/15.
 */
public class DrawSizeReturnDTO {

    private Long index;        //序号
    private String drawnum; //图号
    private Long pages;        //自然张数
    private String xsize; //图幅 默认A4
    private Long did;        //图纸目录did
    private Long version_num;  //图纸目录版本号

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getVersion_num() {
        return version_num;
    }

    public void setVersion_num(Long version_num) {
        this.version_num = version_num;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getDrawnum() {
        return drawnum;
    }

    public void setDrawnum(String drawnum) {
        this.drawnum = drawnum;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public String getXsize() {
        return xsize;
    }

    public void setXsize(String xsize) {
        this.xsize = xsize;
    }
}
