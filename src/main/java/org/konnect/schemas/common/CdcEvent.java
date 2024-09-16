package org.konnect.schemas.common;

//import org.konnect.schemas.CdcEventObject;


// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class CdcEvent{
//  public CdcEventObject before;
//  public CdcEventAfter after;
  public String op;
  public long ts_ms;
}




