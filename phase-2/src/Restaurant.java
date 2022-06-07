public class Restaurant {
    private final String name;
    private final Location location;
    private Integer spending;

    public Restaurant(String init_name, Location location) {
        this.name = init_name;
        this.location = location;
        this.spending = 0;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getSpending() {
        return spending;
    }

}
