package Entities;


public class Activity {

    private String name;
    private String description;
    private int awardedPoints;

    public Activity(String name, String description, int awardedPoints) {
        this.name = name;
        this.description = description;
        this.awardedPoints = awardedPoints;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAwardedPoints() {
        return awardedPoints;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAwardedPoints(int awardedPoints) {
        this.awardedPoints = awardedPoints;
    }

    @Override
    public String toString() {
        return name + " (" + awardedPoints + " pts)";
    }
}
