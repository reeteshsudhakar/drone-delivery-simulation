import java.util.TreeMap;

/**
 * Drone class used to represent a drone that delivers ingredients in the interface.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Drone {
    // Object attributes
    private Integer tag;
    private Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private Location homeBase;
    private Location currentLocation;
    private Integer sales;
    private TreeMap<Ingredient, Package> payload;
    private Pilot pilot;
    private Drone leader;
    private TreeMap<Integer, Drone> followers;

    /**
     * Constructor for Drone class.
     *
     * @param init_tag drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel drone fuel
     * @param homeBase home base currentLocation
     */
    public Drone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload) {
        this.tag = init_tag;
        this.capacity = init_capacity;
        this.remainingCapacity = init_capacity;
        this.fuel = init_fuel;
        this.currentLocation = currentLocation;
        this.homeBase = homeBase;
        this.sales = sales;
        this.payload = payload;
        this.pilot = null;
        this.leader = null;
        this.followers = new TreeMap<>();
    }

    /**
     * Getter for Drone tag.
     * @return the tag of the drone
     */
    public Integer getTag() {
        return this.tag;
    }

    public boolean hasPilot() {
        return pilot != null;
    }

    public boolean hasLeader() {
        return leader != null;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public Drone getLeader() {
        return leader;
    }

    public void assignPilot(Pilot pilot) {
        this.leader = null;
        this.pilot = pilot;
    }

    public void assignLeader(Drone drone) {
        this.pilot = null;
        this.followers.clear();
        this.leader = drone;
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

    public TreeMap<Integer, Drone> getFollowers() {
        return this.followers;
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
     * Method to clear the sales of a drone to 0.
     */
    public void clearSales() {
        this.sales = 0;
    }

    /**
     * Method to load a package to a drone.
     * @param loadIngredient the ingredient to be loaded to the drone
     * @param barcode the barcode of the ingredient to be loaded to the drone
     * @param quantity the quantity of the ingredient to be loaded to the drone
     * @param unitPrice the unit price of the ingredient to be loaded to the drone
     */
    public void addToPayload(Ingredient loadIngredient, String barcode, Integer quantity, Integer unitPrice) {
        //If ingredient already exists in the payload, quantity is added, otherwise, ingredient is added to payload
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
        }

        decrementRemainingCapacity(quantity);
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
        //If the ingredient is completely sold, it is removed from the payload, else the quantity is decremented
        this.addSales(quantity * this.getPayload().get(ingredient).getUnitPrice());

        if (this.getPayload().get(ingredient).getQuantity().equals(quantity)) {
            this.getPayload().remove(ingredient);
        } else {
            this.getPayload().get(ingredient).decrementQuantity(quantity);
        }

        this.incrementRemainingCapacity(quantity);
    }

    public static void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel,
                                 TreeMap<String, DeliveryService> services) {
        // checking if the service for the drone exists
        DeliveryService newService = null;
        Location serviceLocation = null;

        if (services.containsKey(serviceName)) {
            newService = services.get(serviceName);
            serviceLocation = newService.getLocation();
        }

        // if the service does not exist in the system, display an error message
        if (newService == null) {
            Display.displayMessage("ERROR","service_identifier_does_not_exist");
            return;
        }

        // checking if the drone already exists in the system
        if (newService.getDrones().containsKey(tag)) {
            Display.displayMessage("ERROR","drone_already_exists");
            return;
        }

        // checking if the capacity is valid (positive) and whether the fuel is valid (non-negative)
        if (capacity == null || capacity <= 0) {
            Display.displayMessage("ERROR","drone_capacity_must_be_greater_than_zero");
            return;
        } else if (fuel == null || fuel < 0) {
            Display.displayMessage("ERROR","drone_fuel_must_be_greater_than_or_equal_to_zero");
            return;
        }

        // creating the drone IFF there is space in the service's home base for it
        if (serviceLocation.getSpacesLeft() == 0) {
            Display.displayMessage("ERROR","not_enough_space_to_create_new_drone");
        } else {
            Drone newDrone = new Drone(tag, capacity, fuel, serviceLocation, serviceLocation, 0, new TreeMap<>());
            newService.getDrones().put(tag, newDrone);
            serviceLocation.decrementSpacesLeft();
            Display.displayMessage("OK","drone_created");
        }
    }

    public static void joinSwarm(Drone leadDrone, Drone swarmDrone, int lead_drone_tag, int swarm_drone_tag) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone == null) {
            Display.displayMessage("ERROR","lead_drone_does_not_exist");
            return;
        } else if (swarmDrone == null) {
            Display.displayMessage("ERROR","swarm_drone_does_not_exist");
            return;
        } else if (leadDrone.getCurrentLocation() != swarmDrone.getCurrentLocation()) {
            Display.displayMessage("ERROR", "lead_and_swarm_drone_must_be_at_same_location");
            return;
        }

        if (leadDrone.equals(swarmDrone)) {
            Display.displayMessage("ERROR", "drone_cannot_join_its_own_swarm");
            return;
        }

        // If swarmDrone is a LeaderDrone, cast it as a FollowerDrone iff leadDrone is a LeaderDrone and has a valid pilot
        // IF swarmDrone is a FollowerDrone, add it to leadDrone's swarm iff it isn't already in the swarm, and leadDrone is a valid LeaderDrone
        if (swarmDrone.hasPilot()) {
            if (!swarmDrone.getFollowers().isEmpty()) {
                Display.displayMessage("ERROR", "swarm_drone_is_leading_a_swarm");
            } else {
                if (leadDrone.hasPilot()) {
                    swarmDrone.getPilot().getPilotedDrones().remove(swarm_drone_tag);
                    swarmDrone.assignLeader(leadDrone);
                    leadDrone.getFollowers().put(swarm_drone_tag, swarmDrone);
                    Display.displayMessage("OK", "change_completed");
                } else if (leadDrone.hasLeader()) {
                    Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
                } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                    Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
                }
            }
        } else if (swarmDrone.hasLeader()) {
            if (leadDrone.hasPilot()) {
                if (swarmDrone.getLeader().getTag().equals(lead_drone_tag)) {
                    Display.displayMessage("ERROR","swarm_drone_already_following_lead_drone");
                    return;
                }
                // remove the drone from the old lead drone's swarm and add it to the new one
                swarmDrone.getLeader().getFollowers().remove(swarm_drone_tag);
                swarmDrone.assignLeader(leadDrone);
                leadDrone.getFollowers().put(swarm_drone_tag, swarmDrone);
                Display.displayMessage("OK", "change_completed");
            } else if (leadDrone.hasLeader()) {
                Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
            } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        } else { // the passed in swarm drone tag is just a normal drone, so it doesn't have a pilot or a lead drone
            if (leadDrone.hasPilot()) {
                swarmDrone.assignLeader(leadDrone);
                leadDrone.getFollowers().put(swarm_drone_tag, swarmDrone);
                Display.displayMessage("OK","change_completed");
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        }
    }

    public static void leaveSwarm(Drone swarmDrone, int swarm_drone_tag) {
        // Remove swarmDrone from swarm iff it is in a swarm (ensuring it is not a leader)
        if (swarmDrone == null) {
            Display.displayMessage("ERROR", "swarm_drone_does_not_exist");
        } else if (swarmDrone.hasPilot()) {
            Display.displayMessage("ERROR", "drone_is_not_following_in_a_swarm");
        } else if (swarmDrone.hasLeader()) {
            Pilot pilot = swarmDrone.getLeader().getPilot();
            swarmDrone.getLeader().getFollowers().remove(swarm_drone_tag);
            swarmDrone.assignPilot(pilot);
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_is_not_following_in_a_swarm");
        }
    }

    protected String getPayloadInfo() {
        StringBuilder payloadInfo = new StringBuilder();
        this.getPayload().forEach((key,value) ->
                payloadInfo.append(String.format("&> Barcode: %s, Item Name: %s, Total Quantity: %d, Unit Cost: %d, " +
                                "Total Weight: %d%n", key.getBarcode(), key.getName(), value.getQuantity(),
                        value.getUnitPrice(), key.getWeight() * value.getQuantity())));
        return payloadInfo.toString();
    }

    protected String getDroneInfo() {
        return String.format("Tag: %d, Capacity: %d, Remaining Capacity: %d, Fuel: %d, Sales: $%d, " +
                        "Location: %s%n",
                this.getTag(), this.getCapacity(), this.getRemainingCapacity(), this.getFuel(),
                this.getSales(), this.getCurrentLocation().getName());
    }

    @Override
    public String toString() {
        if (this.hasLeader()) {
            return this.getDroneInfo() + this.getPayloadInfo();
        } else if (this.hasPilot()) {
            StringBuilder swarmString = new StringBuilder();
            swarmString.append(String.format("&> pilot:%s%n", this.getPilot().getUsername()));
            if (this.followers.size() > 0) {
                swarmString.append("drone is directing this swarm: [ drone tags ");
                for (Drone drone : this.followers.values()) {
                    if (!drone.getTag().equals(this.getTag())){
                        swarmString.append(String.format("| %d ", drone.getTag()));
                    }
                }
                swarmString.append("]\n");
            }
            return this.getDroneInfo() + swarmString + this.getPayloadInfo();
        }
        return this.getDroneInfo() + this.getPayloadInfo();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Drone)) {
            return false;
        } else {
            Drone drone = (Drone) obj;
            return this.getTag().equals(drone.getTag());
        }
    }
}
