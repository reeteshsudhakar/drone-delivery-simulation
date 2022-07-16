/**
 * DroneFactory class to convert drones between followers and leaders.
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class DroneFactory {

    /**
     * Converts a drone from a follower to a leader.
     * @param drone The drone to convert.
     * @param service The service to use for the conversion.
     * @param pilot The pilot to use for the conversion.
     * @return The converted drone.
     */
    public static LeaderDrone followerToLeader(FollowerDrone drone, DeliveryService service, Pilot pilot) {
        LeaderDrone newDrone = new LeaderDrone(drone.getTag(), drone.getCapacity(), drone.getRemainingCapacity(), drone.getFuel(),
                drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
        newDrone.assignPilot(pilot);
        service.drones.put(newDrone.getTag(), newDrone);
        return newDrone;
    }

    /**
     * Converts a drone from a leader to a follower.
     * @param drone The drone to convert.
     * @param service The service to use for the conversion.
     * @param leader The leader to use for the conversion.
     * @return The converted drone.
     */
    public static FollowerDrone leaderToFollower(LeaderDrone drone, DeliveryService service, LeaderDrone leader) {
        FollowerDrone newDrone =  new FollowerDrone(drone.getTag(), drone.getCapacity(), drone.getRemainingCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload(), leader);
        service.drones.put(newDrone.getTag(), newDrone);
        return newDrone;
    }
}
