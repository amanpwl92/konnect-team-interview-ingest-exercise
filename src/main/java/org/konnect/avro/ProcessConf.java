/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.konnect.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class ProcessConf extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -8497149327659148537L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ProcessConf\",\"namespace\":\"org.konnect.avro\",\"fields\":[{\"name\":\"plugins\",\"type\":[\"null\",{\"type\":\"array\",\"items\":[\"string\"]}],\"default\":null},{\"name\":\"lmdb_map_size\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"router_flavor\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"cluster_max_payload\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ProcessConf> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ProcessConf> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ProcessConf> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ProcessConf> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ProcessConf> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ProcessConf to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ProcessConf from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ProcessConf instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ProcessConf fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.util.List<java.lang.Object> plugins;
  private java.lang.CharSequence lmdb_map_size;
  private java.lang.CharSequence router_flavor;
  private int cluster_max_payload;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ProcessConf() {}

  /**
   * All-args constructor.
   * @param plugins The new value for plugins
   * @param lmdb_map_size The new value for lmdb_map_size
   * @param router_flavor The new value for router_flavor
   * @param cluster_max_payload The new value for cluster_max_payload
   */
  public ProcessConf(java.util.List<java.lang.Object> plugins, java.lang.CharSequence lmdb_map_size, java.lang.CharSequence router_flavor, java.lang.Integer cluster_max_payload) {
    this.plugins = plugins;
    this.lmdb_map_size = lmdb_map_size;
    this.router_flavor = router_flavor;
    this.cluster_max_payload = cluster_max_payload;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return plugins;
    case 1: return lmdb_map_size;
    case 2: return router_flavor;
    case 3: return cluster_max_payload;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: plugins = (java.util.List<java.lang.Object>)value$; break;
    case 1: lmdb_map_size = (java.lang.CharSequence)value$; break;
    case 2: router_flavor = (java.lang.CharSequence)value$; break;
    case 3: cluster_max_payload = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'plugins' field.
   * @return The value of the 'plugins' field.
   */
  public java.util.List<java.lang.Object> getPlugins() {
    return plugins;
  }


  /**
   * Sets the value of the 'plugins' field.
   * @param value the value to set.
   */
  public void setPlugins(java.util.List<java.lang.Object> value) {
    this.plugins = value;
  }

  /**
   * Gets the value of the 'lmdb_map_size' field.
   * @return The value of the 'lmdb_map_size' field.
   */
  public java.lang.CharSequence getLmdbMapSize() {
    return lmdb_map_size;
  }


  /**
   * Sets the value of the 'lmdb_map_size' field.
   * @param value the value to set.
   */
  public void setLmdbMapSize(java.lang.CharSequence value) {
    this.lmdb_map_size = value;
  }

  /**
   * Gets the value of the 'router_flavor' field.
   * @return The value of the 'router_flavor' field.
   */
  public java.lang.CharSequence getRouterFlavor() {
    return router_flavor;
  }


  /**
   * Sets the value of the 'router_flavor' field.
   * @param value the value to set.
   */
  public void setRouterFlavor(java.lang.CharSequence value) {
    this.router_flavor = value;
  }

  /**
   * Gets the value of the 'cluster_max_payload' field.
   * @return The value of the 'cluster_max_payload' field.
   */
  public int getClusterMaxPayload() {
    return cluster_max_payload;
  }


  /**
   * Sets the value of the 'cluster_max_payload' field.
   * @param value the value to set.
   */
  public void setClusterMaxPayload(int value) {
    this.cluster_max_payload = value;
  }

  /**
   * Creates a new ProcessConf RecordBuilder.
   * @return A new ProcessConf RecordBuilder
   */
  public static org.konnect.avro.ProcessConf.Builder newBuilder() {
    return new org.konnect.avro.ProcessConf.Builder();
  }

  /**
   * Creates a new ProcessConf RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ProcessConf RecordBuilder
   */
  public static org.konnect.avro.ProcessConf.Builder newBuilder(org.konnect.avro.ProcessConf.Builder other) {
    if (other == null) {
      return new org.konnect.avro.ProcessConf.Builder();
    } else {
      return new org.konnect.avro.ProcessConf.Builder(other);
    }
  }

  /**
   * Creates a new ProcessConf RecordBuilder by copying an existing ProcessConf instance.
   * @param other The existing instance to copy.
   * @return A new ProcessConf RecordBuilder
   */
  public static org.konnect.avro.ProcessConf.Builder newBuilder(org.konnect.avro.ProcessConf other) {
    if (other == null) {
      return new org.konnect.avro.ProcessConf.Builder();
    } else {
      return new org.konnect.avro.ProcessConf.Builder(other);
    }
  }

  /**
   * RecordBuilder for ProcessConf instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ProcessConf>
    implements org.apache.avro.data.RecordBuilder<ProcessConf> {

    private java.util.List<java.lang.Object> plugins;
    private java.lang.CharSequence lmdb_map_size;
    private java.lang.CharSequence router_flavor;
    private int cluster_max_payload;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(org.konnect.avro.ProcessConf.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.plugins)) {
        this.plugins = data().deepCopy(fields()[0].schema(), other.plugins);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.lmdb_map_size)) {
        this.lmdb_map_size = data().deepCopy(fields()[1].schema(), other.lmdb_map_size);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.router_flavor)) {
        this.router_flavor = data().deepCopy(fields()[2].schema(), other.router_flavor);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.cluster_max_payload)) {
        this.cluster_max_payload = data().deepCopy(fields()[3].schema(), other.cluster_max_payload);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing ProcessConf instance
     * @param other The existing instance to copy.
     */
    private Builder(org.konnect.avro.ProcessConf other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.plugins)) {
        this.plugins = data().deepCopy(fields()[0].schema(), other.plugins);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lmdb_map_size)) {
        this.lmdb_map_size = data().deepCopy(fields()[1].schema(), other.lmdb_map_size);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.router_flavor)) {
        this.router_flavor = data().deepCopy(fields()[2].schema(), other.router_flavor);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.cluster_max_payload)) {
        this.cluster_max_payload = data().deepCopy(fields()[3].schema(), other.cluster_max_payload);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'plugins' field.
      * @return The value.
      */
    public java.util.List<java.lang.Object> getPlugins() {
      return plugins;
    }


    /**
      * Sets the value of the 'plugins' field.
      * @param value The value of 'plugins'.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder setPlugins(java.util.List<java.lang.Object> value) {
      validate(fields()[0], value);
      this.plugins = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'plugins' field has been set.
      * @return True if the 'plugins' field has been set, false otherwise.
      */
    public boolean hasPlugins() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'plugins' field.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder clearPlugins() {
      plugins = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'lmdb_map_size' field.
      * @return The value.
      */
    public java.lang.CharSequence getLmdbMapSize() {
      return lmdb_map_size;
    }


    /**
      * Sets the value of the 'lmdb_map_size' field.
      * @param value The value of 'lmdb_map_size'.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder setLmdbMapSize(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.lmdb_map_size = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'lmdb_map_size' field has been set.
      * @return True if the 'lmdb_map_size' field has been set, false otherwise.
      */
    public boolean hasLmdbMapSize() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'lmdb_map_size' field.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder clearLmdbMapSize() {
      lmdb_map_size = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'router_flavor' field.
      * @return The value.
      */
    public java.lang.CharSequence getRouterFlavor() {
      return router_flavor;
    }


    /**
      * Sets the value of the 'router_flavor' field.
      * @param value The value of 'router_flavor'.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder setRouterFlavor(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.router_flavor = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'router_flavor' field has been set.
      * @return True if the 'router_flavor' field has been set, false otherwise.
      */
    public boolean hasRouterFlavor() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'router_flavor' field.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder clearRouterFlavor() {
      router_flavor = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'cluster_max_payload' field.
      * @return The value.
      */
    public int getClusterMaxPayload() {
      return cluster_max_payload;
    }


    /**
      * Sets the value of the 'cluster_max_payload' field.
      * @param value The value of 'cluster_max_payload'.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder setClusterMaxPayload(int value) {
      validate(fields()[3], value);
      this.cluster_max_payload = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'cluster_max_payload' field has been set.
      * @return True if the 'cluster_max_payload' field has been set, false otherwise.
      */
    public boolean hasClusterMaxPayload() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'cluster_max_payload' field.
      * @return This builder.
      */
    public org.konnect.avro.ProcessConf.Builder clearClusterMaxPayload() {
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProcessConf build() {
      try {
        ProcessConf record = new ProcessConf();
        record.plugins = fieldSetFlags()[0] ? this.plugins : (java.util.List<java.lang.Object>) defaultValue(fields()[0]);
        record.lmdb_map_size = fieldSetFlags()[1] ? this.lmdb_map_size : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.router_flavor = fieldSetFlags()[2] ? this.router_flavor : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.cluster_max_payload = fieldSetFlags()[3] ? this.cluster_max_payload : (java.lang.Integer) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ProcessConf>
    WRITER$ = (org.apache.avro.io.DatumWriter<ProcessConf>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ProcessConf>
    READER$ = (org.apache.avro.io.DatumReader<ProcessConf>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










