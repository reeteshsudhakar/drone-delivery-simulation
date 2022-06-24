import java.util.TreeMap;

/**
 * Location class to represent a location for services, restaurants, and drones in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Location implements Comparable<Location> {
    // Object attributes
    private String name;
    private Integer x_coordinate;
    private Integer y_coordinate;
    private Integer spaceLimit;
    private Integer spacesLeft;

    /**
     * Constructor for Location class.
     * @param name Name of location
     * @param x_coordinate X coordinate of location
     * @param y_coordinate Y coordinate of location
     * @param spaceLimit Space limit for drones of the location
     */
    public Location(String name, Integer x_coordinate, Integer y_coordinate, Integer spaceLimit) {
        this.name = name;
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.spaceLimit = spaceLimit;
        this.spacesLeft = spaceLimit;
    }

    /**
     * Method to calculate the distance between two locations.
     * @param destination Location to calculate distance to
     * @return Distance between two locations
     */
    Integer calculateDistance(Location destination) {
        if (this.equals(destination)) {
            return 0;
        } else {
            return 1 + (int) Math.floor(Math.sqrt(Math.pow(this.getX_coordinate() - destination.getX_coordinate(), 2)
                    + Math.pow(this.getY_coordinate() - destination.getY_coordinate(), 2)));
        }
    }

    /**
     * Getter for name.
     * @return Name of location
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for x_coordinate.
     * @return X coordinate of location
     */
    public Integer getX_coordinate() {
        return this.x_coordinate;
    }

    /**
     * Getter for y_coordinate.
     * @return Y coordinate of location
     */
    public Integer getY_coordinate() {
        return this.y_coordinate;
    }

    /**
     * Getter for spacesLeft.
     * @return Spaces left for drones of the location
     */
    public Integer getSpacesLeft() {
        return this.spacesLeft;
    }

    /**
     * Method to decrement the number of spaces left of a location if a drone arrives.
     */
    public void decrementSpacesLeft() {
        this.spacesLeft -= 1;
    }

    /**
     * Method to increment the number of spaces left of a location if a drone leaves.
     */
    public void incrementSpacesLeft() {
        this.spacesLeft += 1;
    }

    /**
     * Method to check if two methods are equal.
     * @param obj Object to compare to
     * @return true if two locations are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        } else {
            Location l = (Location) obj;
            return this.getName().equals(l.getName());
        }
    }

    /**
     * Override of toString method to display information about a Location.
     * @return String representation of a Location
     */
    @Override
    public String toString() {
        return String.format("Name: %s, (x,y): (%d, %d), Space: [%d / %d] remaining",
                this.name, this.x_coordinate, this.y_coordinate, this.spacesLeft, this.spaceLimit);
    }

    /**
     * Override of compareTo method to compare two locations to sort them.
     * @param other Location to compare to
     * @return Integer representing the comparison of locations based on their name
     */
    @Override
    public int compareTo(Location other) {
        return this.getName().compareTo(other.getName());
    }

    public static void makeLocation(String name, Integer x_coordinate, Integer y_coordinate,
                                    Integer spaceLimit, TreeMap<String, Location> locations) {
        // checking if the location already exists
        if (locations.containsKey(name)) {
            Display.displayMessage("ERROR","location_already_exists");
            return;
        }

        // checking if the space limit is valid (positive) and whether the passed in arguments are valid
        if (spaceLimit < 0) {
            Display.displayMessage("ERROR", "location_space_limit_must_not_be_negative");
            return;
        } else if (name == null || name.equals("")) {
            Display.displayMessage("ERROR", "location_name_must_not_be_empty");
            return;
        }

        // creating the location and adding it to the collection
        Location location = new Location(name, x_coordinate, y_coordinate, spaceLimit);
        locations.put(name, location);
        Display.displayMessage("OK","location_created");
    }

    public static void checkDistance(String departurePoint, String arrivalPoint, TreeMap<String, Location> locations) {
        // checking if the departure and arrival points are valid
        Location departureLocation;
        Location arrivalLocation;

        if (locations.containsKey(departurePoint)) {
            departureLocation = locations.get(departurePoint);
        } else {
            Display.displayMessage("ERROR", "departure_location_does_not_exist");
            return;
        }

        if (locations.containsKey(arrivalPoint)) {
            arrivalLocation = locations.get(arrivalPoint);
        } else {
            Display.displayMessage("ERROR", "arrival_location_does_not_exist");
            return;
        }

        // if the departure and arrival points are valid, calculate and display the distance between them
        int distance = departureLocation.calculateDistance(arrivalLocation);
        Display.displayMessage("OK", String.format("distance = %d", distance));
    }
}