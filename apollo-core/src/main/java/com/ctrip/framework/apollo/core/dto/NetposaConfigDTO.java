package com.ctrip.framework.apollo.core.dto;

import java.util.List;


public class NetposaConfigDTO {

  private String appId;

  private List<ConfigDTO> data;


  public NetposaConfigDTO(String appId, List<ConfigDTO> data) {
    this.appId = appId;
    this.data = data;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public List<ConfigDTO> getData() {
    return data;
  }

  public void setData(List<ConfigDTO> data) {
    this.data = data;
  }
}
