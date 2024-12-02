package com.example.camlingo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Objects;

import database.AppDatabaseHelper;
import database.FireBaseQuestionLoader;
import model.LessonModel;
import model.MultipleChoiceQuestion;
import utils.AppUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find CardViews by ID
        CardView myLearningCard = findViewById(R.id.continue_learning_card);
        CardView dailyQuestsCard = findViewById(R.id.daily_quests_card);
        CardView leaderboardCard = findViewById(R.id.leaderboard_card);
        TextView userNameTxtView = findViewById(R.id.userName);

        // load user info in tool bar
        AppUser user = new AppUser(MainActivity.this);
        user.getUserInfo(userNameTxtView,true); // true shows "welcome back snackbar"

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

        // daily question questions
        FireBaseQuestionLoader qloader = new FireBaseQuestionLoader(this);
        qloader.loadQuestions();


        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Clear default title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        dailyQuestsCard.setOnClickListener(v -> {
            //Toast.makeText(MainActivity.this, "DailyQuest Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, DailyQuestActivity.class));
        });

        // My Learning button click
        myLearningCard.setOnClickListener(v -> {
            //no activity to open yet, just a placeholder here
            Intent intent = new Intent(MainActivity.this, MyLeaningActivity.class);
            startActivity(intent);
        });

        // Leader board card button click
        leaderboardCard.setOnClickListener(v -> {
            //no activity to open yet, just a placeholder here
            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(intent);
            Toast.makeText(MainActivity.this, "leaderboardCard Clicked", Toast.LENGTH_SHORT).show();
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