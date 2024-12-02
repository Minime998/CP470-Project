package model;

public class LessonModel {
    private String media;
    private String itemText;
    private String phrase;
    private String itemId;


    public LessonModel(String itemText, String phrase, String media, String itemId) {
        this.media = media;
        this.itemText = itemText;
        this.phrase = phrase;
        this.itemId = itemId;
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
