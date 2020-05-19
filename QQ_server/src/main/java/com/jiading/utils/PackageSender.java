package com.jiading.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiading.domain.InfoUser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @program: QQ_server
 * @description: 发送数据包
 * @author: JiaDing
 * @create: 2020-05-11 11:29
 **/
public class PackageSender {
    private static OutputStream out;
    private  static ObjectMapper mapper;
    static {
        mapper=new ObjectMapper();
    }
    //信息要封装在InfoUser中
    public static void sendPackage(Socket socket, InfoUser user){
        try {
            out=socket.getOutputStream();
            String temp=mapper.writeValueAsString(user);
            out.write(temp.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
