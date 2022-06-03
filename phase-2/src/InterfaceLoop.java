import java.util.ArrayList;
import java.util.Scanner;

public class InterfaceLoop {

    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<DeliveryService> services = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Drone> drones = new ArrayList<>();

    InterfaceLoop() { }

    void makeIngredient(String init_barcode, String init_name, Integer init_weight) {
        Ingredient ingredient = new Ingredient(init_barcode, init_name, init_weight);
        ingredients.add(ingredient);
    }

    void displayIngredients() {
        for (Ingredient ingredient : ingredients) {
            System.out.printf("Barcode: %s, Name: %s, Weight: %d%n",ingredient.getInit_barcode(),
                    ingredient.getInit_name(), ingredient.getInit_weight());
        }
    }

    void makeLocation(String init_name, Integer init_x_coord, Integer init_y_coord, Integer init_space_limit) {
        Location location = new Location(init_name, init_x_coord, init_y_coord, init_space_limit);
        locations.add(location);
    }

    void displayLocations() {
        for (Location location : locations) {
            System.out.printf("Name: %s, Coordinates: (%d, %d), Capacity: %d, Spaces Remaining: [%d / %d]%n",
                    location.getInit_name(), location.getInit_x(), location.getInit_y(),
                    location.getInit_space_limit(), location.getSpaces_left(), location.getInit_space_limit());
        }
    }

    void checkDistance(String departure_point, String arrival_point) {
        boolean departureFound = false;
        boolean arrivalFound = false;
        Location departureLocation = null;
        Location arrivalLocation = null;
        for (Location location : locations) {
            if (location.getInit_name().equals(departure_point)) {
                departureFound = true;
                departureLocation = location;
            }
            if (location.getInit_name().equals(arrival_point)) {
                arrivalFound = true;
                arrivalLocation = location;
            }
        }

        if (!departureFound) {
            System.out.println("ERROR: Invalid departure location specified.");
        }
        if (!arrivalFound) {
            System.out.println("ERROR: Invalid destination location specified.");
        }

        int distance = departureLocation.calculateDistance(arrivalLocation);
        System.out.printf("Distance: %d%n", distance);

    }

    void makeDeliveryService(String init_name, Integer init_revenue, String located_at) {
        boolean found = false;
        for (Location location : locations) {
            if (location.getInit_name().equals(located_at)) {
                found = true;
                DeliveryService service = new DeliveryService(init_name, init_revenue, located_at);
                services.add(service);
            }
        }

        if (!found) {
            System.out.println("ERROR: Location not found.");
        }
    }

    void displayServices() {
        for (DeliveryService deliveryService : services) {
            System.out.printf("Name: %s, Revenue: $%d, Location: %s%n", deliveryService.getInit_name(),
                    deliveryService.getInit_revenue(), deliveryService.getLocated_at());
        }
    }

    void makeRestaurant(String init_name, String located_at) {
        boolean found = false;
        for (Location location : locations) {
            if (location.getInit_name().equals(located_at)) {
                found = true;
                Restaurant restaurant = new Restaurant(init_name, located_at);
                restaurants.add(restaurant);
            }
        }

        if (!found) {
            System.out.println("ERROR: Location not found.");
        }
    }

    void displayRestaurants() {
        for (Restaurant restaurant : restaurants) {
            System.out.printf("Name: %s, Location: %s%n, Total Spent: $%d", restaurant.getInit_name(),
                    restaurant.getLocated_at(), restaurant.getInit_spending());
        }
    }

    void makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel) {
        boolean found = false;
        for (DeliveryService service : services) {
            if (service.getInit_name().equals(service_name)) {
                found = true;
                Drone drone = new Drone(service_name, init_tag, init_capacity, init_fuel);
                drones.add(drone);
            }
        }

        if (!found) {
            System.out.println("ERROR: Service not found.");
        }
    }

    void displayDrones(String service_name) {
        for (Drone drone : drones) {
            if (drone.getService_name().equals(service_name)) {
                System.out.printf("Tag: %d, Capacity: %d, Fuel: %d%n", drone.getInit_tag(),
                        drone.getInit_capacity(), drone.getInit_fuel());
            }
        }
    }

    void displayAllDrones() {
        for (Drone drone : drones) {
            System.out.printf("Tag: %d, Capacity: %d, Fuel: %d%n", drone.getInit_tag(),
                    drone.getInit_capacity(), drone.getInit_fuel());
        }
    }

    /*
    first check if the drone exists under that service, then if destination exists.
    If both are true, then check if the drone has enough fuel to make it to the destination AND back to home base.
    If it does, then check if the destination has enough space to hold the drone.
    If it does, then fly the drone to the location and update the fuel and location of the drone.
     */
    void flyDrone(String service_name, Integer drone_tag, String destination_name) {
        boolean droneFound = false;
        boolean destinationFound = false;
        for (Drone drone : drones) {
            if (drone.getInit_tag().equals(drone_tag) && drone.getService_name().equals(service_name)) {
                droneFound = true;
                break;
            }
        }

        if (!droneFound) {
            System.out.println("ERROR: Drone with specified information not found.");
        } else {
            for (Location location : locations) {
                if (location.getInit_name().equals(destination_name)) {
                    destinationFound = true;
                    break;
                }
            }
        }

        if (!destinationFound) {
            System.out.println("ERROR: Destination not found.");
        } else {
            // check if the the drone has fuel to make it to destination and back, and so on
        }

    }

    /*
    First check if the drone exists under that service, then if the ingredient exists.
    If both are true, then check if the drone has enough space to hold the ingredient.
    If it does, then add the ingredient to the drone's payload and update the space left.
     */
    void loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price) {

    }

    /*
    First check if the drone exists under that service, then if the drone is at the home base.
    If the drone is at the home base, then update the fuel of the drone.
     */
    void loadFuel(String service_name, Integer drone_tag, Integer petrol) {

    }

    void purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag,
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
}
