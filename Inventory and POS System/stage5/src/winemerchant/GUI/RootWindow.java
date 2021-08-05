package winemerchant.GUI;

import winemerchant.SQL.Database;
import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierRecord;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.SQLException;

public class RootWindow extends JFrame {
    //Database
    private Database database;

    //Swing elements
    private JTabbedPane tabbedPane;

    //Custom Panels for TabbedPane
    private SupplierOrderPanel supplierOrderPanel;
    private SupplierOrderListPanel supplierOrderListPanel;
    private InventoryPanel inventoryPanel;
    private POSPanel posPanel;

    //Objects where we will store the inventory
    private Inventory inventory;
    private SupplierRecord supplierRecord;


    public RootWindow(String databaseURL) throws SQLException {
        database = new Database(databaseURL);
        tabbedPane = new JTabbedPane();
        inventory = database.buildInventory();
        supplierRecord = database.buildSupplierRecord();
        supplierOrderPanel = new SupplierOrderPanel(supplierRecord);
        supplierOrderListPanel = new SupplierOrderListPanel(supplierRecord);
        inventoryPanel = new InventoryPanel(inventory);
        posPanel = new POSPanel(inventory);

        tabbedPane.setBackground(Color.GRAY);
        tabbedPane.setForeground(Color.BLACK);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabbedPane.getSelectedIndex();
                if (index == 1) {
                    supplierOrderListPanel.setInitialView();
                } else if (index == 2) {
                    inventoryPanel.refreshData();
                } else if (index == 3) {
                    inventory.refreshData();
                    posPanel.resetUI();
                }
             }
        });

        //Populate the window for initial launch.
        setTitle("Wine Merchant");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane.addTab("Input Supplier Order", supplierOrderPanel.getMainPanel());
        tabbedPane.addTab("Supplier Orders List", supplierOrderListPanel.getMainPanel());
        tabbedPane.addTab("Inventory", inventoryPanel.getMainPanel());
        tabbedPane.addTab("Customer Sale", posPanel.getMainPanel());
        tabbedPane.setName("TabbedPane");
        add(tabbedPane);
        pack();
        setVisible(true);
    }
}
