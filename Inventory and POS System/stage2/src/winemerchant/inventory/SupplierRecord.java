package winemerchant.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplierRecord {
    private final Map<String, List<SupplierOrder>> supplierOrderMap;
    private final Inventory inventory;

    public SupplierRecord(Inventory inventory) {
        supplierOrderMap = new HashMap<>();
        this.inventory = inventory;
    }

    public void addOrder(String supplier, SupplierOrder order) {
        if (!supplierOrderMap.containsKey(supplier)) {
            supplierOrderMap.put(supplier, new ArrayList<>());
        }
        supplierOrderMap.get(supplier).add(order);
        inventory.addBottles(order.getWine(), order.getAmountPurchased(), order.getPurchasedPrice());
    }

    public void viewAllSupplierRecord() {
        if (supplierOrderMap.isEmpty()) {
            System.out.println("No supplier records found");
        } else {
            for (String supplier : supplierOrderMap.keySet()) {
                System.out.println("Supplier: " + supplier + ": " + supplierOrderMap.get(supplier).size() + " orders.");
            }
            System.out.println("Total orders: " + getTotalOrders() + " Total Value: $" + getTotalValueOfOrders());
        }
    }

    private int getTotalOrders() {
        int sum = 0;
        for (String supplier : supplierOrderMap.keySet()) {
            sum += getSupplierOrderListByName(supplier).size();
        }
        return sum;
    }

    private double getTotalValueOfOrders() {
        double total = 0;
        for (String supplier : supplierOrderMap.keySet()) {
            for (SupplierOrder order : getSupplierOrderListByName(supplier)) {
                total += order.getPurchasedPrice();
            }
        }
        return total;
    }

    public void viewSupplier(String supplier) {
        List<SupplierOrder> supplierOrderList = getSupplierOrderListByName(supplier);
        for (int i = 0; i < supplierOrderList.size(); i++) {
            SupplierOrder order = supplierOrderList.get(i);
            System.out.println(i + 1 + " " + order);
        }
        System.out.println("Total orders: " + supplierOrderList.size() + " total value: $" + getSupplierTotalValue(supplier) + " to pay: $" + getSupplierPendingOrders(supplier));
    }

    private double getSupplierPendingOrders(String supplier) {
        List<SupplierOrder> supplierOrderList = getSupplierOrderListByName(supplier);
        return supplierOrderList.stream()
                .filter(supplierOrder -> !supplierOrder.isPaid())
                .collect(Collectors.toList()).stream().mapToDouble(order -> order.getPurchasedPrice()).sum();
    }

    private double getSupplierTotalValue(String supplier) {
        List<SupplierOrder> supplierOrderList = getSupplierOrderListByName(supplier);
        double totalValue = 0;
        for (SupplierOrder order : supplierOrderList) {
            totalValue += order.getPurchasedPrice();
        }
        return totalValue;
    }


    public void setSupplierOrderAsPaid(String supplier, int orderIndex) {
        List<SupplierOrder> supplierOrderList = supplierOrderMap.get(supplier);
        supplierOrderList.get(orderIndex).setPaid(true);
    }

    public List<SupplierOrder> getSupplierOrderListByName(String supplier) {
        if (supplierOrderMap.containsKey(supplier)) {
            return supplierOrderMap.get(supplier);
        }
        return null;
    }

    public boolean contains(String supplier) {
        return supplierOrderMap.containsKey(supplier);
    }

    public boolean isEmpty() {
        return supplierOrderMap.isEmpty();
    }
}