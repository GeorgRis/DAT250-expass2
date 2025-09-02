package pollapp;

import java.util.List;

/**
 * Represents a question in a poll, which contains a list of choices.
 */
public class Question {
    private String questionId;
    private String questionText;
    private List<Choice> choices;

    /**
     * Default constructor required for a Java Bean.
     */
    public Question() {
    }

    /**
     * Constructor to create a new Question.
     * @param questionId The unique ID of the question.
     * @param questionText The text content of the question.
     * @param choices A list of choices for this question.
     */
    public Question(String questionId, String questionText, List<Choice> choices) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.choices = choices;
    }

    // Getters and Setters

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}

