package pollapp;

import java.util.List;

/**
 * Request object for creating polls with custom options.
 */
public class PollRequest {
    private String question;
    private List<String> options;

    public PollRequest() {
    }

    public PollRequest(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}