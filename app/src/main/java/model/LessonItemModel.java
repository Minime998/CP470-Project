package model;

public class LessonItemModel {
    private final String media;
    private final String itemText;
    private final String phrase;
    private final String itemId;
    private boolean complete;

    public LessonItemModel(String itemText, String phrase, String media, String itemId, boolean complete) {
        this.media = media;
        this.itemText = itemText;
        this.phrase = phrase;
        this.itemId = itemId;
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getMedia() {
        return media;
    }

    public String getItemText() {
        return itemText;
    }


    public String getPhrase() {
        return phrase;
    }

    public String getItemId() {
        return itemId;
    }
}
