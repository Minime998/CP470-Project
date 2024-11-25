package database;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;


public class AppDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "camlingo.db";
    private static final int VERSION_NUM = 1;

    // Table and column names
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_OPTIONS = "options";
    public static final String COLUMN_MEDIA = "media";

    // Statement to create the table
    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT NOT NULL, " +
                    COLUMN_QUESTION + " TEXT NOT NULL, " +
                    COLUMN_ANSWER + " TEXT NOT NULL, " +
                    COLUMN_OPTIONS + " TEXT NOT NULL, " +
                    COLUMN_MEDIA + " INTEGER NOT NULL" +
                    ");";

    public AppDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
        Log.d("DatabaseHelper", "Database and questions table created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }
}
