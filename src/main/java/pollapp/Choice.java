package pollapp;

/**
 * Represents a Choice (or VoteOption) for a Question.
 */
public class Choice {

    private String id;
    private String caption;
    private int presentationOrder;

    public Choice() {
    }

    public Choice(String id, String caption, int presentationOrder) {
        this.id = id;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }
}
