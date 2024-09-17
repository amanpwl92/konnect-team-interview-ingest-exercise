package org.konnect;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.avro.util.Utf8;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.*;
import org.konnect.avro.ServiceEvent1;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.mapper.DynamicTemplate;

public class IngestExerciseConsumer {

  public static void main(String[] args) throws Exception {
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    final String topic = "cdc-events";

    Consumer<String, ServiceEvent1> consumer = new KafkaConsumer<>(props);
    System.out.println("consumer started");

    try (consumer) {
      consumer.subscribe(Collections.singletonList(topic));
      RestHighLevelClient openSearchClient = new RestHighLevelClient(
          RestClient.builder(new HttpHost("localhost", 9200, "http"))
      );
      while (true) {
        ConsumerRecords<String, ServiceEvent1> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, ServiceEvent1> record : records) {
          Object data = record.value();
          String key = record.key();
          String eventType = key.split(":")[0];
//          ObjectMapper objectMapper = new ObjectMapper();
//          String json = objectMapper.writeValueAsString(data);
//          ObjectMapper objectMapper = new ObjectMapper();
//          Map<String, Object> jsonMap = objectMapper.convertValue(data, Map.class);
//          BaseEvent event = null;
//
//          if(eventType.equals("service")) {
//            event = objectMapper.readValue(data, ServiceEvent.class);
//          } else if (eventType.equals("node")) {
//            event = objectMapper.readValue(data, NodeEvent.class);
//          } else if (eventType.equals("route")) {
//            event = objectMapper.readValue(data, RouteEvent.class);
//          }
//
//          if (event == null) {
//            continue;
//          }
          Map<String, Object> map = convert(data);
          System.out.printf("Consuming JSON record with key %s and value %s%n", record.key(), record.value());
          IndexRequest request = new IndexRequest("cdc")
              .id(key.split(":")[1])
              .source(map);
          IndexResponse response = openSearchClient.index(request, RequestOptions.DEFAULT);
          // Convert JSON string to Java object

//          System.out.println("Indexed document with ID: " + response.getId());
          System.out.printf("Consumed JSON record with key %s and value %s%n", record.key(), data);
        }
      }
    }
  }

  public static Map<String, Object> convert(Object obj) {
    Map<String, Object> map = new HashMap<>();
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true); // Allow access to private fields

      if (shouldIncludeField(field)) {
        try {
          Object value = field.get(obj);
          // Convert to standard Java types
          map.put(field.getName(), convertValue(value));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return map;
  }

  private static boolean shouldIncludeField(Field field) {
    // Exclude specific fields based on type or name
    // Add more conditions as necessary
    return !( // Example for excluding specific types
        field.getName().equalsIgnoreCase("serialVersionUID") ||          // Exclude by name
        field.getName().equalsIgnoreCase("SCHEMA$") ||
            field.getName().equalsIgnoreCase("MODEL$") ||          // Exclude by name
            field.getName().equalsIgnoreCase("ENCODER") ||
            field.getName().equalsIgnoreCase("DECODER") ||          // Exclude by name
            field.getName().equalsIgnoreCase("WRITER$") ||
            field.getName().equalsIgnoreCase("READER$"));
  }

  private static Object convertValue(Object value) {
    if (value instanceof Utf8) {
      return value.toString(); // Convert Utf8 to String
    } else if (value instanceof List) {
      // Convert List of items
      return ((List<?>) value).stream()
          .map(IngestExerciseConsumer::convertValue) // Recursively convert each item
          .toList();
    } else if (value instanceof Collection) {
      // Convert Collection of items
      return ((Collection<?>) value).stream()
          .map(IngestExerciseConsumer::convertValue) // Recursively convert each item
          .toList();
    } else if (value != null && isCustomAvroType(value.getClass())) {
      // Convert custom Avro types to Map
      return convertAvroObjectToMap(value);
    }
    return value; // Return as is if no conversion is needed
  }

  // Check if the class is a custom Avro type that needs conversion
  private static boolean isCustomAvroType(Class<?> clazz) {
    // Define the logic to check for custom Avro types
    // For example, you can check if it's a generated Avro class
    return false; // Modify this condition as needed
  }

  // Convert an Avro object to a Map (basic example)
  private static Map<String, Object> convertAvroObjectToMap(Object avroObject) {
    Map<String, Object> avroMap = new HashMap<>();
    // Perform conversion logic based on your Avro schema
    // Example: Use reflection to get fields and their values
    // Similar to how convertValue works but for Avro objects
    return avroMap;
  }

}

