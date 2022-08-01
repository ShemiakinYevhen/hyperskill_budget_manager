package budget.sortingStrategies;

import java.util.*;
import java.util.stream.Collectors;

public class SortPurchasesByType implements SortPurchasesStrategy {

    @Override
    public void sortAndPrintPurchases(Map <String, Map<String, Double>> purchaseList) {
        Map<String, Double> sortByTypeAnalyzes = new HashMap<>();

        for (String categoryName : purchaseList.keySet()) {
            double categoryTotal = 0;
            for (String itemName : purchaseList.get(categoryName).keySet()) {
                categoryTotal += purchaseList.get(categoryName).get(itemName);
            }
            sortByTypeAnalyzes.put(categoryName, categoryTotal);
        }

        Set<Double> sortedSetOfCategoriesTotals = sortByTypeAnalyzes.values().stream().sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println("\nTypes:");

        double totalOfAllCategories = printSortedItems(sortedSetOfCategoriesTotals, sortByTypeAnalyzes, "%s - $%.2f\n");

        System.out.printf("Total sum: $%.2f\n", totalOfAllCategories);
    }
}
