package com.ctrip.framework.apollo.metaservice.controller;

import com.ctrip.framework.apollo.core.MetaDomainConsts;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.utils.NetposaPropertiesUtil;
import com.ctrip.framework.apollo.metaservice.service.DiscoveryService;
import com.google.common.collect.Lists;
import com.netflix.appinfo.InstanceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

//  @Autowired
//  private DiscoveryService discoveryService;

//
//  @RequestMapping("/meta")
//  public List<ServiceDTO> getMetaService() {
//    List<InstanceInfo> instances = discoveryService.getMetaServiceInstances();
//    List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {
//
//      @Override
//      public ServiceDTO apply(InstanceInfo instance) {
//        ServiceDTO service = new ServiceDTO();
//        service.setAppName(instance.getAppName());
//        service.setInstanceId(instance.getInstanceId());
//        service.setHomepageUrl(instance.getHomePageUrl());
//        return service;
//      }
//
//    }).collect(Collectors.toList());
//    return result;
//  }
//
//  @RequestMapping("/config")
//  public List<ServiceDTO> getConfigService(
//      @RequestParam(value = "appId", defaultValue = "") String appId,
//      @RequestParam(value = "ip", required = false) String clientIp) {
//    List<InstanceInfo> instances = discoveryService.getConfigServiceInstances();
//    List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {
//
//      @Override
//      public ServiceDTO apply(InstanceInfo instance) {
//        ServiceDTO service = new ServiceDTO();
//        service.setAppName(instance.getAppName());
//        service.setInstanceId(instance.getInstanceId());
//        service.setHomepageUrl(instance.getHomePageUrl());
//        return service;
//      }
//
//    }).collect(Collectors.toList());
//    return result;
//  }
//
//  @RequestMapping("/admin")
//  public List<ServiceDTO> getAdminService() {
//    List<InstanceInfo> instances = discoveryService.getAdminServiceInstances();
//    List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {
//
//      @Override
//      public ServiceDTO apply(InstanceInfo instance) {
//        ServiceDTO service = new ServiceDTO();
//        service.setAppName(instance.getAppName());
//        service.setInstanceId(instance.getInstanceId());
//        service.setHomepageUrl(instance.getHomePageUrl());
//        return service;
//      }
//
//    }).collect(Collectors.toList());
//    return result;
//  }


  @RequestMapping("/meta")
  public List<ServiceDTO> getMetaService() {
    return Lists.newArrayList(createLocalServerInfo());
  }

  @RequestMapping("/config")
  public List<ServiceDTO> getConfigService() {
    return Lists.newArrayList(createLocalServerInfo());
  }


  @RequestMapping("/admin")
  public List<ServiceDTO> getAdminService() {
    return Lists.newArrayList(createLocalServerInfo());
  }

  private ServiceDTO createLocalServerInfo(){
    ServiceDTO service = new ServiceDTO();
    service.setAppName("configservice");
    service.setInstanceId("configservice");
    service.setHomepageUrl(MetaDomainConsts.getDevMeta());
    return service;
  }

}
