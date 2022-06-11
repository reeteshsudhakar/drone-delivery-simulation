import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<DeliveryService> services = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Drone> drones = new ArrayList<>();

    InterfaceLoop() { }

    /**
     * Method to make ingredients for delivery services to sell.
     * @param barcode the barcode of the ingredient
     * @param name the name of the ingredient
     * @param weight the unit weight of the ingredient
     */
    void makeIngredient(String barcode, String name, Integer weight) {
        // checking if the ingredient already exists
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getBarcode().equals(barcode)) {
                displayMessage("ERROR", "ingredient_already_exists_within_system");
                return;
            }
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
        ingredients.add(ingredient);
        displayMessage("OK","change_completed");
    }

    /**
     * Method to display the ingredients in the system.
     */
    void displayIngredients() {
        // displaying all the ingredients in the system by iterating through the collection
        Collections.sort(ingredients);
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient.toString());
        }
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
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                displayMessage("ERROR", "location_already_exists_within_system");
                return;
            }
        }

        // checking if the space limit is valid (positive) and whether the passed in arguments are valid
        if (spaceLimit <= 0) {
            displayMessage("ERROR", "location_space_limit_must_be_greater_than_zero");
            return;
        } else if (name == null || name.equals("")) {
            displayMessage("ERROR", "location_name_must_not_be_empty");
            return;
        }

        // creating the location and adding it to the collection
        Location location = new Location(name, x_coordinate, y_coordinate, spaceLimit);
        locations.add(location);
        displayMessage("OK","change_completed");
    }

    /**
     * Method to display the locations in the system.
     */
    void displayLocations() {
        // displaying all the locations in the system by iterating through the collection
        for (Location location : locations) {
            System.out.println(location.toString());
        }
        displayMessage("OK","display_completed");
    }

    /**
     * Method to check the distance between two specified locations in the system.
     * @param departurePoint the name of the departure location
     * @param arrivalPoint the name of the arrival location
     */
    void checkDistance(String departurePoint, String arrivalPoint) {
        // checking if the departure and arrival points are valid
        Location departureLocation = null;
        Location arrivalLocation = null;
        for (Location location : locations) {
            if (location.getName().equals(departurePoint)) {
                departureLocation = location;
            }
            if (location.getName().equals(arrivalPoint)) {
                arrivalLocation = location;
            }
        }

        // if the departure or arrival points are not valid, display an error message
        if (departureLocation == null) {
            displayMessage("ERROR", "invalid_departure_location_specified");
            return;
        } else if (arrivalLocation == null) {
            displayMessage("ERROR", "invalid_arrival_location_specified");
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
        for (DeliveryService service : services) {
            if (service.getName().equals(name)) {
                displayMessage("ERROR", "service_already_exists_within_system");
                return;
            }
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
        boolean found = false;
        for (Location location : locations) {
            if (location.getName().equals(locatedAt)) {
                found = true;
                DeliveryService newService = new DeliveryService(name, revenue, location);
                services.add(newService);
                break;
            }
        }

        // if the location does not exist in the system, display an error message
        if (!found) {
            displayMessage("ERROR", "location_not_found_in_system");
        } else {
            displayMessage("OK","service_created");
        }
    }

    /**
     * Method to display the delivery services in the system.
     */
    void displayServices() {
        // displaying all the delivery services in the system by iterating through the collection
        Collections.sort(services);
        for (DeliveryService deliveryService : services) {
            System.out.println(deliveryService.toString());
        }

        displayMessage("OK","display_completed");
    }

    /**
     * Method to make a restaurant for the system.
     * @param name the name of the restaurant
     * @param locatedAt the name of the location the restaurant is located at
     */
    void makeRestaurant(String name, String locatedAt) {
        // checking if the restaurant already exists
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(name)) {
                displayMessage("ERROR", "restaurant_already_exists_within_system");
                return;
            }
        }

        // checking if the passed in location exists
        boolean found = false;
        for (Location location : locations) {
            if (location.getName().equals(locatedAt)) {
                found = true;
                Restaurant restaurant = new Restaurant(name, location);
                restaurants.add(restaurant);
            }
        }

        // if the location does not exist in the system, display an error message
        if (!found) {
            displayMessage("ERROR", "location_not_found_in_system");
        } else {
            displayMessage("OK","change_completed");
        }
    }

    /**
     * Method to display the restaurants in the system.
     */
    void displayRestaurants() {
        // displaying all the restaurants in the system by iterating through the collection
        Collections.sort(restaurants);
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant.toString());
        }

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
        boolean found = false;
        for (DeliveryService service : services) {
            if (service.getName().equals(serviceName)) {
                found = true;
                newService = service;
                serviceLocation = service.getLocation();
                break;
            }
        }

        // if the service does not exist in the system, display an error message
        if (!found) {
            displayMessage("ERROR","service_not_found_in_system");
            return;
        }

        // checking if the drone already exists in the system
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getService().getName().equals(serviceName)) {
                displayMessage("ERROR","drone_already_exists_within_system");
                return;
            }
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
            Drone newDrone = new Drone(newService, tag, capacity, fuel, serviceLocation);
            drones.add(newDrone);
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
        Collections.sort(drones);
        for (Drone drone : drones) {
            if (drone.getService().getName().equals(serviceName)) {
                drone.displayDroneInfo();
            }
        }

        displayMessage("OK","display_completed");
    }

    /**
     * Method to display all of the drones in the system.
     */
    void displayAllDrones() {
        // displaying all the drones in the system by iterating through the collection
        for (DeliveryService service : services) {
            System.out.printf("Service name [%s] drones:%n", service.getName());
            for (Drone drone : drones) {
                if (drone.getService().getName().equals(service.getName())) {
                    drone.displayDroneInfo();
                }
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
        boolean droneFound = false;
        boolean destinationFound = false;
        Drone movedDrone = null;
        Location destinationLocation = null;
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getService().getName().equals(serviceName)) {
                droneFound = true;
                movedDrone = drone;
                break;
            }
        }

        // if the drone does not exist in the system, display an error message
        if (!droneFound) {
            displayMessage("ERROR", "drone_not_found_in_system");
            return;
        } else {
            for (Location location : locations) {
                if (location.getName().equals(destination)) {
                    destinationFound = true;
                    destinationLocation = location;
                    break;
                }
            }
        }

        // if the destination location does not exist in the system, display an error message
        if (!destinationFound) {
            displayMessage("ERROR","destination_not_found_in_system");
            return;
        }

        // checking if the drone has enough fuel to fly to and whether there is space in the destination
        int distance = movedDrone.getLocation().calculateDistance(destinationLocation);
        int returnDistance = destinationLocation.calculateDistance(movedDrone.getHomeBase());
        if (distance > movedDrone.getFuel()) {
            displayMessage("ERROR","not_enough_fuel_to_reach_destination");
            return;
        } else if (distance + returnDistance > movedDrone.getFuel()) {
            displayMessage("ERROR","not_enough_fuel_to_return_to_home_base_from_destination");
            return;
        } else if (destinationLocation.getSpacesLeft() == 0) {
            displayMessage("ERROR","not_enough_space_for_drone_at_destination");
            return;
        }

        // if the drone can fly to the destination, move it to the destination, update the fuel and the spaces left
        movedDrone.getLocation().incrementSpacesLeft();
        destinationLocation.decrementSpacesLeft();

        movedDrone.setLocation(destinationLocation);
        movedDrone.setFuel(movedDrone.getFuel() - distance);


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
        boolean droneFound = false;
        boolean ingredientFound = false;
        Drone loadDrone = null;
        Ingredient loadIngredient = null;
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getService().getName().equals(serviceName)) {
                droneFound = true;
                loadDrone = drone;
                break;
            }
        }

        // if the drone does not exist in the system, display an error message
        if (!droneFound) {
            displayMessage("ERROR", "drone_not_found_in_system");
            return;
        } else { // if the drone exists in the system, check if the ingredient exists in the system
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getBarcode().equals(barcode)) {
                    ingredientFound = true;
                    loadIngredient = ingredient;
                    break;
                }
            }
        }

        // if the ingredient does not exist in the system, display an error message
        if (!ingredientFound) {
            displayMessage("ERROR","ingredient_not_found_in_system");
            return;
        }

        // checking if the drone is at the service's home base
        if (!loadDrone.getLocation().equals(loadDrone.getHomeBase())) {
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
            displayMessage("ERROR","no_space_left_to_load_ingredients");
        } else if (loadDrone.getRemainingCapacity() < quantity) {
            displayMessage("ERROR","not_enough_space_for_requested_ingredients");
        } else {
            for (Ingredient ingredient : loadDrone.getPayload().keySet()) {
                if (ingredient.getBarcode().equals(barcode)) {
                    loadDrone.getPayload().get(ingredient).incrementQuantity(quantity);
                    displayMessage("OK","change_completed");
                    return;
                }
            }
            Package newPackage = new Package(quantity, unitPrice);
            loadDrone.getPayload().put(loadIngredient, newPackage);

            loadDrone.decrementRemainingCapacity(quantity);
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
        boolean droneFound = false;
        Drone loadFuelDrone = null;
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getService().getName().equals(serviceName)) {
                droneFound = true;
                loadFuelDrone = drone;
                break;
            }
        }

        // if the petrol is not valid, display an error message
        if (petrol == null || petrol < 0) {
            displayMessage("ERROR","petrol_must_be_greater_than_zero");
            return;
        }

        // if the drone exists in the system, update the fuel IFF the drone is at the service's home base
        if (!droneFound) {
            displayMessage("ERROR", "drone_not_found_in_system");
        } else if (!loadFuelDrone.getLocation().equals(loadFuelDrone.getHomeBase())) {
            displayMessage("ERROR", "drone_not_located_at_home_base");
        } else {
            loadFuelDrone.setFuel(loadFuelDrone.getFuel() + petrol);
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
        boolean restaurantFound = false;
        Restaurant buyerRestaurant = null;
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(restaurantName)) {
                restaurantFound = true;
                buyerRestaurant = restaurant;
                break;
            }
        }

        // if the restaurant does not exist in the system, display an error message
        if (!restaurantFound) {
            displayMessage("ERROR", "restaurant_not_found_in_system");
            return;
        }

        // checking if the drone exists in the system
        boolean droneFound = false;
        Drone buyerDrone = null;
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getService().getName().equals(serviceName)) {
                droneFound = true;
                buyerDrone = drone;
                break;
            }
        }

        // if the drone does not exist in the system, display an error message
        if (!droneFound) {
            displayMessage("ERROR","drone_not_found_in_system");
            return;
        }

        // if the drone is not at the restaurant's location, display an error message
        if (!buyerDrone.getLocation().equals(buyerRestaurant.getLocation())) {
            displayMessage("ERROR","drone_not_located_at_restaurant");
            return;
        }

        // checking if the ingredient exists in the system
        boolean ingredientExists = false;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getBarcode().equals(barcode)) {
                ingredientExists = true;
                break;
            }
        }

        // if the ingredient does not exist in the system, display an error message
        if (!ingredientExists) {
            displayMessage("ERROR","ingredient_not_found_in_system");
            return;
        }

        // finding the ingredient in the drone's payload
        boolean ingredientFound = false;
        Ingredient buyerIngredient = null;
        for (Ingredient ingredient : buyerDrone.getPayload().keySet()) {
            if (ingredient.getBarcode().equals(barcode)) {
                ingredientFound = true;
                buyerIngredient = ingredient;
                break;
            }
        }

        // if the ingredient is not found in the drone's payload, display an error message
        if (!ingredientFound) {
            displayMessage("ERROR","ingredient_not_found_in_payload");
            return;
        }

        if (quantity <= 0) {
            displayMessage("ERROR","quantity_requested_must_be_greater_than_zero");
            return;
        }

        // completing the purchase if the drone has enough of the ingredient requested for purchase
        if (buyerDrone.getPayload().get(buyerIngredient).getQuantity().compareTo(quantity) < 0) {
            displayMessage("ERROR","drone_does_not_have_enough_of_ingredient_requested");
            return;
        } else if (buyerDrone.getPayload().get(buyerIngredient).getQuantity().equals(quantity)) {
            buyerRestaurant.addSpending(buyerDrone.getPayload().get(buyerIngredient).getUnitPrice() * quantity);
            buyerDrone.addSales(buyerDrone.getPayload().get(buyerIngredient).getUnitPrice() * quantity);
            buyerDrone.getPayload().remove(buyerIngredient);
            buyerDrone.incrementRemainingCapacity(quantity);
        } else {
            buyerRestaurant.addSpending(buyerDrone.getPayload().get(buyerIngredient).getUnitPrice() * quantity);
            buyerDrone.addSales(buyerDrone.getPayload().get(buyerIngredient).getUnitPrice() * quantity);
            buyerDrone.getPayload().get(buyerIngredient).decrementQuantity(quantity);
            buyerDrone.incrementRemainingCapacity(quantity);
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

        while (true) {
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
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));

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
