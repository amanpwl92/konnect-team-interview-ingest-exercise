package org.konnect.schemas.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CDCEventWrapper {
  private Event before;

  private Event after;
  private String op;
  @JsonProperty("ts_ms")
  private long tsMs;

  // Getters and setters
  public Event getBefore() { return before; }
  public void setBefore(Event before) { this.before = before; }

  public Event getAfter() { return after; }
  public void setAfter(Event after) { this.after = after; }

  public String getOp() { return op; }
  public void setOp(String op) { this.op = op; }

  public long getTsMs() { return tsMs; }
  public void setTsMs(long tsMs) { this.tsMs = tsMs; }
}

