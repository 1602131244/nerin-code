package com.nerin.nims.opt.nbcc.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ProjectPersonsDto {
    private List<ProjectPersonDto> designs;
    private List<ProjectPersonDto> checks;
    private List<ProjectPersonDto> verifys;
    private List<ProjectPersonDto> approves;

    public List<ProjectPersonDto> getDesigns() {
        return designs;
    }

    public void setDesigns(List<ProjectPersonDto> designs) {
        this.designs = designs;
    }

    public List<ProjectPersonDto> getChecks() {
        return checks;
    }

    public void setChecks(List<ProjectPersonDto> checks) {
        this.checks = checks;
    }

    public List<ProjectPersonDto> getVerifys() {
        return verifys;
    }

    public void setVerifys(List<ProjectPersonDto> verifys) {
        this.verifys = verifys;
    }

    public List<ProjectPersonDto> getApproves() {
        return approves;
    }

    public void setApproves(List<ProjectPersonDto> approves) {
        this.approves = approves;
    }
}
