package budget;

import budget.sortingStrategies.SortAllPurchases;
import budget.sortingStrategies.SortPurchasesByType;
import budget.sortingStrategies.SortPurchasesContext;
import budget.sortingStrategies.SortPurchasesForCertainType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final Map<String, Map<String, Double>> purchaseList = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static double balance = 0;
    private static final String pathToFile = System.getProperty("user.dir") + File.separator + "purchases.txt";

    public static void main(String[] args) {
        purchaseList.put("Food", new HashMap<>());
        purchaseList.put("Clothes", new HashMap<>());
        purchaseList.put("Entertainment", new HashMap<>());
        purchaseList.put("Other", new HashMap<>());

        while (true) {
            System.out.println("""

                    Choose your action:
                    1) Add income
                    2) Add purchase
                    3) Show list of purchases
                    4) Balance
                    5) Save
                    6) Load
                    7) Analyze (Sort)
                    0) Exit""");

            int command;

            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println("Unknown command!");
                continue;
            }

            switch (command) {
                case 1 -> addIncome();
                case 2 -> addPurchase();
                case 3 -> printListOfPurchases();
                case 4 -> printBalance();
                case 5 -> savePurchases();
                case 6 -> loadPurchases();
                case 7 -> analyzePurchases();
                case 0 -> {
                    System.out.println("\nBye!");
                    return;
                }
            }
        }
    }

    private static void addIncome() {
        while (true) {
            System.out.println("\nEnter income:");

            try {
                int income = Integer.parseInt(scanner.nextLine());
                balance += income;
                return;
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid input data! Try again");
            }
        }
    }

    private static void addPurchase() {
        while (true) {
            int categoryNumber;

            System.out.println("""

                    Choose the type of purchase
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other
                    5) Back""");

            try {
                categoryNumber = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid category number input! Try again");
                continue;
            }

            switch (categoryNumber) {
                case 1 -> putPurchaseInAppropriateList("Food");
                case 2 -> putPurchaseInAppropriateList("Clothes");
                case 3 -> putPurchaseInAppropriateList("Entertainment");
                case 4 -> putPurchaseInAppropriateList("Other");
                case 5 -> { return; }
            }

            System.out.println("Purchase was added!");
        }
    }

    private static void putPurchaseInAppropriateList(String categoryName) {
        System.out.println("\nEnter purchase name:");
        String purchaseName = scanner.nextLine();

        while (true) {
            System.out.println("Enter its price:");

            try {
                double purchasePrice = Double.parseDouble(scanner.nextLine());
                purchaseList.get(categoryName).put(purchaseName, purchasePrice);
                balance -= purchasePrice;
                return;
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid price input! Try again");
            }
        }
    }

    private static void printListOfPurchases() {
        while (true) {
            int categoryNumber;

            System.out.println("""

                    Choose the type of purchases
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other
                    5) All
                    6) Back""");

            try {
                categoryNumber = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid category number input! Try again");
                continue;
            }

            switch (categoryNumber) {
                case 1 -> printCategory("Food");
                case 2 -> printCategory("Clothes");
                case 3 -> printCategory("Entertainment");
                case 4 -> printCategory("Other");
                case 5 -> printAllCategories();
                case 6 -> { return; }
            }
        }
    }

    private static void printCategory(String categoryName) {
        System.out.println("\n" + categoryName + ":");
        if (purchaseList.get(categoryName).isEmpty()) {
            System.out.println("The purchase list is empty\n");
        } else {
            double total = 0;
            for (String itemName : purchaseList.get(categoryName).keySet()) {
                System.out.printf("%s $%.2f\n", itemName, purchaseList.get(categoryName).get(itemName));
                total += purchaseList.get(categoryName).get(itemName);
            }
            System.out.printf("Total sum: $%.2f\n", total);
        }
    }

    private static void printAllCategories() {
        System.out.println("\nAll:");
        double total = 0;
        for (String categoryName : purchaseList.keySet()) {
            Map<String, Double> tempMap = purchaseList.get(categoryName);
            if (!tempMap.isEmpty()) {
                for (String itemName : tempMap.keySet()) {
                    System.out.printf("%s $%.2f\n", itemName, tempMap.get(itemName));
                    total += tempMap.get(itemName);
                }
            }
        }

        if (total == 0) {
            System.out.println("The purchase list is empty\n");
        } else {
            System.out.printf("Total sum: $%.2f\n", total);
        }
    }

    private static void printBalance() {
        if (balance < 0) {
            System.out.println("\nBalance: $0.00");
        } else {
            System.out.printf("\nBalance: $%.2f\n", balance);
        }
    }

    private static void savePurchases() {
        File saveFile = new File(pathToFile);

        if (!saveFile.exists() || !saveFile.isFile()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Unable to create new save file: " + pathToFile);
                throw new RuntimeException(e);
            }
        }

        try (FileWriter writer = new FileWriter(saveFile, false)) {
            writer.write("Balance: " + balance + "\n");

            for (String categoryName : purchaseList.keySet()) {
                writer.write(categoryName + ":\n");
                for (String itemName : purchaseList.get(categoryName).keySet()) {
                    writer.write(itemName + "|" + purchaseList.get(categoryName).get(itemName) + "\n");
                }
                writer.write("*\n");
            }
        } catch (IOException e) {
            System.out.println("File not found!");
        }

        System.out.println("\nPurchases were saved!");
    }

    private static void loadPurchases() {
        File saveFile = new File(pathToFile);

        if (!saveFile.exists() || !saveFile.isFile()) {
            System.out.println("File not found! No saved purchases yet");
            return;
        }

        Scanner fileScanner;

        try {
            fileScanner = new Scanner(saveFile);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to create scanner for existing save file");
            throw new RuntimeException(e);
        }

        balance = Double.parseDouble(fileScanner.nextLine().split(": ")[1]);

        while (fileScanner.hasNext()) {
            String categoryName = fileScanner.nextLine().replaceAll(":", "");

            if (!purchaseList.containsKey(categoryName)) {
                System.out.println(categoryName + " category was not found in purchases list!");
                continue;
            }

            String newLine = fileScanner.nextLine();

            while (!newLine.equals("*") && fileScanner.hasNext()) {
                try {
                    String itemName = newLine.split("\\|")[0];
                    double itemPrice = Double.parseDouble(newLine.split("\\|")[1]);

                    purchaseList.get(categoryName).put(itemName, itemPrice);

                    newLine = fileScanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Error occurred when tried to parse this line:\n" + newLine);
                }
            }
        }

        System.out.println("\nPurchases were loaded!");
    }

    private static void analyzePurchases() {
        while (true) {
            System.out.println("""

                    How do you want to sort?
                    1) Sort all purchases
                    2) Sort by type
                    3) Sort certain type
                    4) Back""");

            int command;

            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println("Unknown command!");
                continue;
            }

            SortPurchasesContext context = new SortPurchasesContext();

            switch (command) {
                case 1 -> context.setStrategy(new SortAllPurchases());
                case 2 -> context.setStrategy(new SortPurchasesByType());
                case 3 -> context.setStrategy(new SortPurchasesForCertainType(scanner));
                case 4 -> { return; }
                default ->  {
                    System.out.println("Invalid command input! Try again");
                    continue;
                }
            }

            context.sortAndPrintPurchases(purchaseList);
        }
    }
}
