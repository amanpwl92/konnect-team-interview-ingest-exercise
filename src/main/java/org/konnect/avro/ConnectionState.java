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
public class ConnectionState extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1663098338354965827L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ConnectionState\",\"namespace\":\"org.konnect.avro\",\"fields\":[{\"name\":\"is_connected\",\"type\":\"boolean\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ConnectionState> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ConnectionState> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ConnectionState> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ConnectionState> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ConnectionState> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ConnectionState to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ConnectionState from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ConnectionState instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ConnectionState fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private boolean is_connected;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ConnectionState() {}

  /**
   * All-args constructor.
   * @param is_connected The new value for is_connected
   */
  public ConnectionState(java.lang.Boolean is_connected) {
    this.is_connected = is_connected;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return is_connected;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: is_connected = (java.lang.Boolean)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'is_connected' field.
   * @return The value of the 'is_connected' field.
   */
  public boolean getIsConnected() {
    return is_connected;
  }


  /**
   * Sets the value of the 'is_connected' field.
   * @param value the value to set.
   */
  public void setIsConnected(boolean value) {
    this.is_connected = value;
  }

  /**
   * Creates a new ConnectionState RecordBuilder.
   * @return A new ConnectionState RecordBuilder
   */
  public static org.konnect.avro.ConnectionState.Builder newBuilder() {
    return new org.konnect.avro.ConnectionState.Builder();
  }

  /**
   * Creates a new ConnectionState RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ConnectionState RecordBuilder
   */
  public static org.konnect.avro.ConnectionState.Builder newBuilder(org.konnect.avro.ConnectionState.Builder other) {
    if (other == null) {
      return new org.konnect.avro.ConnectionState.Builder();
    } else {
      return new org.konnect.avro.ConnectionState.Builder(other);
    }
  }

  /**
   * Creates a new ConnectionState RecordBuilder by copying an existing ConnectionState instance.
   * @param other The existing instance to copy.
   * @return A new ConnectionState RecordBuilder
   */
  public static org.konnect.avro.ConnectionState.Builder newBuilder(org.konnect.avro.ConnectionState other) {
    if (other == null) {
      return new org.konnect.avro.ConnectionState.Builder();
    } else {
      return new org.konnect.avro.ConnectionState.Builder(other);
    }
  }

  /**
   * RecordBuilder for ConnectionState instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ConnectionState>
    implements org.apache.avro.data.RecordBuilder<ConnectionState> {

    private boolean is_connected;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(org.konnect.avro.ConnectionState.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.is_connected)) {
        this.is_connected = data().deepCopy(fields()[0].schema(), other.is_connected);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing ConnectionState instance
     * @param other The existing instance to copy.
     */
    private Builder(org.konnect.avro.ConnectionState other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.is_connected)) {
        this.is_connected = data().deepCopy(fields()[0].schema(), other.is_connected);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'is_connected' field.
      * @return The value.
      */
    public boolean getIsConnected() {
      return is_connected;
    }


    /**
      * Sets the value of the 'is_connected' field.
      * @param value The value of 'is_connected'.
      * @return This builder.
      */
    public org.konnect.avro.ConnectionState.Builder setIsConnected(boolean value) {
      validate(fields()[0], value);
      this.is_connected = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'is_connected' field has been set.
      * @return True if the 'is_connected' field has been set, false otherwise.
      */
    public boolean hasIsConnected() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'is_connected' field.
      * @return This builder.
      */
    public org.konnect.avro.ConnectionState.Builder clearIsConnected() {
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConnectionState build() {
      try {
        ConnectionState record = new ConnectionState();
        record.is_connected = fieldSetFlags()[0] ? this.is_connected : (java.lang.Boolean) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ConnectionState>
    WRITER$ = (org.apache.avro.io.DatumWriter<ConnectionState>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ConnectionState>
    READER$ = (org.apache.avro.io.DatumReader<ConnectionState>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeBoolean(this.is_connected);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.is_connected = in.readBoolean();

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.is_connected = in.readBoolean();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










