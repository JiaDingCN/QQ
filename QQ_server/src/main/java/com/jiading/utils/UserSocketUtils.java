package com.jiading.utils;

import com.jiading.domain.InfoUser;
import com.jiading.service.impl.JsonProcessService;
import com.jiading.service.impl.UserSocketProcessService;

import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: QQ_server
 * @description: 储存用户名和socket的对应关系
 * @author: JiaDing
 * @create: 2020-05-10 23:02
 **/
public class UserSocketUtils {
    private static ConcurrentHashMap<String, Socket>socketMap;
    static{
        socketMap=new ConcurrentHashMap<>();
        HeartPackageSender heartSender=new HeartPackageSender();
        heartSender.start();
    }
    public static Socket getSocket(String username) throws Exception {
        if(socketMap.containsKey(username)){
            return socketMap.get(username);
        }else{
            throw new Exception("该用户不在线!");
        }
    }
    public static boolean isOnline(String username){
        return(socketMap.containsKey(username));
    }
    public static void addSocket(String username,Socket socket){
        socketMap.put(username,socket);
    }
    public static void removeSocket(String username,Socket socket) throws Exception {
        if(socketMap.containsKey(username)){
            if(socketMap.get(username).equals(socket)){
                socketMap.remove(username);
                return;
            }else{
                throw new Exception("该用户和该socket不对应！");
            }
        }else{
            throw new Exception("该用户未处于登录状态！");
        }

    }
     //定期发送心跳包
    public static class HeartPackageSender extends Thread{
        @Override
        public void run(){
            InfoUser user=new InfoUser();
            user.setInfoType(InfoUser.InfoTypes.HEART.toString());
            while(true){
                //3分钟发一次心跳包
                try {
                    sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Enumeration<Socket> elements = socketMap.elements();
                while(elements.hasMoreElements()){
                    Socket socket=elements.nextElement();
                    PackageSender.sendPackage(socket,user);
                }
            }
        }
    }
    /*public static UserSocketProcessService getUserSocketProcessService(String username){
        if(socketMap.containsKey(username)){
            return socketMap.get(username);
        }else{
            UserSocketProcessService service=new UserSocketProcessService();
            socketMap.put(username,service);
            return service;
        }
    }*/
    /*public static void signInRegisterIp(User user,String ip){
        if(ipMap.containsKey(user.getUsername())){
            ipMap.replace(user.getUsername(),ip);
        }else{
            ipMap.put(user.getUsername(),ip);
        }

    }
    public static void offLineAndDropIp(User user){
        ipMap.remove(user.getUsername());
    }
    public static boolean checkWhetherOnline(User user){
        return ipMap.containsKey(user.getUsername());
    }
    public static String getUserIp(User user){
        return ipMap.get(user.getUsername());
    }*/

}
