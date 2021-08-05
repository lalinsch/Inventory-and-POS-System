package winemerchant.GUI;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.Sale;

import javax.swing.*;
import java.util.regex.Pattern;


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
    private JButton singleConfirmButton;
    private JLabel wine1Label;
    private JComboBox mixedWineComboBox1;
    private JTextField customerNameField;
    private JTextField saleAmountField;
    private JRadioButton loyaltyDiscountButton;
    private JButton submitButton;
    private JLabel successLabel;
    private JLabel messageLabel;
    private JPanel mixedTypePanel;
    private JComboBox mixedWineComboBox2;
    private JButton mixedConfirmButton;
    private ButtonGroup buttonGroup;
    private Sale sale;
    final String Digits     = "(\\p{Digit}+)";
    final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
// signed decimal integer.
    final String Exp        = "[eE][+-]?"+Digits;
    final String fpRegex    =
            ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
                    "[+-]?(" +         // Optional sign character
                    "NaN|" +           // "NaN" string
                    "Infinity|" +      // "Infinity" string

                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from the Java Language Specification, 2nd
                    // edition, section 3.10.2.

                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                    // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\.("+Digits+")("+Exp+")?)|"+

                    // Hexadecimal strings
                    "((" +
                    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "(\\.)?)|" +

                    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                    ")[pP][+-]?" + Digits + "))" +
                    "[fFdD]?))" +
                    "[\\x00-\\x20]*");// Optional trailing "whitespace"

    public POSPanel (Inventory inventory) {
        nameComponents();
        this.inventory = inventory;
        setGUIElementActions();
        resetUI();
        buttonGroup.add(singleTypeOrderButton);
        buttonGroup.add(mixedTypeOrderButton);
    }

    private void setSaleElementsVisibility(boolean bool) {
        if (bool) {
            customerNameField.setEnabled(true);
            saleAmountField.setEnabled(true);
            loyaltyDiscountButton.setEnabled(true);
            submitButton.setEnabled(true);
        } else {
            customerNameField.setEnabled(false);
            customerNameField.setText("");
            saleAmountField.setEnabled(false);
            saleAmountField.setText("");
            loyaltyDiscountButton.setEnabled(false);
            loyaltyDiscountButton.setSelected(false);
        }

    }

    private void populateComboBoxes() {
        singleWineKindBox.removeAllItems();
        mixedWineComboBox1.removeAllItems();
        mixedWineComboBox2.removeAllItems();
        for (String wineType : inventory.getInventoryMap().keySet()) {
            singleWineKindBox.addItem(wineType);
            mixedWineComboBox1.addItem(wineType);
            mixedWineComboBox2.addItem(wineType);
        }
    }

    private boolean isValidMix() {
        if (((String) mixedWineComboBox1.getSelectedItem()).equals((String) mixedWineComboBox2.getSelectedItem())) {
            System.out.println("Wines in a mixed case have to be different");
            return false;
        } else {
            return true;
        }
    }

    public void setGUIElementActions() {
        this.buttonGroup = new ButtonGroup();
        buttonGroup.add(singleTypeOrderButton);
        buttonGroup.add(mixedTypeOrderButton);
        singleTypeOrderButton.addActionListener(e -> {
            successLabel.setText("");
            sale.setMixed(false);
            mixedTypePanel.setVisible(false);
            singleTypePanel.setVisible(true);
            singleConfirmButton.setEnabled(true);
            setSaleElementsVisibility(false);
        });

        mixedTypeOrderButton.addActionListener(e -> {
            successLabel.setText("");
            sale.setMixed(true);
            singleTypePanel.setVisible(false);
            mixedTypePanel.setVisible(true);
            singleConfirmButton.setEnabled(false);
            setSaleElementsVisibility(false);
        });

        singleConfirmButton.addActionListener(e -> {
            if (!sale.isMixed()) {
                sale.setSingleOrderWineType((String) singleWineKindBox.getSelectedItem());
                setSaleElementsVisibility(true);
                messageLabel.setText("Selected wine: "  + sale.getSingleOrderWineType());
            }
        });

        mixedConfirmButton.addActionListener(e -> {
            if (sale.isMixed() && !isValidMix()) {
                messageLabel.setText("Error: Wines can't be the same in a mixed case");
                setSaleElementsVisibility(false);
            } else {
                sale.setMixedOrderWineOne((String) mixedWineComboBox1.getSelectedItem());
                sale.setMixedOrderWineTwo((String) mixedWineComboBox2.getSelectedItem());
                setSaleElementsVisibility(true);
                messageLabel.setText("Selected wines: 1. " + sale.getMixedOrderWineOne() + " 2. " + sale.getMixedOrderWineTwo());
                System.out.println("Mixed order set as wine 1: " + sale.getMixedOrderWineOne() + " wine 2: " + sale.getMixedOrderWineTwo());
            }
        });

        submitButton.addActionListener(e -> {
            if (customerInputIsValid()) {
                if (inventory.inputSale(sale)) {
                    if (sale.isMixed()) {
                        sale.setMixedOrderWineOne((String) mixedWineComboBox1.getSelectedItem());
                        sale.setMixedOrderWineTwo((String) mixedWineComboBox2.getSelectedItem());
                    } else {
                        sale.setSingleOrderWineType((String) singleWineKindBox.getSelectedItem());
                    }

                    if (loyaltyDiscountButton.isSelected()) {
                        sale.setDiscounted(true);
                    }

                    sale.setCustomerName(customerNameField.getText());

                    sale.setSaleAmount(Double.valueOf(saleAmountField.getText()));
                    if(sale.isDiscounted()) {
                        sale.setSaleAmount(sale.getSaleAmount() * .85);
                    }
                    successLabel.setText("Order sold to " + sale.getCustomerName() + " of £" + String.format("%.2f", sale.getSaleAmount()));
                    resetUI();
                } else {
                    successLabel.setText("Error: Something went wrong, check your inventory");
                }
            } else {
                successLabel.setText("Error: Order wasn't submitted, check the format of your data");
            }
        });


    }

    public void resetUI() {
        buttonGroup.clearSelection();
        populateComboBoxes();
        singleTypePanel.setVisible(false);
        mixedTypePanel.setVisible(false);
        bottomPanel.setVisible(true);
        submitButton.setEnabled(false);
        setSaleElementsVisibility(false);
        messageLabel.setText("");
        sale = new Sale();
    }

    public boolean customerInputIsValid() {
        if ((customerNameField.getText().matches("[A-Z][a-z]*")) && Pattern.matches(fpRegex, saleAmountField.getText())) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void nameComponents() {
        //a space for naming components for testing purposes
        topPanel.setName("PosTopPanel");
        bottomPanel.setName("PosBottomPanel");
        singleTypePanel.setName("SingleTypePanel");
        mixedTypePanel.setName("MixedTypePanel");
        instructionalLabel.setName("PosInstructionLabel");
        singleTypeOrderButton.setName("SingleTypeOrderButton");
        mixedTypeOrderButton.setName("MixedTypeOrderButton");
        singleWineKindBox.setName("SingleWineKindBox");
        singleConfirmButton.setName("SingleConfirmButton");
        mixedWineComboBox1.setName("MixedWineComboBox1");
        mixedWineComboBox2.setName("MixedWineComboBox2");
        mixedConfirmButton.setName("MixedConfirmButton");
        customerNameField.setName("CustomerNameField");
        saleAmountField.setName("SaleAmountField");
        loyaltyDiscountButton.setName("LoyaltyDiscountButton");
        submitButton.setName("SubmitOrderButton");
        messageLabel.setName("SaleMessageLabel");
        successLabel.setName("SaleSuccessMessageLabel");
    }

}
