package org.konnect;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.konnect.avro.NodeEvent;
import org.konnect.avro.RouteEvent;
import org.konnect.avro.ServiceEvent;
import org.konnect.utils.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestExerciseProducer {

  private static final Logger logger = LoggerFactory.getLogger(IngestExerciseProducer.class);
  private static ObjectMapper mapper;
  private static KafkaUtils kafkaUtils;

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

  private static void createObjectMapper() {
      mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
      mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }

  private static void createKafkaUtils(Properties props) {
    kafkaUtils = new KafkaUtils(props);
  }

  public static void main(String[] args) throws Exception {
    logger.info("Producer started");
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    String filePath = "./stream.jsonl";
    String errorFilePath = "./stream-error-" +
        new Date().getTime() +
        ".jsonl";
    logger.info("Error file path is {}", errorFilePath);
    logger.info("Producer setup for object mapper, kafka utils.");
    createObjectMapper();
    createKafkaUtils(props);
    logger.info("Producer setup for object mapper, kafka utils done.");

    try {
      String line;
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      while ((line = reader.readLine()) != null) {
        try {
          logger.info("Reading line {} from file.", line);
          Map eventData = mapper.readValue(line, Map.class);
          String eventKey = ((LinkedHashMap) eventData.get("after")).get("key").toString();
          String eventValue = mapper.writeValueAsString(((LinkedHashMap) ((LinkedHashMap)
              eventData.get("after")).get("value")).get("object"));
          logger.info("Sending cdc event to kafka.");
          produceCdcEvents(eventKey, eventValue);

        } catch (Exception ex) {
          logger.info("Some error occurred while processing line - {}. Error details - {}. Saving line to" +
              "error file.", line, ex);
          saveLineToErrorFile(line, errorFilePath);
        }
      }

    } catch (IOException e) {
      logger.error("Error reading file {} due to {}", filePath, e);
    } finally {
      logger.info("Shutting down resources.");
      kafkaUtils.shutdown();
    }
  }

  private static void produceCdcEvents(String eventKey, String eventValue) throws JsonProcessingException {
    logger.info("In method produceCdcEvents, with event key {} and event value {}.", eventKey, eventValue);
    String eventType = extractEventType(eventKey);
    ServiceEvent serviceEvent; RouteEvent routeEvent; NodeEvent nodeEvent;

    switch (eventType) {
      case "service" -> {
        logger.info("Got cdc event for service entity.");
        serviceEvent = mapper.readValue(eventValue, ServiceEvent.class);
        serviceEvent.setKonnectEntity(eventType);
        kafkaUtils.produceEvent(eventType + ":" + serviceEvent.getId(), serviceEvent, false);
        logger.info("Cdc event for service entity sent to kafka successfully.");
      }
      case "node" -> {
        logger.info("Got cdc event for node entity.");
        nodeEvent = mapper.readValue(eventValue, NodeEvent.class);
        nodeEvent.setKonnectEntity(eventType);
        kafkaUtils.produceEvent(eventType + ":" + nodeEvent.getId(), nodeEvent, false);
        logger.info("Cdc event for node entity sent to kafka successfully.");
      }
      case "route" -> {
        logger.info("Got cdc event for route entity.");
        routeEvent = mapper.readValue(eventValue, RouteEvent.class);
        routeEvent.setKonnectEntity(eventType);
        kafkaUtils.produceEvent(eventType + ":" + routeEvent.getId(), routeEvent, false);
        logger.info("Cdc event for route entity sent to kafka successfully.");
      }
    }
  }

  private static String extractEventType(String key) {
    // Extract the type from the "key" (e.g., "node", "route", "service")
    String[] parts = key.split("/o/");
    if (parts.length > 1) {
      String[] eventParts = parts[1].split("/");
      return eventParts[0]; // node, route, service
    }
    logger.error("Could not extract event type from key {}.", key);
    throw new RuntimeException("Could not extract event type from key: " + key);
  }

  private static void saveLineToErrorFile(String line, String errorFilePath) throws IOException {
    logger.info("In method saveLineToErrorFile, saving line {} to file {}", line, errorFilePath);
    BufferedWriter writer = new BufferedWriter((new FileWriter(errorFilePath, true)));
    writer.write(line);
    writer.newLine();
    writer.close();
    logger.info("Line saved to error file.");
  }

}


