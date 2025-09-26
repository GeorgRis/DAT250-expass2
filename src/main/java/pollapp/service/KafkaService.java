package pollapp.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pollapp.events.PollCreatedEvent;
import pollapp.events.VoteEvent;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AdminClient adminClient;

    public KafkaService(KafkaTemplate<String, Object> kafkaTemplate, AdminClient adminClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.adminClient = adminClient;
    }

    public void createTopicForPoll(String pollId) {
        String topicName = "poll-" + pollId;
        NewTopic topic = new NewTopic(topicName, 1, (short) 1);

        try {
            adminClient.createTopics(Collections.singletonList(topic)).all().get();
            System.out.println("Created topic: " + topicName);
        } catch (Exception e) {
            System.err.println("Error creating topic: " + e.getMessage());
        }
    }

    public void publishPollCreatedEvent(String pollId, String question, String ownerId) {
        PollCreatedEvent event = new PollCreatedEvent(pollId, question, ownerId);
        String topicName = "poll-events";

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, pollId, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Published poll created event for poll: " + pollId);
            } else {
                System.err.println("Failed to publish poll created event: " + ex.getMessage());
            }
        });
    }

    public void publishVoteEvent(String pollId, String choiceId, String voterId, String eventType) {
        VoteEvent event = new VoteEvent(pollId, choiceId, voterId, eventType);
        String topicName = "poll-" + pollId;

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, choiceId, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Published vote event for poll: " + pollId);
            } else {
                System.err.println("Failed to publish vote event: " + ex.getMessage());
            }
        });
    }
}