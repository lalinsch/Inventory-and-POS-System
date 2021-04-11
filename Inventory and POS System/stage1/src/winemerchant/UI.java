package winemerchant;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;
import winemerchant.inventory.Wine;

import java.util.List;
import java.util.Scanner;

public class UI {
    public Inventory inventory = new Inventory();
    public SupplierRecord supplierRecord;
    public Scanner scanner;

    public UI(Scanner scanner) {
        supplierRecord = new SupplierRecord(inventory);
        this.scanner = scanner;
    }

    public UI() {
        supplierRecord = new SupplierRecord(inventory);
    }

    public void start() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
//                    checkStock();
                    continue;
                case "2":
                    addStock();
                    continue;
                case "3":
//                    inputSale();
                    continue;
                case "4":
//                    viewSales();
                    continue;
                case "5":
                    manageSupplierOrders();
                    continue;
                case "0":
                    break;
                default:
                    System.out.println("Invalid input");
                    continue;
            }
            break;
        }
    }

    private void printMenu() {
        System.out.println("Welcome! Choose one of the following options:");
        System.out.println("1. Check inventory\n2. Add stock\n3. Input sale\n" +
                "4. View sales\n5. Manage supplier orders\n0. Exit");
    }


    public void addStock() {
        String command = "";
        String supplier = "";
        int amountPurchased = 0;
        int purchasedPrice = 0;
        //Outer while loop which asks what kind of wine is being added until the user presses "0" to go back to the main menu
        while (!command.equals("0")) {
            System.out.println("Which wine do you want to add: 1. Arenal, 2. Colina 3. Cinco Estrellas:");
            System.out.println("Press 0 to go back");
            command = scanner.nextLine();
            if (command.equals("0")) {
                break;
            }
            Wine wine = chooseWineTypeToAdd(command);
            //internal loop that calculates how many bottles are coming into inventory depending if they're cases (12) or individual bottles
            while (wine != null) {
                System.out.println("Are you adding: 1.Cases or 2. Bottles? Press 0 to go back");
                String input = scanner.nextLine();
                if (input.equals("0")) {
                    break;
                }
                //User fills in the rest of the supplier order details
                amountPurchased = calculateNumberOfBottles(input);
                System.out.println("Who is the supplier?");
                supplier = scanner.nextLine();
                System.out.println("What was the order purchase price?");
                purchasedPrice = Integer.parseInt(scanner.nextLine());
                //Creates a supplier record with the input details (which also adds the bottles to the inventory)
                supplierRecord.addOrder(supplier, new SupplierOrder(wine, amountPurchased, purchasedPrice));
                System.out.println("Successfully added " + amountPurchased + " units to the stock");
            }
        }
    }

    public int calculateNumberOfBottles(String input) {
        while (true) {
            if (input.equals("1")) {
                System.out.println("How many cases would you like to add?");
                String inputAmount = scanner.nextLine();
                if (!inputAmount.matches("^(100|[1-9][0-9]?)$")) {
                    System.out.println("Try again, only with numbers between 1-100");
                    continue;
                } else {
                    return 12 * Integer.parseInt(inputAmount);
                }
            } else if (input.equals("2")) {
                System.out.println("How many bottles would you like to add?");
                String inputAmount = scanner.nextLine();
                if (!inputAmount.matches("^(100|[1-9][0-9]?)$")) {
                    System.out.println("Try again, only with numbers between 1-100");
                    continue;
                } else {
                    return Integer.parseInt(inputAmount);
                }
            }
        }
    }

    private Wine chooseWineTypeToAdd(String command) {
        switch (command) {
            case "1": {
                return new Wine(Wine.WineType.MERLOT);
            }
            case "2": {
                return new Wine(Wine.WineType.ROSE);
            }
            case "3": {
                return new Wine(Wine.WineType.SAUVIGNON);
            }
            default: {
                System.out.println("Invalid input");
                return null;
            }
        }
    }

    private void manageSupplierOrders() {
        if (supplierRecord.isEmpty()) {
            System.out.println("There are no supplier records yet");
        } else {
            while (true) {
                supplierRecord.viewAllSupplierRecord();
                System.out.println("Which supplier's order do you want to manage? Press 0 to go back");
                String supplier = scanner.nextLine();
                if (supplier.equals("0")) {
                    break;
                } else if (!supplierRecord.contains(supplier)) {
                    System.out.println("No such supplier found, try again");
                    continue;
                } else {
                    supplierRecord.viewSupplier(supplier);
                    System.out.println("Which order would you like to edit?");
                    int orderNumber = Integer.parseInt(scanner.nextLine()) - 1;
                    List<SupplierOrder> supplierOrderList = supplierRecord.getSupplierOrderListByName(supplier);
                    if (orderNumber < 0 || orderNumber > supplierOrderList.size() - 1) {
                        System.out.println("Invalid order number");
                    } else {
                        supplierRecord.setSupplierOrderAsPaid(supplier, orderNumber);
                        System.out.println("Order has been marked as paid.");
                    }
                }
            }
        }
    }
}
