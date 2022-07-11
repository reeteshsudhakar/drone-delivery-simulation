import java.util.TreeMap;

/**
 * Ingredient class to create ingredients for restaurants to purchase from services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Ingredient implements Comparable<Ingredient> {
    // collection of ingredients
    static TreeMap<String, Ingredient> ingredients = new TreeMap<>();

    // Object attributes
    private final String barcode;
    private final String name;
    private final Integer weight;

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
     * Method to make an ingredient
     * @param barcode barcode of the ingredient
     * @param name name of the ingredient
     * @param weight unit weight of the ingredient
     */
    public static void makeIngredient(String barcode, String name, Integer weight) {
        // checking if the ingredient already exists
        if (ingredients.containsKey(barcode)) {
            Display.displayMessage("ERROR","ingredient_already_exists");
            return;
        }
        // checking if the weight is valid (positive) and whether the passed in arguments are valid
        if (weight <= 0) {
            Display.displayMessage("ERROR","ingredient_weight_must_be_greater_than_zero");
            return;
        } else if (barcode == null || name == null || barcode.equals("") || name.equals("")) {
            Display.displayMessage("ERROR","ingredient_barcode_and_name_must_not_be_empty");
            return;
        }

        // creating the ingredient and adding it to the collection
        ingredients.put(barcode, new Ingredient(barcode, name, weight));
        Display.displayMessage("OK","ingredient_created");
    }

    /**
     * Override of the compareTo method to compare ingredients by barcode to sort them.
     * @param other Ingredient to compare to
     * @return Integer representing the comparison
     */
    @Override
    public int compareTo(Ingredient other) {
        return this.barcode.compareTo(other.barcode);
    }

    /**
     * Override of the toString method to display the ingredient's information.
     * @return String representation of ingredient
     */
    @Override
    public String toString() {
        return "Barcode: " + this.barcode + "\nName: " + this.name + "\nUnit Weight: " + this.weight;
    }

    /**
     * Getter for barcode.
     * @return Barcode of ingredient
     */
    public String getBarcode() { return this.barcode; }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public Integer getWeight() { return this.weight; }

    /**
     * Getter for name.
     * @return Name of ingredient
     */
    public String getName() { return this.name; }
}
