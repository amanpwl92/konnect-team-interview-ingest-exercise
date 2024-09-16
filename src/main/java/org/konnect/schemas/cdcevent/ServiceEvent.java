package org.konnect.schemas.cdcevent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ServiceEvent extends BaseEvent {
  private String host;
  private String name;

  private String path;
  private int port;
  private boolean enabled;
  private int retries;
  private String protocol;
  @JsonProperty("created_at")
  private long createdAt;
  @JsonProperty("updated_at")
  private long updatedAt;
  @JsonProperty("read_timeout")
  private int readTimeout;
  @JsonProperty("write_timeout")
  private int writeTimeout;
  @JsonProperty("connect_timeout")
  private int connectTimeout;
  private List<String> tags;

  // Getters and setters for each field

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public int getRetries() {
    return retries;
  }

  public void setRetries(int retries) {
    this.retries = retries;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
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

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getWriteTimeout() {
    return writeTimeout;
  }

  public void setWriteTimeout(int writeTimeout) {
    this.writeTimeout = writeTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}


