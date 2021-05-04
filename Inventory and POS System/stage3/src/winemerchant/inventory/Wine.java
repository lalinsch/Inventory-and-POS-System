package winemerchant.inventory;

public class Wine {
    private String type;
    private String kind;
    private String supplier;
    private boolean paidToSupplier;

    public String getType() {
        return type;
    }

    public String getKind() {
        return kind;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public boolean isPaidToSupplier() {
        return paidToSupplier;
    }

    public void setPaidToSupplier(boolean paidToSupplier) {
        this.paidToSupplier = paidToSupplier;
    }

    public double getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(double supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    private double supplierPrice;
    private double pricePerBottle;
    private WineType wineType;
    private int productStock;

    public enum WineType {
        MERLOT("Red", "Merlot"),
        ROSE("Rosé", "Zinfandel"),
        SAUVIGNON("White", "Sauvignon Blanc");

        private final String type;
        private final String kind;

        WineType(String type, String kind) {
            this.type = type;
            this.kind = kind;
        }

        public String getType() {
            return type;
        }
        public String getKind() {
            return kind;
        }

        @Override
        public String toString() {
            return type + " " + kind;
        }
    }

    public Wine(WineType wineType) {
        setWineType(wineType);
        setPricePerBottle(375.00);
    }

    public double getPricePerBottle() {
        return pricePerBottle;
    }

    public WineType getWineType() {
        return wineType;
    }

    public void setWineType(WineType type){
        setType(type.type);
        setKind(type.kind);
        this.wineType = type;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPricePerBottle(double pricePerBottle) {
        this.pricePerBottle = pricePerBottle;
    }
}
