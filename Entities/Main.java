package Entities;

import java.util.Scanner;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Main {
    // Map to track the list of discounts acquired by each user (since User class cannot be modified)
    private static final Map<User, List<DiscountTicket>> ACQUIRED_DISCOUNTS_MAP = new HashMap<>();

    public static void main(String[] args) {
        // --- Initialization ---
        AuthManager auth = new AuthManager();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;
        boolean appRunning = true;

        // ** Initial Data Setup (Activities and Discounts) **

        // Standard test users
        auth.registerUser("juan", "1234");
        auth.registerUser("maria", "abcd");
        auth.registerUser("user0", "pass0");
        auth.registerUser("user500", "pass500");
        auth.registerUser("userInf", "passInf");

        // Discount/Ticket items (used for BT option, cost = required points)
        DiscountTicket discount1 = new DiscountTicket("Amazon", "20% off Amazon brand X", 50);
        DiscountTicket discount2 = new DiscountTicket("Nike", "20% off Nike specific shoe", 40);
        DiscountTicket discount3 = new DiscountTicket("LocalStore", "$30 Voucher for Local Store", 30);

        // Activity items (used for DA option, points awarded, high points for easy testing)
        // Puntos altos para facilitar el canjeo
        Activity activity1 = new Activity("Reduce Carbon Footprint", "Complete 10 public transport trips", 100);
        Activity activity2 = new Activity("Participate in Local Cleanup", "Plant a tree", 70);
        Activity activity3 = new Activity("Compost Organic Waste", "Participate in a volunteer activity", 90);


        // Points adjustments for test users (silent setup):
        User u500 = auth.login("user500", "pass500");
        if (u500 != null && u500.getPoints() == 0) {
            u500.addActivity(new Activity("Initial Setup", "Initial points", 500));
        }
        User uInf = auth.login("userInf", "passInf");
        if (uInf != null && uInf.getPoints() == 0) {
            try {
                // Use Reflection to set high points (simulates infinity)
                Field pointsField = User.class.getDeclaredField("points");
                pointsField.setAccessible(true);
                pointsField.setInt(uInf, 1_000_000);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Keep silent if reflection fails
            }
        }
        // *******************************************************


        System.out.println("Initializing application...");


        // --- Main Application Loop ---
        while (appRunning) {

            // --- Authentication Menu (When NO user is logged in) ---
            if (currentUser == null) {
                // Display options clearly
                System.out.print("\nCreate Account (CA), Login (L) or Exit (S): ");
                String initialChoice = scanner.nextLine().toUpperCase();

                String username, password;

                switch (initialChoice) {
                    case "CA":
                        // Option: Create Account
                        System.out.print("Enter username: ");
                        username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();

                        // Try to log in with the new credentials. If successful, user already exists.
                        if (auth.login(username, password) != null) {
                            System.out.println("Error: User already exists. Please log in.");
                        } else {
                            // User not found with these credentials. Attempt registration.
                            auth.registerUser(username, password);

                            // Check if the user was successfully registered (by trying to log in again).
                            if (auth.login(username, password) != null) {
                                System.out.println("Account created successfully! Please log in.");
                            } else {
                                // If login fails after registration, the username must have existed already with a different password.
                                System.out.println("Error: Username already in use. Please log in with your existing password or choose a new username.");
                            }
                        }

                        break;

                    case "L":
                        // Option: Login
                        System.out.print("username: ");
                        username = scanner.nextLine();
                        System.out.print("password: ");
                        password = scanner.nextLine();

                        currentUser = auth.login(username, password);

                        if (currentUser == null) {
                            System.out.println("Error: Incorrect username or password. Try again.");
                        } else {
                            System.out.println("Welcome " + currentUser.getName() + "!");
                        }
                        break;

                    case "S":
                        // Option: Exit application completely
                        appRunning = false;
                        System.out.println("Closing application. Goodbye!");
                        break;

                    default:
                        System.out.println("Unrecognized option. Please type 'CA', 'L', or 'S'.");
                        break;
                }

            } else {
                // --- Main Menu (When user IS logged in) ---

                // Get the list of acquired discounts for the current user
                List<DiscountTicket> currentUserAcquiredDiscounts = ACQUIRED_DISCOUNTS_MAP.computeIfAbsent(currentUser, k -> new ArrayList<>());

                // Display options directly in the prompt
                System.out.print("\nView Profile (VP), Consult Wallet (CW), Buy Discount (BT), Do Activity (DA), Logout (S): ");
                String choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case "VP":
                        // View Profile
                        System.out.println("\n--- USER PROFILE (VP) ---");
                        System.out.println("User: " + currentUser.getName());
                        System.out.println("Available Points: " + currentUser.getPoints());
                        System.out.println("Completed Activities: " + currentUser.getCompletedActivities().size());
                        break;

                    case "CW":
                        // Consult Wallet / Redeem Acquired Discounts
                        System.out.println("\n--- CONSULT WALLET (CW) ---");
                        System.out.println("You have " + currentUser.getPoints() + " available points.");

                        // Show acquired discounts (purchased via BT)
                        if (currentUserAcquiredDiscounts.isEmpty()) {
                            System.out.println("\nNo discounts currently acquired to redeem.");
                        } else {
                            System.out.println("\nAcquired Discounts Ready to Use (No points cost):");
                            for (int i = 0; i < currentUserAcquiredDiscounts.size(); i++) {
                                DiscountTicket d = currentUserAcquiredDiscounts.get(i);
                                System.out.println((i + 1) + ". " + d.getCompany() + " - " + d.getDescription());
                            }

                            System.out.print("\nWhich discount do you want to use now? (Enter number or 'N' for no): ");
                            String useChoice = scanner.nextLine();

                            if (!useChoice.equalsIgnoreCase("N")) {
                                try {
                                    int index = Integer.parseInt(useChoice) - 1;
                                    if (index >= 0 && index < currentUserAcquiredDiscounts.size()) {
                                        DiscountTicket usedDiscount = currentUserAcquiredDiscounts.remove(index);
                                        System.out.println("Discount '" + usedDiscount.getCompany() + "' used successfully! You can apply this benefit now.");
                                        System.out.println("It has been removed from your wallet.");
                                    } else {
                                        System.out.println("Invalid discount number.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number or 'N'.");
                                }
                            }
                        }
                        break;

                    case "BT":
                        // Buy Ticket / Acquire Discount (Spends points)
                        System.out.println("\n--- ACQUIRE DISCOUNT (BT - Purchase Points) ---");
                        System.out.println("Available Discounts to Purchase (Points Cost):");
                        System.out.println("1. " + discount1.getCompany() + " - " + discount1.getDescription() + " - Cost: " + discount1.getRequiredPoints() + " points");
                        System.out.println("2. " + discount2.getCompany() + " - " + discount2.getDescription() + " - Cost: " + discount2.getRequiredPoints() + " points");
                        System.out.println("3. " + discount3.getCompany() + " - " + discount3.getDescription() + " - Cost: " + discount3.getRequiredPoints() + " points");
                        System.out.println("Your current balance is: " + currentUser.getPoints() + " points.");


                        System.out.print("\nChoose one to purchase (1, 2, or 3): ");
                        String purchaseChoice = scanner.nextLine();

                        DiscountTicket selectedDiscount = null;
                        if (purchaseChoice.equals("1")) {
                            selectedDiscount = discount1;
                        } else if (purchaseChoice.equals("2")) {
                            selectedDiscount = discount2;
                        } else if (purchaseChoice.equals("3")) {
                            selectedDiscount = discount3;
                        }

                        if (selectedDiscount != null) {
                            int cost = selectedDiscount.getRequiredPoints();

                            if (currentUser.redeemPoints(selectedDiscount)) {
                                // Purchase successful! Add the acquired discount to the user's list
                                currentUserAcquiredDiscounts.add(selectedDiscount);
                                System.out.println("Success! You purchased 1x " + selectedDiscount.getCompany() + " for " + cost + " points!");
                                System.out.println("It has been added to your wallet (CW) ready to be used.");
                                System.out.println("Remaining Balance: " + currentUser.getPoints() + " points.");
                            } else {
                                System.out.println("Error: You do not have enough points to purchase this discount. Cost: " + cost + ". You have: " + currentUser.getPoints());
                            }
                        } else {
                            System.out.println("Invalid discount option.");
                        }
                        break;

                    case "DA":
                        // Do Activity (earn points)
                        System.out.println("\n--- DO ACTIVITY (DA - EARN POINTS) ---");
                        System.out.println("Eco-Sustainable Activities that award points:");
                        System.out.println("1. " + activity1.getName() + " - Earns " + activity1.getAwardedPoints() + " points");
                        System.out.println("2. " + activity2.getName() + " - Earns " + activity2.getAwardedPoints() + " points");
                        System.out.println("3. " + activity3.getName() + " - Earns " + activity3.getAwardedPoints() + " points");
                        System.out.print("Select one to earn points (1, 2, or 3): ");
                        String activityChoice = scanner.nextLine();

                        Activity earnedActivity = null;
                        if (activityChoice.equals("1")) {
                            earnedActivity = activity1;
                        } else if (activityChoice.equals("2")) {
                            earnedActivity = activity2;
                        } else if (activityChoice.equals("3")) {
                            earnedActivity = activity3;
                        }

                        if (earnedActivity != null) {
                            currentUser.addActivity(earnedActivity);
                            System.out.println("You completed '" + earnedActivity.getName() + "' and earned " + earnedActivity.getAwardedPoints() + " points!");
                            System.out.println("Your new total points is: " + currentUser.getPoints());
                        } else {
                            System.out.println("Invalid activity option.");
                        }
                        break;

                    case "S":
                        // Logout (returns to authentication menu)
                        System.out.println("Logging out " + currentUser.getName() + "...");
                        currentUser = null;
                        break;

                    default:
                        System.out.println("Unrecognized option. Please use one of the abbreviations: VP, CW, BT, DA, S.");
                        break;
                }
            }
        }

        scanner.close();
    }
}
