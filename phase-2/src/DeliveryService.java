/**
 * Delivery Service class to handle the delivery of ingredients to restaurants.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class DeliveryService {
    // Object attributes
    private final String name;
    private Integer revenue;
    private final Location location;

    /**
     * Constructor for DeliveryService class.
     * @param init_name name of the Delivery Service.
     * @param init_revenue initial revenue of the Delivery Service.
     * @param location location of the Delivery Service.
     */
    public DeliveryService(String init_name, Integer init_revenue, Location location) {
        this.name = init_name;
        this.revenue = init_revenue;
        this.location = location;
    }

    /**
     * Getter for the name of the Delivery Service.
     * @return name of the Delivery Service.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the revenue of the Delivery Service.
     * @return revenue of the Delivery Service.
     */
    public Integer getRevenue() {
        return revenue;
    }

    /**
     * Getter for the location of the Delivery Service.
     * @return location of the Delivery Service.
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Revenue: $%d, Location: %s", name, revenue, location.getName());
    }
}
