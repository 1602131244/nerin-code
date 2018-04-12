package com.nerin.nims.opt.app.domain;

import com.nerin.nims.opt.app.config.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by yinglgu on 2016/11/24.
 */
@Entity
@Table(name = "nerin_ebs_dutycode", schema = Constants.DB_PREFIX)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbsDutyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ebs_duty_id")
    private Long ebsDutyId;

    private String dutyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbsDutyId() {
        return ebsDutyId;
    }

    public void setEbsDutyId(Long ebsDutyId) {
        this.ebsDutyId = ebsDutyId;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }
}
