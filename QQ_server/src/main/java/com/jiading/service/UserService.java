package com.jiading.service;

import com.jiading.domain.User;

import java.net.Socket;
import java.util.List;

public interface UserService {
    //1. 注册
    boolean signUp(User user);
    //2. 登录
    boolean signIn(User user, Socket socket);

    //3.验证验证码是否正确填写
    boolean verifyCode(User user);
    //4.退出登录
    boolean exit(User user,Socket socket);
    //5. 添加好友
    boolean addFriend(User user,String username);
    //6.查询所有好友
    List<String> findFriends(User user);
}
