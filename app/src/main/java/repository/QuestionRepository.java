package repository;

import com.example.camlingo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import database.AppDatabaseHelper;
import model.MultipleChoiceQuestion;

public class QuestionRepository {
    private static QuestionRepository instance;
    private final Map<String, MultipleChoiceQuestion> questionMap = new HashMap<>();
    private final AppDatabaseHelper dbHelper;
    private SQLiteDatabase db;



    public QuestionRepository(Context context){
        dbHelper = new AppDatabaseHelper(context);
    }

    // method to add a question to database using MCQ object
    public void addQuestion(MultipleChoiceQuestion question) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COLUMN_TYPE, question.getType().toString());
        values.put(AppDatabaseHelper.COLUMN_QUESTION, question.getQuestion());
        values.put(AppDatabaseHelper.COLUMN_ANSWER, question.getAnswer());
        values.put(AppDatabaseHelper.COLUMN_OPTIONS, String.join(",", question.getOptions()));
        if (question.getMedia() == null){
            values.put(AppDatabaseHelper.COLUMN_MEDIA, "");
        }else {
            values.put(AppDatabaseHelper.COLUMN_MEDIA, question.getMedia());
        }

        try {
            long result = db.insert(AppDatabaseHelper.TABLE_QUESTIONS, null, values);
            if (result == -1) {
                Log.e("QuestionRepository", "Failed to insert question: " + question.getQuestion());
            } else {
                Log.d("QuestionRepository", "Successfully inserted question with ID: " + result);
            }
        } catch (Exception e) {
            Log.e("QuestionRepository", "Error inserting question: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }


    public boolean isTableEmpty() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + AppDatabaseHelper.TABLE_QUESTIONS + " ORDER BY RAND() LIMIT 5", null);
        boolean isEmpty = true;
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }
        cursor.close();
        db.close();
        return isEmpty;
    }


//    public void populateSampleQuestions(){
//        // these are sample question similar to what our database will have
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
//                "Refrigerator", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"}, R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
//                "Apple", new String[]{"Banana", "Apple", "Orange", "Grapes"},R.drawable.apple));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
//                "Car", new String[]{"Bus", "Car", "Bicycle", "Truck"},R.drawable.car));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
//                "Toothbrush", new String[]{"Toothbrush", "Toothpaste", "Floss", "Mouthwash"},R.drawable.toothbrush));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
//                "Microwave", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"},R.drawable.microwave));
//
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"Which word doesn't belong?",
//                "Trunk", new String[]{"Ceiling", "Canopy", "Roof", "Trunk"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"Which word doesn't belong?",
//                "Cat", new String[]{"Cat", "Cauliflower", "Cabbage", "Cucumber"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"Which word doesn't belong?",
//                "Shoes", new String[]{"Tea", "Coffee", "Juice", "Shoes"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"Which word doesn't belong?",
//                "Shoes", new String[]{"Cable", "Rope", "Basket", "Thread"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"Which word doesn't belong?",
//                "Watch", new String[]{"Phone", "Laptop", "Computer", "Watch"},R.drawable.refrigerator));
//
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
//                "Bread", new String[]{"Thread", "Red", "Bread", "Bed"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
//                "Tablet", new String[]{"Tablet", "Cabinet", "Table", "Tabletop"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
//                "Belt", new String[]{"Pelt", "Melt", "Bolt", "Belt"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
//                "Plate", new String[]{"Place", "Plain", "Late", "Plate"},R.drawable.refrigerator));
//        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
//                "Backpack", new String[]{"Knapsack", "Backpack", "Backtrack", "Packback"},R.drawable.refrigerator));
//    }


    public MultipleChoiceQuestion getQuestion(int questionId){
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(AppDatabaseHelper.TABLE_QUESTIONS,null,
                AppDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(questionId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TYPE));
            String question = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_QUESTION));
            String answer = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ANSWER));
            String optionsCSV = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_OPTIONS));
            String media = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

            String[] options = optionsCSV.split(",");

            cursor.close();
            db.close();

            return new MultipleChoiceQuestion(
                    MultipleChoiceQuestion.QuestionType.valueOf(type),
                    question,
                    answer,
                    options,
                    media
            );
        } else {
            db.close();
            throw new NoSuchElementException("No question found with ID: " + questionId);
        }
    }

    public List<MultipleChoiceQuestion> getAllQuestions(){
        List<MultipleChoiceQuestion> questions = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String tableName = AppDatabaseHelper.TABLE_QUESTIONS;
        String orderBy = "RANDOM()"; // Order by random
        String limit = "5";

        Cursor cursor = db.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                orderBy,
                limit
        );
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TYPE));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_QUESTION));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ANSWER));
                String optionsCSV = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_OPTIONS));
                String media = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

                String[] options = optionsCSV.split(",");

                questions.add(new MultipleChoiceQuestion(
                        MultipleChoiceQuestion.QuestionType.valueOf(type),
                        question,
                        answer,
                        options,
                        media
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return questions;
    }

}

