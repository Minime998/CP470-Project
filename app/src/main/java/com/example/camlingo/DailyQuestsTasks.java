package com.example.camlingo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import model.MultipleChoiceQuestion;
import repository.QuestionRepository;

public class DailyQuestsTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_quests_tasks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get questions from the repository
        QuestionRepository repository = QuestionRepository.getInstance();
        MultipleChoiceQuestion question = repository.getQuestion("q1"); // get one question

        // pass question to fragment
        ImageQuestionFragment fragment = new ImageQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("question",question);
        fragment.setArguments(args);

        // display the fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.question_fragment,fragment)
                .commit();
    }
}