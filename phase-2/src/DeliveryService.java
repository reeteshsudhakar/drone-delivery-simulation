public class DeliveryService {
    private final String name;
    private Integer revenue;
    private final Location location;

    public DeliveryService(String init_name, Integer init_revenue, Location location) {
        this.name = init_name;
        this.revenue = init_revenue;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public Location getLocation() {
        return location;
    }

}
