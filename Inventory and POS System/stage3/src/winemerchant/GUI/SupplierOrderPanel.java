package winemerchant.GUI;

import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;
import winemerchant.inventory.Wine;

import javax.swing.*;

public class SupplierOrderPanel{
    private JPanel mainPanel;
    private JPanel topPanel;
    private JRadioButton merlotButton;
    private JPanel middlePanel;
    private JLabel instructionLabel;
    private JLabel selectedTypeLabel;
    private JComboBox<Object> supplierComboBox;
    private JLabel supplierLabel;
    private JPanel bottomPanel;
    private JComboBox<Integer> amountComboBox;
    private JButton submitButton;
    private JRadioButton roseButton;
    private JRadioButton sauvignonButton;
    private JLabel messageLabel;
    private JLabel purchasedPriceLabel;
    private JTextField purchasedPriceTextField;
    private JLabel successLabel;
    private ButtonGroup buttonGroup;

    //Variables for creating a new supply order.
    SupplierRecord supplierRecord;
    private int[] amountOptions = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private String supplierName;
    private String[] supplierNames;
    private double purchasedPrice;
    private int casesAmount;
    private int amount;
    private Wine.WineType wineType;
    private Wine wine;
    private boolean isValidInput;

    public SupplierOrderPanel(SupplierRecord supplierRecord) {
        this.supplierRecord = supplierRecord;
        nameComponents();
        this.buttonGroup = new ButtonGroup();
        buttonGroup.add(merlotButton);
        buttonGroup.add(roseButton);
        buttonGroup.add(sauvignonButton);
        successLabel.setVisible(false);

        supplierNames = supplierRecord.getSuppliers();

        //Sets the behaviour of the GUI elements
        merlotButton.addActionListener(e -> {
            wineType = Wine.WineType.MERLOT;
            selectedTypeLabel.setText("Selected type: " + Wine.WineType.MERLOT);
            middlePanel.setVisible(true);
            submitButton.setEnabled(true);
            successLabel.setVisible(false);
            messageLabel.setText("");
        });
        roseButton.addActionListener(e -> {
            wineType = Wine.WineType.ROSE;
            selectedTypeLabel.setText("Selected type: " + Wine.WineType.ROSE);
            middlePanel.setVisible(true);
            submitButton.setEnabled(true);
            successLabel.setVisible(false);
            messageLabel.setText("");
        });
        sauvignonButton.addActionListener(e -> {
            wineType = Wine.WineType.SAUVIGNON;
            selectedTypeLabel.setText("Selected type: " + Wine.WineType.SAUVIGNON);
            middlePanel.setVisible(true);
            submitButton.setEnabled(true);
            successLabel.setVisible(false);
            messageLabel.setText("");
        });

        middlePanel.setVisible(false);
        //Populate the dropdown with the possible options
        for (int amountOption : amountOptions) {
            amountComboBox.addItem(amountOption);
        }


        submitButton.addActionListener(e -> {
            isValidInput = true;
            //Check if the input on the supplier name is valid
            if (supplierComboBox.getSelectedItem() == null) {
                isValidInput = false;
                messageLabel.setText("Error: Please enter a name.");
            } else if (!((String) supplierComboBox.getSelectedItem()).matches("[A-Z][a-z]*")) {
                isValidInput = false;
                messageLabel.setText("Error: Incorrect name format");
            } else {
                supplierComboBox.addItem(supplierComboBox.getSelectedItem());
                supplierName = (String) supplierComboBox.getSelectedItem();
            }

            //check if the numeric input is valid
            try {
                purchasedPrice = Double.parseDouble(purchasedPriceTextField.getText());
                casesAmount = ((Integer) amountComboBox.getSelectedItem());
                amount = casesAmount * 12;
            } catch (NumberFormatException numberFormatException) {
                isValidInput = false;
                numberFormatException.printStackTrace();
                messageLabel.setText("Error: Invalid number format");
            }

            //Display success message and resets the layout
            if (isValidInput) {
                wine = new Wine(wineType, amount, purchasedPrice);
                SupplierOrder supplierOrder = new SupplierOrder(wine, amount, purchasedPrice);
                supplierRecord.addOrder(supplierName, supplierOrder);
                messageLabel.setText("Added " + casesAmount + " cases of " + wine.getKind() + " from " +
                        supplierName + " (" + amount + " bottles).");

                restartView();
            }
        });
    }

    private void nameComponents() {
        // TODO: a space for naming components for testing purposes
        topPanel.setName("TopPanel");
        middlePanel.setName("MiddlePanel");
        bottomPanel.setName("BottomPanel");

        //Top panel
        instructionLabel.setName("InstructionLabel");
        merlotButton.setName("MerlotButton");
        roseButton.setName("RoseButton");
        sauvignonButton.setName("SauvignonButton");

        //Middle panel
        supplierComboBox.setName("SupplierComboBox");
        purchasedPriceTextField.setName("PurchasedPriceTextField");
        amountComboBox.setName("AmountComboBox");

        //Bottom panel
        messageLabel.setName("MessageLabel");
        submitButton.setName("SubmitButton");
        successLabel.setName("SuccessLabel");
    }

    private void populateSupplierCombo() {
        supplierComboBox.removeAllItems();
        for (String name : supplierNames) {
            supplierComboBox.addItem(name);
        }
    }

    private void restartView() {
        successLabel.setText("Success!");
        successLabel.setVisible(true);
        submitButton.setEnabled(false);
        supplierComboBox.setSelectedIndex(0);
        amountComboBox.setSelectedIndex(0);
        purchasedPriceTextField.setText("");
        buttonGroup.clearSelection();
        middlePanel.setVisible(false);

        supplierNames = supplierRecord.getSuppliers();
        populateSupplierCombo();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
