package pollapp.events;

import java.time.Instant;

public class PollCreatedEvent {
    private String pollId;
    private String question;
    private String ownerId;
    private Instant timestamp;

    public PollCreatedEvent() {
    }

    public PollCreatedEvent(String pollId, String question, String ownerId) {
        this.pollId = pollId;
        this.question = question;
        this.ownerId = ownerId;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public String getPollId() { return pollId; }
    public void setPollId(String pollId) { this.pollId = pollId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
