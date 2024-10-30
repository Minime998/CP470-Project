package model;
import androidx.annotation.NonNull;

import java.util.Arrays;

public class MultipleChoiceQuestion {

    // multiple choice questions are either visual or audio type
    public enum QuestionType{
        VISUAL,
        AUDIO
    }

    private String answer;
    private String question;
    private String[] options;
    private QuestionType type; // audio or visual
    private String media; // Store the reference to the media resource

    public MultipleChoiceQuestion(QuestionType type, String question, String answer, String[] optionsArray, String media) {
        if(optionsArray.length != 4){
            throw new IllegalArgumentException("Options array must have exactly 4 elements, one of which is the correct answer");
        }
        if (question == null || question.isEmpty()) { // Check for null or empty question
            throw new IllegalArgumentException("Question can't be null or empty");
        }
        if(answer == null || answer.isEmpty()){
            throw new IllegalArgumentException("answer can't be null");
        }
        if(type == null){
            throw new IllegalArgumentException("type can't be null");
        }

        if(media == null || media.isEmpty()){
            throw new IllegalArgumentException("media can't be null or empty");
        }



        this.answer = answer.toLowerCase();
        this.question = question.toLowerCase();
        this.options = optionsArray;
        this.type = type;
        this.media = media;
    }

    // getters
    public String[] getOptions() {
        return options;
    }
    public String getAnswer() {
        return answer;
    }
    public String getQuestion() {
        return question;
    }
    public QuestionType getType() {
        return type;
    }
    public String getMedia() {
        return media;
    }

    // setters
    public void setOptions(String[] optionList) {
        assert optionList.length == 4 : "Options array must have exactly 4 elements, one of which is the correct answer";
        this.options = optionList;
    }
    public void setAnswer(String correctAnswer) {
        assert correctAnswer != null : "Answer can't be null";
        this.answer = correctAnswer;
    }

    public void setQuestion(String newQuestion) {
        assert newQuestion != null : "Question can't be null";
        this.question = newQuestion;
    }

    public void setType(QuestionType newType) {
        if (newType == null) {
            throw new IllegalArgumentException("Type can't be null");
        }
        if (newType != QuestionType.VISUAL && newType != QuestionType.AUDIO) {
            throw new IllegalArgumentException("Type must be either 'visual' or 'audio'");
        }
        this.type = newType;
    }

    public void setMedia(String media) {
        this.media = media; // Setter for media
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{question = " + question + ", answer=" + answer + ", options= " + Arrays.toString(options) + ", type=" + type + "}";
    }

    public static void main(String [] args){

        String[] options2 = {"TV", "Laptop", "Tablespoon", "Mug"};
        String quest = "What do you see";
        QuestionType type = QuestionType.AUDIO;

        MultipleChoiceQuestion q2 = new MultipleChoiceQuestion(type, quest, "TV", options2, "firegaurd");
        System.out.println("question: " + q2.getQuestion());
        System.out.println("answer: " + q2.getAnswer());
        System.out.println("type: " + q2.getType());
        System.out.println("media: " + q2.getMedia());
        q2.setType(QuestionType.VISUAL);
        System.out.println("type: " + q2.getType());
        System.out.println(Arrays.toString(q2.getOptions()));

    }
}


