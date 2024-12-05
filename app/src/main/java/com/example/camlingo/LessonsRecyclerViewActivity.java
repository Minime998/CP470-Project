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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import model.LessonItemModel;
import model.LessonModel;
import repository.GetLessonListFromFirestore;

public class LessonsRecyclerViewActivity extends AppCompatActivity {
    ArrayList<LessonModel> lessonModels = new ArrayList<>();
    private LessonsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessons_recycler_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lesson_activity_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init views
        RecyclerView recyclerView = findViewById(R.id.lessons_recycler_view);
        adapter = new LessonsRecyclerViewAdapter(this, lessonModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        GetLessonListFromFirestore repository = new GetLessonListFromFirestore();
        repository.getLessons(new GetLessonListFromFirestore.OnLessonsFetchedListener() {
            @Override
            public void onLessonsFetched(ArrayList<LessonModel> lessons) {
                if (lessons.isEmpty()) {
                    Log.i("LessonList", "No lessons found!");
                } else {
                    for (LessonModel lesson : lessons) {
                        Log.i("LessonList", "Lesson: " + lesson.toString());
                        lessonModels.add(lesson);                    }

                    // Reverse the lessonModels ArrayList
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("LessonList", "Error fetching lessons", e);
            }
        });

    }


}