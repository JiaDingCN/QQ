package com.jiading.utils;

import java.util.UUID;

/**
 * Java提供的产生UUID随机字符串工具类
 * 生成全球唯一的字符串
 */
public final class UuidUtil {
	private UuidUtil(){}
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-","");
	}
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(com.jiading.utils.UuidUtil.getUuid());
		System.out.println(com.jiading.utils.UuidUtil.getUuid());
		System.out.println(com.jiading.utils.UuidUtil.getUuid());
		System.out.println(com.jiading.utils.UuidUtil.getUuid());
	}
}
