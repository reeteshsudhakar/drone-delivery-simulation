/**
 * Ingredient class to create ingredients for restaurants to purchase from services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Ingredient implements Comparable<Ingredient> {
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
        return this.barcode;
    }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public Integer getWeight() {
        return this.weight;
    }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public String getName() {
        return this.name;
    }

    /**
     * Override of the toString method to display the ingredient's information.
     * @return String representation of ingredient
     */
    @Override
    public String toString() {
        return "Barcode: " + this.barcode + ", Name: " + this.name + ", Unit Weight: " + this.weight;
    }

    /**
     * Override of the compareTo method to compare ingredients by barcode to sort them.
     * @param other Ingredient to compare to
     * @return Integer representing the comparison
     */
    @Override
    public int compareTo(Ingredient other) {
        return this.getBarcode().compareTo(other.getBarcode());
    }
}
