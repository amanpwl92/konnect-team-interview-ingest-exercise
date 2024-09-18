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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestExerciseConsumer {

  private static final Logger logger = LoggerFactory.getLogger(IngestExerciseConsumer.class);
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
    logger.info("Consumer started.");
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    Consumer<String, Object> consumer = new KafkaConsumer<>(props);

    logger.info("Starting consumer setup for object mapper, open search client, kafka utils.");
    createObjectMapper();
    createOpensearchClient(props);
    createKafkaUtils(props);
    logger.info("Consumer setup for object mapper, open search client, kafka utils done.");

    try {
      List<String> inputTopcis = CdcTopics.getAllTopics();
      logger.info("Consumer subscribing to topics - {}", inputTopcis);
      consumer.subscribe(inputTopcis);
      logger.info("Consumer has subscribed to topics - {}", inputTopcis);
      while (true) {
        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, Object> record : records) {
          try {
            logger.info("Consuming event with key {} and value {}", record.key(), record.value());
            Object data = record.value();
            String key = record.key();
            Map<String, Object> jsonMap = objectMapper.readValue(data.toString(), Map.class);

            if (isOlderUpdate(jsonMap)) {
              logger.info("Not processing older update as we have already processed update after this event.");
              continue;
            }

            logger.info("Sending data to opensearch.");
            sendDataToOpensearch(key, jsonMap);
            logger.info("Consumed event with key {} and value {}", key, data);
          } catch (Exception ex) {
            logger.error("Some error occurred while processing event with key {} and value {}. Will send event to " +
                    "retry topic.",
                record.key(), record.value());
            sendEventToErrorTopic(record.key(), record.value());
          }
        }
      }
    } finally {
      logger.info("Shutting down resources.");
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
    logger.info("In method sendDataToOpensearch, with key {} and json map {}", key, jsonMap);
    IndexRequest request = new IndexRequest("cdc")
        .id(key.split(":")[1])
        .source(jsonMap);
    openSearchClient.index(request, RequestOptions.DEFAULT);
    logger.info("Data sent to opensearch with id {} successfully", key.split(":")[1]);
  }

  private static void sendEventToErrorTopic(String key, Object value) {
    logger.info("In method sendEventToErrorTopic, with key {} and object {}", key, value);
    kafkaUtils.produceEvent(key, value, true);
    logger.info("Event sent to retry topic successfully.");
  }

}

