package com.jiading.domain;

/**
 * @program: QQ_server
 * @description: json报文转换成的对象
 * @author: JiaDing
 * @create: 2020-05-11 09:41
 **/
/*
        注：具体内容可以为空，只是为了统一格式
        * 操作类型
        * 用户名
        * 密码
        * 邮箱
        * 激活码
        * 目标用户的用户名
        * 聊天内容
 */
public class InfoUser {

    @Override
    public String toString() {
        return "InfoUser{" +
                "infoType='" + infoType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", chatInfo='" + chatInfo + '\'' +
                '}';
    }

    public enum InfoTypes {
        SIGNUP, SIGNIN, CHAT, HEART, FRIENDADDED, ADDFRIEND, SENDFILE;
    }
    private String infoType;
    private String username;
    private String password;
    private String email;
    public enum stateCodes {
        NEEDVERIFY, SIGNINSUCCESS, SIGNUUPSUCCESS, SIGNINFALL, SIGNUPFALL_USERNAME_ALREADY_EXISTES,
        SIGNUPFALL_VERIFYCODE_WRONG, ASKCODE, CODEASKED, NEW_FRIEND_ONLINE, NEW_FRIEND_OFFLINE;
    }
    private String code;
    private String toUsername;
    private String chatInfo;

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
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

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getChatInfo() {
        return chatInfo;
    }


    public void setChatInfo(String chatInfo) {
        this.chatInfo = chatInfo;
    }
}
