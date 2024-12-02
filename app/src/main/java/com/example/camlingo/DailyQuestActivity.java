package com.example.camlingo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import java.io.File;


public class DailyQuestActivity extends AppCompatActivity {
    private static final String TAG = "DailyQuestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_quest);

        //find button by id
        Button claimButton = findViewById(R.id.claim_button_DailyQuest);
        Button starQuestButton = findViewById(R.id.startQuest_button_DailyQuest);
        ImageView treasureChest = findViewById(R.id.questHomeIcon);

        Glide.with(treasureChest)
                .load("https://firebasestorage.googleapis.com/v0/b/camlingo-app.firebasestorage.app/o/treasure-chest.png?alt=media&token=f2781c98-a752-49b4-836d-ca80e64fea52")
                .into(treasureChest);

        //"Claim Now" button click
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no activity to open yet, just a placeholder here
                //Intent intent = new Intent(DailyQuestActivity.this, DailyQuestActivity.class);
                //startActivity(intent);
                Toast.makeText(DailyQuestActivity.this, "ClaimButton Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Start today's quest button click
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

    public boolean fileExists(String fileName){
        File file = getApplicationContext().getFileStreamPath(fileName);
        return file.exists();
    }
}