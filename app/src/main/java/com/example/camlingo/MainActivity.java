package com.example.camlingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import database.FireBaseQuestionLoader;
import model.GlobalUserCache;
import utils.User;

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
        LinearLayout screen_loader = findViewById(R.id.screen_loader);
        TextView points = findViewById(R.id.points);

        // Show the loading spinner
        screen_loader.setVisibility(View.VISIBLE);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // cache user info
        fetchUserData(userId, user -> {
            // Hide the spinner
            screen_loader.setVisibility(View.GONE);

            if (user != null) {
                // Update UI with user data
                userNameTxtView.setText(user.getUserName());
                points.setText(String.valueOf(user.getPoints()));
                Log.i("MainActivity", "username: " + user.getUserName());
            } else {
                // Handle null user
                userNameTxtView.setText(R.string.error_loading_user);
                Log.e("MainActivity", "User data not loaded");
            }
        });




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

        dailyQuestsCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DailyQuestActivity.class)));

        // My Learning button click
        myLearningCard.setOnClickListener(v -> {
            //no activity to open yet, just a placeholder here
            Intent intent = new Intent(MainActivity.this, MyLeaningActivity.class);
            startActivity(intent);
        });

        // Leader board card button click
        leaderboardCard.setOnClickListener(v -> Toast.makeText(MainActivity.this, "leaderboardCard Clicked", Toast.LENGTH_SHORT).show());

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

        // logout the user when they click logout
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            // Handle logout action
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == R.id.about) {
            // Handle about action
            AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(this);
            View customView = getLayoutInflater().inflate(R.layout.about_author_dialog, null);
            customDialogBuilder.setView(customView);
            customDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                // Dismiss the dialog
                dialog.dismiss();
            });
            AlertDialog aboutDialog = customDialogBuilder.create();
            aboutDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // callback to nofify when user data is successfully fetched
    public interface OnUserDataLoadedListener {
        void onUserDataLoaded(User user);
    }

    public void fetchUserData(String userId, OnUserDataLoadedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Map Firestore document to User object
                        User user = documentSnapshot.toObject(User.class);

                        // Cache the user globally
                        GlobalUserCache.setCurrentUser(user);
                        Log.i("MainActivity", "User object: " + user.toString());

                        // Get the last login timestamp from Firestore
                        Long lastLoginTimestamp = documentSnapshot.getLong("lastLogin");

                        Log.i("Updating streak time. last login was: ", String.valueOf(lastLoginTimestamp));

                        if (lastLoginTimestamp != null) {
                            // Calculate the time difference in days
                            long currentTimestamp = System.currentTimeMillis();
                            long diffInMillis = currentTimestamp - lastLoginTimestamp;
                            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24); // Convert millis to days

                            Log.i("Updating streak time", String.valueOf(diffInMillis));

                            // Check if the user logged in on consecutive days
                            if (diffInDays == 1) {
                                // Increment streak if login was on consecutive days
                                Long streak = documentSnapshot.getLong("streak");
                                Log.i("Updating streak here", String.valueOf(streak));
                                streak++;
                                db.collection("users").document(userId).update("streak", streak);
                            } else if (diffInDays > 1) {
                                // Reset streak if the user missed more than one day
                                db.collection("users").document(userId).update("streak", 1);
                            }

                            // Update the last login time (adjusting for testing with 2-minute difference)
                            db.collection("users").document(userId).update("lastLogin", currentTimestamp - 120000);
                        }

                        // Notify the listener
                        if (listener != null) {
                            listener.onUserDataLoaded(user);
                        }
                    } else {
                        Log.w("Firestore", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
    }

}