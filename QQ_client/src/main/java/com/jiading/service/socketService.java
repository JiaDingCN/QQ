package com.jiading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jiading.domain.InfoUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @program: QQ_client
 * @description: 建立socket、读写数据
 * @author: JiaDing
 * @create: 2020-05-18 23:50
 **/
public class socketService extends Thread {
    //定义一个Socket对象
    Socket socket = null;
    InputStream socketIn = null;
    OutputStream socketOut = null;
    Scanner scanner = null;
    ObjectMapper mapper=new ObjectMapper();

    public socketService(String host, int port) {
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
        super.run();
        try {
            scanner = new Scanner(System.in);
            socketOut = socket.getOutputStream();
            // 读Sock里面的数据
            socketIn = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = socketIn.read(buf)) != -1) {
                System.out.println(new String(buf, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackage(InfoUser user) throws IOException {
        //将InfoUser转为json
        String json = mapper.writeValueAsString(user);
        socketOut.write(json.getBytes());
        socketOut.flush();
    }
}
