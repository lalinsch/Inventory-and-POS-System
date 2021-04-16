package winemerchant.GUI;

import winemerchant.inventory.SupplierRecord;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ContainerAdapter;

public class SupplierOrderListPanel {
    private JPanel mainPanel;
    private JTable supplierOrderTable;
    private JLabel topLabel;

    private String[] columnNames = {"Supplier", "Order ID", "Wine Type", "Amount", "Price", "Paid"};

    private SupplierRecord supplierRecord;

    public SupplierOrderListPanel(SupplierRecord supplierRecord) {
        this.supplierRecord = supplierRecord;
        supplierOrderTable
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
