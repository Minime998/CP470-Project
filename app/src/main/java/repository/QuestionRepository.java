package repository;

import com.example.camlingo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import model.MultipleChoiceQuestion;

public class QuestionRepository {
    private static QuestionRepository instance;
    private final Map<String, MultipleChoiceQuestion> questionMap = new HashMap<>();

    public QuestionRepository(){
        // these are sample question similar to what our database will have
        questionMap.put("q1", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Refrigerator", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"}, R.drawable.refrigerator));
        questionMap.put("q2", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Apple", new String[]{"Banana", "Apple", "Orange", "Grapes"},R.drawable.apple));
        questionMap.put("q3", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Car", new String[]{"Bus", "Car", "Bicycle", "Truck"},R.drawable.car));
        questionMap.put("q4", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Toothbrush", new String[]{"Toothbrush", "Toothpaste", "Floss", "Mouthwash"},R.drawable.toothbrush));
        questionMap.put("q5", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Microwave", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"},R.drawable.microwave));
        questionMap.put("q6", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Chair", new String[]{"Table", "Chair", "Sofa", "Bed"},R.drawable.refrigerator));
        questionMap.put("q7", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Notebook", new String[]{"Notebook", "Folder", "Pen", "Highlighter"},R.drawable.refrigerator));
        questionMap.put("q8", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Coffee", new String[]{"Tea", "Coffee", "Juice", "Water"},R.drawable.refrigerator));

        questionMap.put("q9", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Train", new String[]{"Car", "Train", "Plane", "Bicycle"},R.drawable.refrigerator));
        questionMap.put("q10", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Printer", new String[]{"Tablet", "Phone", "Computer", "Printer"},R.drawable.refrigerator));
        questionMap.put("q11", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Belt", new String[]{"Hat", "Shoes", "Socks", "Belt"},R.drawable.refrigerator));
        questionMap.put("q12", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Spoon", new String[]{"Fork", "Knife", "Spoon", "Plate"},R.drawable.refrigerator));
        questionMap.put("q13", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Purse", new String[]{"Suitcase", "Backpack", "Purse", "Bag"},R.drawable.refrigerator));
        questionMap.put("q14", new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Pillow", new String[]{"Blanket", "Pillow", "Sheet", "Mattress"},R.drawable.refrigerator));
    }

    public static synchronized QuestionRepository getInstance(){
        if (instance == null){
            instance = new QuestionRepository();
        }
        return instance;
    }

    public MultipleChoiceQuestion getQuestion(String questionId){
        if (questionId == null){
            throw new IllegalArgumentException("question ID can't be null");
        }
        MultipleChoiceQuestion question = questionMap.get(questionId);
        if (question == null) {
            throw new NoSuchElementException("No question found for the given ID: " + questionId);
        }
        return question;
    }

    public Map<String, MultipleChoiceQuestion> getAllQuestions() {
        return new HashMap<>(questionMap); // Return a copy to prevent modification
    }

}

