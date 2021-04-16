package winemerchant;

import winemerchant.GUI.RootWindow;
import winemerchant.inventory.Inventory;
import winemerchant.inventory.SupplierRecord;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
//        Scanner scanner = new Scanner(System.in);
//        UI ui = new UI(scanner);
//        ui.start();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainWindow = new RootWindow();
            }
        });

    }

}
