public class DeliveryService {
    private final String name;
    private Integer revenue;
    private final String locatedAt;

    public DeliveryService(String init_name, Integer init_revenue, String located_at) {
        this.name = init_name;
        this.revenue = init_revenue;
        this.locatedAt = located_at;
    }

    public String getName() {
        return name;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public String getLocatedAt() {
        return locatedAt;
    }
}
