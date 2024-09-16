package org.konnect;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.*;
import org.konnect.schemas.CdcKafkaEventValue;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

public class IngestExerciseConsumer {

  public static void main(String[] args) throws Exception {
    final Properties props = IngestExerciseProducer.loadProperties("configuration/dev.properties");
    final String topic = "cdc-events";

    Consumer<String, String> consumer = new KafkaConsumer<>(props);
    System.out.println("consumer started");

    try (consumer) {
      consumer.subscribe(Collections.singletonList(topic));
      RestHighLevelClient openSearchClient = new RestHighLevelClient(
          RestClient.builder(new HttpHost("localhost", 9200, "http"))
      );
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          String data = record.value();
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> jsonMap = objectMapper.readValue(data, Map.class);

          System.out.printf("Consuming JSON record with key %s and value %s%n", record.key(), record.value());
//          CdcKafkaEventValue data = objectMapper.readValue(record.value(), CdcKafkaEventValue.class);
          IndexRequest request = new IndexRequest("cdc")
              .source(jsonMap);
          IndexResponse response = openSearchClient.index(request, RequestOptions.DEFAULT);
          // Convert JSON string to Java object

          System.out.println("Indexed document with ID: " + response.getId());
          System.out.printf("Consumed JSON record with key %s and value %s%n", record.key(), data);
        }
      }
    }
  }

}

