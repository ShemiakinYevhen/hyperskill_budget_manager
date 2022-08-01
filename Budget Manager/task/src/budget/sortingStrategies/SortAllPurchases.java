package budget.sortingStrategies;

import java.util.*;
import java.util.stream.Collectors;

public class SortAllPurchases implements SortPurchasesStrategy {

    @Override
    public void sortAndPrintPurchases(Map <String, Map<String, Double>> purchaseList) {
        Map<String, Double> allPurchases = new HashMap<>();

        for (String categoryName : purchaseList.keySet()) {
            allPurchases.putAll(purchaseList.get(categoryName));
        }

        if (allPurchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        Set<Double> sortedSetOfPurchasesPrices = allPurchases.values().stream().sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println("\nAll:");

        double totalOfAllPurchases = printSortedItems(sortedSetOfPurchasesPrices, allPurchases, "%s $%.2f\n");

        System.out.println("Total: $" + totalOfAllPurchases);
    }
}
