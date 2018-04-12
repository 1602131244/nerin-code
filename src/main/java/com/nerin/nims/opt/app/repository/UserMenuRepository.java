package com.nerin.nims.opt.app.repository;

import com.nerin.nims.opt.app.domain.Authority;
import com.nerin.nims.opt.app.domain.UserMenu;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface UserMenuRepository extends JpaRepository<UserMenu, Long> {
    @Modifying
    @Query("delete from UserMenu t where t.userNo=?")
    int deleteByUserNo(String userNo);

    List<UserMenu> findByMenuId(long id);
}
