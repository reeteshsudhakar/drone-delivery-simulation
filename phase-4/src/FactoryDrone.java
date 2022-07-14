public class FactoryDrone {

    public static LeaderDrone followerToLeader(FollowerDrone drone, DeliveryService service, Pilot pilot) {
        LeaderDrone newDrone = new LeaderDrone(drone.getTag(), drone.getCapacity(), drone.getRemainingCapacity(), drone.getFuel(),
                drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
        newDrone.assignPilot(pilot);
        service.drones.put(newDrone.getTag(), newDrone);
        return newDrone;
    }

    public static FollowerDrone leaderToFollower(LeaderDrone drone, DeliveryService service, LeaderDrone leader) {
        FollowerDrone newDrone =  new FollowerDrone(drone.getTag(), drone.getCapacity(), drone.getRemainingCapacity(), drone.getFuel(), drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload(), leader);
        service.drones.put(newDrone.getTag(), newDrone);
        return newDrone;
    }
}
