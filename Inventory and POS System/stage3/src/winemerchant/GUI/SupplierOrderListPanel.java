package winemerchant.GUI;

import winemerchant.SQL.Database;
import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.Objects;

public class SupplierOrderListPanel {
    private JPanel mainPanel;
    private JTable ordersTable;
    private JComboBox<String> supplierNameCombo;
    private JButton filterButton;

    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private Database database;
    private SupplierRecord supplierRecord;
    private String[] supplierNames;

    public SupplierOrderListPanel(SupplierRecord supplierRecord) {
        this.supplierRecord = supplierRecord;
        this.model = new DefaultTableModel();
        this.sorter = new TableRowSorter<>(model);
        filterButton.addActionListener(e -> {
            if (supplierNameCombo.getSelectedIndex() == 0) {
                sorter.setRowFilter(null);
            } else {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(Objects.requireNonNull(supplierNameCombo.getSelectedItem()).toString(), 0);
                sorter.setRowFilter(rf);
            }
        });
        setNames();
        createTable();
        setInitialView();
    }

    private void setNames() {
        ordersTable.setName("OrdersTable");
        supplierNameCombo.setName("SupplierOrderComboBox");
        filterButton.setName("FilterButton");
    }


    public void setInitialView() {
        supplierNames = supplierRecord.getSuppliers();
        populateSupplierCombo();
        populateTable();
    }

    private void populateSupplierCombo() {
        supplierNameCombo.removeAllItems();
        supplierNameCombo.addItem("All Orders");
        for (String supplierName : supplierNames) {
            supplierNameCombo.addItem(supplierName);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createTable() {
        ordersTable.setModel(model);
        ordersTable.setRowSorter(sorter);
        model.addColumn("Supplier");
        model.addColumn("Wine");
        model.addColumn("Amount purchased");
        model.addColumn("Purchased price");
        model.addColumn("Paid");
    }

    private void populateTable() {
        //If "All orders" is selected (index 0)
        model.setRowCount(0);
        for (String supplier : supplierNames) {
            addSupplierOrdersToTable(supplier);
        }
    }

    //Get order data to populate table
    private void addSupplierOrdersToTable(String supplier) {
        Object[] rowData = new Object[5];
        List<SupplierOrder> orders = supplierRecord.getSupplierOrderListByName(supplier);
        for (SupplierOrder order : orders) {
            rowData[0] = supplier;
            rowData[1] = order.getWine().getKind();
            rowData[2] = order.getAmountPurchased();
            rowData[3] = "£" + order.getPurchasedPrice();
            rowData[4] = order.isPaid();
            model.addRow(rowData);
        }
    }
}
