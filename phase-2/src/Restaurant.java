/**
 * Restaurant class to represent a restaurant purchasing ingredients in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Restaurant implements Comparable<Restaurant> {
    // Object attributes
    private final String name;
    private final Location locatedAt;
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
        return name;
    }

    /**
     * Getter for location.
     * @return location of restaurant
     */
    public Location getLocation() {
        return locatedAt;
    }

    /**
     * Setter for spending.
     * @param amount amount to increment spending by based on purchases made by restaurant
     */
    public void addSpending(Integer amount) {
        this.spending += amount;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Money Spent: $%d, Location: %s", name, spending, locatedAt.getName());
    }

    @Override
    public int compareTo(Restaurant other) {
        return this.name.compareTo(other.getName());
    }

    public void makePurchase(Drone drone, Ingredient ingredient, Integer quantity) {
        this.addSpending(drone.getPayload().get(ingredient).getUnitPrice() * quantity);
    }
}
