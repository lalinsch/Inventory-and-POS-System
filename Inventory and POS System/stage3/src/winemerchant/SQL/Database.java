package winemerchant.SQL;

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
        createNewTable();
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

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS \"supplier_orders\" (\n" +
                "\t\"order_id\"\tINTEGER,\n" +
                "\t\"supplier_name\"\tTEXT,\n" +
                "\t\"wine_type\"\tTEXT,\n" +
                "\t\"amount_purchased\"\tINTEGER,\n" +
                "\t\"price_paid\"\tNUMERIC,\n" +
                "\t\"is_paid\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"order_id\" AUTOINCREMENT)\n" +
                ");";
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

    public boolean isEmpty() {
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

    //TODO check if table model is sufficient or if it's better to build a supplierRecord
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
        if (isEmpty()) {
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
}
