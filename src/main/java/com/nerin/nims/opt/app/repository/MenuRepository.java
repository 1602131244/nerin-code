package com.nerin.nims.opt.app.repository;

import com.nerin.nims.opt.app.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by Zach on 16/5/22.
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentId(Long parentId);

    List<Menu> findByParentIdAndType(Long parendId, String type);

    @Query("from Menu t where t.type=? order by t.orderNo asc, t.id asc")
    List<Menu> findByType(String type);

    @Query("select t from Menu t, UserMenu t1 where t.id = t1.menuId and t1.userNo=? order by t.orderNo asc, t.id asc")
    List<Menu> findByUserNo(String userNo);
}
