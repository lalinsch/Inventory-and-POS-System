package winemerchant;

import winemerchant.GUI.RootWindow;
import winemerchant.SQL.Database;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        Database database = new Database("Inventory and POS System/stage3/src/winemerchant/SQL/database.db");
        database.createNewTable();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame mainWindow = new RootWindow(database);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

}
