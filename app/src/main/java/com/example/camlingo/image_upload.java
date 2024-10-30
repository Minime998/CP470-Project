package com.example.camlingo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.camlingo.gpt_api.OpenAI;
import com.example.camlingo.gpt_api.gpt_api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class image_upload extends AppCompatActivity {
    private static final String TAG = image_upload.class.getSimpleName();
    String API_URL = "https://api.openai.com/v1/models/gpt-4o-mini/predict";
    String API_KEY = "Sanitized";
    String IMG_URL = "https://images.pexels.com/photos/53977/eagle-owl-raptor-falconry-owl-53977.jpeg?cs=srgb&dl=pexels-pixabay-53977.jpg&fm=jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_upload);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView identify_view = findViewById(R.id.imageView3);
        Glide.with(this).load(IMG_URL).into(identify_view);

        Button identify_button = findViewById(R.id.button_identify);
        identify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage(IMG_URL);
            }
        });

    }
    public void sendImage(String imgURL){
        Log.i(TAG, "sendImage: ");
    }

}