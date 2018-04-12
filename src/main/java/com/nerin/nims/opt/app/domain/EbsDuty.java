package com.nerin.nims.opt.app.domain;

import com.nerin.nims.opt.app.config.Constants;
import com.nerin.nims.opt.app.web.rest.dto.EbsDutySourceDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 2016/11/24.
 */
@Entity
@Table(name = "nerin_ebs_duty", schema = Constants.DB_PREFIX)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbsDuty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nerinIdentity;
    private String codeRemark;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="ebs_duty_id")
    private List<EbsDutyCode> ebsCodes = new ArrayList<EbsDutyCode>();

    public String getCodeRemark() {
        return codeRemark;
    }

    public void setCodeRemark(String codeRemark) {
        this.codeRemark = codeRemark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNerinIdentity() {
        return nerinIdentity;
    }

    public void setNerinIdentity(String nerinIdentity) {
        this.nerinIdentity = nerinIdentity;
    }

    public List<EbsDutyCode> getEbsCodes() {
        return ebsCodes;
    }

    public void setEbsCodes(List<EbsDutyCode> ebsCodes) {
        this.ebsCodes = ebsCodes;
    }
}
