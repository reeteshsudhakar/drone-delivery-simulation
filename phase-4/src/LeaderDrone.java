import java.util.TreeMap;

public class LeaderDrone extends Drone {

    private Pilot pilot;
    private final TreeMap<Integer, Drone> swarm;

    /**
     * Constructor for Drone class.
     *
     * @param init_tag      drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel     drone fuel
     * @param homeBase      home base currentLocation
     */
    public LeaderDrone(Integer init_tag, Integer init_capacity, Integer capacity_remaining, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload) {
        super(init_tag, init_capacity, init_fuel, homeBase, currentLocation);
        this.addSales(sales);
        this.payload = payload;
        this.remainingCapacity = capacity_remaining;
        this.swarm = new TreeMap<>();
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
            Display.displayMessage("ERROR", "not_enough_fuel_to_return_to_home_base_from_destination");
            return;
        }

        for (Drone drone : this.swarm.values()) {
            if (distance > drone.fuel) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_reach_the_destination");
                return;
            } else if (distance + returnDistance > drone.fuel) {
                Display.displayMessage("ERROR", "not_enough_fuel_to_return_to_home_base_from_destination");
                return;
            }
        }

        this.flyToDestination(destinationLocation);
        for (Drone drone : this.swarm.values()) {
            drone.flyToDestination(destinationLocation);
        }

        this.pilot.addSuccessfulTrip();
        Display.displayMessage("OK", "change_completed");
    }

    public TreeMap<Integer, Drone> getSwarm() {
        return this.swarm;
    }

    public Pilot getPilot() {
        return this.pilot;
    }

    public TreeMap<Integer, Drone> getFollowers() {
        return this.swarm;
    }

    @Override
    public String toString() {
        StringBuilder swarmString = new StringBuilder();
        if (this.hasPilot()) {
            swarmString.append(String.format("&> pilot:%s%n", this.getPilot().getUsername()));
        }
        if (this.getSwarm().size() > 0) {
            swarmString.append("drone is directing this swarm: [ drone tags ");
            for (Drone drone : this.getSwarm().values()) {
                swarmString.append(String.format("| %d ", drone.getTag()));
            }
            swarmString.append("]\n");
        }
        return this.getDroneInfo() + swarmString + this.getPayloadInfo();
    }

    /**
     * Method to assign a pilot to a drone
     * @param pilot the pilot to assign to the drone
     */
    public void assignPilot(Pilot pilot) {
        if (this.hasPilot()) {
            this.pilot.getPilotedDrones().remove(this.tag);
        }
        this.pilot = pilot;
        pilot.getPilotedDrones().put(this.tag, this);
    }

    public boolean hasPilot() {
        return this.pilot != null;
    }

    /**
     * Checks if a pilot has already been appointed to a drone
     * @param pilot pilot to check if has been appointed to a drone
     * @return true if the pilot has already been appointed to a given drone, false otherwise
     */
    public boolean pilotAlreadyAppointed(Pilot pilot) {
        if (this.hasPilot()) {
            return this.pilot.getUsername().equals(pilot.getUsername());
        } else {
            return false;
        }
    }

    /**
     * Method to get a license of a drone's pilot
     * @return String representation of a drone's pilot's license
     */
    public String getPilotLicense() {
        if (this.hasPilot()) {
            return this.pilot.getLicense();
        } else {
            return null;
        }
    }

    public void joinSwarm(LeaderDrone leadDrone, String service_name) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone.getCurrentLocation() != this.currentLocation) {
            Display.displayMessage("ERROR", "lead_and_swarm_drone_must_be_at_same_location");
            return;
        }

        if (leadDrone.equals(this)) {
            Display.displayMessage("ERROR", "drone_cannot_join_its_own_swarm");
            return;
        }

        if (!this.swarm.isEmpty()) {
            Display.displayMessage("ERROR", "swarm_drone_is_leading_a_swarm");
            return;
        }

        if (leadDrone.hasPilot()) {
            leadDrone.pilot.getPilotedDrones().remove(this.tag);
            FollowerDrone newFollower = DroneFactory.leaderToFollower(this, DeliveryService.services.get(service_name), leadDrone);
            leadDrone.getSwarm().put(this.tag, newFollower);
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
            return;
        }
    }
}