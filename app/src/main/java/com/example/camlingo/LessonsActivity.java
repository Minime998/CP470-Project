package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LessonsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lesson_activity_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setup views
       Button verbs_lesson_btn = findViewById(R.id.verbs_lesson_btn);
       Button nouns_lesson_btn = findViewById(R.id.nouns_lesson_btn);
       Button vocab_lesson_btn = findViewById(R.id.vocab_lesson_btn);
       Button prepos_lesson_btn = findViewById(R.id.prepos_lesson_btn);
       Button adj_lesson_btn = findViewById(R.id.adj_lesson_btn);

        ArrayList<Button> lessonButtons = new ArrayList<>();
        lessonButtons.add(verbs_lesson_btn);
        lessonButtons.add(nouns_lesson_btn);
        lessonButtons.add(vocab_lesson_btn);
        lessonButtons.add(prepos_lesson_btn);
        lessonButtons.add(adj_lesson_btn);

        for (Button button : lessonButtons){
            button.setOnClickListener(v -> {
                String lessonType = button.getText().toString().toLowerCase();
                Intent intent = new Intent(this, LessonRecyclerViewActivity.class);
                intent.putExtra("lessonType", lessonType);
                startActivity(intent);
            });
        }

    }
}