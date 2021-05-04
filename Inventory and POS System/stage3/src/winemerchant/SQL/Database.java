package winemerchant.SQL;

import winemerchant.inventory.SupplierOrder;

import java.sql.*;

public class Database {
    private String url;

    public Database(String url) {
        this.url = "jdbc:sqlite:" + url;
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
        String wineType = order.getWine().getType();
        int amountPurchased = order.getAmountPurchased();
        double pricePaid = order.getPurchasedPrice();
        boolean isPaid = order.isPaid();

        String sql = "INSERT INTO supplier_orders (supplier_name, wine_type, amount_purchased, price_paid, is_paid) VALUES (?, ?, ?, ?, ?);";

        try(Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
}
