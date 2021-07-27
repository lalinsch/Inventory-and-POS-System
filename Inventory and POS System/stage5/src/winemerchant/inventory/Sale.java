package winemerchant.inventory;

public class Sale {
    private String customerName;
    private double saleAmount;
    private boolean isDiscounted;
    private boolean isMixed;
    private String singleOrderWineType;
    private String mixedOrderWineOne;
    private String mixedOrderWineTwo;

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

    public String getSingleOrderWineType() {
        return singleOrderWineType;
    }

    public void setSingleOrderWineType(String singleOrderWineType) {
        this.singleOrderWineType = singleOrderWineType;
    }

    public String getMixedOrderWineOne() {
        return mixedOrderWineOne;
    }

    public void setMixedOrderWineOne(String mixedOrderWineOne) {
        this.mixedOrderWineOne = mixedOrderWineOne;
    }

    public String getMixedOrderWineTwo() {
        return mixedOrderWineTwo;
    }

    public void setMixedOrderWineTwo(String mixedOrderWineTwo) {
        this.mixedOrderWineTwo = mixedOrderWineTwo;
    }
}
