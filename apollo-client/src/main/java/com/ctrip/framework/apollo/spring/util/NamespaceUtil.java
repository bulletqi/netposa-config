package com.ctrip.framework.apollo.spring.util;

import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.MetaDomainConsts;
import com.ctrip.framework.apollo.core.NetposaConstant;
import com.ctrip.framework.apollo.core.utils.NetposaPropertiesUtil;
import com.ctrip.framework.apollo.model.NamespaceDTO;
import com.ctrip.framework.apollo.util.http.HttpRequest;
import com.ctrip.framework.apollo.util.http.HttpResponse;
import com.ctrip.framework.apollo.util.http.HttpUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取所有发布namesacpe
 */
public class NamespaceUtil {

	private static final Logger logger = LoggerFactory.getLogger(NamespaceUtil.class);

	private static final Gson gson = new Gson();

	/*
	 * 1.应用在配置中心是否有对应的应用
	 * 2.加载系统默认应用及配置项
	 */
	public static List<String> getAllNamespace() {
		List<String> namespaces = new ArrayList<>();
		if(!isHasOwnApp()){
			logger.info("{}对应的app在配置中心没有找到，加载默认应用：{} ，读取公共的namespace：{}",
					NetposaConstant.appId,
					NetposaConstant.default_appId,
					NetposaConstant.default_full_namespace);
			namespaces.add(NetposaConstant.default_full_namespace);
			NetposaConstant.appId = NetposaConstant.default_appId; //默认应用id设置为读取到的应用id
		}else {
			HttpUtil m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
			//从configservice中获取namespace  clusters默认为default
			//{devMeta}/apps/{appId}/clusters/{clusters}/namespaces
			String url = NetposaPropertiesUtil.getDevMeta() + "/apps/" + NetposaPropertiesUtil.getAppId() +
					"/clusters/" + ConfigConsts.CLUSTER_NAME_DEFAULT + "/namespaces";
			HttpRequest request = new HttpRequest(url);
			final HttpResponse<List<NamespaceDTO>> response = m_httpUtil.doGet(request, new TypeToken<List<NamespaceDTO>>() {
			}.getType());
			if (response.getStatusCode() == 200 && response.getBody() != null) {
				List<NamespaceDTO> namespaceList = response.getBody();
				for (NamespaceDTO dto : namespaceList) {
					String namespaceName = dto.getNamespaceName();
					String appId = dto.getAppId();
					if (isPublish(appId, namespaceName)) {
						namespaces.add(namespaceName);
					}
				}
			}
		}
		return namespaces;
	}

	//是否拥有自己的应用
	private static boolean isHasOwnApp(){
		HttpUtil m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
		String url = NetposaPropertiesUtil.getDevMeta() + "/apps/" + NetposaPropertiesUtil.getAppId();
		try {
			HttpRequest request = new HttpRequest(url);
			HttpResponse<JsonObject> response = m_httpUtil.doGet(request, new TypeToken<JsonObject>() {
			}.getType());
			return response.getStatusCode() == 200 && response.getBody() != null;
		}catch (Exception e){
			return false;
		}
	}


	//当前namespaces是否已经包含配置项,返回200说明namespace已经发布且configurations返回字段存在配置
	private static boolean isPublish(String appId ,String namespaces){
		HttpUtil m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
		String url = NetposaPropertiesUtil.getDevMeta() + "/configs/" + appId + "/" +ConfigConsts.CLUSTER_NAME_DEFAULT + "/"+ namespaces;
		HttpRequest request = new HttpRequest(url);
		try {
			HttpResponse response = m_httpUtil.doGet(request,Object.class);
			return (response.getStatusCode() == 200 &&
					!gson.toJsonTree(response.getBody()).getAsJsonObject().get("configurations").isJsonNull());
		}catch (Exception e){
			//未发布的namespace不加载
			return false;
		}
	}
}
