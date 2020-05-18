package com.jiading.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: QQ_server
 * @description: 服务器端socket通信
 * @author: JiaDing
 * @create: 2020-05-10 23:36
 **/
public class SocketService extends Thread{
    ServerSocket server=null;
    public SocketService(int port){
        try{
            server=new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //接受消息
    @Override
    public void run(){
        super.run();
        try{
                //一直接收用户的连接，连接之后发送一条短信给用户
            while(true){
                // 建立socket接口，accept方法是一个阻塞进程,等到有用户连接才往下走
                // 定义Socket类
                Socket  socket = server.accept();
                System.out.println("收到一个连接");
                //将socket交给jsonProcessService处理
                JsonProcessService process=new JsonProcessService(socket);
                process.start();
                System.out.println("将该连接交由jsonProcessService处理");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
