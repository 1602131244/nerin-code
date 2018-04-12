package com.nerin.nims.opt.app.domain;

/**
 * Created by yinglgu on 6/2/2016.
 */
public class LadpUserAuthority {

    private String ladpUser;
    private String authorityName;

    public String getLadpUser() {
        return ladpUser;
    }

    public void setLadpUser(String ladpUser) {
        this.ladpUser = ladpUser;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
