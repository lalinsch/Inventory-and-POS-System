package winemerchant.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int merlotUnitStock = 0;
    private int roseUnitStock = 0;
    private int sauvignonUnitStock = 0;
    private double totalValue = 0;

    public Inventory() {

    }

    public double getTotalValue() {
        return totalValue;
    }

    public void addBottles(Wine wine, int amount, double amountPurchased) {
        if (wine.getKind().equals("Merlot")) {
            merlotUnitStock += amount;
        } else if (wine.getKind().equals("Rosé")) {
            roseUnitStock += amount;
        } else if (wine.getKind().equals("Sauvignon Blanc")) {
            sauvignonUnitStock += amount;
        }
//        switch (wine.getKind()) {
//            case "Merlot" -> merlotUnitStock += amount;
//            case "Rosé" -> roseUnitStock += amount;
//            case "Sauvignon Blanc" -> sauvignonUnitStock += amount;
//        }
        totalValue += amountPurchased;
    }

    public int getUnitStockByType(Wine.WineType type) {
        if (type == Wine.WineType.MERLOT) {
            return merlotUnitStock;
        } else if (type == Wine.WineType.ROSE) {
            return roseUnitStock;
        } else if (type == Wine.WineType.SAUVIGNON) {
            return sauvignonUnitStock;
        } else {
            return 0;
        }
//        switch (type) {
//            case MERLOT -> {
//                return merlotUnitStock;
//            }
//            case ROSE -> {
//                return roseUnitStock;
//            }
//            case SAUVIGNON -> {
//                return sauvignonUnitStock;
//            }
//            default -> {
//                return 0;
//            }
//        }
    }

    public int getMerlotUnitStock() {
        return merlotUnitStock;
    }

    public int getRoseUnitStock() {
        return roseUnitStock;
    }

    public int getSauvignonUnitStock() {
        return sauvignonUnitStock;
    }

    public int getCaseStockByType(Wine.WineType type) {
        if (type == Wine.WineType.MERLOT) {
            return merlotUnitStock / 12;
        } else if (type == Wine.WineType.ROSE) {
            return roseUnitStock / 12;
        } else if (type == Wine.WineType.SAUVIGNON) {
            return sauvignonUnitStock / 12;
        } else {
            return 0;
        }
//        switch (type) {
//            case MERLOT -> {
//                return merlotUnitStock / 12;
//            }
//            case ROSE -> {
//                return roseUnitStock / 12;
//            }
//            case SAUVIGNON -> {
//                return sauvignonUnitStock / 12;
//            }
//            default -> {
//                return 0;
//            }
//        }
    }


    public void getStock() {
        System.out.println("Current wine stock:");
        for (Wine.WineType type : Wine.WineType.values()) {
            System.out.println(type.getColor() + " " + type.getColor() + " cases: " + getCaseStockByType(type) + " (" + getUnitStockByType(type) + " bottles)");
        }
        System.out.println("Total value: $" + getTotalValue());
    }

    public Case takeCaseFromStock(Wine.WineType wineType) {
        switch (wineType) {
            case MERLOT: {
                if (getMerlotUnitStock() < 12) {
                    System.out.println("Not enough stock!");
                } else {
                    merlotUnitStock -= 12;
                    return new Case(Wine.WineType.MERLOT);
                }
            }
            case ROSE: {

            }
            case SAUVIGNON: {
            }
        }
        return null;
    }

    public List<Wine> takeBottlesFromStock(Wine.WineType wineType, int amount) {
        switch (wineType) {
            case MERLOT:
                if (getMerlotUnitStock() < amount) {
                    System.out.println("Not enough stock!");
                } else {
                    List<Wine> halfCase = new ArrayList<>();
                    merlotUnitStock -= amount;
                    return halfCase;
                }
            case ROSE:
                if (getMerlotUnitStock() < amount) {
                    System.out.println("Not enough stock!");
                } else {
                    List<Wine> halfCase = new ArrayList<>();
                    roseUnitStock -= amount;
                    return halfCase;
                }
            case SAUVIGNON: {
            }
        }
        return null;
    }
}
