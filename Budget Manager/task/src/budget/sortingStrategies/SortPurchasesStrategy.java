package budget.sortingStrategies;

import java.util.*;

public interface SortPurchasesStrategy {

    void sortAndPrintPurchases(Map <String, Map<String, Double>> purchaseList);

    default List<String> getKeysByValue(Map<String, Double> map, double value) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    default double printSortedItems(Set<Double> setOfPricesOrTotals, Map<String, Double> purchaseList, String printFormat) {
        double total = 0;

        for (double purchasePrice : setOfPricesOrTotals) {
            List<String> itemsNames = getKeysByValue(purchaseList, purchasePrice);
            total += purchasePrice * itemsNames.size();
            ListIterator<String> listIter = itemsNames.listIterator(itemsNames.size());
            while (listIter.hasPrevious()) {
                System.out.printf(printFormat, listIter.previous(), purchasePrice);
            }
        }

        return total;
    }
}
