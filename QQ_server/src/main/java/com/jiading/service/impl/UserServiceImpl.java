package com.jiading.service.impl;

import com.jiading.dao.UserDao;
import com.jiading.domain.User;
import com.jiading.service.UserService;

import com.jiading.utils.UserSocketUtils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

/**
 * @program: QQ_server
 * @description: 与用户有关的操作
 * @author: JiaDing
 * @create: 2020-05-10 22:56
 * user来自于用户端数据
 **/
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private InputStream in;
    private SqlSessionFactory factory;
    private SqlSession session;

    public UserServiceImpl() throws IOException {
        in = Resources.getResourceAsStream("mybatis-config.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        userDao = session.getMapper(UserDao.class);
    }

    @Override
    protected void finalize() throws Throwable {
        session.commit();
        session.close();
        in.close();
    }

    @Override
    /*
    生成一个验证码并填入user中，之后再使用verifyCode来验证验证码是否正确
    这里如果是false,就是数据错误(也就是用户名冲突)，而不是验证码错误
     */
    public boolean signUp(User user) {
        try {
            String userForCheck = userDao.isUsernameExists(user.getUsername());
            session.commit();

            if (userForCheck == null) {
                System.out.println("true");
                userDao.signUp(user);
                session.commit();

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    登录的时候在UserSocketUtils中更新socket
     */
    @Override
    public boolean signIn(User user, Socket socket) {
        User userForCheck = userDao.signIn(user);
        session.commit();
        if (userForCheck != null && userForCheck.getIsInUse().equals("T")) {
            UserSocketUtils.addSocket(user.getUsername(), socket);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean verifyCode(User user) {
        User userForCheck = userDao.signIn(user);
        session.commit();
        if (user.getCode().equals(userForCheck.getCode())) {
            userDao.changeCodeIntoT(userForCheck);
            session.commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean exit(User user, Socket socket) {
        try {
            UserSocketUtils.removeSocket(user.getUsername(), socket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addFriend(User user, String username) {
        userDao.addFriend(user.getUsername(), username);
        session.commit();
        userDao.addFriend(username, user.getUsername());
        session.commit();
        return true;
    }

    @Override
    public List<String> findFriends(User user) {
        List<String> friends = userDao.findFriends(user.getUsername());
        session.commit();
        return friends;
    }


}
