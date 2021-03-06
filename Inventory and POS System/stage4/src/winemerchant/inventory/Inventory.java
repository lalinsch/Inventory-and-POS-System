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
}
