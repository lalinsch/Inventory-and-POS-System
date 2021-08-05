import com.google.gson.annotations.Since;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.swing.data.Index;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.*;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.After;
import winemerchant.GUI.RootWindow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.regex.Pattern;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

public class WineMerchantTest extends SwingTest {
  private static String databaseName = "test.db";
  private String[] args = {"-fileName", databaseName};

  public WineMerchantTest() throws SQLException {
    super(new RootWindow(databaseName));
  }

  //Database components
  Connection connection;


  //Main Window Components
  @SwingComponent
  JTabbedPaneFixture tabbedPane;

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

  //Third Pane Components
  @SwingComponent JLabelFixture merlotAmountLabel;
  @SwingComponent JLabelFixture roseAmountLabel;
  @SwingComponent JLabelFixture sauvignonAmountLabel;
  @SwingComponent JLabelFixture totalAmountLabel;

  //Fourth Pane Components
  @SwingComponent JPanelFixture posTopPanel;
  @SwingComponent JPanelFixture singleTypePanel;
  @SwingComponent JPanelFixture mixedTypePanel;
  @SwingComponent JPanelFixture posBottomPanel;
  @SwingComponent JLabelFixture posInstructionLabel;
  @SwingComponent JRadioButtonFixture singleTypeOrderButton;
  @SwingComponent JRadioButtonFixture mixedTypeOrderButton;
  @SwingComponent JComboBoxFixture singleWineKindBox;
  @SwingComponent JButtonFixture singleConfirmButton;
  @SwingComponent JComboBoxFixture mixedWineComboBox1;
  @SwingComponent JComboBoxFixture mixedWineComboBox2;
  @SwingComponent JButtonFixture mixedConfirmButton;
  @SwingComponent JTextComponentFixture customerNameField;
  @SwingComponent JTextComponentFixture saleAmountField;
  @SwingComponent JRadioButtonFixture loyaltyDiscountButton;
  @SwingComponent JButtonFixture submitOrderButton;
  @SwingComponent JLabelFixture saleMessageLabel;
  @SwingComponent JLabelFixture saleSuccessMessageLabel;




  @DynamicTest
  CheckResult test1_checkDatabaseFile() throws IOException {
    File file = new File(args[1]);
    if (!file.exists()) {
      return CheckResult.wrong("You should create a database file " +
              "named " + databaseName + ". The file name should be taken from the command line arguments.\n" +
              "The database file shouldn't be deleted after stopping the program!");
    }
    return CheckResult.correct();
  }

  @DynamicTest
  CheckResult test2_checkConnection() {
    getConnection();
    closeConnection();
    return CheckResult.correct();
  }

