package com.example.camlingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import model.GlobalUserCache;


public class DailyQuestActivity extends AppCompatActivity {
    private static final String TAG = "DailyQuestActivity";
    private Button claimButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_quest);

        // init views
        claimButton = findViewById(R.id.claim_button_DailyQuest);
        Button starQuestButton = findViewById(R.id.startQuest_button_DailyQuest);
        ImageView treasureChest = findViewById(R.id.questHomeIcon);
        TextView loginStreak = findViewById(R.id.login_streak);

        // check if user can claim login rewards
//        claimButton.setEnabled(shouldClaimReward());

        long streakCount = GlobalUserCache.getCurrentUser().getStreak();
        String streakText;
        if (streakCount > 1){
            streakText = String.valueOf(streakCount) + " Days";
        }else {
            streakText = String.valueOf(streakCount) + " Day";
        }
        loginStreak.setText(String.valueOf(streakText));

        // show treasure chest icon
        Glide.with(treasureChest)
                .load("https://firebasestorage.googleapis.com/v0/b/camlingo-app.firebasestorage.app/o/treasure-chest.png?alt=media&token=f2781c98-a752-49b4-836d-ca80e64fea52")
                .into(treasureChest);

        // "Claim Now" button click
        claimButton.setOnClickListener(v ->{
            displayCustomDialog();

            FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            long dailyLoginPoint = 50;
                            Long points = documentSnapshot.getLong("points");
                            points += dailyLoginPoint;
                            db.collection("users").document(userId).update("points", points);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user data", e));
        } );

        // Start today's quest button click
        starQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyQuestActivity.this, DailyQuestsTasks.class);
                startActivity(intent);
                Toast.makeText(DailyQuestActivity.this, "StarQuestButton Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daily_quests), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private boolean shouldClaimReward(){
        long lastLoginTimestamp = GlobalUserCache.getCurrentUser().getLastLogin();

        // Calculate the time difference in days
        long currentTimestamp = System.currentTimeMillis();
        long diffInMillis = currentTimestamp - lastLoginTimestamp;
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);; // Convert millis to days

        Log.i("Updating streak time", String.valueOf(diffInDays));

        // Check if the user logged in on consecutive days
        if (diffInDays >= 1) {
            // allow user tp claim login points
            return true;

        } else {
            // prevent user from claiming login points
            return false;
        }
    }

    public void displayCustomDialog(){

        LayoutInflater inflater = getLayoutInflater();
        View customDialog = inflater.inflate(R.layout.login_rewards_dialog_layout, null);
        ImageView reward_icon = customDialog.findViewById(R.id.reward_icon);
        Button continue_btn = customDialog.findViewById(R.id.dialog_continue_button);

        Glide.with(reward_icon)
                .load("https://firebasestorage.googleapis.com/v0/b/camlingo-app.firebasestorage.app/o/treasure-chest.png?alt=media&token=f2781c98-a752-49b4-836d-ca80e64fea52")
                .into(reward_icon);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customDialog);
        AlertDialog dialog = builder.create();
        dialog.show();

        continue_btn.setOnClickListener(view -> {
            dialog.dismiss();
        });

    }
}