import java.util.TreeMap;

/**
 * Drone class used to represent a drone that delivers ingredients in the interface.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Drone implements Comparable<Drone> {
    // Object attributes
    private Integer tag;
    private Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private Location homeBase;
    private Location currentLocation;
    private Integer sales;
    private TreeMap<Ingredient, Package> payload = new TreeMap<>();

    /**
     * Constructor for Drone class.
     *
     * @param init_tag drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel drone fuel
     * @param homeBase home base currentLocation
     */
    public Drone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase) {
        this.tag = init_tag;
        this.capacity = init_capacity;
        this.remainingCapacity = init_capacity;
        this.fuel = init_fuel;
        this.currentLocation = homeBase;
        this.homeBase = homeBase;
        this.sales = 0;
    }

    /**
     * Getter for Drone tag.
     * @return the tag of the drone
     */
    public Integer getTag() {
        return this.tag;
    }

    /**
     * Getter for Drone capacity.
     * @return the capacity of the drone
     */
    public Integer getCapacity() {
        return this.capacity;
    }

    /**
     * Getter for Drone remaining capacity.
     * @return the remaining capacity of the drone
     */
    public Integer getRemainingCapacity() {
        return this.remainingCapacity;
    }

    /**
     * Getter for Drone fuel.
     * @return the fuel of the drone
     */
    public Integer getFuel() {
        return this.fuel;
    }

    /**
     * Getter for Drone home base.
     * @return the home base of the drone
     */
    public Location getHomeBase() {
        return this.homeBase;
    }

    /**
     * Getter for Drone currentLocation.
     * @return the current currentLocation of the drone
     */
    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    /**
     * Getter for Drone sales.
     * @return the sales of the drone
     */
    public Integer getSales() {
        return this.sales;
    }

    /**
     * Getter for Drone payload, with information about
     * ingredients and their quantity and price
     * @return the payload of the drone
     */
    public TreeMap<Ingredient, Package> getPayload() {
        return this.payload;
    }

    /**
     * Method to decrease the remaining capacity of a drone.
     * @param quantity the quantity to be decremented from the drone's remaining capacity
     *                 based on ingredients loaded to the drone
     */
    public void decrementRemainingCapacity(Integer quantity) {
        this.remainingCapacity -= quantity;
    }

    /**
     * Method to increase the remaining capacity of a drone
     * @param quantity the quantity to be incremented to the drone's remaining capacity
     *                 based on ingredients purchased from the drone
     */
    public void incrementRemainingCapacity(Integer quantity) {
        this.remainingCapacity += quantity;
    }

    /**
     * Method to update the currentLocation of a drone.
     * @param currentLocation the new currentLocation of the drone
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Method to load the fuel of a drone
     * @param fuel the fuel to be loaded to the drone
     */
    public void loadDroneFuel(Integer fuel) {
        this.fuel += fuel;
    }

    /**
     * Method to use the fuel of a drone as it flies to a location
     * @param fuel the fuel to be used
     */
    public void useDroneFuel(Integer fuel) {
        this.fuel -= fuel;
    }

    /**
     * Method to increment the sales of a drone.
     * @param amountSold the amount of sales to be incremented to
     *                   the drone's sales based on purchases
     */
    public void addSales(Integer amountSold) {
        this.sales += amountSold;
    }


    /**
     * Method to display information about a drone based on its attributes
     */
    public void displayDroneInfo() {
        System.out.printf("Tag: %d, Capacity: %d, Remaining Capacity: %d, Fuel: %d, Sales: $%d, " +
                        "Location: %s%n",
                this.getTag(), this.getCapacity(), this.getRemainingCapacity(), this.getFuel(),
                this.getSales(), this.getCurrentLocation().getName());

        getPayload().forEach((key,value) ->
                System.out.printf("&> Barcode: %s, Item Name: %s, Total Quantity: %d, Unit Cost: %d, " +
                                "Total Weight: %d%n", key.getBarcode(), key.getName(), value.getQuantity(),
                        value.getUnitPrice(), key.getWeight() * value.getQuantity()));
    }

    /**
     * Method to load a package to a drone.
     * @param loadIngredient the ingredient to be loaded to the drone
     * @param barcode the barcode of the ingredient to be loaded to the drone
     * @param quantity the quantity of the ingredient to be loaded to the drone
     * @param unitPrice the unit price of the ingredient to be loaded to the drone
     */
    public void addToPayload(Ingredient loadIngredient, String barcode, Integer quantity, Integer unitPrice) {
        boolean ingredientInPayload = false;
        for (Ingredient ingredient : this.getPayload().keySet()) {
            if (ingredient.getBarcode().equals(barcode)) {
                this.getPayload().get(ingredient).incrementQuantity(quantity);
                ingredientInPayload = true;
                break;
            }
        }

        if (!ingredientInPayload) {
            this.getPayload().put(loadIngredient, new Package(quantity, unitPrice));
            decrementRemainingCapacity(quantity);
        }
    }

    /**
     * Method to fly the drone to a location.
     * @param destination the location to which the drone is to be flown to
     */
    public void flyToDestination(Location destination) {
        this.getCurrentLocation().incrementSpacesLeft();
        destination.decrementSpacesLeft();
        this.useDroneFuel(getCurrentLocation().calculateDistance(destination));
        this.setCurrentLocation(destination);
    }

    /**
     * Method to complete a transaction with a drone.
     * @param ingredient the ingredient to be purchased from the drone
     * @param quantity the quantity of the ingredient to be purchased from the drone
     */
    public void completePurchase(Ingredient ingredient, Integer quantity) {
        this.addSales(quantity * this.getPayload().get(ingredient).getUnitPrice());

        if (this.getPayload().get(ingredient).getQuantity().equals(quantity)) {
            this.getPayload().remove(ingredient);
        } else {
            this.getPayload().get(ingredient).decrementQuantity(quantity);
        }

        this.incrementRemainingCapacity(quantity);
    }

    /**
     * Override of the compareTo method to compare two drones based on their tag to sort them.
     * @param drone the drone to be compared to the current drone
     * @return an integer representing the comparison of the two drones
     */
    @Override
    public int compareTo(Drone drone) {
        return this.getTag().compareTo(drone.getTag());
    }
}
