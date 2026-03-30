package ui;

import java.util.Scanner;

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

    /** DD/MM/YYYY, separators '/', calendar date (incl. leap years). */
    public String getValidBirthday(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (isValidBirthdayDDMMYYYY(input)) {
                return input;
            }
            System.out.println(
                "Invalid date. Use DD/MM/YYYY");
        }
    }

    public boolean isValidBirthdayDDMMYYYY(String input) {
        if (input == null || input.length() != 10 || input.charAt(2) != '/' || input.charAt(5) != '/') {
            return false;
        }
        String[] parts = input.split("/");
        if (parts.length != 3 || parts[0].length() != 2 || parts[1].length() != 2 || parts[2].length() != 4) {
            return false;
        }
        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            if (year < 1 || year > 9999) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            int maxDay = daysInMonth(month, year);
            return day >= 1 && day <= maxDay;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int daysInMonth(int month, int year) {
        return switch (month) {
            case 2 -> isLeapYear(year) ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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