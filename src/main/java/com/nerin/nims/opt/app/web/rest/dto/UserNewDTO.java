package com.nerin.nims.opt.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nerin.nims.opt.app.domain.Authority;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class UserNewDTO {
    private Long id;
    @NotEmpty(message = "登录名不能为空")
    @Length(min = 2, max = 10, message = "登录名需2~10长度")
    private String login;
    private String firstName;
    private String lastName;

    @Email(message = "邮件格式不正确")
    @NotEmpty(message = "邮件不能为空")
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdDate = new Date();

    private Set<Authority> authorities = new HashSet<>();

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
