import java.util.TreeMap;

/**
 * Drone class used to represent a drone that delivers ingredients in the interface.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Drone {
    // Object attributes
    private final Integer tag;
    private final Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private final Location homeBase;
    private Location currentLocation;
    private Integer sales;
    private final TreeMap<Ingredient, Package> payload;
    private Pilot pilot;
    private Drone leader;
    private final TreeMap<Integer, Drone> followers;

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
        this.pilot = null;
        this.leader = null;
        this.followers = new TreeMap<>();
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
            Drone newDrone = new Drone(tag, capacity, fuel, serviceLocation, serviceLocation);
            newService.getDrones().put(tag, newDrone);
            serviceLocation.decrementSpacesLeft();
            Display.displayMessage("OK","drone_created");
        }
    }

    /**
     * Method to add a drone to a swarm
     * @param leadDrone the drone that is to be followed (the leader of the swarm)
     */
    public void joinSwarm(Drone leadDrone) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone == null) {
            Display.displayMessage("ERROR","lead_drone_does_not_exist");
            return;
        } else if (leadDrone.getCurrentLocation() != this.currentLocation) {
            Display.displayMessage("ERROR", "lead_and_swarm_drone_must_be_at_same_location");
            return;
        }

        if (leadDrone.equals(this)) {
            Display.displayMessage("ERROR", "drone_cannot_join_its_own_swarm");
            return;
        }

        // If swarmDrone is a LeaderDrone, cast it as a FollowerDrone iff leadDrone is a LeaderDrone and has a valid pilot
        // IF swarmDrone is a FollowerDrone, add it to leadDrone's swarm iff it isn't already in the swarm, and leadDrone is a valid LeaderDrone
        if (this.hasPilot()) {
            if (!this.followers.isEmpty()) {
                Display.displayMessage("ERROR", "swarm_drone_is_leading_a_swarm");
            } else {
                if (leadDrone.hasPilot()) {
                    this.pilot.getPilotedDrones().remove(this.tag);
                    this.assignLeader(leadDrone);
                    Display.displayMessage("OK", "change_completed");
                } else if (leadDrone.hasLeader()) {
                    Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
                } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                    Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
                }
            }
        } else if (this.hasLeader()) {
            if (leadDrone.hasPilot()) {
                if (this.leader.getTag().equals(leadDrone.getTag())) {
                    Display.displayMessage("ERROR","swarm_drone_already_following_lead_drone");
                    return;
                }
                // remove the drone from the old lead drone's swarm and add it to the new one
                this.leader.getFollowers().remove(this.tag);
                this.assignLeader(leadDrone);
                Display.displayMessage("OK", "change_completed");
            } else if (leadDrone.hasLeader()) {
                Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
            } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
            }
        } else { // the passed in swarm drone tag is just a normal drone, so it doesn't have a pilot or a lead drone
            if (leadDrone.hasPilot()) {
                this.assignLeader(leadDrone);
                Display.displayMessage("OK","change_completed");
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
            }
        }
    }

    /**
     * Method to remove a drone from a swarm
     */
    public void leaveSwarm() {
        // Remove swarmDrone from swarm iff it is in a swarm (ensuring it is not a leader)
        if (this.hasPilot()) {
            Display.displayMessage("ERROR", "drone_not_in_a_swarm");
        } else if (this.hasLeader()) {
            Pilot pilot = this.leader.pilot;
            this.removeFromSwarm();
            this.assignPilot(pilot);
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_not_in_a_swarm");
        }
    }

    /**
     * Method to fly a drone to the destination specified
     * @param destination the destination to fly to
     */
    public void flyDrone(String destination) {
        Location destinationLocation;
        if (Location.locations.containsKey(destination)) {
            destinationLocation = Location.locations.get(destination);
        } else {
            Display.displayMessage("ERROR","flight_destination_does_not_exist");
            return;
        }

        // Drone can be made a LeaderDrone iff it is already a LeaderDrone & pilot is valid
        if (this.hasLeader()) {
            Display.displayMessage("ERROR", "drone_is_not_a_leader");
            return;
        } else if (!this.hasPilot()) {
            Display.displayMessage("ERROR", "the_drone_does_not_have_a_pilot");
            return;
        }

        if (this.getPilotLicense() == null) {
            Display.displayMessage("ERROR", "pilot_has_no_license");
            return;
        }

        if (destinationLocation.notEnoughSpace(this)) {
            Display.displayMessage("ERROR", "not_enough_space_to_maneuver_the_" +
                    "swarm_to_that_location");
            return;
        }
        // Fly iff there is enough fuel to drop ingredients off and return back to home base
        Integer distance = Location.calculateDistance(this.currentLocation, destinationLocation);
        Integer returnDistance = Location.calculateDistance(destinationLocation, this.homeBase);

        if (distance > this.fuel) {
            Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
            return;
        } else if (distance + returnDistance > this.fuel) {
            Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
            return;
        }

        for (Drone drone : this.followers.values()) {
            if (distance > drone.fuel) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
                return;
            } else if (distance + returnDistance > drone.fuel) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
                return;
            }
        }

        this.flyToDestination(destinationLocation);
        for (Drone drone : this.followers.values()) {
            drone.flyToDestination(destinationLocation);
        }

        this.pilot.addSuccessfulTrip();
        Display.displayMessage("OK", "change_completed");
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

    /**
     * Method to switch the pilots of a drone
     * @param pilot the new pilot of the drone
     */
    public void switchPilot(Pilot pilot) {
        this.pilot.getPilotedDrones().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
    }

    /**
     * Method to switch the leader of a drone to a pilot from a drone
     * @param pilot the new pilot of the drone
     */
    public void becomeLeader(Pilot pilot) {
        this.leader.getFollowers().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
    }

    /**
     * Checks if a pilot has already been appointed to a drone
     * @param pilot pilot to check if has been appointed to a drone
     * @return true if the pilot has already been appointed to a given drone, false otherwise
     */
    public boolean pilotAlreadyAppointed(Pilot pilot) {
        return this.pilot.getUsername().equals(pilot.getUsername());
    }

    /**
     * Method to remove a drone from its leader drone's list of followers
     */
    public void removeFromSwarm() {
        this.leader.getFollowers().remove(this.tag);
    }

    /**
     * Method to display a drone's details
     * @return String representation of the drone's information
     */
    @Override
    public String toString() {
        if (this.hasLeader() || (!this.hasPilot())) {
            return this.getDroneInfo() + this.getPayloadInfo();
        }

        StringBuilder swarmString = new StringBuilder();
        swarmString.append(String.format("&> pilot:%s%n", this.pilot.getUsername()));

        if (this.followers.size() > 0) {
            swarmString.append("drone is directing this swarm: [ drone tags ");
            for (Drone drone : this.followers.values()) {
                if (!drone.tag.equals(this.tag)){
                    swarmString.append(String.format("| %d ", drone.tag));
                }
            }
            swarmString.append("]\n");
        }
        return this.getDroneInfo() + swarmString + this.getPayloadInfo();
    }

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

    /**
     * Method to get a license of a drone's pilot
     * @return String representation of a drone's pilot's license
     */
    public String getPilotLicense() {
        return this.pilot.getLicense();
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
     * Method to check whether a drone has a pilot
     * @return true if the drone has a pilot, false otherwise
     */
    public boolean hasPilot() {
        return pilot != null;
    }

    /**
     * Method to check whether a drone has a leader
     * @return true if the drone has a leader, false otherwise
     */
    public boolean hasLeader() {
        return leader != null;
    }

    /**
     * Getter for Drone pilot
     * @return the pilot of the drone
     */
    public Pilot getPilot() {
        return pilot;
    }

    /**
     * Method to assign a pilot to a drone
     * @param pilot the pilot to assign to the drone
     */
    public void assignPilot(Pilot pilot) {
        this.leader = null;
        this.pilot = pilot;
        pilot.getPilotedDrones().put(this.tag, this);
    }

    /**
     * Method to assign a leader to a drone
     * @param drone the drone to assign as the leader
     */
    public void assignLeader(Drone drone) {
        this.pilot = null;
        this.followers.clear();
        this.leader = drone;
        drone.followers.put(this.tag, this);
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
     * Getter for Drone followers.
     * @return the followers of the drone
     */
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
}
