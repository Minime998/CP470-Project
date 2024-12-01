package com.example.camlingo;

import android.text.TextUtils;
import android.util.Patterns;

public class FormValidator {
    public FormValidator(){}

    private static boolean isValidEmail(String email){
        // validate email by checking it's not empty and is correct format
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private static boolean isValidPassword(String password){
        // validate password by checking that it's not empty string
        return (!TextUtils.isEmpty(password) && password.length() >= 6);
    }

    public boolean checkPassword(String password){
        return  isValidPassword(password);
    }

    public boolean checkUEmail(String email){
        return isValidEmail(email);
    }
}
