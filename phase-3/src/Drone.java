import com.sun.source.tree.Tree;

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
     * @param homeBase home base currentLocation
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
        this.useDroneFuel(Location.calculateDistance(this.getCurrentLocation(), destination));
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

    public void joinSwarm(Drone leadDrone) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone == null) {
            Display.displayMessage("ERROR","lead_drone_does_not_exist");
            return;
        } else if (leadDrone.getCurrentLocation() != this.getCurrentLocation()) {
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
            if (!this.getFollowers().isEmpty()) {
                Display.displayMessage("ERROR", "swarm_drone_is_leading_a_swarm");
            } else {
                if (leadDrone.hasPilot()) {
                    this.getPilot().getPilotedDrones().remove(this.tag);
                    this.assignLeader(leadDrone);
                    leadDrone.getFollowers().put(this.tag, this);
                    Display.displayMessage("OK", "change_completed");
                } else if (leadDrone.hasLeader()) {
                    Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
                } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                    Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
                }
            }
        } else if (this.hasLeader()) {
            if (leadDrone.hasPilot()) {
                if (this.getLeader().getTag().equals(leadDrone.getTag())) {
                    Display.displayMessage("ERROR","swarm_drone_already_following_lead_drone");
                    return;
                }
                // remove the drone from the old lead drone's swarm and add it to the new one
                this.getLeader().getFollowers().remove(this.tag);
                this.assignLeader(leadDrone);
                leadDrone.getFollowers().put(this.tag, this);
                Display.displayMessage("OK", "change_completed");
            } else if (leadDrone.hasLeader()) {
                Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
            } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        } else { // the passed in swarm drone tag is just a normal drone, so it doesn't have a pilot or a lead drone
            if (leadDrone.hasPilot()) {
                this.assignLeader(leadDrone);
                leadDrone.getFollowers().put(this.tag, this);
                Display.displayMessage("OK","change_completed");
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        }
    }

    public void leaveSwarm() {
        // Remove swarmDrone from swarm iff it is in a swarm (ensuring it is not a leader)
        if (this.hasPilot()) {
            Display.displayMessage("ERROR", "drone_is_not_following_in_a_swarm");
        } else if (this.hasLeader()) {
            Pilot pilot = this.getLeader().getPilot();
            this.getLeader().getFollowers().remove(this.tag);
            this.assignPilot(pilot);
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_is_not_following_in_a_swarm");
        }
    }

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
        } else if (this.hasPilot()) {
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
            int distance = Location.calculateDistance(this.getCurrentLocation(), destinationLocation);
            int returnDistance = Location.calculateDistance(destinationLocation, this.getHomeBase());
            if (distance > this.getFuel()) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
            } else if (distance + returnDistance > this.getFuel()) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
            } else {
                for (Drone drone : this.getFollowers().values()) {
                    if (distance > drone.getFuel()) {
                        Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
                        return;
                    } else if (distance + returnDistance > drone.getFuel()) {
                        Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
                        return;
                    }
                }

                this.flyToDestination(destinationLocation);
                for (Drone drone : this.getFollowers().values()) {
                    drone.flyToDestination(destinationLocation);
                }

                this.getPilot().addSuccessfulTrip();
                Display.displayMessage("OK", "change_completed");
            }
        } else {
            Display.displayMessage("ERROR", "drone_does_not_have_a_pilot");
        }
    }

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

    public boolean notAtHomeBase() {
        return !(this.currentLocation.equals(this.homeBase));
    }

    public void switchPilot(Pilot pilot) {
        this.getPilot().getPilotedDrones().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
    }

    public void becomeLeader(Pilot pilot) {
        this.getLeader().getFollowers().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
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

    private String getPayloadInfo() {
        StringBuilder payloadInfo = new StringBuilder();
        this.getPayload().forEach((key,value) ->
                payloadInfo.append(String.format("&> barcode: %s, item_name: %s, total_quantity: %d, unit_cost: %d, " +
                                "total_weight: %d%n", key.getBarcode(), key.getName(), value.getQuantity(),
                        value.getUnitPrice(), key.getWeight() * value.getQuantity())));
        return payloadInfo.toString();
    }

    private String getDroneInfo() {
        return String.format("tag: %d, capacity: %d, remaining_cap: %d, fuel: %d, sales: $%d, " +
                        "location: %s%n",
                this.getTag(), this.getCapacity(), this.getRemainingCapacity(), this.getFuel(),
                this.getSales(), this.getCurrentLocation().getName());
    }

    public String getPilotLicense() {
        return this.getPilot().getLicense();
    }

    public int getIngredientPayload(Ingredient buyerIngredient, int quantity) {
        return this.getPayload().get(buyerIngredient).getQuantity().compareTo(quantity);
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
}
