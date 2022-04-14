package budgetmanager.console;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    static String getPurchaseName() {
        while (true) {
            System.out.print("name > ");
            String name = (SCANNER.next() + SCANNER.nextLine()).trim();
            if (name.length() > 40) {
                System.out.println("Purchase name length cannot exceed 40 characters. Try again");
            } else {
                return name;
            }
        }
    }

    static int[] getMonthAndYearNumbers() {
        while (true) {
            try {
                System.out.print("month:year > ");
                String[] monthYearInput = SCANNER.nextLine().split(":");
                return new int[]{Integer.parseInt(monthYearInput[0]), Integer.parseInt(monthYearInput[1])};
            } catch (NumberFormatException exception) {
                System.out.println("Incorrect input. You should enter month number and year number separated by :");
            }
        }
    }

    static int[] getYearNumber() {
        while (true) {
            try {
                System.out.print("year > ");
                return new int[] {Integer.parseInt(SCANNER.nextLine())};
            } catch (NumberFormatException exception) {
                System.out.println("Incorrect input. You should enter year number");
            }
        }
    }

    static String getLine() {
        System.out.print(" > ");
        return SCANNER.next() + SCANNER.nextLine();
    }

    static double getPrice() {
        while (true) {
            System.out.print("price > ");
            try {
                return Double.parseDouble(SCANNER.next().replace(',', '.'));
            } catch (NumberFormatException exception) {
                System.out.println("Incorrect input type. Try again:");
                SCANNER.nextLine();
            }
        }
    }

    static int getQuantity() {
        while (true) {
            System.out.print("quantity > ");
            try {
                return SCANNER.nextInt();
            } catch (InputMismatchException exception) {
                SCANNER.nextLine();
                System.out.println("Incorrect input type. Try again");
            }
        }
    }

    public static Date getDate() {
        while (true) {
            System.out.print("date(yyyy-mm-dd or curr) > ");
            try {
                String input = SCANNER.next() + SCANNER.nextLine();
                if (input.equals("curr")) {
                    return new java.sql.Date(System.currentTimeMillis());
                }
                return java.sql.Date.valueOf(input);
            } catch (IllegalArgumentException exception) {
                System.out.println("Incorrect input. Try again");
            }
        }
    }
}
