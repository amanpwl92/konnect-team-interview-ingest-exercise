package org.konnect.schemas;

public class CdcKafkaEventValue extends CdcEventValue{
  public String op;
  public long ts_ms;
}
