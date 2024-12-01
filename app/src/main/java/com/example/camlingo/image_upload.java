package com.example.camlingo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

//import com.bumptech.glide.Glide;
//import com.example.camlingo.gpt_api.OpenAI;
//import com.example.camlingo.gpt_api.gpt_api;

//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;


public class image_upload extends AppCompatActivity {
    private ImageButton cameraButton;
    private TextView textView4;
    private Bitmap imageToIdentify;
    private String engText;
    private String freText;
    String fileName = "testImg1.jpg";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //String API_URL = "https://api.openai.com/v1/chat/completions";
    //String API_KEY = "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";
    //private static final String BASE_URL = "https://api.openai.com/v1/models/gpt-4o-mini/predict";
    //private static final String API_KEY = "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"; // Replace with your actual API key


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

        //grant camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }


        // Initialize Buttons
        Button identifyButton = findViewById(R.id.button_identify);
        Button btnTranslateEnglish = findViewById(R.id.btnTranslateEnglish);
        Button btnTranslateFrench = findViewById(R.id.btnTranslateFrench);
        textView4 = findViewById(R.id.textView4);

        cameraButton = findViewById(R.id.imageButton2);
        imageToIdentify = AssetUtilities.assetsToBitmap(this,fileName);
        if(imageToIdentify != null){
            cameraButton.setImageBitmap(imageToIdentify);
        }



        // onClickListener Setup
        identifyButton.setOnClickListener(view -> identifyImage());
        btnTranslateEnglish.setOnClickListener(view -> showEnglishDescription());
        //btnTranslateFrench.setOnClickListener(view -> translateToFrench());
        cameraButton.setOnClickListener(view -> openGallery());


    }
    //IdentifyImage function for identifyButton
    private void identifyImage() {
        if (imageToIdentify == null){
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();

        }

        ImageLabeler lbler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        InputImage mlInputImage = InputImage.fromBitmap(imageToIdentify,0);
        StringBuilder txtOutput = new StringBuilder();

        lbler.process(mlInputImage)
                .addOnSuccessListener(labels ->{
                    if(labels.isEmpty()){
                        textView4.setText("No Objects Identified");
                    } else {
                        ImageLabel highConfidence = labels.get(0);
                        for (ImageLabel label : labels){
                            if(label.getConfidence() > highConfidence.getConfidence()){
                                highConfidence = label;
                            }
                        }
                        String tmp = highConfidence.getText();
                        float confidence = highConfidence.getConfidence();
                        txtOutput.append("Object in the image is a: ").append(tmp);//.append(" : "); //.append(confidence).append("\n");
                    }

                    textView4.setText(txtOutput.toString());

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,"Identification Failed",Toast.LENGTH_SHORT).show();
                });

    }


    //Translate to English
    private void showEnglishDescription() {

    }

    //openGallery function for cameraButton
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}