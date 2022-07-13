import java.util.TreeMap;

public class LeaderDrone extends Drone {

    private Pilot pilot;
    private TreeMap<Integer, Drone> swarm;

    /**
     * Constructor for Drone class.
     *
     * @param init_tag      drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel     drone fuel
     * @param homeBase      home base currentLocation
     */
    public LeaderDrone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload) {
        super(init_tag, init_capacity, init_fuel, homeBase, currentLocation);
        this.addSales(sales);
        this.payload = payload;
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

    public LeaderDrone(FollowerDrone followerDrone, Pilot pilot) {
        this(followerDrone.getTag(), followerDrone.getCapacity(), followerDrone.getFuel(), followerDrone.getHomeBase(), followerDrone.getCurrentLocation(), followerDrone.getSales(), followerDrone.getPayload());
        this.pilot = pilot;
        this.swarm = new TreeMap<>();
    }
//
//    public LeaderDrone(Drone drone, Pilot pilot) {
//        this(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
//        this.pilot = pilot;
//        this.swarm = new TreeMap<>();
//    }

    public TreeMap<Integer, Drone> getSwarm() {
        return this.swarm;
    }

    public Pilot getPilot() {
        return this.pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public TreeMap<Integer, Drone> getFollowers() {
        return this.swarm;
    }

    /**
     * Method to add a drone to a swarm
     *
     * @param leadDrone the drone that is to be followed (the leader of the swarm)
     */
    public void joinSwarm(Drone leadDrone) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone == null) {
            Display.displayMessage("ERROR", "lead_drone_does_not_exist");
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
                if (this.leaderDrone.getTag().equals(leadDrone.getTag())) {
                    Display.displayMessage("ERROR", "swarm_drone_already_following_lead_drone");
                    return;
                }
                // remove the drone from the old lead drone's swarm and add it to the new one
                this.leaderDrone.getFollowers().remove(this.tag);
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
                Display.displayMessage("OK", "change_completed");
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder swarmString = new StringBuilder();
        swarmString.append(String.format("&> pilot:%s%n", this.getPilot().getUsername()));
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
         * Method to switch the pilots of a drone
         * @param pilot the new pilot of the drone
         */
    public void switchPilot(Pilot pilot) {
        this.pilot.getPilotedDrones().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
    }

    /**
     * Method to assign a pilot to a drone
     * @param pilot the pilot to assign to the drone
     */
    public void assignPilot(Pilot pilot) {
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
        return this.pilot.getUsername().equals(pilot.getUsername());
    }

    /**
     * Method to get a license of a drone's pilot
     * @return String representation of a drone's pilot's license
     */
    public String getPilotLicense() {
        return this.pilot.getLicense();
    }
}
