package org.konnect.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.konnect.avro.NodeEvent;
import org.konnect.avro.RouteEvent;
import org.konnect.avro.ServiceEvent;
import org.konnect.enums.CdcTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaUtils {

  private static final Logger logger = LoggerFactory.getLogger(KafkaUtils.class);
  private final Producer<String, ServiceEvent> serviceEventProducer;
  private final Producer<String, NodeEvent> nodeEventProducer;
  private final Producer<String, RouteEvent> routeEventProducer;
  final String serviceOutTopic = CdcTopics.CDC_SERVICE.toString();
  final String routeOutTopic = CdcTopics.CDC_ROUTE.toString();
  final String nodeOutTopic = CdcTopics.CDC_NODE.toString();

  public KafkaUtils(Properties props) {
    this.nodeEventProducer = new KafkaProducer<>(props);
    this.routeEventProducer = new KafkaProducer<>(props);;
    this.serviceEventProducer = new KafkaProducer<>(props);
  }

  public void produceEvent(String key, Object event, boolean isEventForRetryTopic) {
    logger.info("In method produceEvent, with key {}, event {} and isEventForRetryTopic {}", key, event,
        isEventForRetryTopic);
    ProducerRecord<String, RouteEvent> routeEventProducerRecord;
    ProducerRecord<String, NodeEvent> nodeEventProducerRecord;
    ProducerRecord<String, ServiceEvent> serviceEventProducerRecord;
    String topicName;
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    if (event instanceof ServiceEvent) {
      topicName = isEventForRetryTopic ? serviceOutTopic.concat("-retry") : serviceOutTopic;
      logger.info("Sending cdc event of service entity to topic {}.", topicName);
      serviceEventProducerRecord = new ProducerRecord<>(topicName, key, (ServiceEvent) event);
      serviceEventProducer.send(serviceEventProducerRecord);
    } else if (event instanceof NodeEvent) {
      topicName = isEventForRetryTopic ? nodeOutTopic.concat("-retry") : nodeOutTopic;
      logger.info("Sending cdc event of node entity to topic {}.", topicName);
      nodeEventProducerRecord = new ProducerRecord<>(topicName, key, (NodeEvent) event);
      nodeEventProducer.send(nodeEventProducerRecord);
    } else if (event instanceof RouteEvent) {
      topicName = isEventForRetryTopic ? routeOutTopic.concat("-retry") : routeOutTopic;
      logger.info("Sending cdc event of route entity to topic {}.", topicName);
      routeEventProducerRecord = new ProducerRecord<>(topicName, key, (RouteEvent) event);
      routeEventProducer.send(routeEventProducerRecord);
    }
    logger.info("Event sent to kafka successfully.");
  }

  public void shutdown() {
    serviceEventProducer.close();
    nodeEventProducer.close();
    routeEventProducer.close();
  }
}
