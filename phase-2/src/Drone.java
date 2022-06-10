import java.util.HashMap;
import java.util.Map;

/**
 * Drone class used to represent a drone that delivers ingredients in the interface.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Drone {
    // Object attributes
    private final DeliveryService service;
    private final Integer tag;
    private final Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private final Location homeBase;
    private Location location;
    private Integer sales;
    private HashMap<Ingredient, Package> payload;

    /**
     * Constructor for Drone class.
     *
     * @param service delivery service for the drone
     * @param init_tag drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel drone fuel
     * @param homeBase home base location
     */
    public Drone(DeliveryService service, Integer init_tag, Integer init_capacity,
                 Integer init_fuel, Location homeBase) {
        this.service = service;
        this.tag = init_tag;
        this.capacity = init_capacity;
        this.remainingCapacity = init_capacity;
        this.fuel = init_fuel;
        this.location = homeBase;
        this.homeBase = homeBase;
        this.sales = 0;
        this.payload = new HashMap<>();
    }

    /**
     * Getter for Delivery Service.
     * @return delivery service for the drone
     */
    public DeliveryService getService() {
        return service;
    }

    /**
     * Getter for Drone tag.
     * @return the tag of the drone
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * Getter for Drone capacity.
     * @return the capacity of the drone
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Getter for Drone remaining capacity.
     * @return the remaining capacity of the drone
     */
    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    /**
     * Getter for Drone fuel.
     * @return the fuel of the drone
     */
    public Integer getFuel() {
        return fuel;
    }

    /**
     * Getter for Drone home base.
     * @return the home base of the drone
     */
    public Location getHomeBase() {
        return homeBase;
    }

    /**
     * Getter for Drone location.
     * @return the current location of the drone
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Getter for Drone sales.
     * @return the sales of the drone
     */
    public Integer getSales() {
        return sales;
    }

    /**
     * Getter for Drone payload, with information about
     * ingredients and their quantity and price
     * @return the payload of the drone
     */
    public HashMap<Ingredient, Package> getPayload() {
        return payload;
    }

    /**
     * Method to decrease the remaining capacity of a drone.
     * @param quantity the quantity to be decremented from the drone's remaining capacity
     *                 based on ingredients laoded to the drone
     */
    public void decrementCapacity(Integer quantity) {
        this.remainingCapacity -= quantity;
    }

    /**
     * Method to increase the remaining capacity of a drone
     * @param quantity the quantity to be incremented to the drone's remaining capacity
     *                 based on ingredients purchased from the drone
     */
    public void incrementCapacity(Integer quantity) {
        this.remainingCapacity += quantity;
    }

    /**
     * Method to update the location of a drone.
     * @param location the new location of the drone
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Method to update the fuel of a drone
     * @param fuel the new fuel of the drone
     */
    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    /**
     * Method to increment the sales of a drone.
     * @param amountSold the amount of sales to be incremented to
     *                   the drone's sales based on purchases
     */
    public void addSales(Integer amountSold) {
        this.sales += amountSold;
    }
}
