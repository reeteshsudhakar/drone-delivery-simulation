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
    public LeaderDrone(Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase, Location currentLocation) {
        super(init_tag, init_capacity, init_fuel, homeBase, currentLocation);
        this.swarm = new TreeMap<>();
    }

    public LeaderDrone(FollowerDrone followerDrone, Pilot pilot) {
        this(followerDrone.getTag(), followerDrone.getCapacity(), followerDrone.getFuel(), followerDrone.getHomeBase(), followerDrone.getCurrentLocation());
        this.pilot = pilot;
        this.swarm = new TreeMap<>();
    }

    public LeaderDrone(Drone drone, Pilot pilot) {
        this(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation());
        this.pilot = pilot;
        this.swarm = new TreeMap<>();
    }

    public TreeMap<Integer, Drone> getSwarm() {
        return swarm;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
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
        return getDroneInfo() + swarmString + getPayloadInfo();
    }
}
