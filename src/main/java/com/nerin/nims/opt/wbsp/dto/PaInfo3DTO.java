package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PaInfo3DTO {
    private Long projID;
    private String projNumber;
    private String projName;
    private String phaseName;
    private String mgr;
    private Long mgrNum;
    private String gmgrNum;
    private String vmgrNum;
    private String headOrbranch;
    private String division;
    private String dual;
    private Long pubwbsID;
    private String pubwbsName;
    private List<PaInfo3SPECDTO> specialty;
    private Long orgID;
    private Long perID;
    private Long userID;
    private String userName;
    private String userNum;

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public Long getPerID() {
        return perID;
    }

    public void setPerID(Long perID) {
        this.perID = perID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
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

    public Long getMgrNum() {
        return mgrNum;
    }

    public void setMgrNum(Long mgrNum) {
        this.mgrNum = mgrNum;
    }

    public String getGmgrNum() {
        return gmgrNum;
    }

    public void setGmgrNum(String gmgrNum) {
        this.gmgrNum = gmgrNum;
    }

    public String getVmgrNum() {
        return vmgrNum;
    }

    public void setVmgrNum(String vmgrNum) {
        this.vmgrNum = vmgrNum;
    }

    public String getHeadOrbranch() {
        return headOrbranch;
    }

    public void setHeadOrbranch(String headOrbranch) {
        this.headOrbranch = headOrbranch;
    }

    public Boolean getDivision() {
        if ( ("Y").equals(division)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setDivision(String division) {
          this.division = division;
    }

    public String getDual() {
        return dual;
    }

    public void setDual(String dual) {
        this.dual = dual;
    }

    public Long getPubwbsID() {
        return pubwbsID;
    }

    public void setPubwbsID(Long pubwbsID) {
        this.pubwbsID = pubwbsID;
    }

    public String getPubwbsName() {
        return pubwbsName;
    }

    public void setPubwbsName(String pubwbsName) {
        this.pubwbsName = pubwbsName;
    }

    public List<PaInfo3SPECDTO> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<PaInfo3SPECDTO> specialty) {
        this.specialty = specialty;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
