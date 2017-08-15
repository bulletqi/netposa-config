package com.ctrip.framework.apollo.core.dto;

import java.util.Map;

public class ConfigDTO {

  private String namespaceName;

  private Map<String, String> configurations;

  private String releaseKey;


  public ConfigDTO(String namespaceName, Map<String, String> configurations, String releaseKey) {
    this.namespaceName = namespaceName;
    this.configurations = configurations;
    this.releaseKey = releaseKey;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }

  public Map<String, String> getConfigurations() {
    return configurations;
  }

  public void setConfigurations(Map<String, String> configurations) {
    this.configurations = configurations;
  }

  public String getReleaseKey() {
    return releaseKey;
  }

  public void setReleaseKey(String releaseKey) {
    this.releaseKey = releaseKey;
  }
}
