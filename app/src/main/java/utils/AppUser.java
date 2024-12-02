package utils;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.example.camlingo.LoginActivity;
import com.example.camlingo.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUser {
    private Context context;
    private FirebaseAuth mAuth;
    private final String emailRegex =  "^([^@]+)"; // Match everything before '@';

    public AppUser(Context ctx){
        this.context = ctx;
        mAuth = FirebaseAuth.getInstance();
    }

    public void getUserInfo(TextView userNameTxtView, boolean isMainActivity) {
        // initialize auth
        FirebaseUser user = mAuth.getCurrentUser();

        // if current user is null redirect user to login activity
        if (user == null) {
            // start login activity
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

            if (context instanceof MainActivity) {
                ((MainActivity) context).finish();
            }
        } else {
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(user.getEmail());

            if (matcher.find()) {
                String userName = matcher.group(1);
                userNameTxtView.setText(userName); // Get the first capturing group
                String message = "Welcome back " + userName + "!";

                if (isMainActivity) {
                    Snackbar.make(userNameTxtView.getRootView(), message, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();
                }
            }
        }
    }
}
