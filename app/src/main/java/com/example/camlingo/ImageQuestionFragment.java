package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import model.MultipleChoiceQuestion;

public class ImageQuestionFragment extends Fragment {
    private Button checkBtn;
    private Button continueBtn;
    private Button finishBtn;
    private RadioButton option1, option2, option3, option4;
    private RadioGroup optionsGroup;
    private TextView questionText;
    private ImageView questionImage;

    private List<MultipleChoiceQuestion> questions;
    private int currentQuestionIndex = 0;
    private MultipleChoiceQuestion currentQuestion;

    private int questionCount = 0;

    public ImageQuestionFragment() {
        // Required empty public constructor
    }

    // Fragment receives questions
    public static ImageQuestionFragment newInstance(List<MultipleChoiceQuestion> questions) {
        ImageQuestionFragment fragment = new ImageQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("questions", new ArrayList<>(questions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_question, container, false);

        // Initialize views
        checkBtn = view.findViewById(R.id.checkBtn);
        continueBtn = view.findViewById(R.id.continueBtn);
        questionText = view.findViewById(R.id.img_question_text);
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

                }else{
                    Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), currentQuestion.getAnswer(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Continue button click listener
        continueBtn.setOnClickListener(v -> {
//            if (currentQuestionIndex < questions.size() - 1) {
//                currentQuestionIndex++;
//                currentQuestion = updateQuestion();
//            }
            if(questionCount < 5){
                currentQuestionIndex++;
                questionCount++;
                currentQuestion = updateQuestion();
            }else {
                optionsGroup = getView().findViewById(R.id.optionsGroupImg);
                questionText.setText("You've completed all tasks!");
                optionsGroup.setVisibility(View.GONE);

                continueBtn.setVisibility(View.GONE);
                checkBtn.setVisibility(View.GONE);

                finishBtn = getView().findViewById(R.id.finishBtn);
                finishBtn.setVisibility(View.VISIBLE);
                setupFinishButton(view);

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

        // TODO Reset selected option here


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
        });
    }
}
