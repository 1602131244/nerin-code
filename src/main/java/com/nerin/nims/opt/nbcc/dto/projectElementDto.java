package com.nerin.nims.opt.nbcc.dto;

/**
 * Created by Administrator on 2016/7/18.
 */
public class projectElementDto {
    private  long elementId;
    private String elementNumber;
    private String elementName;
    private long personIdDesign;
    private String personNameDesign;
    private long personIdCheck;
    private String personNameCheck;
    private long personIdVerify;
    private String personNameVerify;
    private String specialty;
    private String specialtyName;

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }

    public String getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(String elementNumber) {
        this.elementNumber = elementNumber;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public long getPersonIdDesign() {
        return personIdDesign;
    }

    public void setPersonIdDesign(long personIdDesign) {
        this.personIdDesign = personIdDesign;
    }

    public String getPersonNameDesign() {
        return personNameDesign;
    }

    public void setPersonNameDesign(String personNameDesign) {
        this.personNameDesign = personNameDesign;
    }

    public long getPersonIdCheck() {
        return personIdCheck;
    }

    public void setPersonIdCheck(long personIdCheck) {
        this.personIdCheck = personIdCheck;
    }

    public String getPersonNameCheck() {
        return personNameCheck;
    }

    public void setPersonNameCheck(String personNameCheck) {
        this.personNameCheck = personNameCheck;
    }

    public long getPersonIdVerify() {
        return personIdVerify;
    }

    public void setPersonIdVerify(long personIdVerify) {
        this.personIdVerify = personIdVerify;
    }

    public String getPersonNameVerify() {
        return personNameVerify;
    }

    public void setPersonNameVerify(String personNameVerify) {
        this.personNameVerify = personNameVerify;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }
}
