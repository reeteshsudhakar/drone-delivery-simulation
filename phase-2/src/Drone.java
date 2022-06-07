import java.util.ArrayList;

public class Drone {
    private final DeliveryService service;
    private final Integer tag;
    private final Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private final Location homeBase;
    private Location location;
    private Integer sales;
    private ArrayList<Package> payload;

    public Drone(DeliveryService service, Integer init_tag, Integer init_capacity,
                 Integer init_fuel, Location homeBase) {
        this.service = service;
        this.tag = init_tag;
        this.capacity = init_capacity;
        this.remainingCapacity = init_capacity;
        this.fuel = init_fuel;
        this.location = homeBase;
        this.homeBase = homeBase;
        this.sales = 0;
        this.payload = new ArrayList<>();
    }

    public DeliveryService getService() {
        return service;
    }

    public Integer getTag() {
        return tag;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public Integer getFuel() {
        return fuel;
    }

    public Location getHomeBase() {
        return homeBase;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getSales() {
        return sales;
    }

    public ArrayList<Package> getPayload() {
        return payload;
    }

    public void decrementCapacity(Integer quantity) {
        this.remainingCapacity -= quantity;
    }

    public void incrementCapacity(Integer quantity) {
        this.remainingCapacity += quantity;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public void addFuel(Integer petrol) {
        this.fuel += petrol;
    }
}
