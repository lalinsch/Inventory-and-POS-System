package winemerchant;

import winemerchant.GUI.RootWindow;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        String databaseURL = "Inventory and POS System/stage5/src/winemerchant/SQL/database.db";
        if (args.length > 0) {
            if (args[0].equals("-fileName")) {
                databaseURL = args[1];
            }
        }
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        String finalDatabaseURL = databaseURL;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame mainWindow = new RootWindow(finalDatabaseURL);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}
