package repository;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import model.LessonModel;

public class GetLessonListFromFirestore {

    private final FirebaseFirestore db;

    public GetLessonListFromFirestore() {
        this.db = FirebaseFirestore.getInstance("camlingo");
    }

    // Fetch lessons from Firestore
    public void getLessons(OnLessonsFetchedListener listener) {
        db.collection("lessons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<LessonModel> lessons = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.i("Found lesson", "Lesson found: " + document.getId());

                        // Fetch fields from Firestore
                        String lessonName = document.getString("type");
                        String lessonCollectionName = document.getString("collection_name");
                        Long lessonNumber = document.getLong("lesson_number");

                        // Only add valid lessons
                        if (lessonName != null && lessonCollectionName != null && lessonNumber != null) {
                            lessons.add(new LessonModel(lessonName, lessonCollectionName, lessonNumber));
                        }
                        else{
                            Log.i("GetLessonFromFirestore", "Failed to add lesson");
                        }
                    }

                    // Pass results to listener
                    listener.onLessonsFetched(lessons);
                })
                .addOnFailureListener(e -> {
                    Log.e("Error fetching lessons", e.getMessage());
                    listener.onError(e);
                });
    }

    // Callback interface
    public interface OnLessonsFetchedListener {
        void onLessonsFetched(ArrayList<LessonModel> lessons);

        void onError(Exception e); // Handle errors explicitly
    }
}
