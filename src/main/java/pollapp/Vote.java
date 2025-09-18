package pollapp;

import java.time.Instant;

/**
 * Represents a Vote in the PollApp domain.
 */
public class Vote {

    private String id;
    private String voterId;
    private String pollId;
    private String choiceId;
    private Instant votedAt;

    public Vote() {
    }

    public Vote(String id, String voterId, String pollId, String choiceId, Instant votedAt) {
        this.id = id;
        this.voterId = voterId;
        this.pollId = pollId;
        this.choiceId = choiceId;
        this.votedAt = votedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public Instant getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(Instant votedAt) {
        this.votedAt = votedAt;
    }
}