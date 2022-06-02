public class Restaurant {
    private final String init_name;
    private final String located_at;
    private Integer init_spending;

    public Restaurant(String init_name, String located_at) {
        this.init_name = init_name;
        this.located_at = located_at;
        this.init_spending = 0;
    }

    public String getInit_name() {
        return init_name;
    }

    public String getLocated_at() {
        return located_at;
    }

    public Integer getInit_spending() {
        return init_spending;
    }

}
