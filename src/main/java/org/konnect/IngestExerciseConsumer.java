package org.konnect;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.*;
import org.konnect.enums.CdcTopics;
import org.konnect.utils.KafkaUtils;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

public class IngestExerciseConsumer {

  static Map<String, Long> updatedAtMap = new HashMap<>();
  private static RestHighLevelClient openSearchClient;
  private static ObjectMapper objectMapper;
  private static KafkaUtils kafkaUtils;

  private static void createObjectMapper() {
    objectMapper = new ObjectMapper();
  }

  private static void createKafkaUtils(Properties props) {
    kafkaUtils = new KafkaUtils(props);
  }

  private static void createOpensearchClient(Properties props) {
    openSearchClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost(props.getProperty("opensearch.host"),
            Integer.parseInt(props.getProperty("opensearch.port")),
            props.getProperty("opensearch.scheme")))
    );
  }
  public static void main(String[] args) throws Exception {
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");

    Consumer<String, Object> consumer = new KafkaConsumer<>(props);
    createObjectMapper();
    createOpensearchClient(props);
    createKafkaUtils(props);
    System.out.println("consumer started");


    try {
      consumer.subscribe(CdcTopics.getAllTopics());
      while (true) {
        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, Object> record : records) {
          try {
            System.out.printf("Consuming event with key %s and value %s%n", record.key(), record.value());
            Object data = record.value();
            String key = record.key();
            Map<String, Object> jsonMap = objectMapper.readValue(data.toString(), Map.class);

            if (isOlderUpdate(jsonMap)) {
              // log why not processing
              System.out.println("not processing older update");
              continue;
            }

            sendDataToOpensearch(key, jsonMap);
            System.out.printf("Consumed event with key %s and value %s%n", key, data);
          } catch (Exception ex) {
            System.err.printf("Some error occurred while processing event with key %s and value %s %n",
                record.key(), record.value());
            sendEventToErrorTopic(record.key(), record.value());
          }
        }
      }
    } finally {
      consumer.close();
      kafkaUtils.shutdown();
    }
  }

  public static boolean isOlderUpdate(Map<String, Object> event) {
    String eventId = event.get("id").toString();
    int currentUpdatedAt = (Integer) event.get("updated_at");
    return updatedAtMap.containsKey(eventId) && updatedAtMap.get(eventId) > currentUpdatedAt;
  }

  private static void sendDataToOpensearch(String key, Map<String, Object> jsonMap) throws IOException {
    IndexRequest request = new IndexRequest("cdc")
        .id(key.split(":")[1])
        .source(jsonMap);
    openSearchClient.index(request, RequestOptions.DEFAULT);
  }

  private static void sendEventToErrorTopic(String key, Object value) {
    kafkaUtils.produceEvent(key, value, true);
  }

}

