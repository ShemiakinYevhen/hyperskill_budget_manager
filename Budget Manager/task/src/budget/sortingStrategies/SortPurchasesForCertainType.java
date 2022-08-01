package budget.sortingStrategies;

import java.util.*;
import java.util.stream.Collectors;

public class SortPurchasesForCertainType implements SortPurchasesStrategy {

    private final Scanner scanner;

    public SortPurchasesForCertainType(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void sortAndPrintPurchases(Map <String, Map<String, Double>> purchaseList) {
        String categoryName = getCategoryNameForSorting();

        Map<String, Double> categoryPurchasesList = purchaseList.get(categoryName);

        if (categoryPurchasesList.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        Set<Double> sortedSetOfPurchasesPrices = categoryPurchasesList.values().stream().sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println("\n" + categoryName + ":");

        double totalOfCategory = printSortedItems(sortedSetOfPurchasesPrices, categoryPurchasesList, "%s $%.2f\n");

        System.out.printf("Total sum: $%.2f\n", totalOfCategory);
    }

    private String getCategoryNameForSorting() {
        while (true) {
            System.out.println("""

                    Choose the type of purchase
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other""");

            int command;

            try {
                command = Integer.parseInt(this.scanner.nextLine());
            } catch (NumberFormatException ignored) {
                System.out.println("Unknown command!");
                continue;
            }

            switch (command) {
                case 1 -> {
                    return "Food";
                }
                case 2 -> {
                    return "Clothes";
                }
                case 3 -> {
                    return "Entertainment";
                }
                case 4 -> {
                    return "Other";
                }
                default -> System.out.println("Invalid command input! Try again");
            }
        }
    }
}
