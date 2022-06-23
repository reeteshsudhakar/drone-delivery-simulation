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
    public FollowerDrone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase) {
        super(init_tag, init_capacity, init_fuel, homeBase);
    }

    public FollowerDrone(LeaderDrone drone, LeaderDrone leader) {
        this(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.getHomeBase());
        this.leaderDrone = leader;
    }
}
