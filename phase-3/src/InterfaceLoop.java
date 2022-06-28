import java.util.Scanner;

/**
 * Ingredient delivery service system for restaurants
 * to purchase ingredients via drones operated by a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class InterfaceLoop {

    InterfaceLoop() { }

    /**
     * Method to make ingredients for delivery services to sell.
     * @param barcode the barcode of the ingredient
     * @param name the name of the ingredient
     * @param weight the unit weight of the ingredient
     */
    private void makeIngredient(String barcode, String name, Integer weight) {
        Ingredient.makeIngredient(barcode, name, weight);
    }

    /**
     * Method to make locations for services and restaurants to be located at.
     * @param name the name of the location
     * @param x_coordinate the x-coordinate of the location
     * @param y_coordinate the y-coordinate of the location
     * @param spaceLimit the capacity of drones at the location
     */
    private void makeLocation(String name, Integer x_coordinate, Integer y_coordinate, Integer spaceLimit) {
        Location.makeLocation(name, x_coordinate, y_coordinate, spaceLimit);
    }

    /**
     * Method to check the distance between two specified locations in the system.
     * @param departurePoint the name of the departure location
     * @param arrivalPoint the name of the arrival location
     */
    private void checkDistance(String departurePoint, String arrivalPoint) {
        Location.checkDistance(departurePoint, arrivalPoint);
    }

    /**
     * Method to make a delivery service for the system.
     * @param name the name of the service
     * @param revenue the revenue of the service
     * @param locatedAt the name of the location the service is located at
     */
    private void makeDeliveryService(String name, Integer revenue, String locatedAt) {
        DeliveryService.makeDeliveryService(name, revenue, locatedAt);
    }

    /**
     * Method to make a restaurant for the system.
     * @param name the name of the restaurant
     * @param locatedAt the name of the location the restaurant is located at
     */
    private void makeRestaurant(String name, String locatedAt) {
        Restaurant.makeRestaurant(name, locatedAt);
    }

    /**
     * Method to make a drone for the system.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone (unique for drones in one given service)
     * @param capacity the number of units of ingredients the drone can carry
     * @param fuel the fuel of the drone
     */
    private void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel) {
        Drone.makeDrone(serviceName, tag, capacity, fuel);
    }

    /**
     * Method to make a person for the system.
     * @param init_username the username of the person
     * @param init_fname the first name of the person
     * @param init_lname the last name of the person
     * @param init_year the year of birth of the person
     * @param init_month the month of birth of the person
     * @param init_date the date of birth of the person
     * @param init_address the address of the person
     */
    private void makePerson(String init_username, String init_fname, String init_lname,
                    Integer init_year, Integer init_month, Integer init_date, String init_address) {
        Person.makePerson(init_username, init_fname, init_lname,
                init_year, init_month, init_date, init_address);
    }

    /**
     * Method to hire a person for a service as a worker
     * @param service_name the name of the service
     * @param user_name the username of the person to be hired as a worker
     */
    private void hireWorker(String service_name, String user_name) {
        if (Person.checkUserName(user_name) && DeliveryService.checkServiceName(service_name)) {
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.hireWorker(user_name);
        }
    }

    /**
     * Method to fire a worker from a specified service
     * @param service_name the name of the service
     * @param user_name the username of the worker to be fired
     */
    private void fireWorker(String service_name, String user_name) {
        if (Person.checkUserName(user_name) && DeliveryService.checkServiceName(service_name)) {
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.fireWorker(user_name);
        }
    }

    /**
     * Method to appoint a new manager to a service
     * @param service_name the name of the service
     * @param user_name the name of the manager to be hired
     */
    private void appointManager(String service_name, String user_name) {
        if (Person.checkUserName(user_name) && DeliveryService.checkServiceName(service_name)) {
            Person tempPerson = Person.people.get(user_name);
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.appointManager(user_name, tempPerson);
        }
    }

    /**
     * Method to convert a user to a pilot
     * @param service_name The name of the Delivery Service the pilot will work for
     * @param user_name The username of the person who will become a pilot
     * @param init_license A valid pilot's license
     * @param init_experience The amount of flying experience the pilot has
     */
    private void trainPilot(String service_name, String user_name, String init_license, Integer init_experience) {
        if (init_experience == null || init_license == null || init_license.equals("")) {
            Display.displayMessage("ERROR", "invalid_arguments_entered");
            return;
        }

        if (DeliveryService.checkServiceName(service_name) && Person.checkUserName(user_name)) {
            Person tempPerson = Person.people.get(user_name);
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.trainPilot(user_name, tempPerson, init_license, init_experience);
        }
    }

    private void appointPilot(String service_name, String user_name, Integer drone_tag) {
        if (DeliveryService.checkServiceName(service_name) && Person.checkUserName(user_name)) {
            Person tempPerson = Person.people.get(user_name);
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.appointPilot(user_name, service_name, tempPerson, drone_tag);
        }
    }

    /**
     * Method to fly a drone from one location to another.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param destination the name of the location the drone is flying to
     */
    private void flyDrone(String serviceName, Integer tag, String destination) {
        // checking if the drone exists in the system
        Drone movedDrone;

        if (!DeliveryService.checkServiceName(serviceName)) {
            return;
        }

        DeliveryService service = DeliveryService.services.get(serviceName);

        if (service.hasDrone(tag)) {
            movedDrone = service.getDrone(tag);
            movedDrone.flyDrone(destination);
        } else {
            // if the drone does not exist in the system, display an error message
            Display.displayMessage("ERROR", "drone_does_not_exist");
        }
    }

    /**
     * Have a drone join a swarm
     * @param service_name The Delivery Service that owes both drones
     * @param lead_drone_tag The tag of the drone that is leading the swarm to be joined
     * @param swarm_drone_tag The tag of the drone that is joining the swarm
     */
    private void joinSwarm(String service_name, Integer lead_drone_tag, Integer swarm_drone_tag) {
        if (!DeliveryService.checkServiceName(service_name)) {
            return;
        }

        Drone leadDrone = DeliveryService.findDrone(service_name, lead_drone_tag);
        Drone swarmDrone = DeliveryService.findDrone(service_name, swarm_drone_tag);
        if (swarmDrone == null) {
            Display.displayMessage("ERROR", "swarm_drone_does_not_exist");
            return;
        }
        swarmDrone.joinSwarm(leadDrone);
    }

    /**
     * This method is used to have a drone leave its swarm
     * @param service_name The name of the service that owns the drone leaving the swarm
     * @param swarm_drone_tag The tag of the drone leaving the swarm
     */

    private void leaveSwarm(String service_name, Integer swarm_drone_tag) {
        if (!DeliveryService.checkServiceName(service_name)) {
            return;
        }
        Drone swarmDrone = DeliveryService.findDrone(service_name, swarm_drone_tag);
        if (swarmDrone == null) {
            Display.displayMessage("ERROR", "swarm_drone_does_not_exist");
            return;
        }
        swarmDrone.leaveSwarm();
    }

    /**
     * Method to load a drone with ingredients.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param barcode the barcode of the ingredient
     * @param quantity the quantity of the ingredient to be loaded
     * @param unitPrice the price of the ingredient
     */
    private void loadIngredient(String serviceName, Integer tag, String barcode, Integer quantity, Integer unitPrice) {
        // checking if the drone exists in the system
        Drone loadDrone;
        DeliveryService service;
        if (DeliveryService.services.containsKey(serviceName)) {
            service = DeliveryService.services.get(serviceName);
            if (service.hasDrone(tag)) {
                loadDrone = service.getDrone(tag);
            } else {
                // if the drone does not exist in the system, display an error message
                Display.displayMessage("ERROR", "drone_does_not_exist");
                return;
            }
        } else {
            // if the service does not exist in the system, display an error message
            Display.displayMessage("ERROR", "service_does_not_exist");
            return;
        }

        if (service.noWorkersExist()) {
            Display.displayMessage("ERROR", "delivery_service_does_not_have_regular_workers");
            return;
        }

        // load the ingredient and track quantity and price using a package if there is space to load it
        if (loadDrone.getRemainingCapacity() == 0) {
            Display.displayMessage("ERROR","no_capacity_left_to_load_more_packages");
        } else if (loadDrone.getRemainingCapacity() < quantity) {
            Display.displayMessage("ERROR","not_enough_capacity_to_hold_new_packages");
        } else {
            loadDrone.addToPayload(barcode, quantity, unitPrice);
            Display.displayMessage("OK","change_completed");
        }
    }

    /**
     * Method to load a drone with fuel.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone
     * @param petrol the quantity of petrol to be loaded
     */
    private void loadFuel(String serviceName, Integer tag, Integer petrol) {
        // checking if the drone exists in the system
        Drone loadFuelDrone;

        if (DeliveryService.checkServiceName(serviceName)) {
            DeliveryService service = DeliveryService.services.get(serviceName);
            if (service.noWorkersExist()) {
                Display.displayMessage("ERROR", "delivery_service_does_not_have_regular_workers");
                return;
            }
            if (service.hasDrone(tag)) {
                loadFuelDrone = service.getDrone(tag);
                loadFuelDrone.loadFuel(petrol, service);
            } else {
                // If the drone does not exist in the service, display an error message
                Display.displayMessage("ERROR", "drone_does_not_exist");
            }
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
    private void purchaseIngredient(String restaurantName, String serviceName, Integer tag,
                            String barcode, Integer quantity) {
        // checking if the restaurant exists in the system
        Restaurant buyerRestaurant;
        if (Restaurant.restaurants.containsKey(restaurantName)) {
            buyerRestaurant = Restaurant.restaurants.get(restaurantName);
            buyerRestaurant.purchaseIngredient(tag, barcode, quantity, serviceName);
        } else {
            // if the restaurant does not exist in the system, display an error message
            Display.displayMessage("ERROR", "restaurant_identifier_does_not_exist");
        }
    }

    /**
     * This method collects all the revenue made by a delivery service
     * @param service_name The name of the delivery service
     */
    private void collectRevenue(String service_name) {
        // checking if the service exists in the system
        if (DeliveryService.checkServiceName(service_name)) {
            DeliveryService employer = DeliveryService.services.get(service_name);
            employer.collectRevenue(service_name);
        }
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
                    Display.displayIngredients();
                } else if (tokens[0].equals("make_location")) {
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_locations")) {
                    Display.displayLocations();
                } else if (tokens[0].equals("check_distance")) {
                    checkDistance(tokens[1], tokens[2]);
                } else if (tokens[0].equals("make_service")) {
                    makeDeliveryService(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                } else if (tokens[0].equals("display_services")) {
                    Display.displayServices();
                } else if (tokens[0].equals("make_restaurant")) {
                    makeRestaurant(tokens[1], tokens[2]);
                } else if (tokens[0].equals("display_restaurants")) {
                    Display.displayRestaurants();
                } else if (tokens[0].equals("make_drone")) {
                    makeDrone(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_drones")) {
                    Display.displayDrones(tokens[1]);
                } else if (tokens[0].equals("display_all_drones")) {
                    Display.displayAllDrones();
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
                } else if (tokens[0].equals("make_person")) {
                    makePerson(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]),
                            Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), tokens[7]);
                } else if (tokens[0].equals("display_persons")) {
                    Display.displayPersons();
                } else if (tokens[0].equals("hire_worker")) {
                    hireWorker(tokens[1], tokens[2]);
                } else if (tokens[0].equals("fire_worker")) {
                    fireWorker(tokens[1], tokens[2]);
                } else if (tokens[0].equals("appoint_manager")) {
                    appointManager(tokens[1], tokens[2]);
                } else if (tokens[0].equals("train_pilot")) {
                    trainPilot(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("appoint_pilot")) {
                    appointPilot(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("join_swarm")) {
                    joinSwarm(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                } else if (tokens[0].equals("leave_swarm")) {
                    leaveSwarm(tokens[1], Integer.parseInt(tokens[2]));
                } else if (tokens[0].equals("collect_revenue")) {
                    collectRevenue(tokens[1]);
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
}
