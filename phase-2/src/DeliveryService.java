public class DeliveryService {
    private final String init_name;
    private Integer init_revenue;
    private final String located_at;

    public DeliveryService(String init_name, Integer init_revenue, String located_at) {
        this.init_name = init_name;
        this.init_revenue = init_revenue;
        this.located_at = located_at;
    }

    public String getInit_name() {
        return init_name;
    }

    public Integer getInit_revenue() {
        return init_revenue;
    }

    public String getLocated_at() {
        return located_at;
    }
}
