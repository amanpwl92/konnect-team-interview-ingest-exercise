package org.konnect;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.konnect.avro.LongDeserializer;
import org.konnect.avro.ServiceEvent1;
import org.konnect.schemas.cdcevent.BaseEvent;
import org.konnect.schemas.cdcevent.NodeEvent;
import org.konnect.schemas.cdcevent.RouteEvent;
import org.konnect.schemas.cdcevent.ServiceEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestExerciseProducer {

  private final Producer<String, ServiceEvent1> producer;
  final String outTopic;

  public IngestExerciseProducer(final Producer<String, ServiceEvent1> producer,
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

//  public void produce(String key, BaseEvent event) {
//      try {
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(event);
//        final ProducerRecord<String, BaseEvent> producerRecord = new ProducerRecord<>(outTopic, key, event);
//        producer.send(producerRecord);
//      } catch (JsonProcessingException ex) {
//        System.out.println(ex);
//      }
//    }

  public void produce1(String key, ServiceEvent1 event) {
    try {
//      ObjectMapper mapper = new ObjectMapper();
//      String jsonString = mapper.writeValueAsString(event);
      final ProducerRecord<String, ServiceEvent1> producerRecord = new ProducerRecord<>(outTopic, key, event);
      producer.send(producerRecord);
    } catch (Exception ex) {
      System.out.println(ex);
    }
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
    final Producer<String, ServiceEvent1> producer = new KafkaProducer<>(props);
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
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
      mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      while ((line = reader.readLine()) != null) {
        try {
          Map<String, Object> eventData = mapper.readValue(line, Map.class);
          String eventKey = ((LinkedHashMap)eventData.get("after")).get("key").toString();
          String eventValue = mapper.writeValueAsString(((LinkedHashMap)((LinkedHashMap)
              eventData.get("after")).get("value")).get("object"));
          String eventType = extractEventType(eventKey);
          BaseEvent event = null;
          ServiceEvent1 serviceEvent1 = null;


          if(eventType.equals("service")) {
            serviceEvent1 = mapper.readValue(eventValue, ServiceEvent1.class);
          }
//          else if (eventType.equals("node")) {
//            event = mapper.readValue(eventValue, NodeEvent.class);
//          } else if (eventType.equals("route")) {
//            event = mapper.readValue(eventValue, RouteEvent.class);
//          }

          if(serviceEvent1 == null) {
            continue;
          }

          serviceEvent1.setKonnectEntity(eventType);
          producerApp.produce1(eventType + ":" + serviceEvent1.getId(), serviceEvent1);
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

  private static String extractEventType(String key) {
    // Extract the type from the "key" (e.g., "node", "route", "service")
    String[] parts = key.split("/o/");
    if (parts.length > 1) {
      String[] eventParts = parts[1].split("/");
      return eventParts[0]; // node, route, service
    }
    throw new RuntimeException("Could not extract event type from key: " + key);
  }
}


