package com.nerin.nims.opt.wbsp.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancong on 2016/7/14.
 */
public class PAAssignmentListDTO  {

    private PAMemberSpecDTO paMemberSpecDTO;
    private List member = new ArrayList();

    public PAMemberSpecDTO getPaMemberSpecDTO() {
        return paMemberSpecDTO;
    }

    public void setPaMemberSpecDTO(PAMemberSpecDTO paMemberSpecDTO) {
        this.paMemberSpecDTO = paMemberSpecDTO;
    }

    public List getMember() {
        return member;
    }

    public void setMember(List member) {
        this.member = member;
    }
}
