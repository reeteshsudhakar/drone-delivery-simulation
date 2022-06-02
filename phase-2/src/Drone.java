public class Drone {
    private final String service_name;
    private final Integer init_tag;
    private final Integer init_capacity;
    private Integer init_fuel;

    public Drone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        this.service_name = service_name;
        this.init_tag = init_tag;
        this.init_capacity = init_capacity;
        this.init_fuel = init_fuel;
    }

    public String getService_name() {
        return service_name;
    }

    public Integer getInit_tag() {
        return init_tag;
    }

    public Integer getInit_capacity() {
        return init_capacity;
    }

    public Integer getInit_fuel() {
        return init_fuel;
    }
}
