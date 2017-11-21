package com.ctrip.framework.apollo.core;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetposaConstant  {
	public static String appId;  //应用id
	public static String devMeta; //服务地址
	public static Boolean isEnable; //启动开关


	public static final Lock globalLock = new ReentrantLock();
	public static final Condition condition = globalLock.newCondition();

	//默认应用名字
	public static final String default_appId = "wonder-base";
	public static final String default_base_namespece = "base";
	public static final String default_clusterName = "default";
	public static final String default_orgId = "netposa";
	public static final String default_full_namespace = default_orgId + "." + default_base_namespece ;


}
