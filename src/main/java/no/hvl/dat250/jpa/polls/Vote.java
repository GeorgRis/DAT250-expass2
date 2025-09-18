package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a Vote in the PollApp domain.
 */
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voted_by_id")
    private User votedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votes_on_id")
    private VoteOption votesOn;

    private Instant votedAt;

    public Vote() {
        this.votedAt = Instant.now();
    }

    public Vote(User votedBy, VoteOption votesOn) {
        this();
        this.votedBy = votedBy;
        this.votesOn = votesOn;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVotedBy() {
        return votedBy;
    }

    public void setVotedBy(User votedBy) {
        this.votedBy = votedBy;
    }

    public VoteOption getVotesOn() {
        return votesOn;
    }

    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
    }

    public Instant getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(Instant votedAt) {
        this.votedAt = votedAt;
    }
}