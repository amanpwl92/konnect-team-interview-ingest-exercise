package org.konnect.schemas.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Event {
  private String key;

  @JsonDeserialize(using = EventDeserializer.class)
  private EventValue value;
//  private int type;
//  private String op;
//  private long tsMs;

  // Getters and setters
  public String getKey() { return key; }
  public void setKey(String key) { this.key = key; }

//  public int getType() { return type; }
//  public void setType(int type) { this.type = type; }
//
//  public String getOp() { return op; }
//  public void setOp(String op) { this.op = op; }
//
//  public long getTsMs() { return tsMs; }
//  public void setTsMs(long tsMs) { this.tsMs = tsMs; }

  public EventValue getValue() {
    return value;
  }

  public void setValue(EventValue value) {
    this.value = value;
  }
}

