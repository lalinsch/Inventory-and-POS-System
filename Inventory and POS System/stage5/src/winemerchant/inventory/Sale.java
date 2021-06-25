package winemerchant.inventory;

public class Sale {
    private String customerName;
    private double saleAmount;
    private boolean isDiscounted;
    private boolean isMixed;
    private Wine.WineType singleOrderWineType;
    private Wine.WineType mixedOrderWineOne;
    private Wine.WineType mixedOrderWineTwo;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public void setDiscounted(boolean discounted) {
        isDiscounted = discounted;
    }

    public boolean isMixed() {
        return isMixed;
    }

    public void setMixed(boolean mixed) {
        isMixed = mixed;
    }

    public Wine.WineType getSingleOrderWineType() {
        return singleOrderWineType;
    }

    public void setSingleOrderWineType(Wine.WineType singleOrderWineType) {
        this.singleOrderWineType = singleOrderWineType;
    }

    public Wine.WineType getMixedOrderWineOne() {
        return mixedOrderWineOne;
    }

    public void setMixedOrderWineOne(Wine.WineType mixedOrderWineOne) {
        this.mixedOrderWineOne = mixedOrderWineOne;
    }

    public Wine.WineType getMixedOrderWineTwo() {
        return mixedOrderWineTwo;
    }

    public void setMixedOrderWineTwo(Wine.WineType mixedOrderWineTwo) {
        this.mixedOrderWineTwo = mixedOrderWineTwo;
    }
}
