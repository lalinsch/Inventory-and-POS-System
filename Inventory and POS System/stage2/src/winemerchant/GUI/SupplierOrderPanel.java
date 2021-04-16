package winemerchant.GUI;

import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;
import winemerchant.inventory.Wine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupplierOrderPanel{
    private JPanel mainPanel;
    private JPanel topPanel;
    private JRadioButton merlotButton;
    private JPanel middlePanel;
    private JLabel instructionLabel;
    private JLabel selectedTypeLabel;
    private JComboBox supplierComboBox;
    private JLabel supplierLabel;
    private JPanel bottomPanel;
    private JComboBox amountComboBox;
    private JButton submitButton;
    private JRadioButton roseButton;
    private JRadioButton sauvignonButton;
    private JLabel messageLabel;
    private JLabel purchasedPriceLabel;
    private JTextField purchasedPriceTextField;
    private JLabel successLabel;

    //Variables for creating a new supply order.
    SupplierRecord supplierRecord;
    private int[] amountOptions = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private String supplierName;
    private double purchasedPrice;
    private int casesAmount;
    private int amount;
    private Wine wine;
    private boolean isValidInput;

    public SupplierOrderPanel(SupplierRecord supplierRecord) {
        this.supplierRecord = supplierRecord;
        nameComponents();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(merlotButton);
        buttonGroup.add(roseButton);
        buttonGroup.add(sauvignonButton);
        successLabel.setVisible(false);

        //Sets the behaviour of the GUI elements
        merlotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wine = new Wine(Wine.WineType.MERLOT);
                selectedTypeLabel.setText("Selected type: " + Wine.WineType.MERLOT);
                middlePanel.setVisible(true);
                submitButton.setEnabled(true);
                successLabel.setVisible(false);
                messageLabel.setText("");
            }
        });
        roseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wine = new Wine(Wine.WineType.ROSE);
                selectedTypeLabel.setText("Selected type: " + Wine.WineType.ROSE);
                middlePanel.setVisible(true);
                submitButton.setEnabled(true);
                successLabel.setVisible(false);
                messageLabel.setText("");
            }
        });
        sauvignonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wine = new Wine(Wine.WineType.SAUVIGNON);
                selectedTypeLabel.setText("Selected type: " + Wine.WineType.SAUVIGNON);
                middlePanel.setVisible(true);
                submitButton.setEnabled(true);
                successLabel.setVisible(false);
                messageLabel.setText("");
            }
        });

        middlePanel.setVisible(false);
        //Populate the dropdown with the possible options
        for (int i = 0; i < amountOptions.length; i++) {
            amountComboBox.addItem(amountOptions[i]);
        }


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isValidInput = true;
                //Check if the input on the supplier name is valid
                if (supplierComboBox.getSelectedItem() == null) {
                    isValidInput = false;
                    messageLabel.setText("Error: Please enter a name.");
                } else if (!((String) supplierComboBox.getSelectedItem()).matches("[A-Z][a-z]*")) {
                    isValidInput = false;
                    messageLabel.setText("Error: Incorrect name format");
                } else {
                    supplierComboBox.addItem((String) supplierComboBox.getSelectedItem());
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
                    successLabel.setText("Success!");
                    successLabel.setVisible(true);
                    submitButton.setEnabled(false);
                    supplierComboBox.setSelectedIndex(0);
                    amountComboBox.setSelectedIndex(0);
                    purchasedPriceTextField.setText("");
                    SupplierOrder supplierOrder = new SupplierOrder(wine, amount, purchasedPrice);
                    supplierRecord.addOrder(supplierName, supplierOrder);
                    messageLabel.setText("Added " + casesAmount + " cases of " + wine.getKind() + " from " +
                            supplierName + " (" + amount + " bottles).");
                    middlePanel.setVisible(false);
                }
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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
