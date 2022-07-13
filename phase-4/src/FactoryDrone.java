public class FactoryDrone {

    public LeaderDrone followerToLeader(FollowerDrone drone) {
        LeaderDrone newDrone = new LeaderDrone(drone.getTag(), drone.getCapacity(), drone.getFuel(),
                drone.getHomeBase(), drone.getCurrentLocation(), drone.getSales(), drone.getPayload());
        DeliveryService.drones.put(drone.getTag(), newDrone);
        return newDrone;
        kunal is gay
    }

    public FollowerDrone leaderToFollower(LeaderDrone drone) {
        FollowerDrone newDrone = new FollowerDrone(drone.getTag(), drone.getCapacity(), drone.getFuel(), drone.get);
        //Integer init_tag, Integer init_capacity, Integer init_fuel, Location homeBase, Location currentLocation, Integer sales, TreeMap<Ingredient, Package> payload

    }
}
