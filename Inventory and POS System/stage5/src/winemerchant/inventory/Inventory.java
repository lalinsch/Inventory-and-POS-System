package winemerchant.inventory;

import winemerchant.SQL.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> inventoryMap;

    private Database database;

    public Inventory(Database database) {
        this.database = database;
        setInventoryMap(database.getInventoryMap());
    }

    public void refreshData() {
        setInventoryMap(database.getInventoryMap());
    }

    public void setInventoryMap(Map<String, Integer> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }

    public Map<String, Integer> getInventoryMap() {
        return inventoryMap;
    }

    public String getWineUnitCount(String wineType) {
        if(!inventoryMap.keySet().contains(wineType)) {
            return "0";
        } else {
            return String.valueOf(inventoryMap.get(wineType));
        }
    }

    public String getTotalCount() {
        int total;
        if (inventoryMap.isEmpty()) {
            return "0";
        } else {
            total = inventoryMap.values().stream().mapToInt(wine -> wine).sum();
            return String.valueOf(total);
        }
    }

    public boolean inputSale(Sale sale) {
        if (sale.isMixed()) {
            String wineTypeOne = sale.getMixedOrderWineOne().toString();
            String wineTypeTwo = sale.getMixedOrderWineTwo().toString();
            if (inventoryMap.containsKey(wineTypeOne) && (inventoryMap.containsKey(wineTypeTwo)) ) {
                int wineOneAmount = inventoryMap.get(wineTypeOne);
                int wineTwoAmount = inventoryMap.get(wineTypeTwo);
                if (wineOneAmount < 6 || wineTwoAmount < 6) {
                    System.out.println("Not enough bottles for sale");
                    return false;
                } else if (database.inputSaleToInventory(sale.getMixedOrderWineOne(), 6) && database.inputSaleToInventory(sale.getMixedOrderWineTwo(), 6)) {
                    refreshData();
                    return true;
                }
            }
        } else {
            String wineType = sale.getSingleOrderWineType().toString();
            if (inventoryMap.containsKey(sale.getSingleOrderWineType())) {
                int wineAmount = inventoryMap.get(wineType);
                if (wineAmount < 12) {
                    System.out.println("Not enough bottles for sale");
                    return false;
                } else if(database.inputSaleToInventory(sale.getSingleOrderWineType(), 12)) {
                    refreshData();
                    return true;
                }
            }
        }
        return false;
    }
}
