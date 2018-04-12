package com.nerin.nims.opt.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.nerin.nims.opt.app.config.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Zach on 16/5/22.
 */
@Entity
@Table(name = "nerin_menu", schema = Constants.DB_PREFIX)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Size(min = 2,max = 50)
    @Column(length = 50)
    private String name;
    private String description;
    private String url;
    private String icon;
    private String type;
    @Column(nullable = true, length = 10)
    private int orderNo;
    @Column(nullable = true, length = 1)
    private int outsideUrl;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getOutsideUrl() {
        return outsideUrl;
    }

    public void setOutsideUrl(int outsideUrl) {
        this.outsideUrl = outsideUrl;
    }

    @Column(name = "parent_id",updatable = false,insertable = false)
    private Long parentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parent;

//    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "parent",fetch = FetchType.EAGER)
    @OrderBy("orderNo ASC")
    @JsonView(Menu.class)
    private List<Menu> children;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
