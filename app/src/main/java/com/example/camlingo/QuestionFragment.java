package com.example.camlingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import model.MultipleChoiceQuestion;

public class QuestionFragment extends Fragment {
    private Button checkBtn;
    private Button continueBtn;
    private Button finishBtn;
    private Button playAudioButton;
    private ProgressBar progressBar;
    private RadioButton option1, option2, option3, option4;
    private RadioGroup optionsGroup;
    private TextView questionText;
    private TextView scoreText;
    private ImageView questionImage;

    private List<MultipleChoiceQuestion> questions;
    private int currentQuestionIndex = 0;
    private MultipleChoiceQuestion currentQuestion;

    private int questionCount = 0;
    private int score = 0;
    private final double totalQuestions = 5.0;

    public QuestionFragment() {
        // Required empty public constructor
    }

    // Fragment receives questions
    public static QuestionFragment newInstance(List<MultipleChoiceQuestion> questions) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("questions", new ArrayList<>(questions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // Initialize views
        checkBtn = view.findViewById(R.id.checkBtn);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50 // Height in pixels, adjust as needed for "large"
        ));

        continueBtn = view.findViewById(R.id.continueBtn);
        playAudioButton = view.findViewById(R.id.playAudioButton);
        questionText = view.findViewById(R.id.img_question_text);
        scoreText = view.findViewById(R.id.scoreText);
        questionImage = view.findViewById(R.id.question_img);
        option1 = view.findViewById(R.id.img_option1);
        option2 = view.findViewById(R.id.img_option2);
        option3 = view.findViewById(R.id.img_option3);
        option4 = view.findViewById(R.id.img_option4);

        // Get the list of questions from arguments
        questions = (List<MultipleChoiceQuestion>) getArguments().getSerializable("questions");

        // Display the first question
        currentQuestion = updateQuestion();
        questionCount = 1;


        // Check button click listener
        checkBtn.setOnClickListener(v -> {
            optionsGroup = getView().findViewById(R.id.optionsGroupImg);

            int selectedId = optionsGroup.getCheckedRadioButtonId();

            // check selected answer if it's correct
            if(selectedId == -1){
                Toast.makeText(getActivity(), "You must select an option", Toast.LENGTH_SHORT).show();
            }else{

                // get the selected option
                RadioButton selectionRadioBtn = getView().findViewById(selectedId);
                String selectedAnswer = selectionRadioBtn.getText().toString();

                // compare answer to correct answer
                if(selectedAnswer.equalsIgnoreCase(currentQuestion.getAnswer())){
                    Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    checkBtn.setVisibility(View.GONE);
                    continueBtn.setVisibility(View.VISIBLE);
                    score++;

                }else{
                    checkBtn.setVisibility(View.GONE);
                    continueBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Continue button click listener
        continueBtn.setOnClickListener(v -> {
            if(questionCount < totalQuestions){
                currentQuestionIndex++;
                // Update progress bar
                int progress = (int) (( questionCount / totalQuestions) * 100);
                progressBar.setProgress(progress);

                questionCount++;

                currentQuestion = updateQuestion();
                optionsGroup.clearCheck();

                Log.i("QuestionFragment", "progress: " + progress);
            }else if(questionCount == totalQuestions){
                progressBar.setProgress(100);
                continueBtn.setText("View results");
                continueBtn.setBackgroundColor(Color.parseColor("#FF5722"));
                questionCount++;
            }
            else {
                progressBar.setVisibility(View.GONE);
                String resultText = "Score: " + score + "/" + 5;
                scoreText.setText(resultText);
                scoreText.setVisibility(View.VISIBLE);
                optionsGroup = getView().findViewById(R.id.optionsGroupImg);
                questionText.setText(R.string.finished_quest);
                optionsGroup.setVisibility(View.GONE);

                continueBtn.setVisibility(View.GONE);
                checkBtn.setVisibility(View.GONE);

                finishBtn = getView().findViewById(R.id.finishBtn);
                finishBtn.setVisibility(View.VISIBLE);

                questionImage.setVisibility(View.VISIBLE);
                questionImage.setImageResource(R.drawable.baseline_check_24);

                setupFinishButton(view); // close activity when finish button clicked
            }
        });

        return view;
    }


    private MultipleChoiceQuestion updateQuestion() {
        // Get the current question
        MultipleChoiceQuestion question = questions.get(currentQuestionIndex);

        // Update UI elements with question data
        questionText.setText(question.getQuestion());
        option1.setText(question.getOptions()[0]);
        option2.setText(question.getOptions()[1]);
        option3.setText(question.getOptions()[2]);
        option4.setText(question.getOptions()[3]);

        if(question.getType() == MultipleChoiceQuestion.QuestionType.VISUAL){
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(questionImage)
                    .load(question.getMedia())
                    .into(questionImage);
        }
        else if(question.getType() == MultipleChoiceQuestion.QuestionType.CONTEXT){
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(questionImage)
                    .load(question.getMedia())
                    .into(questionImage);
        }

        // Set the image for the question
//        questionImage.setImageResource(question.getMedia());

        // Reset buttons for next question
        checkBtn.setVisibility(View.VISIBLE);
        continueBtn.setVisibility(View.GONE);

        return question;
    }

    private void setupFinishButton(View view){
        finishBtn = view.findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DailyQuestActivity.class);
            startActivity(intent);

            // Close the parent activity
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }
}
