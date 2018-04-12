package com.nerin.nims.opt.app.repository;

import com.nerin.nims.opt.app.domain.EbsDutyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yinglgu on 2016/12/8.
 */
public interface EbsDutyCodeRepository extends JpaRepository<EbsDutyCode, Long> {

    @Modifying
    @Transactional
    @Query("delete from EbsDutyCode e where e.ebsDutyId is null")
    void deleteCodeWithNull();
}
