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

    //Objects where we will store the inventory
    private Inventory inventory;
    private SupplierRecord supplierRecord;


    public RootWindow(Database database) throws SQLException {
        this.database = database;
        tabbedPane = new JTabbedPane();
        inventory = new Inventory();
        supplierRecord = database.buildSupplierRecord();
        supplierOrderPanel = new SupplierOrderPanel(supplierRecord);
        supplierOrderListPanel = new SupplierOrderListPanel(supplierRecord);

        tabbedPane.setBackground(Color.GRAY);
        tabbedPane.setForeground(Color.BLACK);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabbedPane.getSelectedIndex();
                if (index == 1) {
                    supplierOrderListPanel.setInitialView();
                }
            }
        });

        //Populate the window for initial launch.
        setTitle("Wine Merchant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane.addTab("Input Supplier Order", supplierOrderPanel.getMainPanel());
        tabbedPane.addTab("Supplier Orders List", supplierOrderListPanel.getMainPanel());
        tabbedPane.setName("TabbedPane");
        add(tabbedPane);
        setSize(600, 500);
        pack();
        setVisible(true);
    }
}
