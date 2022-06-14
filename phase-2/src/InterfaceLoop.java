import java.util.Scanner;
import java.util.TreeMap;

/*
TODO: test interface against NEW test commands
TODO: code cleanup (see where methods can be moved to other classes, simplified)
 */

/**
 * Minimum Viable Product (MVP) of the interface loop for restaurants
 * to purchase ingredients via drones operated by a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class InterfaceLoop {

    // collections of objects for the interface loop
    TreeMap<String, Ingredient> ingredients = new TreeMap<>();
    TreeMap<String, Location> locations = new TreeMap<>();
    TreeMap<String, DeliveryService> services = new TreeMap<>();
    TreeMap<String, Restaurant> restaurants = new TreeMap<>();

    InterfaceLoop() { }

    /**
     * Method to make ingredients for delivery services to sell.
     * @param barcode the barcode of the ingredient
     * @param name the name of the ingredient
     * @param weight the unit weight of the ingredient
     */
    void makeIngredient(String barcode, String name, Integer weight) {
        // checking if the ingredient already exists
        if (ingredients.containsKey(barcode)) {
            displayMessage("ERROR","ingredient_already_exists");
            return;
        }
        // checking if the weight is valid (positive) and whether the passed in arguments are valid
        if (weight <= 0) {
            displayMessage("ERROR", "ingredient_weight_must_be_greater_than_zero");
            return;
        } else if (barcode == null || name == null || barcode.equals("") || name.equals("")) {
            displayMessage("ERROR", "ingredient_barcode_and_name_must_not_be_empty");
            return;
        }

        // creating the ingredient and adding it to the collection
        Ingredient ingredient = new Ingredient(barcode, name, weight);
        ingredients.put(barcode, ingredient);
        displayMessage("OK","change_completed");
    }

    /**
     * Method to display the ingredients in the system.
     */
    void displayIngredients() {
        // displaying all the ingredients in the system by iterating through the collection
        ingredients.forEach((k, v) -> System.out.println(v.toString()));
        displayMessage("OK","display_completed");
    }

    /**
     * Method to make locations for services and restaurants to be located at.
     * @param name the name of the location
     * @param x_coordinate the x-coordinate of the location
     * @param y_coordinate the y-coordinate of the location
     * @param spaceLimit the capacity of drones at the location
     */
    void makeLocation(String name, Integer x_coordinate, Integer y_coordinate, Integer spaceLimit) {
        // checking if the location already exists
        if (locations.containsKey(name)) {
            displayMessage("ERROR","location_already_exists");
            return;
        }

        // checking if the space limit is valid (positive) and whether the passed in arguments are valid
        if (spaceLimit < 0) {
            displayMessage("ERROR", "location_space_limit_must_not_be_negative");
            return;
        } else if (name == null || name.equals("")) {
            displayMessage("ERROR", "location_name_must_not_be_empty");
            return;
        }

        // creating the location and adding it to the collection
        Location location = new Location(name, x_coordinate, y_coordinate, spaceLimit);
        locations.put(name, location);
        displayMessage("OK","change_completed");
    }

    /**
     * Method to display the locations in the system.
     */
    void displayLocations() {
        // displaying all the locations in the system by iterating through the collection
        locations.forEach((k, v) -> System.out.println(v.toString()));
        displayMessage("OK","display_completed");
    }

    /**
     * Method to check the distance between two specified locations in the system.
     * @param departurePoint the name of the departure location
     * @param arrivalPoint the name of the arrival location
     */
    void checkDistance(String departurePoint, String arrivalPoint) {
        // checking if the departure and arrival points are valid
        Location departureLocation;
        Location arrivalLocation;

        if (locations.containsKey(departurePoint)) {
            departureLocation = locations.get(departurePoint);
        } else {
            displayMessage("ERROR", "departure_location_does_not_exist");
            return;
        }

        if (locations.containsKey(arrivalPoint)) {
            arrivalLocation = locations.get(arrivalPoint);
        } else {
            displayMessage("ERROR", "arrival_location_does_not_exist");
            return;
        }

        // if the departure and arrival points are valid, calculate and display the distance between them
        int distance = departureLocation.calculateDistance(arrivalLocation);
        displayMessage("OK", String.format("distance = %d", distance));
    }

    /**
     * Method to make a delivery service for the system.
     * @param name the name of the service
     * @param revenue the revenue of the service
     * @param locatedAt the name of the location the service is located at
     */
    void makeDeliveryService(String name, Integer revenue, String locatedAt) {
        // checking if the service already exists
        if (services.containsKey(name)) {
            displayMessage("ERROR", "service_already_exists");
            return;
        }

        // checking if the revenue is valid (positive) and whether the passed in arguments are valid
        if (revenue < 0) {
            displayMessage("ERROR", "service_revenue_must_be_greater_than_or_equal_to_zero");
            return;
        } else if (name == null || name.equals("")) {
            displayMessage("ERROR", "service_name_must_not_be_empty");
            return;
        } else if (locatedAt == null || locatedAt.equals("")) {
            displayMessage("ERROR", "service_located_at_must_not_be_empty");
            return;
        }

        // creating the service and adding it to the collection IF the location exists in the system
        // if the location does not exist in the system, display an error message
        if (locations.containsKey(locatedAt)) {
            DeliveryService newService = new DeliveryService(name, revenue, locations.get(locatedAt));
            services.put(name, newService);
            displayMessage("OK","service_created");
        } else {
            displayMessage("ERROR", "location_identifier_does_not_exist");
        }
    }

    /**
     * Method to display the delivery services in the system.
     */
    void displayServices() {
        // displaying all the delivery services in the system by iterating through the collection
        services.forEach((k, v) -> System.out.println(v.toString()));
        displayMessage("OK","display_completed");
    }

    /**
     * Method to make a restaurant for the system.
     * @param name the name of the restaurant
     * @param locatedAt the name of the location the restaurant is located at
     */
    void makeRestaurant(String name, String locatedAt) {
        // checking if the restaurant already exists
        if (restaurants.containsKey(name)) {
            displayMessage("ERROR", "restaurant_already_exists");
            return;
        }

        // checking if the passed in location exists
        // if the location does not exist in the system, display an error message
        if (locations.containsKey(locatedAt)) {
            Restaurant restaurant = new Restaurant(name, locations.get(locatedAt));
            restaurants.put(name, restaurant);
            displayMessage("OK","change_completed");
        } else {
            displayMessage("ERROR", "location_identifier_does_not_exist");
        }
    }

    /**
     * Method to display the restaurants in the system.
     */
    void displayRestaurants() {
        // displaying all the restaurants in the system by iterating through the collection
        restaurants.forEach((k, v) -> System.out.println(v.toString()));
        displayMessage("OK","display_completed");
    }

    /**
     * Method to make a drone for the system.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone (unique for drones in one given service)
     * @param capacity the number of units of ingredients the drone can carry
     * @param fuel the fuel of the drone
     */
    void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel) {
        // checking if the service for the drone exists
        DeliveryService newService = null;
        Location serviceLocation = null;

        if (services.containsKey(serviceName)) {
            newService = services.get(serviceName);
            serviceLocation = newService.getLocation();
        }

        // if the service does not exist in the system, display an error message
        if (newService == null) {
            displayMessage("ERROR","service_identifier_does_not_exist");
            return;
        }

        // checking if the drone already exists in the system
        if (newService.getDrones().containsKey(tag)) {
            displayMessage("ERROR","drone_already_exists");
            return;
        }

        // checking if the capacity is valid (positive) and whether the fuel is valid (non-negative)
        if (capacity == null || capacity <= 0) {
            displayMessage("ERROR","drone_capacity_must_be_greater_than_zero");
            return;
        } else if (fuel == null || fuel < 0) {
            displayMessage("ERROR","drone_fuel_must_be_greater_than_or_equal_to_zero");
            return;
        }

        // creating the drone IFF there is space in the service's home base for it
        if (serviceLocation.getSpacesLeft() == 0) {
            displayMessage("ERROR","not_enough_space_to_create_new_drone");
        } else {
            Drone newDrone = new Drone(tag, capacity, fuel, serviceLocation);
            newService.getDrones().put(tag, newDrone);
            serviceLocation.decrementSpacesLeft();
            displayMessage("OK","change_completed");
        }
    }

    /**
     * Method to display the drones in the system attached to a specified service.
     * @param serviceName the name of the service the drone is assigned to
     */
    void displayDrones(String serviceName) {
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
     * Method to display all of the drones in the system.
     */
    void displayAllDrones() {
        // displaying all the drones in the system by iterating through the collection
        for (DeliveryService service : services.values()) {
            System.out.printf("Service name [%s] drones:%n", service.getName());
            for (Drone drone : service.getDrones().values()) {
                drone.displayDroneInfo();
            }
        }

        displayMessage("OK","display_completed");
    }

    /**
     * Method to fly a drone from one location to another.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param destination the name of the location the drone is flying to
     */
    void flyDrone(String serviceName, Integer tag, String destination) {
        // checking if the drone exists in the system
        Drone movedDrone = null;
        Location destinationLocation = null;

        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            if (service.getDrones().containsKey(tag)) {
                movedDrone = service.getDrones().get(tag);
            }
        }

        // if the drone does not exist in the system, display an error message
        if (movedDrone == null) {
            displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else {
            if (locations.containsKey(destination)) {
                destinationLocation = locations.get(destination);
            }
        }

        // if the destination location does not exist in the system, display an error message
        if (destinationLocation == null) {
            displayMessage("ERROR","flight_destination_does_not_exist");
            return;
        }

        // checking if the drone has enough fuel to fly to and whether there is space in the destination
        int distance = movedDrone.getCurrentLocation().calculateDistance(destinationLocation);
        int returnDistance = destinationLocation.calculateDistance(movedDrone.getHomeBase());
        if (distance > movedDrone.getFuel()) {
            displayMessage("ERROR","not_enough_fuel_to_reach_destination");
            return;
        } else if (distance + returnDistance > movedDrone.getFuel()) {
            displayMessage("ERROR","not_enough_fuel_to_reach_home_base_from_destination");
            return;
        } else if (destinationLocation.getSpacesLeft() == 0) {
            displayMessage("ERROR","not_enough_space_for_drone_at_destination");
            return;
        }

        // if the drone can fly to the destination, move it to the destination, update the fuel and the spaces left
        movedDrone.flyToDestination(destinationLocation);
        displayMessage("OK","change_completed");
    }

    /**
     * Method to load a drone with ingredients.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param barcode the barcode of the ingredient
     * @param quantity the quantity of the ingredient to be loaded
     * @param unitPrice the price of the ingredient
     */
    void loadIngredient(String serviceName, Integer tag, String barcode, Integer quantity, Integer unitPrice) {
        // checking if the drone exists in the system
        Drone loadDrone = null;
        Ingredient loadIngredient = null;
        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            if (service.getDrones().containsKey(tag)) {
                loadDrone = service.getDrones().get(tag);
            }
        }

        // if the drone does not exist in the system, display an error message
        if (loadDrone == null) {
            displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else { // if the drone exists in the system, check if the ingredient exists in the system
            if (ingredients.containsKey(barcode)) {
                loadIngredient = ingredients.get(barcode);
            }
        }

        // if the ingredient does not exist in the system, display an error message
        if (loadIngredient == null) {
            displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // checking if the drone is at the service's home base
        if (!loadDrone.getCurrentLocation().equals(loadDrone.getHomeBase())) {
            displayMessage("ERROR","drone_not_located_at_home_base");
            return;
        }

        // checking if the quantity and price of the ingredient are valid
        if (quantity <= 0) {
            displayMessage("ERROR","quantity_must_be_greater_than_zero");
            return;
        } else if (unitPrice <= 0) {
            displayMessage("ERROR","unit_price_must_be_greater_than_zero");
            return;
        }

        // load the ingredient and track quantity and price using a package if there is space to load it
        if (loadDrone.getRemainingCapacity() == 0) {
            displayMessage("ERROR","no_capacity_left_to_load_more_packages");
        } else if (loadDrone.getRemainingCapacity() < quantity) {
            displayMessage("ERROR","not_enough_capacity_to_hold_new_packages");
        } else {
            loadDrone.addToPayload(loadIngredient, barcode, quantity, unitPrice);
            displayMessage("OK","change_completed");
        }
    }

    /**
     * Method to load a drone with fuel.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param petrol the quantity of petrol to be loaded
     */
    void loadFuel(String serviceName, Integer tag, Integer petrol) {
        // checking if the drone exists in the system
        Drone loadFuelDrone = null;
        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            if (service.getDrones().containsKey(tag)) {
                loadFuelDrone = service.getDrones().get(tag);
            }
        }

        // if the drone does not exist or the petrol to fill the drone is not valid, display an error message
        if (loadFuelDrone == null) {
            displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else if (petrol <= 0) {
            displayMessage("ERROR", "petrol_must_be_greater_than_zero");
            return;
        }

        // if the drone is at the service's home base, fill the drone with fuel
        if (!loadFuelDrone.getCurrentLocation().equals(loadFuelDrone.getHomeBase())) {
            displayMessage("ERROR", "drone_not_located_at_home_base");
        } else {
            loadFuelDrone.loadDroneFuel(petrol);
            displayMessage("OK", "change_completed");
        }
    }

    /**
     * Method for restaurants to purchase ingredients from drones located at the restaurant's location.
     * @param restaurantName the name of the restaurant requesting the purchase
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone to be used for the purchase
     * @param barcode the barcode of the ingredient requested for purchase
     * @param quantity the quantity of the ingredient requested for purchase
     */
    void purchaseIngredient(String restaurantName, String serviceName, Integer tag,
                            String barcode, Integer quantity) {
        // checking if the restaurant exists in the system
        Restaurant buyerRestaurant = null;
        if (restaurants.containsKey(restaurantName)) {
            buyerRestaurant = restaurants.get(restaurantName);
        }

        // if the restaurant does not exist in the system, display an error message
        if (buyerRestaurant == null) {
            displayMessage("ERROR", "restaurant_identifier_does_not_exist");
            return;
        }

        // checking if the drone exists in the system
        Drone buyerDrone = null;
        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            if (service.getDrones().containsKey(tag)) {
                buyerDrone = service.getDrones().get(tag);
            }
        }

        // if the drone does not exist in the system, display an error message
        if (buyerDrone == null) {
            displayMessage("ERROR","drone_does_not_exist");
            return;
        }

        // checking if the ingredient exists in the system
        boolean ingredientExists = ingredients.containsKey(barcode);

        // if the ingredient does not exist in the system, display an error message
        if (!ingredientExists) {
            displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // if the drone is not at the restaurant's location, display an error message
        if (!buyerDrone.getCurrentLocation().equals(buyerRestaurant.getLocation())) {
            displayMessage("ERROR","drone_not_located_at_restaurant");
            return;
        }

        // finding the ingredient in the drone's payload
        Ingredient buyerIngredient = null;
        for (Ingredient ingredient : buyerDrone.getPayload().keySet()) {
            if (ingredient.getBarcode().equals(barcode)) {
                buyerIngredient = ingredient;
                break;
            }
        }

        // if the ingredient is not found in the drone's payload, display an error message
        if (buyerIngredient == null) {
            displayMessage("ERROR","ingredient_not_found_in_payload");
            return;
        } else if (quantity <= 0) {
            displayMessage("ERROR","quantity_requested_must_be_greater_than_zero");
            return;
        }

        // completing the purchase if the drone has enough of the ingredient requested for purchase
        if (buyerDrone.getPayload().get(buyerIngredient).getQuantity().compareTo(quantity) < 0) {
            displayMessage("ERROR","drone_does_not_have_enough_of_ingredient_requested");
            return;
        } else {
            buyerRestaurant.makePurchase(buyerDrone, buyerIngredient, quantity);
            buyerDrone.completePurchase(buyerIngredient, quantity);
        }
        displayMessage("OK","change_completed");
    }

    /**
     * Main loop to run the simulation and pass in arguments from the command line.
     */
    public void commandLoop() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        while (commandLineInput.hasNextLine()) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                //noinspection StatementWithEmptyBody
                if (tokens[0].indexOf("//") == 0) {
                    // deliberate empty body to recognize and skip over comments
                } else if (tokens[0].equals("make_ingredient")) {
                    makeIngredient(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("display_ingredients")) {
                    displayIngredients();
                } else if (tokens[0].equals("make_location")) {
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_locations")) {
                    displayLocations();
                } else if (tokens[0].equals("check_distance")) {
                    checkDistance(tokens[1], tokens[2]);
                } else if (tokens[0].equals("make_service")) {
                    makeDeliveryService(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                } else if (tokens[0].equals("display_services")) {
                    displayServices();
                } else if (tokens[0].equals("make_restaurant")) {
                    makeRestaurant(tokens[1], tokens[2]);
                } else if (tokens[0].equals("display_restaurants")) {
                    displayRestaurants();
                } else if (tokens[0].equals("make_drone")) {
                    makeDrone(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_drones")) {
                    displayDrones(tokens[1]);
                } else if (tokens[0].equals("display_all_drones")) {
                    displayAllDrones();
                } else if (tokens[0].equals("fly_drone")) {
                    flyDrone(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                } else if (tokens[0].equals("load_ingredient")) {
                    loadIngredient(tokens[1], Integer.parseInt(tokens[2]), tokens[3], Integer.parseInt(tokens[4]),
                            Integer.parseInt(tokens[5]));
                } else if (tokens[0].equals("load_fuel")) {
                    loadFuel(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("purchase_ingredient")) {
                    purchaseIngredient(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4],
                            Integer.parseInt(tokens[5]));
                } else if (tokens[0].equals("stop")) {
                    System.out.println("stop acknowledged");
                    break;
                } else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }
        System.out.println("simulation terminated");
        commandLineInput.close();
    }

    /**
     * Method to display a message from the interface
     * @param status the status of the message
     * @param text_output the text to be displayed
     */
    void displayMessage(String status, String text_output) {
        System.out.println(status.toUpperCase() + ":" + text_output.toLowerCase());
    }
}
