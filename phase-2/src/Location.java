public class Location {
    private final String name;
    private final Integer x_coord;
    private final Integer y_coord;
    private final Integer spaceLimit;
    private Integer spacesLeft;

    public Location(String name, Integer x_coord, Integer y_coord, Integer spaceLimit) {
        this.name = name;
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.spaceLimit = spaceLimit;
        this.spacesLeft = spaceLimit;
    }

    Integer calculateDistance(Location destination) {
        return 1 + (int) Math.floor(Math.sqrt(Math.pow(getInit_x() - destination.getInit_x(), 2)
                + Math.pow(getInit_y() - destination.getInit_y(), 2)));
    }

    public String getName() {
        return name;
    }

    public Integer getInit_x() {
        return x_coord;
    }

    public Integer getInit_y() {
        return y_coord;
    }

    public Integer getInit_space_limit() {
        return spaceLimit;
    }

    public Integer getSpaces_left() {
        return spacesLeft;
    }

    public void decrementSpaces_left() {
        this.spacesLeft -= 1;
    }

    public void incrementSpaces_left() {
        this.spacesLeft += 1;
    }
}