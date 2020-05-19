package com.jiading.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-19 00:38
 **/
public class Friend {
    private final StringProperty username;
    private final StringProperty status;//在线、离线

    public Friend(String username, String status) {
        this.username = new SimpleStringProperty(username);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public static List<Friend> getFriendsListFromString(String friendsString) {
        if(friendsString.equals("SIGNINSUCCESS;")){
            return null;
        }
        LinkedList<Friend> list = new LinkedList<>();
        String[] split = friendsString.split(";");
        for (int i = 1; i < split.length; i++) {
            String[] friendInfo = split[i].split(",");
            //Friend friend=new Friend(username, status);
            //friend.setUsername(friendInfo[0]);
            Friend friend = null;
            if (friendInfo[1].equals("T")) {
                friend = new Friend(friendInfo[0], "在线");
                //friend.setStatus("在线");
            } else {
                friend = new Friend(friendInfo[0], "离线");
                //friend.setStatus("离线");
            }
            list.add(friend);
        }
        return list;
    }
    public static Friend getFriendFromInfoUser(InfoUser user){
        Friend friend;
        if(user.getChatInfo().equals("NEW_FRIEND_OFFLINE")){
            friend=new Friend(user.getToUsername(),"离线");
        }else if(user.getChatInfo().equals("NEW_FRIEND_ONLINE")){
            friend=new Friend(user.getToUsername(),"在线");
        }else{
            friend = new Friend(user.getUsername(),"在线");
        }
        return friend;
    }
}
