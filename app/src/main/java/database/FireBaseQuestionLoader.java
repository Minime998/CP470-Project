package database;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;

import model.LessonModel;
import model.MultipleChoiceQuestion;
import repository.QuestionRepository;

public class FireBaseQuestionLoader {
    private final QuestionRepository questionRepository;

    private static final String QUESTION_LOADER_PREFS = "QuestionLoaderPrefs";
    private static final String LAST_FETCH_KEY = "lastFetchTimestamp";
    private static final long FETCH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    private final FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");
    private final Context context;

    public FireBaseQuestionLoader(Context context){
        this.context = context;
        this.questionRepository = new QuestionRepository(context);
    }


    public void loadQuestions(){
        if(shouldFetchQuestions()){
            fetchQuestionsFromFirestore();
            // Update the last fetch timestamp
            updateLastFetchTimestamp();
        } else {
        Log.i("FireBaseQuestionLoader", "Questions fetch skipped. Using local data.");
        }
    }

    private boolean shouldFetchQuestions(){
        SharedPreferences pref = context.getSharedPreferences(QUESTION_LOADER_PREFS,Context.MODE_PRIVATE);
        long lastFetchTimestamp = pref.getLong(LAST_FETCH_KEY,0);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastFetchTimestamp) > FETCH_INTERVAL_MS;
    }

    public void fetchQuestionsFromFirestore(){

        CollectionReference questionRef = db.collection("daily_quests");

        questionRef.orderBy("text")
                .limit(10) // get only 5 questions
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        // process each question
                        String typeField = document.getString("type").toUpperCase();

                        String text = document.getString("text");
                        List<String> optionsList = (List<String>) document.get("options");
                        assert optionsList != null;
                        String[] options = optionsList.toArray(new String[0]); // Convert List to String[]
                        String correctAnswer = document.getString("correctAnswer");
                        String mediaUrl = document.getString("mediaUrl");

                        // create MultipleChoiceQuestion object
                        MultipleChoiceQuestion.QuestionType type = MultipleChoiceQuestion.QuestionType.valueOf(typeField);
                        MultipleChoiceQuestion question = new MultipleChoiceQuestion(type,text,correctAnswer, options, mediaUrl);

                        // Add the question to the local repository
                        questionRepository.addQuestion(question);

                        // get questions from the repository
                        Log.i("FireBaseQuestionLoader", "New question: " + question);
                    }

                })
                .addOnFailureListener(e -> {
                    System.err.println("Error fetching daily challenge questions: " + e.getMessage());
                });
    }

    private void updateLastFetchTimestamp(){
        SharedPreferences prefs = context.getSharedPreferences(QUESTION_LOADER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(LAST_FETCH_KEY, System.currentTimeMillis());
        Log.i("FireBaseQuestionLoader","updated last fetch: " + System.currentTimeMillis());
        editor.apply();
    }
}
