package pollapp.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import pollapp.Pollmanager;
import pollapp.events.VoteEvent;

@Service
public class KafkaEventListener {

    private final Pollmanager pollManager;

    public KafkaEventListener(Pollmanager pollManager) {
        this.pollManager = pollManager;
    }

    @KafkaListener(topics = "poll-*", groupId = "poll-app-group")
    public void handleVoteEvent(@Payload VoteEvent voteEvent) {
        System.out.println("Received vote event: " + voteEvent.getEventType() +
                " for poll: " + voteEvent.getPollId());

        try {
            if ("VOTE_CAST".equals(voteEvent.getEventType())) {
                // Create vote in database
                pollManager.createVote(voteEvent.getVoterId(),
                        voteEvent.getPollId(),
                        voteEvent.getChoiceId());
                System.out.println("Vote registered in database");
            } else if ("VOTE_UPDATED".equals(voteEvent.getEventType())) {
                // Handle vote update logic here
                System.out.println("Vote update processed");
            }
        } catch (Exception e) {
            System.err.println("Error processing vote event: " + e.getMessage());
        }
    }
}