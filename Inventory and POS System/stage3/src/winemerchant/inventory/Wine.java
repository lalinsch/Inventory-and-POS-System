package winemerchant.inventory;

public class Wine {
    private String color;
    private String kind;
    private double pricePerBottle;


    public Wine(WineType wineType, int amountPurchased, double pricePaid) {
        setWineType(wineType);
        setPricePerBottle(amountPurchased, pricePaid);
    }

    public String getColor() {
        return color;
    }

    public double getPricePerBottle() {
        return pricePerBottle;
    }

    public void setWineType(WineType type) {
        setColor(type.color);
        setKind(type.kind);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setPricePerBottle(int amountPurchased, double pricePaid) {
        double pricePerBottle = pricePaid / amountPurchased;

        //Adding 20% markup to the price paid for each bottle.
        this.pricePerBottle = pricePerBottle * 1.20;
    }

    public String getKind() {
        return kind;
    }

    //Wine Type constants
    public enum WineType {
        MERLOT("Red", "Merlot"),
        ROSE("Rosé", "Zinfandel"),
        SAUVIGNON("White", "Sauvignon Blanc");

        private final String color;
        private final String kind;

        WineType(String color, String kind) {
            this.color = color;
            this.kind = kind;
        }

        public String getColor() {
            return color;
        }

        public String getKind() {
            return kind;
        }

        @Override
        public String toString() {
            return color + " " + kind;
        }
    }
}
