import java.util.TreeMap;

/**
 * Delivery Service class to handle the delivery of ingredients to restaurants.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class DeliveryService implements Comparable <DeliveryService> {
    // Object attributes
    private final String name;
    private Integer revenue;
    private final Location locatedAt;
    private TreeMap<Integer, Drone> drones;

    /**
     * Constructor for DeliveryService class.
     * @param init_name name of the Delivery Service.
     * @param init_revenue initial revenue of the Delivery Service.
     * @param location location of the Delivery Service.
     */
    public DeliveryService(String init_name, Integer init_revenue, Location location) {
        this.name = init_name;
        this.revenue = init_revenue;
        this.locatedAt = location;
        this.drones = new TreeMap<>();
    }

    /**
     * Getter for the name of the Delivery Service.
     * @return name of the Delivery Service.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the location of the Delivery Service.
     * @return location of the Delivery Service.
     */
    public Location getLocation() {
        return locatedAt;
    }

    public TreeMap<Integer, Drone> getDrones() {
        return drones;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Revenue: $%d, Location: %s", name, revenue, locatedAt.getName());
    }

    @Override
    public int compareTo(DeliveryService o) {
        return this.name.compareTo(o.getName());
    }
}