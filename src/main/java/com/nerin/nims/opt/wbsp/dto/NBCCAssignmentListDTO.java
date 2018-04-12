package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancong on 2016/7/14.
 */
public class NBCCAssignmentListDTO {

    List<PAMemberDTO> design;
    List<PAMemberDTO> check;
    List<PAMemberDTO> review;
    List<PAMemberDTO> approve;

    public List<PAMemberDTO> getDesign() {
        return design;
    }

    public void setDesign(List<PAMemberDTO> design) {
        this.design = design;
    }

    public List<PAMemberDTO> getCheck() {
        return check;
    }

    public void setCheck(List<PAMemberDTO> check) {
        this.check = check;
    }

    public List<PAMemberDTO> getReview() {
        return review;
    }

    public void setReview(List<PAMemberDTO> review) {
        this.review = review;
    }

    public List<PAMemberDTO> getApprove() {
        return approve;
    }

    public void setApprove(List<PAMemberDTO> approve) {
        this.approve = approve;
    }
}
