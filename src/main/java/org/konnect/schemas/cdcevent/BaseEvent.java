package org.konnect.schemas.cdcevent;

public class BaseEvent {
  private String id;
  private String konnectEntity;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKonnectEntity() {
    return konnectEntity;
  }

  public void setKonnectEntity(String konnectEntity) {
    this.konnectEntity = konnectEntity;
  }
}
