package budgetmanager.console;

import budgetmanager.database.DatabaseManager;
import budgetmanager.database.Purchase;
import budgetmanager.util.CurrencyFormatter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ConsoleInterface {

    private final DatabaseManager DB_MANAGER = new DatabaseManager();

    public void mainMenu() {
        while (true) {
            String inputLine = ConsoleInput.getLine();
            switch (InputRecognizer.recognize(inputLine)) {
                case ADD_BALANCE -> addBalance(inputLine.toLowerCase().split("\\s+")[2]);
                case GET_BALANCE -> printBalance();
                case ADD_PURCHASE -> addPurchase(inputLine.toLowerCase().split("\\s+")[1]);
                case DELETE_PURCHASE -> deletePurchase(inputLine);
                case GET_PURCHASES -> getPurchases(inputLine);
                case HELP -> printHelpMessage();
                case INCORRECT -> System.out.println("Incorrect input");
                case EXIT -> {
                    return;
                }
            }
        }
    }

    private void getPurchases(String inputLine) {
        String[] inputElements = inputLine.split("\\s+");
        try {
            String category = Arrays.stream(inputElements)
                    .filter(i -> i.matches("all|food|clothes|entertainment|other"))
                    .findFirst().get();
            String timeScope = Arrays.stream(inputElements)
                    .filter(i -> i.matches("last-week|month|year"))
                    .findFirst().orElse("all");
            String orderType = Arrays.stream(inputElements)
                    .filter(i -> i.matches("oldest|recent|cheapest|expensive"))
                    .findFirst().orElse("all");
            int[] dateNumbers = new int[0];
            if (timeScope.equals("month")) {
                dateNumbers = ConsoleInput.getMonthAndYearNumbers();
            } else if (timeScope.equals("year")) {
                dateNumbers = ConsoleInput.getYearNumber();
            }
            printPurchases(DB_MANAGER.getPurchases(category, orderType, timeScope, dateNumbers));
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Could not load data from database");
        }
    }

    private void addPurchase(String category) {
        Purchase purchase = new Purchase();
        purchase.setCategory(category.toLowerCase());
        purchase.setName(ConsoleInput.getPurchaseName());
        purchase.setPrice(ConsoleInput.getPrice());
        purchase.setQuantity(ConsoleInput.getQuantity());
        purchase.setDate(ConsoleInput.getDate());
        try {
            DB_MANAGER.addPurchase(purchase);
            System.out.println("Purchase was added!");
        } catch (SQLException exception) {
            System.out.println("Could not add given purchase to the database");
        }
    }

    private void deletePurchase(String input) {
        String[] elements = input.split("'");
        try {
            DB_MANAGER.deletePurchase(elements[1], java.sql.Date.valueOf(elements[2].trim()));
            System.out.println("Purchase was deleted!");
        } catch (SQLException exception) {
            System.out.println("Could not delete given purchase from the database");
        } catch (IllegalArgumentException exception) {
            System.out.println("Incorrect date");
        }
    }

    private void addBalance(String value) {
        try {
            double income = Double.parseDouble(value);
            DB_MANAGER.addIncome(income);
            System.out.println("Income was added!");
        } catch (NumberFormatException exception) {
            System.out.println("Could not parse number: " + value);
        } catch (Exception exception) {
            System.out.println("Could not add income to the database");
        }
    }

    private void printBalance() {
        try {
            double balance = DB_MANAGER.getCurrentBalance();
            System.out.printf("%nCurrent Balance: %s%n", CurrencyFormatter.format(balance));
        } catch (SQLException exception) {
            System.out.println("\nCould not get balance info from the database");
        }
    }

    private void printPurchases(List<Purchase> purchases) {
        if (purchases.isEmpty()) {
            System.out.println("\nNo purchases");
        } else {
            double expenseSum = purchases.stream()
                                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                                        .sum();
            String separator = "|" + "-".repeat(105) + "|";
            System.out.printf("%n| %-40s | %-20s | %-8s | %-13s | %-10s |%n", "Name", "Price", "Quantity", "Category", "Date");
            System.out.println(separator);
            for (Purchase purchase : purchases) {
                System.out.println(purchase);
                System.out.println(separator);
            }
            System.out.printf("  %40s   %-40s%n", "Expense sum:", CurrencyFormatter.format(expenseSum));
        }
    }

    private void printHelpMessage() {
        System.out.println("""
                Adding purchase
                To add purchase type: add PURCHASE_CATEGORY.
                Available categories: food, clothes, entertainment, other.
                Then you will be asked to specify purchase name, price, quantity and date.
                
                Deleting purchase
                To delete purchase type: delete 'PURCHASE_NAME' PURCHASE_DATE
                PURCHASE_DATE correct format: yyyy-mm-dd. Alternatively you can use current date by typing: curr
                
                Getting purchases list
                To get purchases list type: get PURCHASE_CATEGORY.
                Available categories: all, food, clothes, entertainment, other.
                Additionally you can specify time scope and sorting of given purchases.
                Correct order of adding more options is: get PURCHASE_CATEGORY TIME_SCOPE SORT_TYPE.
                Available time scopes: last-week, month, year.
                Available sort types: oldest, recent, cheapest, expensive.
                
                Balance managing
                Every purchase will lower your balance.
                In order to add money type: add balance AMOUNT.
                To get current balance type: get balance
                
                Other available commands:
                help   - shows this message
                exit   - closes program
                """);
    }
}
