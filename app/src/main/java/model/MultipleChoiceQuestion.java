package model;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;

public class MultipleChoiceQuestion implements Serializable {

    // multiple choice questions are either visual or audio type
    public enum QuestionType{
        VISUAL,
        AUDIO,
        CONTEXT
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

}


