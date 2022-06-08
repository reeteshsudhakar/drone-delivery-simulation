public class Location {
    private final String name;
    private final Integer x_coordinate;
    private final Integer y_coordinate;
    private final Integer spaceLimit;
    private Integer spacesLeft;

    public Location(String name, Integer x_coordinate, Integer y_coordinate, Integer spaceLimit) {
        this.name = name;
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.spaceLimit = spaceLimit;
        this.spacesLeft = spaceLimit;
    }

    Integer calculateDistance(Location destination) {
        return 1 + (int) Math.floor(Math.sqrt(Math.pow(getX_coordinate() - destination.getX_coordinate(), 2)
                + Math.pow(getY_coordinate() - destination.getY_coordinate(), 2)));
    }

    public String getName() {
        return name;
    }

    public Integer getX_coordinate() {
        return x_coordinate;
    }

    public Integer getY_coordinate() {
        return y_coordinate;
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