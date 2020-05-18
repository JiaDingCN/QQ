package com.jiading;

import com.jiading.service.impl.SocketService;

/**
 * @program: QQ_server
 * @description: 主类
 * @author: JiaDing
 * @create: 2020-05-18 22:53
 **/
public class Main {
    public static void main(String[] args) {
        int PORT=6768;
        SocketService service=new SocketService(PORT);
        System.out.println("开始监听"+PORT+"端口");
        service.start();
    }
}
