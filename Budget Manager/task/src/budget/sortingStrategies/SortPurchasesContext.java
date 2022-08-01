package budget.sortingStrategies;

import java.util.Map;

public class SortPurchasesContext {

    SortPurchasesStrategy strategy;

    public void setStrategy(SortPurchasesStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortAndPrintPurchases(Map <String, Map<String, Double>> purchaseList) {
        this.strategy.sortAndPrintPurchases(purchaseList);
    }
}
