public class Restaurant {
    private final String name;
    private final String locatedAt;
    private Integer spending;

    public Restaurant(String init_name, String located_at) {
        this.name = init_name;
        this.locatedAt = located_at;
        this.spending = 0;
    }

    public String getName() {
        return name;
    }

    public String getLocatedAt() {
        return locatedAt;
    }

    public Integer getSpending() {
        return spending;
    }

}
