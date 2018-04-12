package com.nerin.nims.opt.app.domain;

import com.nerin.nims.opt.app.config.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by yinglgu on 6/2/2016.
 */
@Entity
@Table(name = "nerin_user_menu", schema = Constants.DB_PREFIX)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userNo;
    private long menuId;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }
}
