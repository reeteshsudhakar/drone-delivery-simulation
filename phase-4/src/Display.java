// TODO: look into styling things using CSS (https://docs.oracle.com/javafx/2/css_tutorial/jfxpub-css_tutorial.htm)

/**
 * This class is used to display the messages from the program.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Display {

    /**
     * Method to display all of the drones in the system.
     */
    public static void displayAllDrones() {
        // displaying all the drones in the system by iterating through the collection
        for (DeliveryService service : DeliveryService.services.values()) {
            System.out.printf("service name [%s] drones:%n", service.getName());
            for (Drone drone : service.getDrones().values()) {
                System.out.print(drone.toString());
            }
        }

        Main.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the delivery services in the system.
     */
    public static void displayServices() {
        // displaying all the delivery services in the system by iterating through the collection
        for (DeliveryService service : DeliveryService.services.values()) {
            System.out.println(service.toString());
        }
        Main.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the drones in the system attached to a specified service.
     * @param serviceName the name of the service the drone is assigned to
     */
    public static void displayDrones(String serviceName) {
        // displaying the drones in the system attached to the specified service
        if (DeliveryService.services.containsKey(serviceName)) {
            DeliveryService service = DeliveryService.services.get(serviceName);
            service.getDrones().forEach((k, v) -> System.out.print(v.toString()));
            Main.displayMessage("OK","display_completed");
        } else {
            Main.displayMessage("ERROR","service_does_not_exist");
        }
    }

    /**
     * Method to display the restaurants in the system.
     */
    public static void displayRestaurants() {
        // displaying all the restaurants in the system by iterating through the collection
        for (Restaurant restaurant : Restaurant.restaurants.values()) {
            System.out.println(restaurant.toString());
        }
        Main.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the locations in the system.
     */
    public static void displayLocations() {
        // displaying all the locations in the system by iterating through the collection
        for (Location location : Location.locations.values()) {
            System.out.println(location.toString());
        }
        Main.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the people in our service
     */
    public static void displayPersons() {
        // displaying all the people in the system by iterating through the collection
        for (Person person : Person.people.values()) {
            System.out.println(person.toString());
        }
        Main.displayMessage("OK","display_completed");
    }
}
