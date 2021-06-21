import org.assertj.swing.data.Index;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.*;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.After;
import winemerchant.GUI.RootWindow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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