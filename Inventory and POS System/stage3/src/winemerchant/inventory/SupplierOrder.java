package winemerchant.inventory;

public class SupplierOrder {
    private Wine wine;
    private int amountPurchased;
    private double purchasedPrice;
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public double getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public SupplierOrder(Wine wine, int amountPurchased, double purchasedPrice) {
        this.wine = wine;
        this.amountPurchased = amountPurchased;
        this.purchasedPrice = purchasedPrice;
    }

    public Wine getWine() {
        return wine;
    }

    public int getAmountPurchased() {
        return amountPurchased;
    }



    @Override
    public String toString() {
        return "Wine:" + wine.getKind() +
                ", Amount purchased:" + amountPurchased +
                ", Purchased price: $" + purchasedPrice +
                ", is Paid:" + isPaid;
    }
}
