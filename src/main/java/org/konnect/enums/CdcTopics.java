package org.konnect.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CdcTopics {
  CDC_SERVICE,
  CDC_ROUTE,
  CDC_NODE;

  @Override
  public String toString() {
    return name().replace('_', '-').toLowerCase();
  }

  public static List<String> getAllTopics() {
    return Arrays.stream(CdcTopics.values())
        .map(Enum::toString)
        .collect(Collectors.toList());
  }
}
