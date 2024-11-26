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

    // method to add questions to database using MCQ object
    public void addQuestions(MultipleChoiceQuestion question) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COLUMN_TYPE, question.getType().toString());
        values.put(AppDatabaseHelper.COLUMN_QUESTION, question.getQuestion());
        values.put(AppDatabaseHelper.COLUMN_ANSWER, question.getAnswer());
        values.put(AppDatabaseHelper.COLUMN_OPTIONS, String.join(",", question.getOptions()));
        values.put(AppDatabaseHelper.COLUMN_MEDIA, question.getMedia());

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
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + AppDatabaseHelper.TABLE_QUESTIONS, null);
        boolean isEmpty = true;
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }
        cursor.close();
        db.close();
        return isEmpty;
    }


    public void populateSampleQuestions(){
        // these are sample question similar to what our database will have
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Refrigerator", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"}, R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Apple", new String[]{"Banana", "Apple", "Orange", "Grapes"},R.drawable.apple));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Car", new String[]{"Bus", "Car", "Bicycle", "Truck"},R.drawable.car));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Toothbrush", new String[]{"Toothbrush", "Toothpaste", "Floss", "Mouthwash"},R.drawable.toothbrush));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Microwave", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"},R.drawable.microwave));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Chair", new String[]{"Table", "Chair", "Sofa", "Bed"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Notebook", new String[]{"Notebook", "Folder", "Pen", "Highlighter"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Coffee", new String[]{"Tea", "Coffee", "Juice", "Water"},R.drawable.refrigerator));

        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Train", new String[]{"Car", "Train", "Plane", "Bicycle"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Printer", new String[]{"Tablet", "Phone", "Computer", "Printer"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Belt", new String[]{"Hat", "Shoes", "Socks", "Belt"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Spoon", new String[]{"Fork", "Knife", "Spoon", "Plate"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Purse", new String[]{"Suitcase", "Backpack", "Purse", "Bag"},R.drawable.refrigerator));
        addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you hear?",
                "Pillow", new String[]{"Blanket", "Pillow", "Sheet", "Mattress"},R.drawable.refrigerator));
    }


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
            int media = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

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
        Cursor cursor = db.query(AppDatabaseHelper.TABLE_QUESTIONS,
                null,null,null,null,null, null);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TYPE));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_QUESTION));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ANSWER));
                String optionsCSV = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_OPTIONS));
                int media = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

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

