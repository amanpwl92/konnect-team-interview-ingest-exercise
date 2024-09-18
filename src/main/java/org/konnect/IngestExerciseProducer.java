package org.konnect;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.konnect.avro.NodeEvent;
import org.konnect.avro.RouteEvent;
import org.konnect.avro.ServiceEvent;
import org.konnect.enums.CdcTopics;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestExerciseProducer {

  private final Producer<String, ServiceEvent> serviceEventProducer;
  private final Producer<String, NodeEvent> nodeEventProducer;
  private final Producer<String, RouteEvent> routeEventProducer;
  final String serviceOutTopic = CdcTopics.CDC_SERVICE.toString();
  final String routeOutTopic = CdcTopics.CDC_ROUTE.toString();
  final String nodeOutTopic = CdcTopics.CDC_NODE.toString();

  public IngestExerciseProducer(final Producer<String, ServiceEvent> serviceEventProducer,
                                final Producer<String, NodeEvent> nodeEventProducer,
                                final Producer<String, RouteEvent> routeEventProducer) {
    this.serviceEventProducer = serviceEventProducer;
    this.nodeEventProducer = nodeEventProducer;
    this.routeEventProducer = routeEventProducer;
  }

  public void produceServiceEvent(String key, ServiceEvent event) {
    try {
      final ProducerRecord<String, ServiceEvent> producerRecord = new ProducerRecord<>(serviceOutTopic, key, event);
      serviceEventProducer.send(producerRecord);
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public void produceNodeEvent(String key, NodeEvent event) {
    try {
      final ProducerRecord<String, NodeEvent> producerRecord = new ProducerRecord<>(nodeOutTopic, key, event);
      nodeEventProducer.send(producerRecord);
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public void produceRouteEvent(String key, RouteEvent event) {
    try {
      final ProducerRecord<String, RouteEvent> producerRecord = new ProducerRecord<>(routeOutTopic, key, event);
      routeEventProducer.send(producerRecord);
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public void shutdown() {
    serviceEventProducer.close();
    routeEventProducer.close();
    nodeEventProducer.close();
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
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    final Producer<String, ServiceEvent> serviceEventProducer = new KafkaProducer<>(props);
    final Producer<String, RouteEvent> routeEventProducer = new KafkaProducer<>(props);
    final Producer<String, NodeEvent> nodeEventProducer = new KafkaProducer<>(props);
    final IngestExerciseProducer producerApp = new IngestExerciseProducer(serviceEventProducer, nodeEventProducer,
        routeEventProducer);

    String filePath = "./stream.jsonl";
    try {
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
          ServiceEvent serviceEvent;
          RouteEvent routeEvent;
          NodeEvent nodeEvent;

          switch (eventType) {
            case "service" -> {
              serviceEvent = mapper.readValue(eventValue, ServiceEvent.class);
              serviceEvent.setKonnectEntity(eventType);
              producerApp.produceServiceEvent(eventType + ":" + serviceEvent.getId(), serviceEvent);
            }
            case "node" -> {
              nodeEvent = mapper.readValue(eventValue, NodeEvent.class);
              nodeEvent.setKonnectEntity(eventType);
              producerApp.produceNodeEvent(eventType + ":" + nodeEvent.getId(), nodeEvent);
            }
            case "route" -> {
              routeEvent = mapper.readValue(eventValue, RouteEvent.class);
              routeEvent.setKonnectEntity(eventType);
              producerApp.produceRouteEvent(eventType + ":" + routeEvent.getId(), routeEvent);
            }
          }
        } catch (Exception ex) {
          System.err.print("Event not mapped to object");
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


