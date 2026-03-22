package ui;

import java.util.*;

// Handles console input to ensure app doesnt crash from invalid user data 
public class InputValidator {
    private Scanner scanner;

    // Constructor
    public InputValidator(Scanner sc) {
        this.scanner = sc;
    }

    public int getValidInt() {
        
        while (true) {
            try {
                String input = scanner.nextLine();  // read the whole line
                int value = Integer.parseInt(input.trim()); // try to convert to int
                return value;
            } catch (NumberFormatException e) {
                // user typed something that isn't a number
                System.out.println("ERROR: Invalid input! Please enter a number: ");
            }
        }
    }

    // Overload with min and max values
    public int getValidInt(String prompt, int min, int max) {
        int input = -1;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(prompt);
            String rawInput = scanner.nextLine().trim();

            try {
                input = Integer.parseInt(rawInput);

                if (input >= min && input <= max) {
                    isValid = true;
                } else {
                    System.out.println(">>> ERROR: Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(">>> ERROR: Invalid input. Please enter a valid number.");
            }
        }

        return input;
    }

    public String getValidString(String prompt) {
        String input = "";

        while(input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("ERROR: Input cannot be empty. Please try again.");
            }
        }

        return input;
    }

    public boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + "(y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("ERROR: Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
}