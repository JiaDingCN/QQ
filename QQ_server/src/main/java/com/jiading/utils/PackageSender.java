package com.jiading.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiading.domain.InfoUser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @program: QQ_server
 * @description: 发送数据包
 * @author: JiaDing
 * @create: 2020-05-11 11:29
 **/
public class PackageSender {
    private static PrintWriter out;
    private  static ObjectMapper mapper;
    static {
        mapper=new ObjectMapper();
    }
    //信息要封装在InfoUser中
    public static void sendPackage(Socket socket, InfoUser user){
        try {
            out=new PrintWriter(socket.getOutputStream());
            out.print(mapper.writeValueAsString(user));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
