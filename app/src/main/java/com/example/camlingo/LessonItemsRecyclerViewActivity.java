package com.example.camlingo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import model.LessonItemModel;

public class LessonItemsRecyclerViewActivity extends AppCompatActivity {
    ArrayList<LessonItemModel> lessonItemModels = new ArrayList<>();
    private LessonItemsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_item_recycler_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String lessonDocumentName = getIntent().getStringExtra("documentName");
        String lessonCollection = getIntent().getStringExtra("lessonCollection");
        String lessonName = getIntent().getStringExtra("lessonName") + ":";
        lessonName = lessonName.substring(0, 1).toUpperCase() + lessonName.substring(1).toLowerCase();
        if (lessonDocumentName != null && lessonCollection != null && lessonName != null){
            fetchLessonData(lessonDocumentName, lessonCollection);
            Log.i("RecyclerView", "lessonName: " + lessonName);
        }

        RecyclerView recyclerView = findViewById(R.id.lessonRecycleView);

        adapter = new LessonItemsRecyclerViewAdapter(this, lessonItemModels, lessonName);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView lessonHeader = findViewById(R.id.lesson_header);
        String lessonHeaderText = "Common English " + lessonCollection;
        lessonHeaderText = lessonHeaderText.toUpperCase();
        lessonHeader.setText(lessonHeaderText);
    }

    private void fetchLessonData( String lessonDocumentName, String lessonCollection){
        FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");
        lessonDocumentName = lessonDocumentName.toLowerCase();
        lessonCollection = lessonCollection.toLowerCase();

        // Dynamically construct Firestore path

        Log.i("RecyclerView", "doc:" + lessonDocumentName + ",subsect: " + lessonCollection);

        db.collection("lessons")
                .document(lessonDocumentName)
                .collection(lessonCollection)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String itemText = document.getString("text");
                        String phrase = document.getString("phrase");
                        String media = document.getString("mediaUrl");
                        String itemId = document.getId();
                        LessonItemModel lessonItemModelItem = new LessonItemModel(itemText,phrase,media,itemId);
                        lessonItemModels.add(lessonItemModelItem);
                        Log.i("RecyclerView", "verb found: " + itemText);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error fetching lesson data: " + e.getMessage());
                });
    }

}