package Entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String password;
    private int points;
    private List<Activity> completedActivities;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.points = 0;
        this.completedActivities = new ArrayList<>();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public List<Activity> getCompletedActivities() {
        return completedActivities;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Functionality
    public void addActivity(Activity activity) {
        completedActivities.add(activity);
        points += activity.getAwardedPoints();
    }

    public boolean redeemPoints(DiscountTicket d) {
        if (points >= d.getRequiredPoints()) {
            points -= d.getRequiredPoints();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " - Points: " + points;
    }
    public String getUsername() {
        return name;
    }
}