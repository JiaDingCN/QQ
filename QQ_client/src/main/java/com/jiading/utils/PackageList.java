package com.jiading.utils;

import com.jiading.domain.InfoUser;


import java.util.LinkedList;

/**
 * @program: QQ_client
 * @description: 存放输入和输出的数据包
 * @author: JiaDing
 * @create: 2020-05-19 09:48
 **/
public class PackageList {
    //加好友的包单独放在一个list中
    public static LinkedList<InfoUser> newFriends = new LinkedList<>();
    private static LinkedList<InfoUser> packagesToSend = new LinkedList<>();
    private static LinkedList<InfoUser> packagesForReceive = new LinkedList<>();

    public static void sendPackage(InfoUser user) {
        packagesToSend.add(user);
    }

    public static InfoUser getReceivedPackage() {
        return packagesForReceive.pollFirst();
    }

    public static InfoUser getPackagesToSend() {
        return packagesToSend.pollFirst();
    }

    public static void putReceivedPackages(InfoUser user) {
        packagesForReceive.add(user);
    }

    public static boolean hasPackagesToSend() {
        return !packagesToSend.isEmpty();
    }

    public static boolean hasPackagesToReceive() {
        return !packagesForReceive.isEmpty();
    }

    //开一个线程专门用来发心跳包
    class heartSender extends Thread {
        @Override
        public void run() {
            while(true){
                try {
                    sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InfoUser user=new InfoUser();
                user.setInfoType(InfoUser.InfoTypes.HEART.toString());
                PackageList.sendPackage(user);
            }
        }
    }
}
