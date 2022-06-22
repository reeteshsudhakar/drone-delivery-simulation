import java.util.Collection;
import java.util.TreeMap;

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
    static void displayAllDrones(Collection<DeliveryService> services) {
        // displaying all the drones in the system by iterating through the collection
        for (DeliveryService service : services) {
            System.out.printf("Service name [%s] drones:%n", service.getName());
            for (Drone drone : service.getDrones().values()) {
                drone.displayDroneInfo();
            }
        }

        displayMessage("OK","display_completed");
    }

    /**
     * Method to display the delivery services in the system.
     */
    static void displayServices(Collection<DeliveryService> services) {
        // displaying all the delivery services in the system by iterating through the collection
        services.forEach(item -> System.out.println(item.toString()));
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the drones in the system attached to a specified service.
     * @param serviceName the name of the service the drone is assigned to
     */
    static void displayDrones(String serviceName, TreeMap<String, DeliveryService> services) {
        // displaying the drones in the system attached to the specified service
        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            service.getDrones().forEach((k, v) -> v.displayDroneInfo());
            displayMessage("OK","display_completed");
        } else {
            displayMessage("ERROR","service_does_not_exist");
        }
    }

    /**
     * Method to display the ingredients in the system.
     */
    static void displayIngredients(Collection<Ingredient> ingredients) {
        // displaying all the ingredients in the system by iterating through the collection
        ingredients.forEach(item -> System.out.println(item.toString()));
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the restaurants in the system.
     */
    static void displayRestaurants(Collection<Restaurant> restaurants) {
        // displaying all the restaurants in the system by iterating through the collection
        restaurants.forEach(item -> System.out.println(item.toString()));
        displayMessage("OK","display_completed");
    }

    /**
     * Method to display the locations in the system.
     */
    static void displayLocations(Collection<Location> locations) {
        // displaying all the locations in the system by iterating through the collection
        locations.forEach(item -> System.out.println(item.toString()));
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display a message from the interface
     * @param status the status of the message
     * @param text_output the text to be displayed
     */
    static void displayMessage(String status, String text_output) {
        System.out.println(status.toUpperCase() + ":" + text_output.toLowerCase());
    }

    /**
     * Method to display the people in our service
     */
    static void displayPersons(Collection<Person> people) {
        people.forEach(item -> System.out.println(item.toString()));
        Display.displayMessage("OK","display_completed");
    }
}
