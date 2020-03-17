package com.xxbb.vo;

public class UserVo {
    private String password;
    private Integer ifFreeze;
    private Integer id;
    private String username;
    private String role;

    public UserVo() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIfFreeze() {
        return ifFreeze;
    }

    public void setIfFreeze(Integer ifFreeze) {
        this.ifFreeze = ifFreeze;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "password='" + password + '\'' +
                ", ifFreeze=" + ifFreeze +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
