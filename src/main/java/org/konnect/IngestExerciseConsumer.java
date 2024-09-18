package org.konnect;

import java.time.Duration;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.*;
import org.konnect.enums.CdcTopics;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

public class IngestExerciseConsumer {

  static Map<String, Long> updatedAtMap = new HashMap<>();
  public static void main(String[] args) throws Exception {
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");

    Consumer<String, Object> consumer = new KafkaConsumer<>(props);

    System.out.println("consumer started");

    try (consumer) {
      consumer.subscribe(CdcTopics.getAllTopics());
      RestHighLevelClient openSearchClient = new RestHighLevelClient(
          RestClient.builder(new HttpHost("localhost", 9200, "http"))
      );
      while (true) {
        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, Object> record : records) {
          System.out.printf("Consuming JSON record with key %s and value %s%n", record.key(), record.value());
          Object data = record.value();
          String key = record.key();
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> jsonMap = objectMapper.readValue(data.toString(), Map.class);

          if(isOlderUpdate(jsonMap)) {
            // log why not processing
            System.out.println("not processing older update");
            continue;
          }

          IndexRequest request = new IndexRequest("cdc")
              .id(key.split(":")[1])
              .source(jsonMap);
          IndexResponse response = openSearchClient.index(request, RequestOptions.DEFAULT);
          System.out.printf("Consumed JSON record with key %s and value %s%n", key, data);
        }
      }
    }
  }

  public static boolean isOlderUpdate(Map<String, Object> event) {
    String eventId = event.get("id").toString();
    int currentUpdatedAt = (Integer) event.get("updated_at");
    return updatedAtMap.containsKey(eventId) && updatedAtMap.get(eventId) > currentUpdatedAt;
  }

}

