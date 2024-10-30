package com.example.camlingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText UsernameEditText;
    private EditText PassWEditText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.button_login);
        UsernameEditText = findViewById(R.id.username_input);
        PassWEditText = findViewById(R.id.password_input);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        //preserve username so it will appear next time
        String inputusername = prefs.getString("DefaultEmail", "email@domain.com");
        UsernameEditText.setText(inputusername);

        //when the login button is pressed
        loginButton.setOnClickListener(view -> {
            String user = UsernameEditText.getText().toString();
            String password = PassWEditText.getText().toString();

            //place for checking user and password validation

            //preserved username
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("DefaultUserName", user);
            edit.apply();

            //start login activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}