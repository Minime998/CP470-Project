package model;

public class LessonModel {
    private final String lessonName;
    private final String lessonCollectionName;
    private final long lessonNumber;
    private String lessonMediaUrl;

    public LessonModel(String lessonName, String lessonCollectionName, long lessonNumber, String lessonMediaUrl){
        this.lessonName = lessonName;
        this.lessonCollectionName = lessonCollectionName;
        this.lessonNumber = lessonNumber;
        this.lessonMediaUrl = lessonMediaUrl;
    }

    public String getLessonMediaUrl() {
        return lessonMediaUrl;
    }

    public void setLessonMediaUrl(String lessonMediaUrl) {
        this.lessonMediaUrl = lessonMediaUrl;
    }

    public String getLessonName() {
        return lessonName;
    }

    public long getLessonNumber() {
        return lessonNumber;
    }



    public String getLessonCollectionName() {
        return lessonCollectionName;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonName='" + lessonName + '\'' +
                ", lessonCollectionName='" + lessonCollectionName + '\'' +
                '}';
    }
}

