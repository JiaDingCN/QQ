package com.jiading.dao;

import com.jiading.domain.Friend;
import com.jiading.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface UserDao {
    //1.注册用户
    @Insert("insert into user(username,password,email,code,isInUse)values(#{username},PASSWORD(#{password}),#{email},#{code},#{isInUse})")
    public void signUp(User user);

    //2.登录:返回User如果不是null就表示登录成功了
    @Select("select * from user where username=#{username} and password=PASSWORD(#{password})")
    public User signIn(User user);

    //3. 查找用户名是否已存在：返回User如果不是null就是已经存在
    @Select("select * from user where username=#{username}")
    public User isUsernameExists(@Param("username")String username);

    //4. 返回所有好友的用户名列表
    @Select("select friendUsername from friends where myUsername=#{username}")
    public List<String>findFriends(@Param("username")String username);
    //5. 添加好友
    @Insert("insert into friends values(#{username},#{friendUsername})")
    public void addFriend(@Param("username")String username,@Param("friendUsername")String friendUsername);
}
