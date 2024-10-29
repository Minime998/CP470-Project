package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String userName = "John Lingo"; // Replace with actual user name
    private CardView continueLearningCard, dailyQuestsCard, leaderboardCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find CardViews by ID
        continueLearningCard = findViewById(R.id.continue_learning_card);
        dailyQuestsCard = findViewById(R.id.daily_quests_card);
        leaderboardCard = findViewById(R.id.leaderboard_card);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Clear default title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Set the toolbar title to include user name
        toolbar.setTitle("Welcome " + userName);

        dailyQuestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyQuestActivity.class));
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
            // Handle settings action
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}