  @DynamicTest
  CheckResult test3_checkIfSupplierTableExists() {
    try {
      ResultSet resultSet = getConnection().createStatement().executeQuery(
              "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
      while (resultSet.next()) {
        if (resultSet.getString("name").equals("supplier_orders")) {
          closeConnection();
          return CheckResult.correct();
        }
      }
    } catch (SQLException e) {
      closeConnection();
      return CheckResult.wrong("Can't execute a query in your database! Make sure that your database isn't broken and you close your connection at the end of the program!");
    }

    closeConnection();
    return CheckResult.wrong("Your database doesn't have a table named 'supplier_orders'");
  }

  @DynamicTest
  CheckResult test4_checkIfInventoryTableExists() {
    try {
      ResultSet resultSet = getConnection().createStatement().executeQuery(
              "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
      while (resultSet.next()) {
        if (resultSet.getString("name").equals("inventory")) {
          closeConnection();
          return CheckResult.correct();
        }
      }
    } catch (SQLException e) {
      closeConnection();
      return CheckResult.wrong("Can't execute a query in your database! Make sure that your database isn't broken and you close your connection at the end of the program!");
    }

    closeConnection();
    return CheckResult.wrong("Your database doesn't have a table named 'inventory'");
  }

  @DynamicTest(feedback = "SubmitButton should be disabled.")
  CheckResult test5_checkSwingUIElements() {

    requireVisible(tabbedPane);
    tabbedPane.requireSelectedTab(Index.atIndex(0));
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

  @DynamicTest(feedback = "at the beginning, your OrdersTable should be empty and everything in your inventory should be with a '0' value as well")
  CheckResult test6_checkTableAndInventoryAreEmpty() {
    tabbedPane.selectTab(1);
    ordersTable.requireRowCount(0);
    tabbedPane.selectTab(2);
    merlotAmountLabel.requireText("0");
    roseAmountLabel.requireText("0");
    sauvignonAmountLabel.requireText("0");
    tabbedPane.selectTab(0);
    return CheckResult.correct();
  }

  @DynamicTest (feedback = "MiddlePanel should be visible after clicking on a wine type")
  CheckResult test7_checkMiddlePanel() {
    merlotButton.click();
    requireVisible(middlePanel);
    requireNotVisible(successLabel);
    requireEnabled(submitButton);
    return correct();
  }

  @DynamicTest (feedback = "Adding a valid name, purchased price amount should make SuccessLabel become visible")
  CheckResult test8_checkValidName() {
    supplierComboBox.enterText("Saba");
    amountComboBox.selectItem(2);
    purchasedPriceTextField.setText("845");
    submitButton.click();
    successLabel.requireVisible();
    return correct();
  }

  @DynamicTest (feedback = "The program should check that a valid name and purchased price amount has been entered" +
          "\n MessageLabel should include the word 'error'")
  CheckResult test9_checkValidNumericValue() {
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
  CheckResult test10_checkSuccessfulMessage() {
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

  @DynamicTest (feedback = "Your JTabbedPane should contain three tabs and change view when clicked" +
          "The table should have 5 columns")
  CheckResult test11_checkTabbedPane() {
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
  CheckResult test12_checkTable() {
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
  CheckResult test13_checkFilter() {
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

  @DynamicTest
  CheckResult test14_addNewSupplierToTableAndCheckWithDatabase() {
    tabbedPane.selectTab(0);
    roseButton.click();
    supplierComboBox.enterText("Testy");
    amountComboBox.selectItem(2);
    purchasedPriceTextField.setText("4500.01");
    submitButton.click();

    merlotButton.click();
    supplierComboBox.enterText("Mctesterson");
    amountComboBox.selectItem(3);
    purchasedPriceTextField.setText("7000.43");
    submitButton.click();

    String[] rowData = {"Mctesterson", "Red", "4", "7000.43", "false"};
    String[] result = getRow("Mctesterson");
    if (rowData != result) {
      CheckResult.wrong("Received incorrect data received from database. Make sure the column names in your databse are: " +
              "'supplier_name', 'wine_type', 'amount_purchased', 'price_paid', and 'is_paid'. Additionally, 'wine_type' should" +
              "only refer to the colour of the wine 'White', 'Red' or'Rosé'");
    }
    closeConnection();
    return CheckResult.correct();
  }

  @DynamicTest
  CheckResult test15_checkInventoryElementsArePresent() {
    tabbedPane.selectTab(2);
    merlotAmountLabel.requireVisible();
    roseAmountLabel.requireVisible();
    sauvignonAmountLabel.requireVisible();
    totalAmountLabel.requireVisible();
    return CheckResult.correct();
  }

  @DynamicTest
  CheckResult test16_checkInventoryAmountsAreCorrect() {
    merlotAmountLabel.requireText("156");
    roseAmountLabel.requireText("36");
    sauvignonAmountLabel.requireText("48");
    totalAmountLabel.requireText("240");
    return CheckResult.correct();
  }

  @DynamicTest (feedback = "Make sure all the GUI elements are present and named according to the instructions")
  CheckResult test17_checkPOSElementsArePresent() {
    tabbedPane.selectTab(3);
    posTopPanel.requireVisible();
    posBottomPanel.requireVisible();
    customerNameField.requireDisabled();
    saleAmountField.requireDisabled();
    loyaltyDiscountButton.requireDisabled();
    submitOrderButton.requireDisabled();
    singleTypePanel.requireNotVisible();
    mixedTypePanel.requireNotVisible();
    singleTypeOrderButton.requireVisible();
    singleTypeOrderButton.requireEnabled();
    mixedTypeOrderButton.requireVisible();
    mixedTypeOrderButton.requireEnabled();
    posInstructionLabel.requireVisible();
    return CheckResult.correct();
  }

  @DynamicTest(feedback = "After clicking single type, your single panel should be visible. Your SingleWineKindBox should include " +
          "3 options to select from, and the confirm button should make sure the correct option is selected by displaying a message" +
          " on SaleMessageLabel.")
  CheckResult test18_submitSingleTypeOrder() {
    Pattern p = Pattern.compile("^.*[S|s]elected.*$");

    singleTypeOrderButton.click();
    singleTypePanel.requireVisible();
    mixedTypePanel.requireNotVisible();
    mixedTypeOrderButton.click();
    singleTypePanel.requireNotVisible();
    mixedTypePanel.requireVisible();
    singleTypeOrderButton.click();

    singleWineKindBox.requireItemCount(3);
    singleWineKindBox.selectItem(0);
    singleConfirmButton.requireEnabled();
    singleConfirmButton.click();

    saleMessageLabel.requireText(p);
    customerNameField.requireEnabled();
    saleAmountField.requireEnabled();
    loyaltyDiscountButton.requireEnabled();
    submitOrderButton.requireEnabled();

    return CheckResult.correct();
  }

  @DynamicTest (feedback = "On the order submission screen, your program should check that a valid name and sale amount have been entered." +
          "The valid name should be in the format 'Johnny' and the amount should only allow numerical values '500.75 for example. Your SaleSuccessMessageLabel " +
          "should include the word 'Error'")
  CheckResult test19_validateCustomerData() {
    Pattern p = Pattern.compile("^.*[E|e]rror.*$");

    customerNameField.enterText("L@lo");
    saleAmountField.enterText("4000");
    submitOrderButton.click();
    saleSuccessMessageLabel.requireText(p);

    customerNameField.deleteText();
    customerNameField.enterText("Lalo");
    saleAmountField.deleteText();
    saleAmountField.enterText("abcd");
    submitOrderButton.click();
    saleSuccessMessageLabel.requireText(p);

    return CheckResult.correct();
  }

  @DynamicTest (feedback = "If valid data is entered, your program should submit the order and display a summary on SaleSuccessMessage. " +
          "The message should include the customer name and sale amount.")
  CheckResult test20_submitOrder() {
    Pattern p = Pattern.compile("^.*[L|l]alo.*5000\\.50.*$");
    customerNameField.deleteText();
    customerNameField.enterText("Lalo");
    saleAmountField.deleteText();
    saleAmountField.enterText("5000.50");
    submitOrderButton.click();
    saleSuccessMessageLabel.requireText(p);

    return CheckResult.correct();
  }

  @DynamicTest (feedback = "When the LoyaltyDiscountButton is selected, make sure your program can apply that discount and display it " +
          "on SaleSuccessMessageLabel")
  CheckResult test21_checkDiscountIsApplied() {
    Pattern p = Pattern.compile(".*[J|j]ose.*85.*$");
    singleTypeOrderButton.click();
    singleWineKindBox.selectItem(1);
    singleConfirmButton.click();
    customerNameField.enterText("Jose");
    saleAmountField.enterText("100");
    loyaltyDiscountButton.click();
    submitOrderButton.click();
    saleSuccessMessageLabel.requireText(p);
    return CheckResult.correct();
  }

  @DynamicTest (feedback = "When submitting a mixed order, it shouldn't be possible to select two wines of the same kind")
  CheckResult test22_selectMixedWineOrder() {
      Pattern p = Pattern.compile("^.*[E|e]rror.*$");
      mixedTypeOrderButton.click();
      mixedTypePanel.requireVisible();
      customerNameField.requireDisabled();
      saleAmountField.requireDisabled();
      submitOrderButton.requireDisabled();
      loyaltyDiscountButton.requireDisabled();
      mixedWineComboBox1.requireItemCount(3);
      mixedWineComboBox2.requireItemCount(3);
      mixedWineComboBox1.selectItem(0);
      mixedWineComboBox2.selectItem(0);
      mixedConfirmButton.click();

      customerNameField.requireDisabled();
      saleAmountField.requireDisabled();
      submitOrderButton.requireDisabled();
      loyaltyDiscountButton.requireDisabled();

      saleMessageLabel.requireText(p);
      return CheckResult.correct();
  }

  @DynamicTest (feedback = "After selecting a correct combination, it should be possible to submit a mixed order sale")
  CheckResult test23_submitMixedOrder() {
    Pattern p = Pattern.compile("^.*[S|s]elected.*$");
    Pattern s = Pattern.compile(".*[M|m]ixed.*888.*$");
    mixedWineComboBox1.selectItem(2);
    mixedConfirmButton.click();

    saleMessageLabel.requireText(p);
    customerNameField.requireEnabled();
    saleAmountField.requireEnabled();
    submitOrderButton.requireEnabled();
    loyaltyDiscountButton.requireEnabled();
    customerNameField.enterText("Mixed");
    saleAmountField.enterText("888");
    submitOrderButton.click();
    saleSuccessMessageLabel.requireText(s);
    return CheckResult.correct();
  }

  @DynamicTest (feedback = "Any orders that your program processes should affect the stock count")
  CheckResult test24_checkStockAfterSale() {
    tabbedPane.selectTab(2);
    merlotAmountLabel.requireText("144");
    roseAmountLabel.requireText("18");
    sauvignonAmountLabel.requireText("42");
    totalAmountLabel.requireText("204");
    return CheckResult.correct();
  }

  @DynamicTest (feedback = "If one or more of the selected bottles doesn't have enough stock, it shouldn't be possible " +
          "to continue with the order. Your SaleSuccessMessageLable should display a message that includes the word 'Error'" +
          "if this happens")
  CheckResult test25_checkStockBeforeContinuing() {
    Pattern p = Pattern.compile("^.*[E|e]rror.*$");
    tabbedPane.selectTab(3);
    sellCaseOfRose();
    sellCaseOfRose();
    saleSuccessMessageLabel.requireText(p);

    return CheckResult.correct();
  }

  private void sellCaseOfRose() {
    singleTypeOrderButton.click();
    singleWineKindBox.selectItem(0);
    singleConfirmButton.click();
    customerNameField.enterText("Edu");
    saleAmountField.enterText("6000");
    submitOrderButton.click();
  }

  private String[] getRow(String supplier) {
    String[] result = new String[0];
    try {
      PreparedStatement pstmt = getConnection().prepareStatement("SELECT * FROM supplier_orders where supplier_name = ?");
      pstmt.setString(1, supplier);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      String name = rs.getString("supplier_name");
      String wine = rs.getString("wine_type");
      String amount = String.valueOf(rs.getInt("amount_purchased"));
      String pricePaid = String.valueOf(rs.getDouble("price_paid"));
      String isPaid = String.valueOf(rs.getBoolean("is_paid"));
      result = new String[] {name, wine, amount, pricePaid, isPaid};
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  @After
  public void deleteFile() {
    try {
      deleteAllRows();
      Files.delete(Paths.get(databaseName));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void closeConnection() {
    if (connection == null)
      return;
    try {
      connection.close();
    } catch (SQLException ignored) {
    }
    connection = null;
  }

  private Connection getConnection() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
      } catch (SQLException exception) {
        throw new WrongAnswer("Can't connect to the database! Make sure you close your database" +
                " connection at the end of the program!");
      }
    }
    return connection;
  }

  private void deleteAllRows() {
    try {
      getConnection().createStatement().execute("DELETE FROM supplier_orders");
      getConnection().createStatement().execute("DELETE FROM inventory");
      closeConnection();
    } catch (SQLException exception) {
      throw new WrongAnswer("Can't execute a query in your database! Make sure that your database isn't broken and you close your connection at the end of the program!");
    }
  }
}