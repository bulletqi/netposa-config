package com.ctrip.framework.apollo.internals;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import com.ctrip.framework.apollo.core.MetaDomainConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.utils.ApolloThreadFactory;
import com.ctrip.framework.apollo.exceptions.ApolloConfigException;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.apollo.tracer.spi.Transaction;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.ctrip.framework.apollo.util.ExceptionUtil;
import com.ctrip.framework.apollo.util.http.HttpRequest;
import com.ctrip.framework.apollo.util.http.HttpResponse;
import com.ctrip.framework.apollo.util.http.HttpUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.reflect.TypeToken;

public class ConfigServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ConfigServiceLocator.class);
  private HttpUtil m_httpUtil;
  private ConfigUtil m_configUtil;
  private AtomicReference<List<ServiceDTO>> m_configServices;
  private Type m_responseType;
  private ScheduledExecutorService m_executorService;
  private static final Joiner.MapJoiner MAP_JOINER = Joiner.on("&").withKeyValueSeparator("=");
  private static final Escaper queryParamEscaper = UrlEscapers.urlFormParameterEscaper();

  /**
   * Create a config service locator.
   */
  public ConfigServiceLocator() {
    List<ServiceDTO> initial = Lists.newArrayList();
    m_configServices = new AtomicReference<>(initial);
    m_responseType = new TypeToken<List<ServiceDTO>>() {
    }.getType();
    m_httpUtil = ApolloInjector.getInstance(HttpUtil.class);
    m_configUtil = ApolloInjector.getInstance(ConfigUtil.class);
    this.m_executorService = Executors.newScheduledThreadPool(1,
        ApolloThreadFactory.create("ConfigServiceLocator", true));
    this.tryUpdateConfigServices();
    this.schedulePeriodicRefresh();
  }

  /**
   * Get the config service info from remote meta server.
   *
   * @return the services dto
   */
  public List<ServiceDTO> getConfigServices() {
    if (m_configServices.get().isEmpty()) {
      updateConfigServices();
    }

    return m_configServices.get();
  }

  private boolean tryUpdateConfigServices() {
    try {
      updateConfigServices();
      return true;
    } catch (Throwable ex) {
      //ignore
    }
    return false;
  }

  private void schedulePeriodicRefresh() {
    this.m_executorService.scheduleAtFixedRate(
        new Runnable() {
          @Override
          public void run() {
            logger.debug("refresh config services");
            Tracer.logEvent("Apollo.MetaService", "periodicRefresh");
            tryUpdateConfigServices();
          }
        }, m_configUtil.getRefreshInterval(), m_configUtil.getRefreshInterval(),
        m_configUtil.getRefreshIntervalTimeUnit());
  }

//  private synchronized void updateConfigServices() {
//    String url = assembleMetaServiceUrl();
//
//    HttpRequest request = new HttpRequest(url);
//    int maxRetries = 2;
//    Throwable exception = null;
//
//    for (int i = 0; i < maxRetries; i++) {
//      Transaction transaction = Tracer.newTransaction("Apollo.MetaService", "getConfigService");
//      transaction.addData("Url", url);
//      try {
//        HttpResponse<List<ServiceDTO>> response = m_httpUtil.doGet(request, m_responseType);
//        transaction.setStatus(Transaction.SUCCESS);
//        List<ServiceDTO> services = response.getBody();
//        if (services == null || services.isEmpty()) {
//          logConfigServiceToCat("Empty response!");
//          continue;
//        }
//        m_configServices.set(services);
//        logConfigServicesToCat(services);
//        return;
//      } catch (Throwable ex) {
//        Tracer.logEvent("ApolloConfigException", ExceptionUtil.getDetailMessage(ex));
//        transaction.setStatus(ex);
//        exception = ex;
//      } finally {
//        transaction.complete();
//      }
//
//      try {
//        m_configUtil.getOnErrorRetryIntervalTimeUnit().sleep(m_configUtil.getOnErrorRetryInterval());
//      } catch (InterruptedException ex) {
//        //ignore
//      }
//    }
//
//    throw new ApolloConfigException(
//        String.format("Get config services failed from %s", url), exception);
//  }

  //config和admin服务合并以后 直接返回config地址
  private synchronized void updateConfigServices() {
    List<ServiceDTO> services = Lists.newArrayList();
    ServiceDTO service = new ServiceDTO();
    service.setAppName("configservice");
    service.setInstanceId("configservice");
    service.setHomepageUrl(MetaDomainConsts.getDevMeta());
    services.add(service);
    m_configServices.set(services);
  }

  private String assembleMetaServiceUrl() {
    String domainName = m_configUtil.getMetaServerDomainName();
    String appId = m_configUtil.getAppId();
    String localIp = m_configUtil.getLocalIp();

    Map<String, String> queryParams = Maps.newHashMap();
    queryParams.put("appId", queryParamEscaper.escape(appId));
    if (!Strings.isNullOrEmpty(localIp)) {
      queryParams.put("ip", queryParamEscaper.escape(localIp));
    }

    return domainName + "/services/config?" + MAP_JOINER.join(queryParams);
  }

  private void logConfigServicesToCat(List<ServiceDTO> serviceDtos) {
    for (ServiceDTO serviceDto : serviceDtos) {
      logConfigServiceToCat(serviceDto.getHomepageUrl());
    }
  }

  private void logConfigServiceToCat(String serviceUrl) {
    Tracer.logEvent("Apollo.Config.Services", serviceUrl);
  }
}
