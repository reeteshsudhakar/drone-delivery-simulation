import java.util.ArrayList;

public class LeaderDrone extends Drone {

    private Pilot pilot;
    private ArrayList<FollowerDrone> followers;

    /**
     * Constructor for Drone class.
     *
     * @param init_tag      drone tag (unique)
     * @param init_capacity drone capacity for ingredients
     * @param init_fuel     drone fuel
     * @param homeBase      home base currentLocation
     */
    public LeaderDrone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase) {
        super(init_tag, init_capacity, init_fuel, homeBase);
    }

    public LeaderDrone(FollowerDrone followerDrone, Pilot pilot) {
        this(followerDrone.getTag(), followerDrone.getCapacity(), followerDrone.getFuel(), followerDrone.getHomeBase());
        this.pilot = pilot;
        this.followers = new ArrayList<>();
    }
}
