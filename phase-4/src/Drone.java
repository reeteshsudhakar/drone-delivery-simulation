import java.util.TreeMap;

/**
 * Drone class used to represent a drone that delivers ingredients in the interface.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Drone {
    // Object attributes
    protected final Integer tag;
    protected final Integer capacity;
    protected Integer remainingCapacity;
    protected Integer fuel;
    protected final Location homeBase;
    protected Location currentLocation;
    protected Integer sales;
    protected TreeMap<Ingredient, Package> payload;
//    protected Pilot pilot;

    /**
     * Constructor for Drone class.
     *
     * @param tag drone tag (unique)
     * @param capacity drone capacity for ingredients
     * @param fuel drone fuel
     * @param homeBase home base of the drone
     * @param currentLocation current location of drone
     */
    public Drone(Integer tag, Integer capacity, Integer fuel, Location homeBase, Location currentLocation) {
        this.tag = tag;
        this.capacity = capacity;
        this.remainingCapacity = capacity;
        this.fuel = fuel;
        this.currentLocation = currentLocation;
        this.homeBase = homeBase;
        this.sales = 0;
        this.payload = new TreeMap<>();
    }

    /**
     * Method to load a package to a drone
     * @param barcode the barcode of the ingredient to be loaded to the drone
     * @param quantity the quantity of the ingredient to be loaded to the drone
     * @param unitPrice the unit price of the ingredient to be loaded to the drone
     */
    public void addToPayload(String barcode, Integer quantity, Integer unitPrice) {

        Ingredient loadIngredient;
        // if the drone exists in the system, check if the ingredient exists in the system
        if (Ingredient.ingredients.containsKey(barcode)) {
            loadIngredient = Ingredient.ingredients.get(barcode);
        } else {
            Display.displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // checking if the drone is at the service's home base
        if (this.notAtHomeBase()) {
            Display.displayMessage("ERROR","drone_not_located_at_home_base");
            return;
        }

        // checking if the quantity and price of the ingredient are valid
        if (quantity <= 0) {
            Display.displayMessage("ERROR","quantity_must_be_greater_than_zero");
            return;
        } else if (unitPrice <= 0) {
            Display.displayMessage("ERROR","unit_price_must_be_greater_than_zero");
            return;
        }

        //If ingredient already exists in the payload, quantity is added, otherwise, ingredient is added to payload
        boolean ingredientInPayload = false;

        for (Ingredient ingredient : this.payload.keySet()) {
            if (ingredient.getBarcode().equals(barcode)) {
                this.payload.get(ingredient).incrementQuantity(quantity);
                ingredientInPayload = true;
                break;
            }
        }

        if (!ingredientInPayload) {
            this.payload.put(loadIngredient, new Package(quantity, unitPrice));
        }

        decrementRemainingCapacity(quantity);
        Display.displayMessage("OK","change_completed");
    }

    /**
     * Method to complete a transaction with a drone.
     * @param ingredient the ingredient to be purchased from the drone
     * @param quantity the quantity of the ingredient to be purchased from the drone
     */
    public void completePurchase(Ingredient ingredient, Integer quantity) {
        //If the ingredient is completely sold, it is removed from the payload, else the quantity is decremented
        this.addSales(quantity * this.payload.get(ingredient).getUnitPrice());

        if (this.payload.get(ingredient).getQuantity().equals(quantity)) {
            this.payload.remove(ingredient);
        } else {
            this.payload.get(ingredient).decrementQuantity(quantity);
        }

        this.incrementRemainingCapacity(quantity);
    }

    /**
     * Method to create a Drone
     * @param serviceName service of the drone
     * @param tag tag of the drone
     * @param capacity initial capacity of the drone
     * @param fuel fuel of the drone
     */
    public static void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel) {
        // checking if the service for the drone exists
        DeliveryService newService = null;
        Location serviceLocation = null;

        if (DeliveryService.services.containsKey(serviceName)) {
            newService = DeliveryService.services.get(serviceName);
            serviceLocation = newService.getLocation();
        }

        // if the service does not exist in the system, display an error message
        if (newService == null) {
            Display.displayMessage("ERROR", "service_identifier_does_not_exist");
            return;
        }

        // checking if the drone already exists in the system
        if (newService.getDrones().containsKey(tag)) {
            Display.displayMessage("ERROR", "drone_already_exists");
            return;
        }

        // checking if the capacity is valid (positive) and whether the fuel is valid (non-negative)
        if (capacity == null || capacity <= 0) {
            Display.displayMessage("ERROR", "drone_capacity_must_be_greater_than_zero");
            return;
        } else if (fuel == null || fuel < 0) {
            Display.displayMessage("ERROR", "drone_fuel_must_be_greater_than_or_equal_to_zero");
            return;
        }

        // creating the drone IFF there is space in the service's home base for it
        if (serviceLocation.getSpacesLeft() == 0) {
            Display.displayMessage("ERROR", "not_enough_space_to_create_new_drone");
        } else {
            Drone newDrone = new Drone(tag, capacity, fuel, serviceLocation, serviceLocation);
            newService.getDrones().put(tag, newDrone);
            serviceLocation.decrementSpacesLeft();
            Display.displayMessage("OK", "drone_created");
        }
    }

    /**
     * Method to load a drone with fuel
     * @param petrol the amount of fuel to load
     * @param service the service of the drone that is loading the fuel
     */
    public void loadFuel(Integer petrol, DeliveryService service) {
        // if the petrol to fill the drone is not valid, display an error message
        if (petrol <= 0) {
            Display.displayMessage("ERROR", "petrol_must_be_greater_than_zero");
            return;
        }

        // if the drone is at the service's home base, fill the drone with fuel
        if (this.notAtHomeBase()) {
            Display.displayMessage("ERROR", "drone_not_located_at_home_base");
            return;
        }

        if (service.noWorkersExist()) {
            Display.displayMessage("ERROR", "delivery_service_does_not_have_regular_workers");
            return;
        }

        this.loadDroneFuel(petrol);
        Display.displayMessage("OK", "change_completed");
    }

    /**
     * Method to check if drone is at home base
     * @return true if not at home base, false if at home base
     */
    public boolean notAtHomeBase() {
        return !(this.currentLocation.equals(this.homeBase));
    }


