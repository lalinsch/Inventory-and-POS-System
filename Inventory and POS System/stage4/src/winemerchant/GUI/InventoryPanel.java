package winemerchant.GUI;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class InventoryPanel {
    private Inventory inventory;

    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JLabel merlotLabel;
    private JLabel roseLabel;
    private JLabel sauvignonLabel;
    private JLabel totalLabel;
    private JTable inventoryTable;

    public InventoryPanel(Inventory inventory) {
        this.inventory = inventory;
        nameComponents();
        refreshData();
    }

    public void refreshData() {
        inventory.refreshData();
        merlotLabel.setText(inventory.getWineUnitCount("Red Merlot"));
        sauvignonLabel.setText(inventory.getWineUnitCount("White Sauvignon Blanc"));
        roseLabel.setText(inventory.getWineUnitCount("Rosé Zinfandel"));
        totalLabel.setText(inventory.getTotalCount());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void nameComponents() {
        merlotLabel.setName("MerlotAmountLabel");
        roseLabel.setName("RoseAmountLabel");
        sauvignonLabel.setName("SauvignonAmountLabel");
        totalLabel.setName("TotalAmountLabel");
    }
}
