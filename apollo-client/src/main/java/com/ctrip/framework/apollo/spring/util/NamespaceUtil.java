package com.ctrip.framework.apollo.spring.util;

import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.MetaDomainConsts;
import com.ctrip.framework.apollo.core.utils.NetposaPropertiesUtil;
import com.ctrip.framework.apollo.model.NamespaceDTO;
import com.ctrip.framework.apollo.util.http.HttpRequest;
import com.ctrip.framework.apollo.util.http.HttpResponse;
import com.ctrip.framework.apollo.util.http.HttpUtil;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取所有发布namesacpe
 */
public class NamespaceUtil {

	public static List<String> getAllNamespace() {
		List<String> namespaces = new ArrayList<>();
		HttpUtil m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
		//从configservice中获取namespace  clusters默认为default
		//{devMeta}/apps/{appId}/clusters/{clusters}/namespaces
		String url = NetposaPropertiesUtil.getDevMeta() + "/apps/" + NetposaPropertiesUtil.getAppId() +
				"/clusters/"+ ConfigConsts.CLUSTER_NAME_DEFAULT+"/namespaces";
		HttpRequest request = new HttpRequest(url);
		final HttpResponse<List<NamespaceDTO>> response = m_httpUtil.doGet(request, new TypeToken<List<NamespaceDTO>>() {
		}.getType());
		if (response.getStatusCode() == 200 && response.getBody() != null) {
			List<NamespaceDTO> namespaceList = response.getBody();
			for (NamespaceDTO dto : namespaceList) {
				String namespaceName = dto.getNamespaceName();
				String appId = dto.getAppId();
				if(isPublish(appId,namespaceName)){
					namespaces.add(namespaceName);
				}
			}
		}
		return namespaces;

	}

	//是不是已经发布的namespace
	private static boolean isPublish(String appId ,String namespaces){
		HttpUtil m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
		String url = NetposaPropertiesUtil.getDevMeta() + "/apps/" + appId +
				"/clusters/"+ ConfigConsts.CLUSTER_NAME_DEFAULT + "/namespaces/"+namespaces+"/releases/latest";
		HttpRequest request = new HttpRequest(url);
		HttpResponse response = m_httpUtil.doGet(request,Object.class);
		return (response.getStatusCode() == 200 && response.getBody() != null);
	}
}
