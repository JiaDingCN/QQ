package com.jiading.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiading.domain.InfoUser;
import com.jiading.domain.User;
import com.jiading.service.UserService;
import com.jiading.utils.MailUtils;
import com.jiading.utils.PackageSender;
import com.jiading.utils.UserSocketUtils;
import com.jiading.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: QQ_server
 * @description: 处理接收的json
 * @author: JiaDing
 * @create: 2020-05-10 23:44
 **/
public class JsonProcessService extends Thread {
    Socket socket;
    String username = null;

    //only for debug
    //String fileFolder = "/root/";
    String fileFolder="E:\\testSocket\\";

    public JsonProcessService(Socket socket) {
        //通过socket对象可以获得输出流，用来写数据
        //OutputStream os = null;

        // 向客户端发送消息
        //os.write("服务器正在向你发送消息！".getBytes());
        //在服务器上显示连接的上的电脑、
        //System.out.println(socket.getInetAddress().getHostAddress()+"连接上了！");
        //通过socket对象可以获得输入流，用来读取用户数据
        // os = socket.getOutputStream();
        //InputStream is=socket.getInputStream();
        //this.out=os;
        //this.in=is;
        this.socket = socket;
    }

    @Override
    public void run() {
        //读取数据
        int len = 0;
        byte[] buf = new byte[1024];
        while (true) {
            try {
                if (!((len = socket.getInputStream().read(buf)) != -1)) break;
            } catch (Exception e) {
                e.printStackTrace();
                if (username != null) {
                    UserSocketUtils.removeSocket(username);
                }
                return;
            }
            //解析读取到的json文件
            String jsonFromByte = new String(buf, 0, len, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            UserService service = null;
            try {
                service = new UserServiceImpl();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InfoUser user = mapper.readValue(jsonFromByte, InfoUser.class);

                //onlyForDebug
                System.out.println(user);


                //将该socket保存到map中
                username = user.getUsername();
                UserSocketUtils.addSocket(user.getUsername(), socket);
                //处理该数据包
                if (user.getInfoType().equals(InfoUser.InfoTypes.HEART.toString())) {
                    ;
                } else if (user.getInfoType().equals(InfoUser.InfoTypes.SENDFILE.toString())) {
                    UserSocketUtils.isInTransferFileSet.add(socket);
                    String path=fileFolder+user.getCode();
                    Integer numbersOfPackages=Integer.valueOf(user.getChatInfo());
                    File file=new File(path);
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fos=new FileOutputStream(file);
                    DataInputStream dis=new DataInputStream(socket.getInputStream());
                    Integer length=0;
                    int count=0;
                    System.out.println("-------------------接收文件----------------");
                    while(count<numbersOfPackages){
                        length=dis.read(buf,0,buf.length);
                        System.out.println(length);

                            fos.write(buf,0,length);
                            fos.flush();
                            count++;

                    }
                    fos.close();
                    String toUsername=user.getToUsername();
                    InfoUser user2=new InfoUser();
                    user2.setInfoType(InfoUser.InfoTypes.SENDFILE.toString());
                    user2.setUsername(user.getUsername());
                    user2.setCode(user.getCode());
                    Socket toSocket = UserSocketUtils.getSocket(toUsername);
                    UserSocketUtils.isInTransferFileSet.add(toSocket);
                    DataOutputStream dos=new DataOutputStream(toSocket.getOutputStream());
                    FileInputStream fis=new FileInputStream(file);
                    user2.setChatInfo(user.getChatInfo());
                    PackageSender.sendPackage(toSocket,user2);
                    System.out.println(user2);
                   count=0;
                    System.out.println("---------------------发送文件----------------");
                    while(count<numbersOfPackages){
                        length=fis.read(buf,0,buf.length);
                        System.out.println(length);
                            dos.write(buf,0,length);
                            dos.flush();
                            count++;

                    }
                    System.out.println("文件发送完毕");
                    fis.close();
                    UserSocketUtils.isInTransferFileSet.remove(socket);
                    UserSocketUtils.isInTransferFileSet.remove(toSocket);
                    file.delete();
                } else if (user.getInfoType().equals(InfoUser.InfoTypes.CHAT.toString())) {
                    //默认能发送数据包的就是在线的，这个在前台控制
                    Socket toSocket = UserSocketUtils.getSocket(user.getToUsername());
                    InfoUser infoUser = new InfoUser();
                    infoUser.setInfoType(InfoUser.InfoTypes.CHAT.toString());
                    infoUser.setUsername(user.getUsername());
                    infoUser.setChatInfo(user.getChatInfo());
                    PackageSender.sendPackage(toSocket, infoUser);
                } else if (user.getInfoType().equals(InfoUser.InfoTypes.ADDFRIEND.toString())) {
                    User userForBackUse = User.getFromInfoUserForSignIn(user);
                    //要添加的好友名称保存在toUsername中
                    service.addFriend(userForBackUse, user.getToUsername());
                    //如果好友此时在线，提醒其更新好友列表
                    //查询好友此时是否在线，返回好友在线状态
                    InfoUser info = new InfoUser();
                    if (UserSocketUtils.isOnline(user.getToUsername())) {
                        InfoUser toInfo = new InfoUser();
                        toInfo.setInfoType(InfoUser.InfoTypes.FRIENDADDED.toString());
                        toInfo.setUsername(user.getUsername());
                        Socket toSocket = UserSocketUtils.getSocket(user.getToUsername());
                        PackageSender.sendPackage(toSocket, toInfo);
                        info.setChatInfo(InfoUser.stateCodes.NEW_FRIEND_ONLINE.toString());
                    } else {
                        info.setChatInfo(InfoUser.stateCodes.NEW_FRIEND_OFFLINE.toString());
                    }
                    info.setInfoType(InfoUser.InfoTypes.FRIENDADDED.toString());
                    info.setToUsername(user.getToUsername());
                    PackageSender.sendPackage(socket, info);
                } else {
                    //注册或者登陆
                    if (user.getInfoType().equals(InfoUser.InfoTypes.SIGNIN.toString())) {
                        User userForBackUse = User.getFromInfoUserForSignIn(user);
                        //signIn,登录
                        boolean flag = service.signIn(userForBackUse, socket);
                        InfoUser backInfo = new InfoUser();
                        backInfo.setInfoType(InfoUser.InfoTypes.SIGNIN.toString());
                        if (flag) {
                            //登录成功了，返回好友列表
                            List<String> friends = service.findFriends(userForBackUse);
                            StringBuilder builder = new StringBuilder();
                            builder.append(InfoUser.stateCodes.SIGNINSUCCESS.toString() + ";");
                            Iterator<String> iterator = friends.iterator();
                            while (iterator.hasNext()) {
                                String friendUsername = iterator.next();
                                if (UserSocketUtils.isOnline(friendUsername)) {
                                    builder.append(friendUsername + ",T;");
                                } else {
                                    builder.append(friendUsername + ",F;");
                                }
                            }
                            backInfo.setChatInfo(builder.toString());
                        } else {
                            backInfo.setChatInfo(InfoUser.stateCodes.SIGNINFALL.toString());
                        }
                        PackageSender.sendPackage(socket, backInfo);
                    } else {
                        User fromInfoUserForSignUp = User.getFromInfoUserForSignUp(user);
                        //signup，注册
                        if (user.getChatInfo().equals(InfoUser.stateCodes.ASKCODE.toString())) {
                            //生成注册码，发送邮件
                            String uuid = UuidUtil.getUuid();
                            fromInfoUserForSignUp.setCode(uuid);
                            fromInfoUserForSignUp.setIsInUse("F");
                            boolean flag = service.signUp(fromInfoUserForSignUp);
                            InfoUser info = new InfoUser();
                            info.setInfoType(InfoUser.InfoTypes.SIGNUP.toString());
                            if (flag) {
                                System.out.println("before mail");
                                MailUtils.sendCode(fromInfoUserForSignUp.getEmail(), uuid);
                                System.out.println("after mail");
                                info.setChatInfo(InfoUser.stateCodes.CODEASKED.toString());
                                info.setInfoType(InfoUser.InfoTypes.SIGNUP.toString());
                            } else {
                                info.setChatInfo(InfoUser.stateCodes.SIGNUPFALL_USERNAME_ALREADY_EXISTES.toString());
                            }
                            System.out.println("sentPackage:"+info);
                            PackageSender.sendPackage(socket, info);
                        } else {
                            boolean flag = service.verifyCode(fromInfoUserForSignUp);
                            InfoUser info = new InfoUser();
                            info.setInfoType(InfoUser.InfoTypes.SIGNUP.toString());
                            if (flag) {
                                info.setChatInfo(InfoUser.stateCodes.SIGNUUPSUCCESS.toString());
                            } else {
                                info.setChatInfo(InfoUser.stateCodes.SIGNUPFALL_VERIFYCODE_WRONG.toString());
                            }
                            PackageSender.sendPackage(socket, info);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //断开连接
        UserSocketUtils.removeSocket(username);
    }
}
