package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancong on 2016/7/14.
 */
public class PAMemberListDTO {

    private String duty  ;
    private List member = new ArrayList();

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public List getMember() {
        return member;
    }

    public void setMember(List member) {
        this.member = member;
    }
}
