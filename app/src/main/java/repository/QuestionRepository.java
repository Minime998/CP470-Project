package repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import database.AppDatabaseHelper;
import model.MultipleChoiceQuestion;

public class QuestionRepository {
    private final AppDatabaseHelper dbHelper;

    public QuestionRepository(Context context) {
        this.dbHelper = new AppDatabaseHelper(context);
    }

    // Add a question to the database
    public void addQuestion(MultipleChoiceQuestion question) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(AppDatabaseHelper.COLUMN_TYPE, question.getType().toString());
            values.put(AppDatabaseHelper.COLUMN_QUESTION, question.getQuestion());
            values.put(AppDatabaseHelper.COLUMN_ANSWER, question.getAnswer());
            values.put(AppDatabaseHelper.COLUMN_OPTIONS, String.join(",", question.getOptions()));
            values.put(AppDatabaseHelper.COLUMN_MEDIA, question.getMedia() == null ? "" : question.getMedia());

            long result = db.insert(AppDatabaseHelper.TABLE_QUESTIONS, null, values);
            if (result == -1) {
                Log.e("QuestionRepository", "Failed to insert question: " + question.getQuestion());
            } else {
                Log.d("QuestionRepository", "Successfully inserted question with ID: " + result);
            }
        } catch (Exception e) {
            Log.e("QuestionRepository", "Error inserting question: " + e.getMessage(), e);
        }
    }

    // Check if the questions table is empty
    public boolean isTableEmpty() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + AppDatabaseHelper.TABLE_QUESTIONS, null)) {
            return cursor.moveToFirst() && cursor.getInt(0) == 0;
        }
    }

    // Get a question by its ID
    public MultipleChoiceQuestion getQuestion(int questionId) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(AppDatabaseHelper.TABLE_QUESTIONS, null,
                     AppDatabaseHelper.COLUMN_ID + "=?",
                     new String[]{String.valueOf(questionId)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TYPE));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_QUESTION));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ANSWER));
                String optionsCSV = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_OPTIONS));
                String media = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

                String[] options = (optionsCSV != null && !optionsCSV.isEmpty()) ? optionsCSV.split(",") : new String[0];

                return new MultipleChoiceQuestion(
                        MultipleChoiceQuestion.QuestionType.valueOf(type),
                        question,
                        answer,
                        options,
                        media
                );
            } else {
                throw new NoSuchElementException("No question found with ID: " + questionId);
            }
        }
    }

    // Get all questions (randomized)
    public List<MultipleChoiceQuestion> getAllQuestions() {
        List<MultipleChoiceQuestion> questions = new ArrayList<>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(AppDatabaseHelper.TABLE_QUESTIONS, null,
                     null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TYPE));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_QUESTION));
                String answer = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ANSWER));
                String optionsCSV = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_OPTIONS));
                String media = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));

                String[] options = (optionsCSV != null && !optionsCSV.isEmpty()) ? optionsCSV.split(",") : new String[0];

                questions.add(new MultipleChoiceQuestion(
                        MultipleChoiceQuestion.QuestionType.valueOf(type),
                        question,
                        answer,
                        options,
                        media
                ));
            }
        }
        Collections.shuffle(questions); // shuffle questions
        return questions.subList(0, Math.min(questions.size(), 5));
    }
}
