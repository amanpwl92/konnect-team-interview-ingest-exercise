package org.konnect.schemas;

import java.util.ArrayList;

class CdcEventObject{
  public String id;
  public String host;
  public String name;
  public String path;
  public int port;
  public ArrayList<String> tags;
  public boolean enabled;
  public int retries;
  public String protocol;
  public int created_at;
  public int updated_at;
  public int read_timeout;
  public int write_timeout;
  public int connect_timeout;
}
