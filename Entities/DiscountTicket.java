package Entidades;

public class DiscountTicket {

    private String company;
    private String description;
    private int requiredPoints;

    public DiscountTicket(String company, String description, int requiredPoints) {
        this.company = company;
        this.description = description;
        this.requiredPoints = requiredPoints;
    }

    // Getters
    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    // Setters
    public void setCompany(String company) {
        this.company = company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    @Override
    public String toString() {
        return company + " - " + description + " (" + requiredPoints + " pts)";
    }
}
