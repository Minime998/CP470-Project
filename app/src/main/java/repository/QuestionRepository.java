package repository;

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
    private final AppDatabaseHelper dbHelper;
    private SQLiteDatabase db;



    public QuestionRepository(Context context){
        dbHelper = new AppDatabaseHelper(context);
    }

    // method to add a question to 'questions' table using MCQ object
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

