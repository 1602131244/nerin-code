package com.nerin.nims.opt.app.repository;

import com.nerin.nims.opt.app.domain.EbsDuty;
import com.nerin.nims.opt.app.domain.UserMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface EbsDutyRepository extends JpaRepository<EbsDuty, Long> {

}
