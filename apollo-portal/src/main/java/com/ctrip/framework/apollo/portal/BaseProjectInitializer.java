package com.ctrip.framework.apollo.portal;

import com.ctrip.framework.apollo.common.entity.App;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.core.NetposaConstant;
import com.ctrip.framework.apollo.portal.constant.RoleType;
import com.ctrip.framework.apollo.portal.listener.AppCreationEvent;
import com.ctrip.framework.apollo.portal.listener.AppNamespaceCreationEvent;
import com.ctrip.framework.apollo.portal.repository.AppNamespaceRepository;
import com.ctrip.framework.apollo.portal.repository.AppRepository;
import com.ctrip.framework.apollo.portal.service.AppNamespaceService;
import com.ctrip.framework.apollo.portal.service.AppService;
import com.ctrip.framework.apollo.portal.service.RolePermissionService;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.portal.util.RoleUtils;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//初始化项目
@Component
public class BaseProjectInitializer implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger logger = LoggerFactory.getLogger(BaseProjectInitializer.class);

	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private UserInfoHolder userInfoHolder;
	@Autowired
	private AppService appService;
	@Autowired
	private AppNamespaceService appNamespaceService;
	@Autowired
	private AppRepository appRepository;
	@Autowired
	private AppNamespaceRepository appNamespaceRepository;


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		NetposaConstant.globalLock.lock();
		try {
			initAPPAndNamespace();
			Thread.sleep(TimeUnit.SECONDS.toSeconds(1));
			NetposaConstant.condition.signal();
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}finally {
			NetposaConstant.globalLock.unlock();
		}
	}

	private void initAPPAndNamespace(){
		App managedApp = appRepository.findByAppId(NetposaConstant.default_appId);
		AppNamespace namespace = appNamespaceRepository.findByNameAndIsPublic(
									NetposaConstant.default_full_namespace ,true);
		if (managedApp == null && namespace == null) {
			//初始化app
			App app = new App();
			app.setAppId(NetposaConstant.default_appId);
			app.setName("图解系统公共基础配置");
			app.setOwnerName(userInfoHolder.getUser().getUserId());
			app.setOrgId(NetposaConstant.default_orgId);
			app.setOrgName("解析系统应用研发部");
			App createdApp = appService.createAppInLocal(app); //创建默认应用
			//基于事件驱动
			publisher.publishEvent(new AppCreationEvent(createdApp));

			//初始化namespace
			AppNamespace appNamespace = new AppNamespace();
			appNamespace.setName(NetposaConstant.default_base_namespece);  //namespace的名字
			appNamespace.setAppId(NetposaConstant.default_appId);
			appNamespace.setFormat("properties");
			appNamespace.setPublic(true);
			appNamespace.setComment("图解系统公共基础配置");
			AppNamespace createdAppNamespace = appNamespaceService.createAppNamespaceInLocal(appNamespace);
			assignNamespaceRoleToOperator(NetposaConstant.default_appId, appNamespace.getName());
			publisher.publishEvent(new AppNamespaceCreationEvent(createdAppNamespace));
			logger.debug("图解系统公共配置项目初始化成功-> appId:{}",NetposaConstant.default_appId);
		}
	}

	//授权
	private void assignNamespaceRoleToOperator(String appId, String namespaceName) {
		//default assign modify、release namespace role to namespace creator
		String operator = userInfoHolder.getUser().getUserId();
		rolePermissionService
				.assignRoleToUsers(RoleUtils.buildNamespaceRoleName(appId, namespaceName, RoleType.MODIFY_NAMESPACE),
						Sets.newHashSet(operator), operator);
		rolePermissionService
				.assignRoleToUsers(RoleUtils.buildNamespaceRoleName(appId, namespaceName, RoleType.RELEASE_NAMESPACE),
						Sets.newHashSet(operator), operator);
	}

}
