package org.konnect;


import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class IngestExerciseProducer {

  private final Producer<String, GenericRecord> producer;
  final String outTopic;

  public IngestExerciseProducer(final Producer<String, GenericRecord> producer,
                                final String topic) {
    this.producer = producer;
    outTopic = topic;
  }

  public Future<RecordMetadata> produce(final String message) {
    final String[] parts = message.split("-");
    final String key, value;
    if (parts.length > 1) {
      key = parts[0];
      value = parts[1];
    } else {
      key = null;
      value = parts[0];
    }
//    final ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<>(outTopic, key, value);
    final ProducerRecord<String, GenericRecord> producerRecord = null;
    return producer.send(producerRecord);
  }

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
    final Producer<String, GenericRecord> producer = new KafkaProducer<>(props);
    final IngestExerciseProducer producerApp = new IngestExerciseProducer(producer, topic);

//    String filePath = args[1];
    String filePath = "./stream.jsonl";
    try {
      List<String> linesToProduce = Files.readAllLines(Paths.get(filePath));
      List<Future<RecordMetadata>> metadata = linesToProduce.stream()
          .filter(l -> !l.trim().isEmpty())
          .map(producerApp::produce)
          .collect(Collectors.toList());
      producerApp.printMetadata(metadata, filePath);

    } catch (IOException e) {
      System.err.printf("Error reading file %s due to %s %n", filePath, e);
    }
    finally {
      producerApp.shutdown();
    }
  }
}


