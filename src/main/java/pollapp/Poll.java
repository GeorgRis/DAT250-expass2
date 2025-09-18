package pollapp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Poll in the PollApp domain.
 */
public class Poll {

    private String id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private String ownerId;
    private List<Choice> choices = new ArrayList<>();

    public Poll() {
    }

    public Poll(String id, String question, Instant publishedAt, Instant validUntil, String ownerId) {
        this.id = id;
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        this.choices.add(choice);
    }
}