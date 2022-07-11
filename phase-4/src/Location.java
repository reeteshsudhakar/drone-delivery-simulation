import java.util.TreeMap;

/**
 * Location class to represent a location for services, restaurants, and drones in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Location implements Comparable<Location> {
    // collection of locations
    static TreeMap<String, Location> locations = new TreeMap<>();

    // Object attributes
    private final String name;
    private final Integer xCoordinate;
    private final Integer yCoordinate;
    private final Integer spaceLimit;
    private Integer spacesLeft;

    /**
     * Constructor for Location class.
     * @param name Name of location
     * @param xCoordinate X coordinate of location
     * @param yCoordinate Y coordinate of location
     * @param spaceLimit Space limit for drones of the location
     */
    public Location(String name, Integer xCoordinate, Integer yCoordinate, Integer spaceLimit) {
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.spaceLimit = spaceLimit;
        this.spacesLeft = spaceLimit;
    }

    /**
     * Method to calculate the distance between two locations.
     * @param destination Location to calculate distance to
     * @return Distance between two locations
     */
    public static Integer calculateDistance(Location start, Location destination) {
        if (start.equals(destination)) {
            return 0;
        } else {
            return 1 + (int) Math.floor(Math.sqrt(Math.pow(start.xCoordinate - destination.xCoordinate, 2)
                    + Math.pow(start.yCoordinate - destination.yCoordinate, 2)));
        }
    }

    /**
     * Method to create a location
     * @param name name of the location
     * @param xCoordinate x coordinate of the location
     * @param yCoordinate y coordinate of the location
     * @param spaceLimit space limit for drones of the location
     */
    public static void makeLocation(String name, Integer xCoordinate, Integer yCoordinate,
                                    Integer spaceLimit) {
        // checking if the location already exists
        if (locations.containsKey(name)) {
            Display.displayMessage("ERROR","location_already_exists");
            return;
        }

        // checking if the space limit is valid (positive) and whether the passed in arguments are valid
        if (spaceLimit < 0) {
            Display.displayMessage("ERROR","location_space_limit_must_not_be_negative");
            return;
        } else if (name == null || name.equals("")) {
            Display.displayMessage("ERROR","location_name_must_not_be_empty");
            return;
        }

        // creating the location and adding it to the collection
        Location location = new Location(name, xCoordinate, yCoordinate, spaceLimit);
        locations.put(name, location);
        Display.displayMessage("OK","location_created");
    }

    /**
     * Method to check the distance between two locations
     * @param departurePoint departure location
     * @param arrivalPoint arrival location
     */
    public static void checkDistance(String departurePoint, String arrivalPoint) {
        // checking if the departure and arrival points are valid
        Location departureLocation;
        Location arrivalLocation;

        if (locations.containsKey(departurePoint)) {
            departureLocation = locations.get(departurePoint);
        } else {
            Display.displayMessage("ERROR","departure_location_does_not_exist");
            return;
        }

        if (locations.containsKey(arrivalPoint)) {
            arrivalLocation = locations.get(arrivalPoint);
        } else {
            Display.displayMessage("ERROR","arrival_location_does_not_exist");
            return;
        }

        // if the departure and arrival points are valid, calculate and display the distance between them
        Integer distance = Location.calculateDistance(departureLocation, arrivalLocation);
        Display.displayMessage("OK",String.format("distance = %d", distance));
    }

    /**
     * Method to check if a location has enough space for a drone to land
     * @param drone drone to check space for
     * @return false if there is NOT enough space, true otherwise
     */
    public boolean notEnoughSpace(Drone drone) {
        return this.spacesLeft < drone.getFollowers().size() + 1;
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
            return this.name.equals(l.name);
        }
    }

    /**
     * Override of toString method to display information about a Location.
     * @return String representation of a Location
     */
    @Override
    public String toString() {
        return String.format("Name: %s, (x,y): (%d, %d), Space: [%d / %d] Remaining",
                this.name, this.xCoordinate, this.yCoordinate, this.spacesLeft, this.spaceLimit);
    }

    /**
     * Override of compareTo method to compare two locations to sort them.
     * @param other Location to compare to
     * @return Integer representing the comparison of locations based on their name
     */
    @Override
    public int compareTo(Location other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Getter for name.
     * @return Name of location
     */
    public String getName() {
        return this.name;
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
}