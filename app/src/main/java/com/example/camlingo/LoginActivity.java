package com.example.camlingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.GlobalUserCache;
import utils.User;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText UsernameEditText;
    private EditText PassWEditText;
    private SharedPreferences prefs;
    private FirebaseAuth mAuth;
    public static String TAG = "LoginActivity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i(TAG, "User already logged in");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // reference parent activity
        View parentLayout = findViewById(R.id.loginActivity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);
        UsernameEditText = findViewById(R.id.username_input);
        PassWEditText = findViewById(R.id.password_input);


        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String emailSaved = prefs.getString("DefaultEmail", "user@example.com");
        UsernameEditText.setText(emailSaved);

        FormValidator formValidator = new FormValidator();

        //when the login button is pressed
        loginButton.setOnClickListener(view -> {

            String email = String.valueOf(UsernameEditText.getText());
            String password = String.valueOf(PassWEditText.getText());

            // login form validation
            if(!formValidator.checkUEmail(email)){
                Toast toast = Toast.makeText(this, "Invalid email",Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(!formValidator.checkPassword(password)) {
                Toast toast = Toast.makeText(this, "Invalid password. 6 chars min", Toast.LENGTH_SHORT);
                toast.show();
            }else {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");

                                    // start main activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                                    Toast.makeText(LoginActivity.this, "Authentication failed. User not found",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        registerButton.setOnClickListener(view -> {
            // start the register activity
            Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}