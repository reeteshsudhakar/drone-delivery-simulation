public class Location {
    private final String init_name;
    private final Integer init_x;
    private final Integer init_y;
    private final Integer init_space_limit;
    private Integer spaces_left;

    public Location(String init_name, Integer init_x, Integer init_y, Integer init_space_limit) {
        this.init_name = init_name;
        this.init_x = init_x;
        this.init_y = init_y;
        this.init_space_limit = init_space_limit;
        this.spaces_left = init_space_limit;
    }

    Integer calculateDistance(Location destination) {
        return 1 + (int) Math.floor(Math.sqrt(Math.pow(getInit_x() - destination.getInit_x(), 2)
                + Math.pow(getInit_y() - destination.getInit_y(), 2)));
    }

    public String getInit_name() {
        return init_name;
    }

    public Integer getInit_x() {
        return init_x;
    }

    public Integer getInit_y() {
        return init_y;
    }

    public Integer getInit_space_limit() {
        return init_space_limit;
    }

    public Integer getSpaces_left() {
        return spaces_left;
    }

    public void decrementSpaces_left() {
        this.spaces_left -= 1;
    }

    public void incrementSpaces_left() {
        this.spaces_left += 1;
    }
}