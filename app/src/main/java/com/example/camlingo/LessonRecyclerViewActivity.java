package com.example.camlingo;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import model.LessonModel;

public class LessonRecyclerViewActivity extends AppCompatActivity {
    ArrayList<LessonModel> lessonModels = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_recycler_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String lessonType = getIntent().getStringExtra("lessonType");
        if (lessonType != null){
            fetchLessonData(lessonType);
            Log.i("RecyclerView", "lessonType: " + lessonType);
        }

        RecyclerView recyclerView = findViewById(R.id.lessonRecycleView);

        adapter = new RecyclerViewAdapter(this, lessonModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchLessonData( String lessonType){
        FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");
        lessonType.toLowerCase();

        // Dynamically construct Firestore path
        String subCollection = "english_" + lessonType;
        String documentName = "learn_"+ lessonType;

        Log.i("RecyclerView", "doc:" + documentName + ",subsect: " + subCollection);

        db.collection("lessons")
                .document(documentName)
                .collection(subCollection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String itemText = document.getString("text");
                        String phrase = document.getString("phrase");
                        String media = document.getString("mediaUrl");
                        String itemId = document.getId();
                        LessonModel lessonModelItem = new LessonModel(itemText,phrase,media,itemId);
                        lessonModels.add(lessonModelItem);
                        Log.i("RecyclerView", "verb found: " + itemText);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error fetching lesson data: " + e.getMessage());
                });
    }

}