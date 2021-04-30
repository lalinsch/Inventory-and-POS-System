import org.assertj.swing.data.Index;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.*;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import winemerchant.GUI.RootWindow;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class WineMerchantTest extends SwingTest {

  public WineMerchantTest() {
    super(new RootWindow());
  }
  //Main Window Components
  @SwingComponent JTabbedPaneFixture tabbedPane;
//  @SwingComponent JPanelFixture submitOrderPanel;
//  @SwingComponent JPanelFixture orderListPanel;

  //First Pane components
  @SwingComponent JPanelFixture topPanel;
  @SwingComponent JPanelFixture middlePanel;
  @SwingComponent JPanelFixture bottomPanel;

  @SwingComponent JLabelFixture instructionLabel;
  @SwingComponent JRadioButtonFixture merlotButton;
  @SwingComponent JRadioButtonFixture roseButton;
  @SwingComponent JRadioButtonFixture sauvignonButton;

  @SwingComponent JComboBoxFixture amountComboBox;
  @SwingComponent JComboBoxFixture supplierComboBox;
  @SwingComponent JTextComponentFixture purchasedPriceTextField;

  @SwingComponent JLabelFixture messageLabel;
  @SwingComponent JButtonFixture submitButton;
  @SwingComponent JLabelFixture successLabel;

  //Second Pane components
  @SwingComponent JComboBoxFixture supplierOrderComboBox;
  @SwingComponent JButtonFixture filterButton;
  @SwingComponent JTableFixture ordersTable;

  @DynamicTest(feedback = "SubmitButton should be disabled.")
  CheckResult test1() {
    requireVisible(tabbedPane);
    tabbedPane.requireSelectedTab(Index.atIndex(0));
//    tabbedPane.requireDisabled(Index.atIndex(1));
    requireVisible(instructionLabel);
    requireVisible(topPanel);
    requireEnabled(merlotButton);
    requireEnabled(roseButton);
    requireEnabled(sauvignonButton);
    requireVisible(bottomPanel);
    requireNotVisible(middlePanel);
    requireNotVisible(successLabel);
    requireDisabled(submitButton);
    return correct();
  }

  @DynamicTest (feedback = "MiddlePanel should be visible after clicking on a wine type")
  CheckResult test2() {
    merlotButton.click();
    requireVisible(middlePanel);
    requireNotVisible(successLabel);
    requireEnabled(submitButton);
    return correct();
  }

  @DynamicTest (feedback = "Adding a valid name, purchased price amount should make SuccessLabel become visible")
  CheckResult test3() {
    supplierComboBox.enterText("Saba");
    amountComboBox.selectItem(2);
    purchasedPriceTextField.setText("845");
    submitButton.click();
    successLabel.requireVisible();
    return correct();
  }

  @DynamicTest (feedback = "The program should check that a valid name and purchased price amount has been entered" +
          "\n MessageLabel should include the word 'error'")
  CheckResult test4() {
    Pattern p = Pattern.compile("^.*[E|e]rror.*$");
    roseButton.click();
    amountComboBox.selectItem(0);
    supplierComboBox.enterText("S@ba");
    purchasedPriceTextField.setText("450");
    submitButton.click();
    messageLabel.requireText(p);
    supplierComboBox.enterText("Saba");
    purchasedPriceTextField.setText("45b");
    submitButton.click();
    messageLabel.requireText(p);
    return correct();
  }

  @DynamicTest (feedback = "When selecting 4 cases, the success message should include the number '48'" +
          " in the String confirming they have successfully been added")
  CheckResult test5() {
    Pattern p = Pattern.compile("^.*48.*$");
    sauvignonButton.click();
    amountComboBox.selectItem(3);
    supplierComboBox.enterText("Tom");
    purchasedPriceTextField.setText("451.15");
    submitButton.click();
    messageLabel.requireText(p);
    successLabel.requireVisible();
    return correct();
  }

  @DynamicTest (feedback = "Your JTabbedPane should contain two tabs and change view when clicked" +
          "The table should have 5 columns")
  CheckResult test6() {
    tabbedPane.selectTab(1);
    tabbedPane.requireSelectedTab(Index.atIndex(1));
    requireVisible(supplierOrderComboBox);
    requireVisible(filterButton);
    requireVisible(ordersTable);
    ordersTable.requireColumnCount(5);
    return correct();
  }

  @DynamicTest (feedback = "After entering some orders in the Submit Order Panel, " +
          "your table should populate with the orders")
  CheckResult test7() {
    tabbedPane.selectTab(0);
    Pattern p = Pattern.compile("^.*72.*$");
    merlotButton.click();
    amountComboBox.selectItem(5);
    supplierComboBox.enterText("Jesus");
    purchasedPriceTextField.setText("7000");
    submitButton.click();
    messageLabel.requireText(p);
    successLabel.requireVisible();
    tabbedPane.selectTab(1);
    ordersTable.requireRowCount(3);
    return correct();
  }

  @DynamicTest (feedback = "In the Submit Order Panel, you should have a JComboBox called SupplierOrderComboBox" +
          ". This should have the name of all suppliers and it should let you filter the orders of each supplier." +
          "The first item in the Combo Box should give the option to return to view all orders")
  CheckResult test8() {
    supplierOrderComboBox.requireItemCount(4);
    String supplier = supplierOrderComboBox.valueAt(1);
    supplierOrderComboBox.selectItem(supplier);
    filterButton.click();
    ordersTable.requireRowCount(1);
    ordersTable.cell(TableCell.row(0).column(0)).requireValue(supplier);
    supplierOrderComboBox.selectItem(0);
    filterButton.click();
    ordersTable.requireRowCount(3);
    return correct();
  }


}
