package model;

public class LessonModel {
    private final String lessonName;
    private final String lessonCollectionName;
    private final long lessonNumber;

    public LessonModel(String lessonName, String lessonCollectionName, long lessonNumber){
        this.lessonName = lessonName;
        this.lessonCollectionName = lessonCollectionName;
        this.lessonNumber = lessonNumber;
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

