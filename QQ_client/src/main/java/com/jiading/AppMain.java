package com.jiading;

import com.jiading.service.SocketService;
import javafx.application.Application;

/**
 * @program: QQ_client
 * @description:
 * @author: JiaDing
 * @create: 2020-05-18 23:38
 **/
public class AppMain{
    /*
    用于启动应用程序
     */
    public static void main(String[] args) {
        SocketService socketService=new SocketService("192.168.1.4",6768);
        //SocketService socketService=new SocketService("39.96.62.124",6768);
        socketService.start();
        Application.launch(Main.class,args);
    }
}
