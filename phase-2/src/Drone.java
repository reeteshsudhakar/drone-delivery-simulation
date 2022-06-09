import java.util.ArrayList;
import java.util.HashMap;

public class Drone {
    private final DeliveryService service;
    private final Integer tag;
    private final Integer capacity;
    private Integer remainingCapacity;
    private Integer fuel;
    private final Location homeBase;
    private Location location;
    private Integer sales;
    private HashMap<Ingredient, Package> payload;

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
        this.payload = new HashMap<>();
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

    public HashMap<Ingredient, Package> getPayload() {
        return payload;
    }

    public void decrementCapacity(Integer quantity) {
        this.remainingCapacity -= quantity;
    }

    public void incrementCapacity(Integer quantity) {
        this.remainingCapacity += quantity;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public void addFuel(Integer petrol) {
        this.fuel += petrol;
    }
}
