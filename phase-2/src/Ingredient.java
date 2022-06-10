/**
 * Ingredient class to create ingredients for restaurants to purchase from services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Ingredient {
    // Object attributes
    private String barcode;
    private String name;
    private Integer weight;

    /**
     * Constructor for Ingredient class.
     * @param barcode Barcode of ingredient
     * @param name Name of ingredient
     * @param weight Weight of ingredient
     */
    public Ingredient(String barcode, String name, Integer weight) {
        this.barcode = barcode;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Getter for barcode.
     * @return Barcode of ingredient
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Barcode: " + barcode + ", Name: " + name + ", Unit Weight: " + weight;
    }
}
