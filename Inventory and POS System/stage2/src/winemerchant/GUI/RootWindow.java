package winemerchant.GUI;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;
import winemerchant.inventory.Wine;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RootWindow extends JFrame {
    //Swing elements
    private JTabbedPane tabbedPane;

    //Custom Panels for TabbedPane
    private SupplierOrderPanel supplierOrderPanel;
    private SupplierOrderListPanel supplierOrderListPanel;

    //Objects where we will store the inventory
    private Inventory inventory;
    private SupplierRecord supplierRecord;


    public RootWindow() {
        tabbedPane = new JTabbedPane();
        inventory = new Inventory();
        supplierRecord = new SupplierRecord(inventory);
        supplierOrderPanel = new SupplierOrderPanel(supplierRecord);
        supplierOrderListPanel = new SupplierOrderListPanel(supplierRecord);

        //Populate the window for initial launch.
        setTitle("Wine Merchant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane.addTab("First", supplierOrderPanel.getMainPanel());
        tabbedPane.addTab("Second", supplierOrderListPanel.getMainPanel());
        add(tabbedPane);
        setSize(600, 500);
        pack();
        setVisible(true);
    }
}
