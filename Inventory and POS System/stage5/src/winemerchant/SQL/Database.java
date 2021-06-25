package winemerchant.SQL;

import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierOrder;
import winemerchant.inventory.SupplierRecord;
import winemerchant.inventory.Wine;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

public class Database {
    private String url;

    public Database(String url) {
        this.url = "jdbc:sqlite:" + url;
        createSupplierOrderTable();
        createInventoryTable();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void createSupplierOrderTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"supplier_orders\" (\n" +
                "'order_id' INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "'supplier_name' TEXT,\n" +
                "'wine_type' TEXT,\n" +
                "'amount_purchased' INTEGER,\n" +
                "'price_paid' NUMERIC,\n" +
                "'is_paid' INTEGER)";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createInventoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS 'inventory' (\n" +
                "'wine_type' TEXT NOT NULL PRIMARY KEY, \n" +
                "'unit_count'  NUMERIC, \n" +
                "'price_per_bottle' NUMERIC);";
        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addSupplierOrder(String supplier, SupplierOrder order) {
        String wineType = order.getWine().getColor();
        int amountPurchased = order.getAmountPurchased();
        double pricePaid = order.getPurchasedPrice();
        boolean isPaid = order.isPaid();

        String sql = "INSERT INTO supplier_orders (supplier_name, wine_type, amount_purchased, price_paid, is_paid) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier);
            pstmt.setString(2, wineType);
            pstmt.setInt(3, amountPurchased);
            pstmt.setDouble(4, pricePaid);
            pstmt.setBoolean(5, isPaid);

            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean supplierOrderEmpty() {
        int count = 0;
        ResultSet rs = null;
        String sql = "SELECT * FROM supplier_orders";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                count++;
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count == 0;
    }

    public SupplierRecord buildSupplierRecord() {
        String sql = "SELECT * FROM supplier_orders";
        SupplierRecord sr = new SupplierRecord(this);
        try (Connection conn = this.connect(); PreparedStatement pstmt = connect().prepareStatement(sql)) {
            sr.setSupplierOrderMap(getSupplierOrderMap());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sr;
    }

    //Not using currently but could be used for other types of implementation of JTable
    public DefaultTableModel buildTableModel() {
        String sql = "SELECT * FROM supplier_orders";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            //populate column names
            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();

            //populate data
            Vector<Vector<Object>> data = new Vector<>();
            while(rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            return new DefaultTableModel(data, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, List<SupplierOrder>> getSupplierOrderMap() {
        if (supplierOrderEmpty()) {
            return new HashMap<>();
        } else {
            HashMap<String, List<SupplierOrder>> dbMap = new HashMap<>();
            String sql = "SELECT * FROM supplier_orders";
            try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String supplier = rs.getString("supplier_name");
                    String wineType = rs.getString("wine_type");
                    int amountPurchased = rs.getInt("amount_purchased");
                    double pricePaid = rs.getDouble("price_paid");
                    boolean isPaid = rs.getBoolean("is_paid");

                    Wine wine;
                    switch (wineType) {
                        case "Red":
                            wine = new Wine(Wine.WineType.MERLOT, amountPurchased, pricePaid);
                            break;
                        case "Rosé":
                            wine = new Wine(Wine.WineType.ROSE, amountPurchased, pricePaid);
                            break;
                        case "White":
                            wine = new Wine(Wine.WineType.SAUVIGNON, amountPurchased, pricePaid);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + wineType);
                    }

                    SupplierOrder supplierOrder = new SupplierOrder(wine, amountPurchased, pricePaid);
                    if (!dbMap.containsKey(supplier)) {
                        dbMap.put(supplier, new ArrayList<>());
                    }
                    dbMap.get(supplier).add(supplierOrder);
                }
                return dbMap;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void addOrderToInventory(Wine wine, int amountPurchased, double purchasedPrice) {
        String sql = "INSERT INTO inventory (wine_type, unit_count)\n" +
                "VALUES(?, ?) \n" +
                "ON CONFLICT(wine_type) \n" +
                "DO UPDATE SET unit_count=unit_count + ?;";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String wineType = wine.toString();
            pstmt.setString(1, wine.toString());
            pstmt.setInt(2, amountPurchased);
            pstmt.setInt(3, amountPurchased);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Inventory buildInventory() {
        String sql = "SELECT * FROM supplier_orders";
        Inventory inventory = new Inventory(this);
        try (Connection conn = this.connect(); PreparedStatement pstmt = connect().prepareStatement(sql)) {
            inventory.setInventoryMap(getInventoryMap());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }


    public Map<String, Integer> getInventoryMap() {
        if(inventoryIsEmpty()) {
            return new HashMap<>();
        } else {
            Map<String, Integer> inventoryMap = new HashMap<>();
            String sql = "SELECT * FROM inventory";
            try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String wineType = rs.getString("wine_type");
                    int amount  = rs.getInt("unit_count");
                    inventoryMap.put(wineType, amount);
                }
                return inventoryMap;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean inventoryIsEmpty() {
        int count = 0;
        ResultSet rs = null;
        String sql = "SELECT * FROM inventory";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                count++;
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count == 0;
    }
}
