public class Drone {
    private final String serviceName;
    private final Integer tag;
    private Integer capacity;
    private Integer fuel;

    public Drone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        this.serviceName = service_name;
        this.tag = init_tag;
        this.capacity = init_capacity;
        this.fuel = init_fuel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getTag() {
        return tag;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void addFuel(Integer petrol) {
        this.fuel += petrol;
    }
}
