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
    public FollowerDrone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload) {
        super(init_tag, init_capacity, init_fuel, homeBase, currentLocation);
        this.addSales(sales);
        this.payload = payload;
    }

 /*   /**
     * Constructor used when a normal drone becomes a follower drone
     * @param drone drone to be converted into a follower drone
     * @param leaderDrone leader drone for this drone
     */
//    public FollowerDrone(Drone drone, LeaderDrone leaderDrone) {
//        this(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
//        this.leaderDrone = leaderDrone;
//        leaderDrone.getSwarm().put(drone.getTag(), this);
//    } */


//    public FollowerDrone(LeaderDrone drone, LeaderDrone leader) {
//        this(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
//        this.leaderDrone = leader;
//        leader.getSwarm().put(this.getTag(), this);
//    }
    public LeaderDrone getLeaderDrone() {
        return leaderDrone;
    }

    public void setLeaderDrone(LeaderDrone leaderDrone) {
        this.leaderDrone = leaderDrone;
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

    /**
     * Method to remove a drone from a swarm
     */
    public void leaveSwarm() {
        // Remove swarmDrone from swarm iff it is in a swarm (ensuring it is not a leader)
        if (this.hasLeader()) {
            this.removeFromSwarm();
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_not_in_a_swarm");
        }
    }

    public boolean hasLeader() {
        if (leaderDrone == null) {
            return false;
        }
        return true;
    }

    /**
     * Method to remove a drone from its leader drone's list of followers
     */
    public void removeFromSwarm() {
        this.leaderDrone.getFollowers().remove(this.tag);
    }

    /**
     * Method to assign a leader to a drone
     * @param drone the drone to assign as the leader
     */
    public void assignLeader(LeaderDrone drone) {
        this.leaderDrone = drone;
        drone.getSwarm().put(this.tag, this);
    }

    /**
     * Method to switch the leader of a drone to a pilot from a drone
     * @param pilot the new pilot of the drone
     */
    public void becomeLeader(Pilot pilot) {
        this.leaderDrone.getFollowers().remove(this.tag);
        this.assignPilot(pilot);
        pilot.getPilotedDrones().put(this.tag, this);
    }
}