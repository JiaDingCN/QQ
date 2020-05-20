package com.jiading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jiading.domain.InfoUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiading.utils.PackageList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @program: QQ_client
 * @description: 建立socket、读写数据
 * @author: JiaDing
 * @create: 2020-05-18 23:50
 **/
public class SocketService extends Thread {
    //定义一个Socket对象
    static Socket socket = null;
    ObjectMapper mapper=new ObjectMapper();

    public static Socket getSocket(){
        return socket;
    }
    public SocketService(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //客户端一连接就可以写数据个服务器了
        new sendMessThread().start();
        super.run();
        try {
            // 读Sock里面的数据
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = s.read(buf)) != -1) {
                //将Json转为InfoUser对象
                InfoUser receivedUser=mapper.readValue(new String(buf, 0, len, StandardCharsets.UTF_8),InfoUser.class);

                //onlyForDebug
                System.out.println(receivedUser);


                if(receivedUser.getInfoType().equals(InfoUser.InfoTypes.FRIENDADDED.toString())){
                    PackageList.newFriends.add(receivedUser);
                }else if(receivedUser.getInfoType().equals(InfoUser.InfoTypes.HEART.toString())){
                    //心跳包不管
                    ;
                }else{
                    PackageList.putReceivedPackages(receivedUser);


                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //往Socket里面写数据，需要新开一个线程
    class sendMessThread extends Thread {
        @Override
        public void run() {
            super.run();
            //写操作
            OutputStream os = null;
            try {
                os = socket.getOutputStream();
                String in = "";
                while(true){
                 if(PackageList.hasPackagesToSend()){
                     InfoUser toSendUser=PackageList.getPackagesToSend();
                     String temp = mapper.writeValueAsString(toSendUser);
                     os.write(temp.getBytes(StandardCharsets.UTF_8));
                     os.flush();
                 }else{
                     //休息500毫秒
                     sleep(500);
                 }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
