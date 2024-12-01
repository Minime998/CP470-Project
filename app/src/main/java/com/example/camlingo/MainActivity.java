package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.widget.PopupMenu;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.FireBaseQuestionLoader;

public class MainActivity extends AppCompatActivity {

    private CardView continueLearningCard, dailyQuestsCard, leaderboardCard;
    private  FirebaseAuth mAuth;
    private TextView userNameTxtView;
    private FirebaseUser user;
    private final String emailRegex =  "^([^@]+)"; // Match everything before '@';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference parent activity
        View parentLayout = findViewById(R.id.mainActivity);

        // initialize auth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userNameTxtView = findViewById(R.id.userName);

        // if current user is null redirect user to login activity
        if(user == null){
            // start login activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(user.getEmail());

            if (matcher.find()){
                String userName = matcher.group(1);
                userNameTxtView.setText(userName); // Get the first capturing group
                String message = "Welcome back " + userName + "!";
                Snackbar.make(findViewById(R.id.cardContainer), message, Snackbar.LENGTH_SHORT)
                        .setAnchorView(findViewById(R.id.cardContainer))
                        .setAction("Action", null)
                        .show();
            }
        }

        ImageView animated_camera = findViewById(R.id.camera_gif);
        Glide.with(this)
                .load(R.drawable.camera)
                .into(animated_camera);

        ImageView animated_bag = findViewById(R.id.bag_gif);
        Glide.with(this)
                .load(R.drawable.backpack)
                .into(animated_bag);

        ImageView animated_leaderboard = findViewById(R.id.leaderboard_gif);
        Glide.with(this)
                .load(R.drawable.leaderboard)
                .into(animated_leaderboard);

        // Find CardViews by ID
        continueLearningCard = findViewById(R.id.continue_learning_card);
        dailyQuestsCard = findViewById(R.id.daily_quests_card);
        leaderboardCard = findViewById(R.id.leaderboard_card);

        // daily question questions
        FireBaseQuestionLoader qloader = new FireBaseQuestionLoader(this);
        qloader.loadQuestions();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Clear default title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        dailyQuestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "DailyQuest Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DailyQuestActivity.class));
            }
        });

        //ContinueLearning button click
        continueLearningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //no activity to open yet, just a placeholder here
                Intent intent = new Intent(MainActivity.this, image_upload.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "continueLearningCard Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //leader board card button click
        leaderboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no activity to open yet, just a placeholder here
                //Intent intent = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(intent);
                Toast.makeText(MainActivity.this, "leaderboardCard Clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            // Handle setting action
            return true;
        }

        // logout the user when they click logout
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            // Handle logout action
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}