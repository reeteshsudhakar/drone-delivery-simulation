import java.util.ArrayList;
import java.util.Scanner;

public class InterfaceLoop {

    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<DeliveryService> services = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Drone> drones = new ArrayList<>();

    InterfaceLoop() { }

    void makeIngredient(String barcode, String name, Integer weight) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getBarcode().equals(barcode)) {
                displayMessage("ERROR", "ingredient_already_exists");
                return;
            }
        }

        Ingredient ingredient = new Ingredient(barcode, name, weight);
        addIngredient(ingredient);
        displayMessage("OK","change_completed");
    }

    void displayIngredients() {
        for (Ingredient ingredient : ingredients) {
            System.out.printf("Barcode: %s, Name: %s, Weight: %d%n",ingredient.getBarcode(),
                    ingredient.getName(), ingredient.getWeight());
        }
        displayMessage("OK","display_completed");
    }

    void makeLocation(String name, Integer x_coord, Integer y_coord, Integer spaceLimit) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                displayMessage("ERROR", "location_already_exists");
                return;
            }
        }

        Location location = new Location(name, x_coord, y_coord, spaceLimit);
        locations.add(location);
        displayMessage("OK","change_completed");
    }

    void displayLocations() {
        for (Location location : locations) {
            System.out.printf("Name: %s, (x,y): (%d, %d), Space: [%d / %d] remaining%n",
                    location.getName(), location.getInit_x(), location.getInit_y(),
                    location.getSpaces_left(), location.getInit_space_limit());
        }
        displayMessage("OK","display_completed");
    }

    void checkDistance(String departurePoint, String arrivalPoint) {
        boolean departureFound = false;
        boolean arrivalFound = false;
        Location departureLocation = null;
        Location arrivalLocation = null;
        for (Location location : locations) {
            if (location.getName().equals(departurePoint)) {
                departureFound = true;
                departureLocation = location;
            }
            if (location.getName().equals(arrivalPoint)) {
                arrivalFound = true;
                arrivalLocation = location;
            }
        }

        if (!departureFound) {
            displayMessage("ERROR", "invalid_departure_location");
            return;
        }
        if (!arrivalFound) {
            displayMessage("ERROR", "invalid_arrival_location");
            return;
        }

        int distance = departureLocation.calculateDistance(arrivalLocation);

        displayMessage("OK", String.format("distance = %d", distance));

    }

    void makeDeliveryService(String name, Integer revenue, String locatedAt) {
        for (DeliveryService service : services) {
            if (service.getName().equals(name)) {
                displayMessage("ERROR", "service_already_exists");
                return;
            }
        }

        boolean found = false;
        for (Location location : locations) {
            if (location.getName().equals(locatedAt)) {
                found = true;
                DeliveryService newService = new DeliveryService(name, revenue, location);
                addDeliveryService(newService);
            }
        }

        if (!found) {
            System.out.println("ERROR: Location not found.");
        }

        displayMessage("OK","service_created");
    }

    void displayServices() {
        for (DeliveryService deliveryService : services) {
            System.out.printf("Name: %s, Revenue: $%d, Location: %s%n", deliveryService.getName(),
                    deliveryService.getRevenue(), deliveryService.getLocation().getName());
        }

        displayMessage("OK","display_completed");
    }

    void makeRestaurant(String name, String locatedAt) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(name)) {
                displayMessage("ERROR", "restaurant_already_exists");
                return;
            }
        }

        boolean found = false;
        for (Location location : locations) {
            if (location.getName().equals(locatedAt)) {
                found = true;
                Restaurant restaurant = new Restaurant(name, locatedAt);
                restaurants.add(restaurant);
            }
        }

        if (!found) {
            System.out.println("ERROR: Location not found.");
        }

        displayMessage("OK","change_completed");
    }

    void displayRestaurants() {
        for (Restaurant restaurant : restaurants) {
            System.out.printf("Name: %s, Location: %s%n, Total Spent: $%d", restaurant.getName(),
                    restaurant.getLocatedAt(), restaurant.getSpending());
        }

        displayMessage("OK","display_completed");
    }

    // add Location to the Drone (initialized to the service location and check for space)
    void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel) {
        Location serviceLocation = null;
        boolean found = false;
        for (DeliveryService service : services) {
            if (service.getName().equals(serviceName)) {
                found = true;
                serviceLocation = service.getLocation();
                break;
            }
        }

        if (!found) {
            displayMessage("ERROR","service_not_found");
            return;
        }

        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getServiceName().equals(serviceName)) {
                displayMessage("ERROR","drone_already_exists");
                return;
            }
        }

        if (serviceLocation.getSpaces_left() == 0) {
            displayMessage("ERROR","no_space_left");
            return;
        } else {
            Drone newDrone = new Drone(serviceName, tag, capacity, fuel, serviceLocation);
            addDrone(newDrone);
            displayMessage("OK","change_completed");
        }
    }

    // need to fix this
    void displayDrones(String serviceName) {
        for (Drone drone : drones) {
            if (drone.getServiceName().equals(serviceName)) {
                System.out.printf("Tag: %d, Capacity: %d, Fuel: %d%n", drone.getTag(),
                        drone.getCapacity(), drone.getFuel());
            }
        }

        displayMessage("OK","display_completed");
    }

    // need to fix this
    void displayAllDrones() {
        for (Drone drone : drones) {
            System.out.printf("Tag: %d, Capacity: %d, Fuel: %d%n", drone.getTag(),
                    drone.getCapacity(), drone.getFuel());
        }

        displayMessage("OK","display_completed");
    }

    /*
    first check if the drone exists under that service, then if destination exists.
    If both are true, then check if the drone has enough fuel to make it to the destination AND back to home base.
    If it does, then check if the destination has enough space to hold the drone.
    If it does, then fly the drone to the location and update the fuel and location of the drone.
     */
    void flyDrone(String serviceName, Integer tag, String destination) {
        boolean droneFound = false;
        boolean destinationFound = false;
        for (Drone drone : drones) {
            if (drone.getTag().equals(tag) && drone.getServiceName().equals(serviceName)) {
                droneFound = true;
                break;
            }
        }

        if (!droneFound) {
            displayMessage("ERROR", "drone_not_found");
            return;
        } else {
            for (Location location : locations) {
                if (location.getName().equals(destination)) {
                    destinationFound = true;
                    break;
                }
            }
        }

        if (!destinationFound) {
            displayMessage("ERROR","destination_not_found");
            return;
        } else {
            // check if the the drone has fuel to make it to destination and back, and so on
        }

    }

    /*
    First check if the drone exists under that service, then if the ingredient exists.
    If both are true, then check if the drone has enough space to hold the ingredient.
    If it does, then add the ingredient to the drone's payload and update the space left.
     */
    void loadIngredient(String serviceName, Integer tag, String barcode, Integer quantity, Integer unitPrice) {

    }

    /*
    First check if the drone exists under that service, then if the drone is at the home base.
    If the drone is at the home base, then update the fuel of the drone.
     */
    void loadFuel(String serviceName, Integer tag, Integer petrol) {

    }

    void purchaseIngredient(String restaurantName, String serviceName, Integer tag,
                            String barcode, Integer quantity) {

    }

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

    void displayMessage(String status, String text_output) {
        System.out.println(status.toUpperCase() + ":" + text_output.toLowerCase());
    }

    void addIngredient(Ingredient newIngredient) {
        if (ingredients.size() == 0) {
            ingredients.add(newIngredient);
        } else {
            boolean added = false;
            for (int i = 0; i < ingredients.size(); i++) {
                if (newIngredient.getName().compareTo(ingredients.get(i).getName()) < 0) {
                    ingredients.add(i, newIngredient);
                    added = true;
                    break;
                }
            }

            if (!added) {
                ingredients.add(newIngredient);
            }
        }
    }

    void addDeliveryService(DeliveryService newService) {
        if (services.size() == 0) {
            services.add(newService);
        } else {
            boolean added = false;
            for (int i = 0; i < services.size(); i++) {
                if (newService.getName().compareTo(services.get(i).getName()) < 0) {
                    services.add(i, newService);
                    added = true;
                    break;
                }
            }

            if (!added) {
                services.add(newService);
            }
        }
    }

    void addDrone(Drone newDrone) {
        if (drones.size() == 0) {
            drones.add(newDrone);
        } else {
            boolean added = false;
            for (int i = 0; i < drones.size(); i++) {
                if (newDrone.getTag().compareTo(drones.get(i).getTag()) <= 0) {
                    drones.add(i, newDrone);
                    added = true;
                    break;
                }
            }

            if (!added) {
                drones.add(newDrone);
            }
        }
    }


}
