package com.example.camlingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import model.LessonModel;
import repository.GetLessonListFromFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText UsernameEditText;
    private EditText PassWEditText;
    private EditText PassWEditText2;
    private SharedPreferences prefs;
    private FirebaseAuth mAuth;
    ArrayList<LessonModel> lessonModels = new ArrayList<>();

    public static String TAG = "RegisterActivity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i(TAG, "User already logged in");
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // reference parent activity
        View parentLayout = findViewById(R.id.registerActivity);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance("camlingo");

        GetLessonListFromFirestore repository = new GetLessonListFromFirestore();
        repository.getLessons(new GetLessonListFromFirestore.OnLessonsFetchedListener() {
            @Override
            public void onLessonsFetched(ArrayList<LessonModel> lessons) {
                if (lessons.isEmpty()) {
                    Log.i("LessonList", "No lessons found!");
                } else {
                    for (LessonModel lesson : lessons) {
                        Log.i("LessonList", "Lesson: " + lesson.toString());
                        lessonModels.add(lesson);                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("LessonList", "Error fetching lessons", e);
            }
        });


        Button loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_register);
        UsernameEditText = findViewById(R.id.username_input);
        TextView userEmailText = findViewById(R.id.username_email);
        PassWEditText = findViewById(R.id.password_input);
        PassWEditText2 = findViewById(R.id.password_input2);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        FormValidator formValidator = new FormValidator();

        //when the login button is pressed
        registerButton.setOnClickListener(view -> {
            String email = String.valueOf(userEmailText.getText());
            String password1 = String.valueOf(PassWEditText.getText());
            String password2 = String.valueOf(PassWEditText2.getText());
            String username = String.valueOf(UsernameEditText.getText());

            // fetch lessons from "lessons"

            // simple form validation
            if(!formValidator.checkUsername(username)){
                Toast toast = Toast.makeText(this, "Invalid username. 3 chars min",Toast.LENGTH_LONG);
                toast.show();
            }
            else if(!formValidator.checkUEmail(email)){
                Toast toast = Toast.makeText(this, "Invalid email",Toast.LENGTH_LONG);
                toast.show();
            }
            else if(!formValidator.checkPassword(password1)){
                Toast toast = Toast.makeText(this, "Invalid password. 6 chars min",Toast.LENGTH_LONG);
                toast.show();
            }else if(!password1.equals(password2) || !formValidator.checkPassword(password2)){
                Toast toast = Toast.makeText(this, "Password mismatch",Toast.LENGTH_LONG);
                toast.show();
            }else {
                mAuth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Map<String, Integer> progress = new HashMap<>();
                                    if (user != null) {
                                        for (LessonModel lesson : lessonModels){
                                            progress.put(lesson.getLessonName(), 0);
                                        }
                                        // Create an associated Firestore document for each user
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("points", 250); // everyone starts with 250 points
                                        userData.put("progress", progress);
                                        userData.put("streak", 1);
                                        userData.put("username", username);

                                        long currentTimestamp = System.currentTimeMillis();
                                        userData.put("lastLogin", currentTimestamp);

                                        db.collection("users").document(user.getUid())
                                                        .set(userData)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    Log.d("Firestore", "User data added successfully!");
                                                                })
                                                                .addOnFailureListener(e ->{
                                                                    Log.d("Firestore", "Error adding user data", e);
                                                                });


                                        Toast.makeText(RegisterActivity.this, "Successfully Registered.",
                                                Toast.LENGTH_SHORT).show();

                                        // redirect user to login
                                        // start login activity
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginButton.setOnClickListener(view -> {
            // start the register activity
            Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

}