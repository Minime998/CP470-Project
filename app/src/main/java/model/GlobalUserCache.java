package model;

import java.util.Map;

import utils.User;

public class GlobalUserCache {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
