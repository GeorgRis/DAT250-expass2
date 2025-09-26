package pollapp.events;

import java.time.Instant;

public class VoteEvent {
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