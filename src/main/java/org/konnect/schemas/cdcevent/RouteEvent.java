package org.konnect.schemas.cdcevent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RouteEvent extends BaseEvent {

  private String name;

  private List<String> paths;
  @JsonProperty("service_id")
  private String serviceId;
  private List<String> methods;
  private boolean enabled;
  @JsonProperty("created_at")
  private long createdAt;
  @JsonProperty("updated_at")
  private long updatedAt;

  private List<String> protocols;

  @JsonProperty("strip_path")
  private boolean stripPath;

  @JsonProperty("path_handling")
  private String pathHandling;

  @JsonProperty("preserve_host")
  private boolean preserveHost;

  @JsonProperty("regex_priority")
  private int regexPriority;

  @JsonProperty("request_buffering")
  private boolean requestBuffering;

  @JsonProperty("response_buffering")
  private boolean responseBuffering;

  @JsonProperty("https_redirect_status_code")
  private int httpsRedirectStatusCode;

  private Service service;

  private static class Service {
    private String id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }

  public List<String> getProtocols() {
    return protocols;
  }

  public void setProtocols(List<String> protocols) {
    this.protocols = protocols;
  }

  public boolean isStripPath() {
    return stripPath;
  }

  public void setStripPath(boolean stripPath) {
    this.stripPath = stripPath;
  }

  public String getPathHandling() {
    return pathHandling;
  }

  public void setPathHandling(String pathHandling) {
    this.pathHandling = pathHandling;
  }

  public boolean isPreserveHost() {
    return preserveHost;
  }

  public void setPreserveHost(boolean preserveHost) {
    this.preserveHost = preserveHost;
  }

  public int getRegexPriority() {
    return regexPriority;
  }

  public void setRegexPriority(int regexPriority) {
    this.regexPriority = regexPriority;
  }

  public boolean isRequestBuffering() {
    return requestBuffering;
  }

  public void setRequestBuffering(boolean requestBuffering) {
    this.requestBuffering = requestBuffering;
  }

  public boolean isResponseBuffering() {
    return responseBuffering;
  }

  public void setResponseBuffering(boolean responseBuffering) {
    this.responseBuffering = responseBuffering;
  }

  public int getHttpsRedirectStatusCode() {
    return httpsRedirectStatusCode;
  }

  public void setHttpsRedirectStatusCode(int httpsRedirectStatusCode) {
    this.httpsRedirectStatusCode = httpsRedirectStatusCode;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public List<String> getMethods() {
    return methods;
  }

  public void setMethods(List<String> methods) {
    this.methods = methods;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }
}

