package personalized.fitnessplanrecommendationsystem;

import java.io.*;  
import java.util.HashMap;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PersonalizedFitnessPlanRecommendationSystem {

    // File name where user data is stored
    private static final String FILE_NAME = "users.txt";
    private static HashMap<String, String> users = new HashMap<>();

    public static void main(String[] args) {
        loadUsersFromFile();  // Load user data from file at startup

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display main menu
            System.out.println("Welcome to Fitness!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            // Validate menu choice input
            String input = scanner.nextLine().trim();
            while (!input.matches("[1-3]")) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                input = scanner.nextLine().trim();
            }
            choice = Integer.parseInt(input);

            // Handle user choice
            switch (choice) {
                case 1:
                    register(scanner);  // Call registration method
                    break;
                case 2:
                    login(scanner);  // Call login method
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        } while (choice != 3);  // Continue until user chooses to exit

        scanner.close();  // Close scanner
    }

    // Registration method 
    private static void register(Scanner scanner) {
        String username = "";
        boolean validInput = false;

        // Loop until valid username is entered
        while (!validInput) {
            System.out.print("Enter a username: ");
            username = scanner.nextLine().trim();

            // Validate username
            if (!CheckUserName_Validation(username)) {
                System.out.println("Invalid UserName! Only letters are allowed, and up to 20 characters. Do not use numbers, spaces, or symbols.");
            } else if (users.containsKey(username)) {
                System.out.println("Username already exists. Please try again.");
            } else {
                validInput = true;  // Valid username entered
            }
        }

        validInput = false;
        String password = "";

        // Loop until valid password is entered
        while (!validInput) {
            System.out.print("Enter a password: ");
            password = scanner.nextLine().trim();

            // Validate password
            if (!CheckPassword_Validation(password)) {
                System.out.println("Invalid password! Password must be exactly 8 characters long, containing a number, one lowercase letter, one uppercase letter, and one special character.");
            } else {
                validInput = true;  // Valid password entered
            }
        }

        // Hash password for security
        try {
            password = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password. Please try again.");
            return;
        }

        // Save user data in memory and file
        users.put(username, password);
        saveUserToFile(username, password);
        System.out.println("Registration successful!");
    }

    // Login method with 5 attempts
    private static void login(Scanner scanner) {
        String username = "";
        int attempts = 0;
        boolean validInput = false;

        // Loop until valid username is entered or attempts exhausted
        while (!validInput && attempts < 5) {
            System.out.print("Enter your username: ");
            username = scanner.nextLine().trim();

            // Validate username
            if (!CheckUserName_Validation(username)) {
                System.out.println("Invalid UserName! Only letters are allowed, and up to 20 characters. Do not use numbers, spaces, or symbols.");
                attempts++;
                System.out.println("Attempts left: " + (5 - attempts));
            } else {
                validInput = true;
            }

            if (attempts == 5) {  // Max attempts reached
                System.out.println("You have reached the maximum number of attempts.");
                return;
            }
        }

        // Check if user exists
        if (users.containsKey(username)) {
            String password = "";
            attempts = 0;
            validInput = false;

            // Loop until valid password is entered or attempts exhausted
            while (!validInput && attempts < 5) {
                System.out.print("Enter your password: ");
                password = scanner.nextLine().trim();

                // Hash and check password
                try {
                    String hashedPassword = hashPassword(password);
                    if (!users.get(username).equals(hashedPassword)) {
                        System.out.println("Incorrect password. Please try again.");
                        attempts++;
                        System.out.println("Attempts left: " + (5 - attempts));
                    } else {
                        validInput = true;
                        System.out.println("Login successful! Welcome " + username + "!");
                        enterFitnessData(scanner);  // Prompt user to enter fitness data
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error hashing password.");
                    return;
                }

                if (attempts == 5) {  // Max attempts reached
                    System.out.println("You have reached the maximum number of attempts.");
                    return;
                }
            }
        } else {
            System.out.println("Username not found. Please register first!");
        }
    }

    // Method to enter fitness data 
    private static void enterFitnessData(Scanner scanner) {
        // Define available fitness plans
        String Plan1_Type = "Cardio";
        String Plan1_HealthGoal = "Weight Loss";

        String Plan2_Type = "Strength Training";
        String Plan2_HealthGoal = "Muscle Building";

        String Plan3_Type = "Flexibility";
        String Plan3_HealthGoal = "Improve Flexibility";

        String Plan4_Type = "HIIT (High-Intensity Interval Training)";
        String Plan4_HealthGoal = "Improve Cardiovascular Health";

        String Plan5_Type = "Yoga";
        String Plan5_HealthGoal = "Stress Relief";

        int fitnessGoal = 0;
        boolean validInput = false;

        // Loop to validate fitness goal
        while (!validInput) {
            System.out.println("Please Enter Fitness Goal\n 1. Weight Loss\n 2. Muscle Building\n 3. Improve Flexibility\n 4. Improve Cardiovascular Health\n 5. Stress Relief ");
            String fitnessInput = scanner.nextLine().trim();
            if (!fitnessInput.matches("[1-5]")) {
                System.out.println("Invalid Input! Please select a number between 1 and 5. No spaces or invalid characters allowed.");
            } else {
                fitnessGoal = Integer.parseInt(fitnessInput);
                validInput = true;
            }
        }

        // Loop to validate fitness level
        validInput = false;
        int currentFitnessLevel = 0;
        while (!validInput) {
            System.out.println("Please Enter current fitness level\n 1. Beginner\n 2. Intermediate\n 3. Advanced ");
            String fitnessLevelInput = scanner.nextLine().trim();
            if (!fitnessLevelInput.matches("[1-3]")) {
                System.out.println("Invalid Input! Please select 1, 2, or 3. No spaces or invalid characters allowed.");
            } else {
                currentFitnessLevel = Integer.parseInt(fitnessLevelInput);
                validInput = true;
            }
        }

        // Validate age
        System.out.println("Please Enter your age (2 digits): ");
        validInput = false;
        int age = 0;
        while (!validInput) {
            String ageInput = scanner.nextLine().trim();
            if (!ageInput.matches("\\d{2}")) {
                System.out.println("Invalid Input! Please enter a valid 2-digit age. No spaces allowed.");
            } else {
                age = Integer.parseInt(ageInput);
                if (age < 10 || age > 99) {
                    System.out.println("Invalid Input! Age must be between 10 and 99.");
                } else {
                    validInput = true;
                }
            }
        }

        scanner.nextLine();  // Consume newline

        // Check if user has illnesses
        String illnesses = "";
        validInput = false;
        while (!validInput) {
            System.out.println("Do you have any illnesses? (yes or no): ");
            illnesses = scanner.nextLine().trim();
            if (!illnesses.equalsIgnoreCase("yes") && !illnesses.equalsIgnoreCase("no")) {
                System.out.println("Invalid Input! Please enter 'yes' or 'no'.");
            } else {
                validInput = true;
            }
        }

        // Check if user has surgeries
        String surgeries = "";
        validInput = false;
        while (!validInput) {
            System.out.println("Do you have any surgeries? (yes or no): ");
            surgeries = scanner.nextLine().trim();
            if (!surgeries.equalsIgnoreCase("yes") && !surgeries.equalsIgnoreCase("no")) {
                System.out.println("Invalid Input! Please enter 'yes' or 'no'.");
            } else {
                validInput = true;
            }
        }

        // Display selected fitness plan based on user input
        System.out.println("You have successfully entered your fitness details.");

        // Base exercise time and selected plan
        int baseExerciseTime = 0;
        String selectedPlan = "";
        int additionalMinutes = 0;

        // Select the plan based on the fitness goal and current fitness level
        if (fitnessGoal == 1) {  // Weight Loss goal
            selectedPlan = Plan1_Type;  // Cardio
            baseExerciseTime = 150;
            additionalMinutes = (currentFitnessLevel == 1) ? 30 : (currentFitnessLevel == 2) ? 20 : 10;
        } else if (fitnessGoal == 2) {  // Muscle Building goal
            selectedPlan = Plan2_Type;  // Strength Training
            baseExerciseTime = 120;
            additionalMinutes = (currentFitnessLevel == 1) ? 30 : (currentFitnessLevel == 2) ? 20 : 10;
        } else if (fitnessGoal == 3) {  // Flexibility goal
            selectedPlan = Plan3_Type;  // Flexibility
            baseExerciseTime = 90;
            additionalMinutes = (currentFitnessLevel == 1) ? 30 : (currentFitnessLevel == 2) ? 20 : 10;
        } else if (fitnessGoal == 4) {  // Cardiovascular Health goal
            selectedPlan = Plan4_Type;  // HIIT
            baseExerciseTime = 90;
            additionalMinutes = (currentFitnessLevel == 1) ? 30 : (currentFitnessLevel == 2) ? 20 : 10;
        } else if (fitnessGoal == 5) {  // Stress Relief goal
            selectedPlan = Plan5_Type;  // Yoga
            baseExerciseTime = 120;
            additionalMinutes = (currentFitnessLevel == 1) ? 30 : (currentFitnessLevel == 2) ? 20 : 10;
        }

        // Display the matched plan and total required exercise time
        System.out.println("Matched Plan: " + selectedPlan + " - Duration: " + (baseExerciseTime + additionalMinutes) + " minutes per week");

        // Add a note if there is any medical history
        if (illnesses.equalsIgnoreCase("yes") || surgeries.equalsIgnoreCase("yes")) {
            System.out.println("Note: Please consult a medical professional before starting this plan due to your medical history.");
        }
    }

    // Validate username format
    private static boolean CheckUserName_Validation(String username) {
        return username.matches("[a-zA-Z]{1,20}");
    }

    // Validate password format
    private static boolean CheckPassword_Validation(String password) {
        password = password.trim();
        boolean correctLength = password.length() == 8;
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        System.out.println("Password length (8 characters): " + (correctLength ? "✔" : "❌"));
        System.out.println("Contains lowercase letter: " + (hasLowercase ? "✔" : "❌"));
        System.out.println("Contains uppercase letter: " + (hasUppercase ? "✔" : "❌"));
        System.out.println("Contains digit: " + (hasDigit ? "✔" : "❌"));
        System.out.println("Contains special character: " + (hasSpecialChar ? "✔" : "❌"));
        return correctLength && hasLowercase && hasUppercase && hasDigit && hasSpecialChar;
    }

    // Hash the password using SHA-256
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Save user to file
    private static void saveUserToFile(String username, String hashedPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ":" + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user data to file.");
        }
    }

    // Load users from file
    private static void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user data from file.");
        }
    }
}
