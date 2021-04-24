package winemerchant.GUI;

import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SupplierOrderListPanel {
    private JPanel mainPanel;
    private JLabel topLabel;
    private JTable ordersTable;
    private JComboBox supplierNameCombo;
    private JButton refreshButton;

    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private SupplierRecord supplierRecord;
    private String[] supplierNames;

    public SupplierOrderListPanel(SupplierRecord supplierRecord) {
        this.supplierRecord = supplierRecord;
        this.model = (DefaultTableModel) ordersTable.getModel();
        this.sorter = new TableRowSorter<>(model);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (supplierNameCombo.getSelectedIndex() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(supplierNameCombo.getSelectedItem().toString(), 0);
                    sorter.setRowFilter(rf);
                }
            }
        });
        createTable();
        setInitialView();
    }


    public void setInitialView() {
        supplierNames = supplierRecord.getSuppliers();
        populateSupplierCombo();
        populateTable();
    }

    private void populateSupplierCombo() {
        supplierNameCombo.removeAllItems();
        supplierNameCombo.addItem("All Orders");
        for (int i = 0; i < supplierNames.length; i++) {
            supplierNameCombo.addItem(supplierNames[i]);
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
        for (int j = 0; j < orders.size(); j++) {
            SupplierOrder order = orders.get(j);
            rowData[0] = supplier;
            rowData[1] = order.getWine().getKind();
            rowData[2] = order.getAmountPurchased();
            rowData[3] = "£" + order.getPurchasedPrice();
            rowData[4] = order.isPaid();
            model.addRow(rowData);
        }
    }
}
