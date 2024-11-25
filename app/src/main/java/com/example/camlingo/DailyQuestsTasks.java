package com.example.camlingo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.List;

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
        QuestionRepository repository = new QuestionRepository(this);

        // populate database with sample questions
        repository.addQuestions(new MultipleChoiceQuestion(MultipleChoiceQuestion.QuestionType.VISUAL,"What do you see?",
                "Refrigerator", new String[]{"Microwave", "Refrigerator", "Oven", "Toaster"}, R.drawable.refrigerator));

        List<MultipleChoiceQuestion> questions = repository.getAllQuestions();


        // pass question to fragment
        ImageQuestionFragment fragment = ImageQuestionFragment.newInstance(questions);


        // display the fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.question_fragment,fragment)
                .commit();
    }
}