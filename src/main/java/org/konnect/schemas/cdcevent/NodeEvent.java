package org.konnect.schemas.cdcevent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class NodeEvent extends BaseEvent {
  private String type;
  private Map<String, String> labels;
  private String version;
  private String hostname;
  @JsonProperty("last_ping")
  private long lastPing;
  @JsonProperty("created_at")
  private long createdAt;
  @JsonProperty("updated_at")
  private long updatedAt;
  @JsonProperty("config_hash")
  private String configHash;

  @JsonProperty("process_conf")
  private ProcessConfiguration processConfiguration;

  @JsonProperty("data_plane_cert_id")
  private String dataPlaneCertId;

  @JsonProperty("connection_state")
  private ConnectionState connectionState;

  public String getDataPlaneCertId() {
    return dataPlaneCertId;
  }

  public void setDataPlaneCertId(String dataPlaneCertId) {
    this.dataPlaneCertId = dataPlaneCertId;
  }

  public ConnectionState getConnectionState() {
    return connectionState;
  }

  public void setConnectionState(ConnectionState connectionState) {
    this.connectionState = connectionState;
  }

  public ProcessConfiguration getProcessConfiguration() {
    return processConfiguration;
  }

  public void setProcessConfiguration(ProcessConfiguration processConfiguration) {
    this.processConfiguration = processConfiguration;
  }

  private static class ProcessConfiguration {
    private List<String> plugins;

    @JsonProperty("lmdb_map_size")
    private String lmdbMapSize;

    @JsonProperty("router_flavor")
    private String routerFlavor;

    @JsonProperty("cluster_max_payload")
    private long clusterMaxPayload;

    public List<String> getPlugins() {
      return plugins;
    }

    public void setPlugins(List<String> plugins) {
      this.plugins = plugins;
    }

    public String getLmdbMapSize() {
      return lmdbMapSize;
    }

    public void setLmdbMapSize(String lmdbMapSize) {
      this.lmdbMapSize = lmdbMapSize;
    }

    public String getRouterFlavor() {
      return routerFlavor;
    }

    public void setRouterFlavor(String routerFlavor) {
      this.routerFlavor = routerFlavor;
    }

    public long getClusterMaxPayload() {
      return clusterMaxPayload;
    }

    public void setClusterMaxPayload(long clusterMaxPayload) {
      this.clusterMaxPayload = clusterMaxPayload;
    }
  }

  private static class ConnectionState {
    @JsonProperty("is_connected")
    private boolean isConnected;

    public boolean isConnected() {
      return isConnected;
    }

    public void setConnected(boolean connected) {
      isConnected = connected;
    }
  }

  // Getters and setters for each field

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public void setLabels(Map<String, String> labels) {
    this.labels = labels;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public long getLastPing() {
    return lastPing;
  }

  public void setLastPing(long lastPing) {
    this.lastPing = lastPing;
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

  public String getConfigHash() {
    return configHash;
  }

  public void setConfigHash(String configHash) {
    this.configHash = configHash;
  }
}


