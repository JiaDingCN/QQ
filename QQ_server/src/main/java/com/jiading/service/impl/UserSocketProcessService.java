package com.jiading.service.impl;

import com.jiading.domain.InfoUser;

import java.net.Socket;

/**
 * @program: QQ_server
 * @description: 处理每个用户对应的socket的
 * @author: JiaDing
 * @create: 2020-05-11 10:03
 **/
@Deprecated
public class UserSocketProcessService extends Thread {
    Socket socket;
    InfoUser info;

    public InfoUser getInfo() {
        return info;
    }

    public void setInfo(InfoUser info) {
        this.info = info;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run(){
        while(true){
            if(info==null){
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{

            }
        }
    }
}
