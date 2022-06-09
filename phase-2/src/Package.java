public class Package {
    private Integer quantity;
    private Integer unitPrice;

    public Package(Integer quantity, Integer unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }
}
