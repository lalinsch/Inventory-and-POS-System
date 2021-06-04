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

    public void setInventoryMap(Map<String, Integer> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }
}
