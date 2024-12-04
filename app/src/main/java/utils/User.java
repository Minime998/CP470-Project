package utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;

    private long points;
    private long streak;
    private long lastLogin;
    private Map<String, Integer> progress;

    // Default constructor required by Firestore
    public User() {}

    public User(String username, long points, long streak, long lastLogin, Map<String, Integer> progress) {
        this.username = username;
        this.points = points;
        this.streak = streak;
        this.progress = progress;
        this.lastLogin = lastLogin;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUserName() {
        return username;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getStreak() {
        return streak;
    }

    public void setStreak(long streak) {
        this.streak = streak;
    }

    public Map<String, Integer> getProgress() {
        return Collections.unmodifiableMap(progress);
    }

    public void setProgress(Map<String, Integer> progress) {
        this.progress = new HashMap<>(progress);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", points=" + points +
                ", streak=" + streak +
                ", progress=" + progress +
                ", lastLogin = " + lastLogin +
                '}';
    }
}
