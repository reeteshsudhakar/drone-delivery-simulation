/**
 * Package class to track quantity and price of ingredients in a drone's payload.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Package {
    // Object attributes
    private Integer quantity;
    private Integer unitPrice;

    /**
     * Constructor for Package class.
     * @param quantity Quantity of ingredient in package
     * @param unitPrice Unit price of ingredient
     */
    public Package(Integer quantity, Integer unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * Getter for quantity.
     * @return quantity of ingredient in package
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Getter for unitPrice.
     * @return unity price of ingredient
     */
    public Integer getUnitPrice() {
        return this.unitPrice;
    }

    /**
     * Method to increment the quantity of ingredients in a package
     * if the drone is loaded with more ingredients.
     * @param quantity quantity of ingredients to be added to drone's payload in package
     */
    public void incrementQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    /**
     * Method to decrement the quantity of ingredients in a package
     * if the drone's ingredients are unloaded as a result of a purchase.
     * @param quantity quantity of ingredients to be removed from drone's payload in package
     */
    public void decrementQuantity(Integer quantity) {
        this.quantity -= quantity;
    }
}
