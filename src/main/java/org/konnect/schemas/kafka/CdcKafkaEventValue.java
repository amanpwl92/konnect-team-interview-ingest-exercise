package org.konnect.schemas.kafka;

import org.konnect.schemas.CdcEventValue;

public class CdcKafkaEventValue extends CdcEventValue {
  public String op;
  public long ts_ms;
}
