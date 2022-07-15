import java.util.TreeMap;

public class FollowerDrone extends Drone {

    private LeaderDrone leaderDrone;

    /**
     * Constructor for Drone class.
     *
     * @param init_tag      drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel     drone fuel
     * @param homeBase      home base currentLocation
     */
    public FollowerDrone(Integer init_tag, Integer init_capacity, Integer capacity_remaining, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload, LeaderDrone leaderDrone) {
        super(init_tag, init_capacity, init_fuel, homeBase, currentLocation);
        this.addSales(sales);
        this.payload = payload;
        this.remainingCapacity = capacity_remaining;
        this.leaderDrone = leaderDrone;
    }

    /**
     * Method to add a drone to a swarm
     *
     * @param leadDrone the drone that is to be followed (the leader of the swarm)
     */
    public void joinSwarm(LeaderDrone leadDrone, String service_name) {
        //Checks if lead drone and swarm drone are valid, and if they are in the same location
        if (leadDrone.getCurrentLocation() != this.currentLocation) {
            Display.displayMessage("ERROR", "lead_and_swarm_drone_must_be_at_same_location");
            return;
        }

        // If swarmDrone is a LeaderDrone, cast it as a FollowerDrone iff leadDrone is a LeaderDrone and has a valid pilot
        // IF swarmDrone is a FollowerDrone, add it to leadDrone's swarm iff it isn't already in the swarm, and leadDrone is a valid LeaderDrone
        // This has a leader drone already
        if (this.hasLeader()) {
            if (leadDrone.hasPilot()) {
                if (this.leaderDrone.getTag().equals(leadDrone.getTag())) {
                    Display.displayMessage("ERROR", "swarm_drone_already_following_lead_drone");
                    return;
                }
                // remove the drone from the old lead drone's swarm and add it to the new one
                this.leaderDrone.getFollowers().remove(this.tag);
                this.assignLeader(leadDrone);
                Display.displayMessage("OK", "change_completed");
            } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");
            }
        } else { // the passed in swarm drone tag is just a normal drone, so it doesn't have a pilot or a lead drone
            if (leadDrone.hasPilot()) {
                this.assignLeader(leadDrone);
                Display.displayMessage("OK", "change_completed");
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_a_pilot");            }
        }
    }

    public void assignLeader(LeaderDrone drone) {
        this.leaderDrone = drone;
        drone.getSwarm().put(this.tag, this);
    }

    /**
     * Method to remove a drone from a swarm
     */
    public void leaveSwarm() {
        // Remove swarmDrone from swarm iff it is in a swarm (ensuring it is not a leader)
        if (this.hasLeader()) {
            LeaderDrone leader = this.leaderDrone;
            leader.getFollowers().remove(this.tag);
            getEmployer(leader).drones.put(this.tag, DroneFactory.followerToLeader(this, getEmployer(leader), leader.getPilot()));
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_not_in_a_swarm");
        }
    }

    public DeliveryService getEmployer(LeaderDrone leader) {
        return leader.getPilot().getSingleEmployer();
    }

    public boolean hasLeader() {
        return leaderDrone != null;
    }

    /**
     * Method to display a drone's details
     * @return String representation of the drone's information
     */
    @Override
    public String toString() {
        return this.getDroneInfo() + this.getPayloadInfo();
    }
}