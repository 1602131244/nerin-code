package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PaInfo2DTO {
    private Long projID;
    private String projNumber;
    private String projName;
    private String phaseName;
    private String mgr;
    private Long draftwbsID;
    private Long pubwbsID;
    private String headOrbranch;
    private String division;
    private String dual;
    private Date phaseStart;
    private Date phaseEnd;
    private String pubwbsName;
    private Long draftwbsCheckout;
    private Boolean Checkoutable;
    private Long userID;
    private Boolean isOldPhase;

    public Long getPubwbsID() {
        return pubwbsID;
    }

    public void setPubwbsID(Long pubwbsID) {
        this.pubwbsID = pubwbsID;
    }

    public String getDual() {
        return dual;
    }

    public void setDual(String dual) {
        this.dual = dual;
    }

    public Date getPhaseStart() {
        return phaseStart;
    }

    public void setPhaseStart(Date phaseStart) {
        this.phaseStart = phaseStart;
    }

    public Date getPhaseEnd() {
        return phaseEnd;
    }

    public void setPhaseEnd(Date phaseEnd) {
        this.phaseEnd = phaseEnd;
    }

    public Long getDraftwbsCheckout() {
        return draftwbsCheckout;
    }

    public void setDraftwbsCheckout(Long draftwbsCheckout) {
        this.draftwbsCheckout = draftwbsCheckout;
    }

    public Long getProjID() {
        return projID;
    }

    public void setProjID(Long projID) {
        this.projID = projID;
    }

    public String getProjNumber() {
        return projNumber;
    }

    public void setProjNumber(String projNumber) {
        this.projNumber = projNumber;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getMgr() {
        return mgr;
    }

    public void setMgr(String mgr) {
        this.mgr = mgr;
    }


    public Long getDraftwbsID() {
        return draftwbsID;
    }

    public void setDraftwbsID(Long draftwbsID) {
        this.draftwbsID = draftwbsID;
    }

    public String getHeadOrbranch() {
        return headOrbranch;
    }

    public void setHeadOrbranch(String headOrbranch) {
        this.headOrbranch = headOrbranch;
    }

    public Boolean getDivision() {
        if (division != null && division.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }


    public void setDivision(String division) {
        this.division = division;
    }

    public Boolean getCheckoutable() {
        return Checkoutable;
    }

    public void setCheckoutable(Boolean checkoutable) {
        Checkoutable = checkoutable;
    }

    public String getPubwbsName() {
        return pubwbsName;
    }

    public void setPubwbsName(String pubwbsName) {
        this.pubwbsName = pubwbsName;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Boolean getOldPhase() {
        return isOldPhase;
    }

    public void setOldPhase(Boolean oldPhase) {
        isOldPhase = oldPhase;
    }
}
