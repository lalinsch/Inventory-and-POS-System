package winemerchant.GUI;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.Sale;

import javax.swing.*;

public class POSPanel {
    private Inventory inventory;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel singleTypePanel;
    private JRadioButton singleTypeOrderButton;
    private JRadioButton mixedTypeOrderButton;
    private JLabel instructionalLabel;
    private JComboBox singleWineKindBox;
    private JButton confirmButton;
    private JLabel wine1Label;
    private JComboBox mixedWineComboBox1;
    private JTextField customerNameField;
    private JTextField saleAmountField;
    private JRadioButton loyaltyDiscountButton;
    private JButton submitButton;
    private JLabel successLabel;
    private JLabel errorLabel;
    private JPanel mixedTypePanel;
    private JComboBox mixedWineComboBox2;
    private ButtonGroup buttonGroup;
    private Sale sale;

    public POSPanel (Inventory inventory) {
        this.inventory = inventory;
        singleTypePanel.setVisible(false);
        mixedTypePanel.setVisible(false);
        bottomPanel.setVisible(true);
        customerNameField.setEnabled(false);
        saleAmountField.setEnabled(false);
        loyaltyDiscountButton.setEnabled(false);
        submitButton.setEnabled(false);
        setMainJRadioButtonActions();
        populateComboBoxes();
    }

    private void populateComboBoxes() {
        for (String wineType : inventory.getInventoryMap().keySet()) {
            singleWineKindBox.addItem(wineType);
            mixedWineComboBox1.addItem(wineType);
            mixedWineComboBox2.addItem(wineType);
        }
    }

    public void setMainJRadioButtonActions() {
        this.buttonGroup = new ButtonGroup();
        buttonGroup.add(singleTypeOrderButton);
        buttonGroup.add(mixedTypeOrderButton);
        singleTypeOrderButton.addActionListener(e -> {
            sale = new Sale();
            sale.setMixed(false);
            mixedTypePanel.setVisible(false);
            singleTypePanel.setVisible(true);
        });

        mixedTypeOrderButton.addActionListener(e -> {
            sale = new Sale();
            sale.setMixed(true);
            singleTypePanel.setVisible(false);
            mixedTypePanel.setVisible(true);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
