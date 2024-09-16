package org.konnect;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.konnect.avro.ObjectData;
import org.konnect.schemas.CdcEvent;
import org.konnect.schemas.CdcEventValue;
import org.konnect.schemas.CdcKafkaEventValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestExerciseProducer {

  private final Producer<String, String> producer;
  final String outTopic;

  public IngestExerciseProducer(final Producer<String, String> producer,
                                final String topic) {
    this.producer = producer;
    outTopic = topic;
  }

//  public Future<RecordMetadata> produce1(final String message) {
//    String path = "src/main/avro/CdcEvent.avsc";
//    try {
//      Schema schema = new Schema.Parser().parse(new File(path));
//    } catch (IOException ex) {
//      System.err.printf("Error reading file %s due to %s %n", path, ex);
//    }
//    ObjectMapper mapper = new ObjectMapper();
//    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
//    CdcEvent cdcEvent;
//    try {
//      cdcEvent = mapper.readValue(message, CdcEvent.class);
//      EventData eventData = mapper.readValue(message, EventData.class);
//    } catch (JsonProcessingException ex) {
//      System.err.printf("Error %s", ex);
//    }
////     eventData = mapper.readValue(line, EventData.class);
////    final ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<>(outTopic, key, value);
//    final ProducerRecord<String, GenericRecord> producerRecord = null;
//    return producer.send(producerRecord);
//  }

  public void produce(String key, CdcKafkaEventValue cdcKafkaEventValue) {

    if (key.contains("/service/") || key.contains("/node/") || key.contains("/route/")) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(cdcKafkaEventValue);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(outTopic, key, jsonString);
        producer.send(producerRecord);
      } catch (JsonProcessingException ex) {
        System.out.println(ex);
      }
    }

//    KafkaAvroSerializer abc
  }

//  public Future<RecordMetadata> produce(final String message) {
//    final String[] parts = message.split("-");
//    final String key, value;
//    if (parts.length > 1) {
//      key = parts[0];
//      value = parts[1];
//    } else {
//      key = null;
//      value = parts[0];
//    }
////    final ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<>(outTopic, key, value);
//    final ProducerRecord<String, GenericRecord> producerRecord = null;
//    return producer.send(producerRecord);
//  }

  public void shutdown() {
    producer.close();
  }

  public static Properties loadProperties(String fileName) throws IOException {
    final Properties envProps = new Properties();
    final FileInputStream input = new FileInputStream(fileName);
    envProps.load(input);
    input.close();

    return envProps;
  }

  public void printMetadata(final Collection<Future<RecordMetadata>> metadata,
                            final String fileName) {
    System.out.println("Offsets and timestamps committed in batch from " + fileName);
    metadata.forEach(m -> {
      try {
        final RecordMetadata recordMetadata = m.get();
        System.out.println("Record written to offset " + recordMetadata.offset() + " timestamp " + recordMetadata.timestamp());
      } catch (InterruptedException | ExecutionException e) {
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
      }
    });
  }

  public static void main(String[] args) throws Exception {
//    if (args.length < 2) {
//      throw new IllegalArgumentException(
//          "This program takes two arguments: the path to an environment configuration file and" +
//              "the path to the file with records to send");
//    }

//    final Properties props = IngestExerciseProducer.loadProperties(args[0]);
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    final String topic = "cdc-events";
//    final Producer<String, String> producer = new KafkaProducer<>(new HashMap<>());
    final Producer<String, String> producer = new KafkaProducer<>(props);
    final IngestExerciseProducer producerApp = new IngestExerciseProducer(producer, topic);

//    String filePath = args[1];
    String filePath = "./stream.jsonl";
    try {
//      List<String> linesToProduce = Files.readAllLines(Paths.get(filePath));
//      List<Future<RecordMetadata>> metadata = linesToProduce.stream()
//          .filter(l -> !l.trim().isEmpty())
//          .map(producerApp::produce1)
//          .collect(Collectors.toList());
//      producerApp.printMetadata(metadata, filePath);
      String line;
      ObjectMapper mapper = new ObjectMapper();
//      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      while ((line = reader.readLine()) != null) {
        // Parse JSON line into a Map-like structure
//        EventData eventData = mapper.readValue(line, EventData.class);
        try {
          CdcEvent eventData = mapper.readValue(line, CdcEvent.class);
          CdcEventValue eventValue = eventData.after.value;
          CdcKafkaEventValue cdcKafkaEventValue = new CdcKafkaEventValue();
          cdcKafkaEventValue.type = eventValue.type;
          cdcKafkaEventValue.object = eventValue.object;
          cdcKafkaEventValue.op = eventData.op;
          cdcKafkaEventValue.ts_ms = eventData.ts_ms;
          ;
          producerApp.produce(eventData.after.key, cdcKafkaEventValue);
        } catch (Exception ex) {
          System.err.printf("Event not mapped to object");
        }
      }

    } catch (IOException e) {
      System.err.printf("Error reading file %s due to %s %n", filePath, e);
    } finally {
      producerApp.shutdown();
    }
  }
}


