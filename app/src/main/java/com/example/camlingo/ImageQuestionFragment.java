package com.example.camlingo;

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
import android.widget.TextView;

import model.MultipleChoiceQuestion;
import repository.QuestionRepository;


public class ImageQuestionFragment extends Fragment {
    private Button checkBtn;
    private Button continueBtn;
    private RadioButton option1, option2, option3, option4;



    private MultipleChoiceQuestion question;

    public ImageQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_question, container, false);

        // get the question from arguments passed to fragment
        if(getArguments() != null){
            question = (MultipleChoiceQuestion) getArguments().getSerializable("question");
        }

        if(question != null) {

            // Initialize Views and Buttons
            TextView questionText = view.findViewById(R.id.img_question_text);
            ImageView questionImage = view.findViewById(R.id.question_img);
            option1 = view.findViewById(R.id.img_option1);
            option2 = view.findViewById(R.id.img_option2);
            option3 = view.findViewById(R.id.img_option3);
            option4 = view.findViewById(R.id.img_option4);
            checkBtn = view.findViewById(R.id.checkBtn);
            continueBtn = view.findViewById(R.id.continueBtn);

            // update UI elements with question data
            questionText.setText(question.getQuestion());
            option1.setText(question.getOptions()[0]);
            option2.setText(question.getOptions()[1]);
            option3.setText(question.getOptions()[2]);
            option4.setText(question.getOptions()[3]);

            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continueBtn.setVisibility(View.VISIBLE);
                    checkBtn.setVisibility(View.GONE);
                }
            });
        }

        return view;
    }

    public void changeBackground(View v){
        v.setBackgroundColor(R.color.gold);
    }

}