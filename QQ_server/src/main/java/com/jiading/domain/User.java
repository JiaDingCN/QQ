package com.jiading.domain;

/**
 * @program: QQ_server
 * @description: 用户
 * @author: JiaDing
 * @create: 2020-05-10 22:06
 **/
public class User {
    private String uid;
    private String username;

    private String password;

    private String email;

    public String getIsInUse() {
        return isInUse;
    }

    public void setIsInUse(String isInUse) {
        this.isInUse = isInUse;
    }
/*
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
*/
    private String code;
    private String isInUse;
    //private String ip;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", isInUse='" + isInUse + '\'' +
                '}';
    }
/*
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

 */
    public static User getFromInfoUserForSignIn(InfoUser infoUser){
        User user=new User();
        user.setUsername(infoUser.getUsername());
        user.setPassword(infoUser.getPassword());
        return user;
    }
    public static User getFromInfoUserForSignUp(InfoUser infoUser){
        User user=new User();
        user.setUsername(infoUser.getUsername());
        user.setPassword(infoUser.getPassword());
        user.setEmail(infoUser.getEmail());
        user.setCode(infoUser.getCode());
        return user;
    }
}
