package pollapp.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;

/**
 * Standalone application to test Kafka integration with the Poll application.
 * This application can both publish vote events and consume poll events.
 */
public class StandaloneKafkaTest {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Kafka Poll Application Tester ===");
        System.out.println("1. Publish Vote Event");
        System.out.println("2. Listen for Poll Events");
        System.out.println("3. Both (Producer and Consumer)");
        System.out.print("Choose option (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                runProducer(scanner);
                break;
            case 2:
                runConsumer(scanner);
                break;
            case 3:
                // Run both in separate threads
                Thread producerThread = new Thread(() -> runProducer(scanner));
                Thread consumerThread = new Thread(() -> runConsumer(scanner));

                consumerThread.start();

                // Give consumer time to start
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                producerThread.start();

                try {
                    producerThread.join();
                    consumerThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            default:
                System.out.println("Invalid option");
        }

        scanner.close();
    }

    private static void runProducer(Scanner scanner) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            System.out.println("\n=== Vote Event Publisher ===");

            while (true) {
                System.out.print("Enter poll ID (or 'quit' to exit): ");
                String pollId = scanner.nextLine();

                if ("quit".equalsIgnoreCase(pollId)) {
                    break;
                }

                System.out.print("Enter choice ID: ");
                String choiceId = scanner.nextLine();

                System.out.print("Enter voter ID (or leave empty for anonymous): ");
                String voterId = scanner.nextLine();
                if (voterId.trim().isEmpty()) {
                    voterId = "anonymous-" + UUID.randomUUID().toString().substring(0, 8);
                }

                // Create vote event
                VoteEvent voteEvent = new VoteEvent(pollId, choiceId, voterId, "VOTE_CAST");

                try {
                    String jsonEvent = objectMapper.writeValueAsString(voteEvent);
                    String topicName = "poll-" + pollId;

                    ProducerRecord<String, String> record = new ProducerRecord<>(topicName, choiceId, jsonEvent);

                    producer.send(record, (metadata, exception) -> {
                        if (exception == null) {
                            System.out.println("  Vote event published successfully!");
                            System.out.println("  Topic: " + metadata.topic());
                            System.out.println("  Partition: " + metadata.partition());
                            System.out.println("  Offset: " + metadata.offset());
                        } else {
                            System.err.println("  Failed to publish vote event: " + exception.getMessage());
                        }
                    }).get(); // Wait for the result

                } catch (Exception e) {
                    System.err.println("Error creating/sending vote event: " + e.getMessage());
                }

                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Producer error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runConsumer(Scanner scanner) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "standalone-test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            System.out.println("\n=== Event Consumer ===");
            System.out.print("Enter topic name to subscribe to (e.g., 'poll-123' or 'poll-events'): ");
            String topicName = scanner.nextLine();

            consumer.subscribe(Collections.singletonList(topicName));

            System.out.println("Listening for events on topic: " + topicName);
            System.out.println("Press Ctrl+C to stop...\n");

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        System.out.println("📨 Received Event:");
                        System.out.println("  Topic: " + record.topic());
                        System.out.println("  Partition: " + record.partition());
                        System.out.println("  Offset: " + record.offset());
                        System.out.println("  Key: " + record.key());
                        System.out.println("  Timestamp: " + Instant.ofEpochMilli(record.timestamp()));

                        // Try to parse as VoteEvent
                        try {
                            VoteEvent voteEvent = objectMapper.readValue(record.value(), VoteEvent.class);
                            System.out.println("  Event Type: " + voteEvent.getEventType());
                            System.out.println("  Poll ID: " + voteEvent.getPollId());
                            System.out.println("  Choice ID: " + voteEvent.getChoiceId());
                            System.out.println("  Voter ID: " + voteEvent.getVoterId());
                            System.out.println("  Event Timestamp: " + voteEvent.getTimestamp());
                        } catch (Exception e) {
                            // If not a VoteEvent, just print raw value
                            System.out.println("  Raw Value: " + record.value());
                        }

                        System.out.println("  ────────────────────────────────────");

                    } catch (Exception e) {
                        System.err.println("Error processing record: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Consumer error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Simple VoteEvent class for testing
     */
    public static class VoteEvent {
        private String pollId;
        private String choiceId;
        private String voterId;
        private Instant timestamp;
        private String eventType;

        public VoteEvent() {
        }

        public VoteEvent(String pollId, String choiceId, String voterId, String eventType) {
            this.pollId = pollId;
            this.choiceId = choiceId;
            this.voterId = voterId;
            this.eventType = eventType;
            this.timestamp = Instant.now();
        }

        // Getters and setters
        public String getPollId() { return pollId; }
        public void setPollId(String pollId) { this.pollId = pollId; }

        public String getChoiceId() { return choiceId; }
        public void setChoiceId(String choiceId) { this.choiceId = choiceId; }

        public String getVoterId() { return voterId; }
        public void setVoterId(String voterId) { this.voterId = voterId; }

        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
    }
}