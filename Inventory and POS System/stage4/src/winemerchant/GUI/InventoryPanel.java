package winemerchant.GUI;

import winemerchant.inventory.Inventory;

import javax.swing.*;

public class InventoryPanel {
    private Inventory inventory;
    private JPanel mainPanel;
    private JTable inventoryTable;


    public InventoryPanel(Inventory inventory) {
        this.inventory = inventory;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
