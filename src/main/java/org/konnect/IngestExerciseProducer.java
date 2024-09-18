package org.konnect;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.konnect.avro.NodeEvent;
import org.konnect.avro.RouteEvent;
import org.konnect.avro.ServiceEvent;
import org.konnect.utils.KafkaUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestExerciseProducer {

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

    String filePath = "./stream.jsonl";
    KafkaUtils kafkaUtils = null;
    try {
      String line;
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      kafkaUtils = new KafkaUtils(props);
      while ((line = reader.readLine()) != null) {
        try {
          ObjectMapper mapper = new ObjectMapper();
          mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
          mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
          mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
          Map eventData = mapper.readValue(line, Map.class);
          String eventKey = ((LinkedHashMap) eventData.get("after")).get("key").toString();
          String eventValue = mapper.writeValueAsString(((LinkedHashMap) ((LinkedHashMap)
              eventData.get("after")).get("value")).get("object"));
          String eventType = extractEventType(eventKey);
          ServiceEvent serviceEvent; RouteEvent routeEvent; NodeEvent nodeEvent;


          switch (eventType) {
            case "service" -> {
              serviceEvent = mapper.readValue(eventValue, ServiceEvent.class);
              serviceEvent.setKonnectEntity(eventType);
              kafkaUtils.produceEvent(eventType + ":" + serviceEvent.getId(), serviceEvent, false);
            }
            case "node" -> {
              nodeEvent = mapper.readValue(eventValue, NodeEvent.class);
              nodeEvent.setKonnectEntity(eventType);
              kafkaUtils.produceEvent(eventType + ":" + nodeEvent.getId(), nodeEvent, false);
            }
            case "route" -> {
              routeEvent = mapper.readValue(eventValue, RouteEvent.class);
              routeEvent.setKonnectEntity(eventType);
              kafkaUtils.produceEvent(eventType + ":" + routeEvent.getId(), routeEvent, false);
            }
          }
        } catch (Exception ex) {
          System.err.printf("Some error occurred while processing line - %s", line);
          saveLineToErrorFile(line);
        }
      }

    } catch (IOException e) {
      System.err.printf("Error reading file %s due to %s %n", filePath, e);
    } finally {
      kafkaUtils.shutdown();
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

  private static void saveLineToErrorFile(String line) throws IOException {
    String filePath = "./stream" +
        new Date() +
        ".jsonl";

    BufferedWriter writer = new BufferedWriter((new FileWriter(filePath)));
    writer.write(line);
    writer.newLine();
    writer.close();
  }

}


