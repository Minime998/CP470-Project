package com.example.camlingo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Objects;

import model.GlobalUserCache;
import utils.User;

public class MyLeaningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_leaning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init views
        TextView userNameTxtView = findViewById(R.id.userName);
        CardView english_basics_card = findViewById(R.id.english_basics_card);
        CardView picture_learning_card = findViewById(R.id.picture_learning_card);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Clear default title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // load user info in tool bar
        User user = GlobalUserCache.getCurrentUser();
        userNameTxtView.setText(user.getUserName());


        ImageView animated_camera = findViewById(R.id.camera_gif);
        Glide.with(this)
                .load(R.drawable.camera)
                .into(animated_camera);

        ImageView animated_bag = findViewById(R.id.bag_gif);
        Glide.with(this)
                .load(R.drawable.backpack)
                .into(animated_bag);

        // Click on english basics card
        english_basics_card.setOnClickListener(v ->{
            startActivity(new Intent(MyLeaningActivity.this, LessonsRecyclerViewActivity.class));
        });

        // Click on picture learning
        picture_learning_card.setOnClickListener(v -> {
            startActivity(new Intent(MyLeaningActivity.this, image_upload.class));
        });
    }


}