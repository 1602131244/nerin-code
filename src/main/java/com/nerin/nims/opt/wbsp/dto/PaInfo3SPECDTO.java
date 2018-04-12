package com.nerin.nims.opt.wbsp.dto;

import java.lang.reflect.Array;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PaInfo3SPECDTO {
    private String id;
    private String name;
    private String[] chgrNum;
    private String edit;
     private Long lockBy;
    private String lockName;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String[] getChgrNum() {
        return chgrNum;
    }

    public void setChgrNum(String[] chgrNum) {
        this.chgrNum = chgrNum;
    }

    public Long getLockBy() {
        return lockBy;
    }

    public void setLockBy(Long lockBy) {
        this.lockBy = lockBy;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
