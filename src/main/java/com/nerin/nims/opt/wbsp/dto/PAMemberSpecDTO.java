package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by Administrator on 2016/8/5.
 */
public class PAMemberSpecDTO {
    private String specialty;
    private String duty;

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    /*@Override
    public boolean equals(Object obj) {
        if (obj instanceof PAMemberSpecDTO) {
            PAMemberSpecDTO paMemberSpecDTO = (PAMemberSpecDTO) obj;
            if(specialty.equals(paMemberSpecDTO.specialty)&& duty.equals(paMemberSpecDTO.duty))
             //   System.out.println("Ture");
                return true;
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode()
    {
        //PAMemberSpecDTO paMemberSpecDTO = (PAMemberSpecDTO) this;
        System.out.println("Hash" + specialty.hashCode()*3+duty.hashCode()*17);
        return specialty.hashCode()*3+duty.hashCode()*17;
    }
*/
}
