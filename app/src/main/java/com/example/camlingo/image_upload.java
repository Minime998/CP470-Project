package com.example.camlingo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.bumptech.glide.Glide;
//import com.example.camlingo.gpt_api.OpenAI;
//import com.example.camlingo.gpt_api.gpt_api;

//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;


public class image_upload extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton cameraButton;
    private Bitmap capturedBitmap;//store the captured image

    private ImageView displayImageView; // To display the captured image

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

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.Image_upload_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //grant camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        // Initialize ImageButton
        cameraButton = findViewById(R.id.imageButton2);

        // Set OnClickListener for the ImageButton
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });


        //Glide.with(this).load(IMG_URL).into(identify_view);

        //identify button click
        Button identify_button = findViewById(R.id.button_identify);
        identify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this part is for handling the image being uploaded
                //something like
                if (capturedBitmap != null) {
                    // Call sendImage with the captured bitmap
                    sendImage(capturedBitmap);
                } else {
                    Log.e(TAG, "No image captured to identify");
                }


                //the following code are original from before TTDoorTT edit
                //sendImage(IMG_URL);

            }
        });

    }

    // Handle the result of the camera intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                capturedBitmap = (Bitmap) data.getExtras().get("data");
                if (capturedBitmap != null) {
                    // Set the captured image as the ImageButton content
                    cameraButton.setImageBitmap(capturedBitmap);
                }
            } else {
                Log.e(TAG, "No image data returned from camera");
            }
        }
    }


    public void sendImage(Bitmap imageBitmap){
        Log.i(TAG, "sendImage: ");
        Toast.makeText(image_upload.this, "Identify object photo Clicked", Toast.LENGTH_SHORT).show();
    }

}