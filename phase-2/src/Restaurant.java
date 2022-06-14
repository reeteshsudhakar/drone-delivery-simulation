/**
 * Restaurant class to represent a restaurant purchasing ingredients in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Restaurant implements Comparable<Restaurant> {
    // Object attributes
    private String name;
    private Location locatedAt;
    private Integer spending;

    /**
     * Constructor for Restaurant class.
     * @param init_name name of restaurant
     * @param location location of restaurant
     */
    public Restaurant(String init_name, Location location) {
        this.name = init_name;
        this.locatedAt = location;
        this.spending = 0;
    }

    /**
     * Getter for name.
     * @return name of restaurant
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for location.
     * @return location of restaurant
     */
    public Location getLocation() {
        return this.locatedAt;
    }

    /**
     * Setter for spending.
     * @param amount amount to increment spending by based on purchases made by restaurant
     */
    public void addSpending(Integer amount) {
        this.spending += amount;
    }

    /**
     * Override of the toString method to display the restaurant's information.
     * @return String representation of restaurant
     */
    @Override
    public String toString() {
        return String.format("Name: %s, Money Spent: $%d, Location: %s", this.name,
                this.spending, this.locatedAt.getName());
    }

    /**
     * Method to complete the purchase for a restaurant
     * @param drone drone to purchase ingredients from
     * @param ingredient ingredient to purchase
     * @param quantity quantity of ingredient to purchase
     */
    public void makePurchase(Drone drone, Ingredient ingredient, Integer quantity) {
        this.addSpending(drone.getPayload().get(ingredient).getUnitPrice() * quantity);
    }

    /**
     * Override of the compareTo method to compare two restaurants.
     * @param other restaurant to compare to
     * @return Integer comparison of the two restaurants
     */
    @Override
    public int compareTo(Restaurant other) {
        return this.getName().compareTo(other.getName());
    }
}
