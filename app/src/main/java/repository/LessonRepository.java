package repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import database.AppDatabaseHelper;
import model.LessonModel;

public class LessonRepository {
    private final AppDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private final String tableName;
    private final String lessonType;

    public LessonRepository(Context context, String tableName, String lessonType){
        dbHelper = new AppDatabaseHelper(context);
        this.tableName = tableName.toLowerCase();
        this.lessonType = lessonType.toLowerCase();
    }

    // method to add a question to 'lessons' table using MCQ object
    public void addLessonItem(LessonModel lessonModelItem) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(lessonType, lessonModelItem.getItemText());
        values.put(AppDatabaseHelper.COLUMN_PHRASE, lessonModelItem.getPhrase());
        values.put(AppDatabaseHelper.COLUMN_MEDIA, lessonModelItem.getMedia());
        values.put(AppDatabaseHelper.COLUMN_ID, lessonModelItem.getItemId());

        try {
            long result = db.insert(tableName, null, values);
            if (result == -1) {
                Log.e("LessonRepository", "Failed to insert lesson: " + lessonModelItem.getItemText());
            } else {
                Log.d("LessonRepository", "Successfully inserted lesson with ID: " + result);
            }
        } catch (Exception e) {
            Log.e("LessonRepository", "Error inserting question: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }

    public List<LessonModel> getAllLessonsItems(){
        List<LessonModel> lessonItems = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor.moveToFirst()) {
            do {
                String lessonItemText = cursor.getString(cursor.getColumnIndexOrThrow(lessonType));
                String lessonItemPhrase = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_PHRASE));
                String lessonItemMedia = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_MEDIA));
                String lessonItemId = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ID));

                lessonItems.add(new LessonModel(
                        lessonItemText,
                        lessonItemPhrase,
                        lessonItemMedia,
                        lessonItemId
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return lessonItems;
    }
}
