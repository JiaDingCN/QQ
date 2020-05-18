package com.jiading.service.impl;

import com.jiading.dao.UserDao;
import com.jiading.domain.User;
import com.jiading.service.UserService;
import com.jiading.utils.MailUtils;
import com.jiading.utils.UserSocketUtils;
import com.jiading.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.List;

/**
 * @program: QQ_server
 * @description: 与用户有关的操作
 * @author: JiaDing
 * @create: 2020-05-10 22:56
 * user来自于用户端数据
 **/
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    /*
    生成一个验证码并填入user中，之后再使用verifyCode来验证验证码是否正确
    这里如果是false,就是数据错误(也就是用户名冲突)，而不是验证码错误
     */
    public boolean signUp(User user) {
        User userForCheck = userDao.isUsernameExists(user.getUsername());
        if(userForCheck==null){
            String code= UuidUtil.getUuid();
            user.setCode(code);
            MailUtils.sendCode(user.getEmail(),code);
            user.setIsInUse("F");
            userDao.signUp(user);
            return true;
        }
        else{
            return false;
        }
    }
    /*
    登录的时候在UserSocketUtils中更新socket
     */
    @Override
    public boolean signIn(User user, Socket socket) {
        User userForCheck = userDao.signIn(user);
        if(userForCheck!=null && userForCheck.getIsInUse().equals("T")){
            UserSocketUtils.addSocket(user.getUsername(),socket);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean verifyCode(User user) {
        User userForCheck = userDao.signIn(user);
        return user.getCode().equals(userForCheck.getCode());
    }

    @Override
    public boolean exit(User user,Socket socket) {
        try {
            UserSocketUtils.removeSocket(user.getUsername(),socket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addFriend(User user, String username) {
        try {
            if(UserSocketUtils.getSocket(username)!=null){
                userDao.addFriend(user.getUsername(),username);
                userDao.addFriend(username,user.getUsername());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public List<String> findFriends(User user) {
        List<String> friends = userDao.findFriends(user.getUsername());
        return friends;
    }


}