//    /**
//     * Method to display a drone's details
//     * @return String representation of the drone's information
//     */
//    @Override
//    public String toString() {
//        if (this.hasLeader() || (!this.hasPilot())) {
//            return this.getDroneInfo() + this.getPayloadInfo();
//        }
//
////        StringBuilder swarmString = new StringBuilder();
////        swarmString.append(String.format("&> pilot:%s%n", this.pilot.getUsername()));
//
//        if (this.followers.size() > 0) {
//            swarmString.append("drone is directing this swarm: [ drone tags ");
//            for (Drone drone : this.followers.values()) {
//                if (!drone.tag.equals(this.tag)){
//                    swarmString.append(String.format("| %d ", drone.tag));
//                }
//            }
//            swarmString.append("]\n");
//        }
//        return this.getDroneInfo() + swarmString + this.getPayloadInfo();
//    }

    /**
     * Method to check if two drones are equal
     * @param obj the object to compare to
     * @return true if the two drones are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Drone)) {
            return false;
        } else {
            Drone drone = (Drone) obj;
            return this.tag.equals(drone.tag);
        }
    }

    /**
     * Method to get the payload information of a drone
     * @return String representation of a drone's payload
     */
    public String getPayloadInfo() {
        StringBuilder payloadInfo = new StringBuilder();
        this.payload.forEach((key,value) ->
                payloadInfo.append(String.format("&> barcode: %s, item_name: %s, total_quantity: %d, unit_cost: %d, " +
                                "total_weight: %d%n", key.getBarcode(), key.getName(), value.getQuantity(),
                        value.getUnitPrice(), key.getWeight() * value.getQuantity())));
        return payloadInfo.toString();
    }

    /**
     * Method to get the drone information of a drone
     * @return String representation of a drone's information
     */
    public String getDroneInfo() {
        return String.format("tag: %d, capacity: %d, remaining_cap: %d, fuel: %d, sales: $%d, " +
                        "location: %s%n",
                this.tag, this.capacity, this.remainingCapacity, this.fuel,
                this.sales, this.currentLocation.getName());
    }

    public Integer getIngredientPayload(Ingredient buyerIngredient, Integer quantity) {
        return this.getPayload().get(buyerIngredient).getQuantity().compareTo(quantity);
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
     * Getter for Drone currentLocation.
     * @return the current currentLocation of the drone
     */
    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public Location getHomeBase() {
        return homeBase;
    }

    /**
     * Getter for Drone sales.
     * @return the sales of the drone
     */
    public Integer getSales() {
        return this.sales;
    }

//    public abstract void joinSwarm()

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
     * Method to fly the drone to a location.
     * @param destination the location to which the drone is to be flown to
     */
    public void flyToDestination(Location destination) {
        this.currentLocation.incrementSpacesLeft();
        destination.decrementSpacesLeft();
        this.useDroneFuel(Location.calculateDistance(this.currentLocation, destination));
        this.setCurrentLocation(destination);
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
     * Method to clear the sales of a drone to 0.
     */
    public void clearSales() {
        this.sales = 0;
    }
}
