import java.util.Scanner;
import java.util.TreeMap;

/**
 * Ingredient delivery service system for restaurants
 * to purchase ingredients via drones operated by a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class InterfaceLoop {

    // collections of objects for the interface loop
    TreeMap<String, Location> locations = new TreeMap<>();
    TreeMap<String, Ingredient> ingredients = new TreeMap<>();
    TreeMap<String, DeliveryService> services = new TreeMap<>();
    TreeMap<String, Restaurant> restaurants = new TreeMap<>();
    TreeMap<String, Person> people = new TreeMap<>();

    InterfaceLoop() { }

    /**
     * Method to make ingredients for delivery services to sell.
     * @param barcode the barcode of the ingredient
     * @param name the name of the ingredient
     * @param weight the unit weight of the ingredient
     */
    void makeIngredient(String barcode, String name, Integer weight) {
        Ingredient.makeIngredient(barcode, name, weight, ingredients);
    }

    /**
     * Method to make locations for services and restaurants to be located at.
     * @param name the name of the location
     * @param x_coordinate the x-coordinate of the location
     * @param y_coordinate the y-coordinate of the location
     * @param spaceLimit the capacity of drones at the location
     */
    void makeLocation(String name, Integer x_coordinate, Integer y_coordinate, Integer spaceLimit) {
        Location.makeLocation(name, x_coordinate, y_coordinate, spaceLimit, locations);
    }

    /**
     * Method to check the distance between two specified locations in the system.
     * @param departurePoint the name of the departure location
     * @param arrivalPoint the name of the arrival location
     */
    void checkDistance(String departurePoint, String arrivalPoint) {
        Location.checkDistance(departurePoint, arrivalPoint, locations);
    }

    /**
     * Method to make a delivery service for the system.
     * @param name the name of the service
     * @param revenue the revenue of the service
     * @param locatedAt the name of the location the service is located at
     */
    void makeDeliveryService(String name, Integer revenue, String locatedAt) {
        DeliveryService.makeDeliveryService(name, revenue, locatedAt, locations, services);
    }

    /**
     * Method to make a restaurant for the system.
     * @param name the name of the restaurant
     * @param locatedAt the name of the location the restaurant is located at
     */
    void makeRestaurant(String name, String locatedAt) {
        Restaurant.makeRestaurant(name, locatedAt, restaurants, locations);
    }

    /**
     * Method to make a drone for the system.
     * @param serviceName the name of the service the drone is assigned to
     * @param tag the tag of the drone (unique for drones in one given service)
     * @param capacity the number of units of ingredients the drone can carry
     * @param fuel the fuel of the drone
     */
    void makeDrone(String serviceName, Integer tag, Integer capacity, Integer fuel) {
        Drone.makeDrone(serviceName, tag, capacity, fuel, services);
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
    void makePerson(String init_username, String init_fname, String init_lname,
                    Integer init_year, Integer init_month, Integer init_date, String init_address) {
        Person.makePerson(init_username, init_fname, init_lname,
                init_year, init_month, init_date, init_address, people);
    }

    /**
     * Method to hire a person for a service as a worker
     * @param service_name the name of the service
     * @param user_name the username of the person to be hired as a worker
     */
    void hireWorker(String service_name, String user_name) {
        if (checkUserName(user_name) && checkServiceName(service_name)) {
            DeliveryService employer = services.get(service_name);
            Person temp = people.get(user_name);

            //Replaces Person object in TreeMap iff they are not a Manager or Pilot
            if (temp instanceof Manager) {
                Display.displayMessage("ERROR", "pilot_cannot_be_assigned_as_worker");
            } else if (temp instanceof Pilot) {
                Display.displayMessage("ERROR", "manager_cannot_be_assigned_as_worker");
            } else if (temp instanceof Worker) {
                Worker hiredWorker = (Worker) temp;
                hiredWorker.addEmployer(employer);
                Display.displayMessage("OK", "change_completed");
            } else {
                Worker hiredWorker = new Worker(temp, employer);
                people.put(user_name, hiredWorker);
                Display.displayMessage("OK", "change_completed");
            }
        } else {
            Display.displayMessage("ERROR", "invalid_username_or_service_name");
        }
    }

    /**
     * Method to fire a worker from a specified service
     * @param service_name the name of the service
     * @param user_name the username of the worker to be fired
     */
    void fireWorker(String service_name, String user_name) {
        if (checkUserName(user_name) && checkServiceName(service_name)) {
            Person firedPerson = people.get(user_name);

            if (firedPerson instanceof Manager) {
                Display.displayMessage("ERROR", "employee_is_managing_a_service");
            } else if (firedPerson instanceof Worker) { //If person is a Worker, removes employer from ArrayList of employers if they exist
                Worker firedWorker = (Worker) firedPerson;
                DeliveryService employer = services.get(service_name);
                if (!firedWorker.getEmployers().contains(employer)) {
                    Display.displayMessage("ERROR", "worker_does_not_work_for_service");
                } else {
                    firedWorker.removeEmployer(employer);
                    if (firedWorker.getEmployers().isEmpty()) {
                        Person newPerson = new Person(firedPerson.getUsername(), firedPerson.getFname(),
                                firedPerson.getLname(), firedPerson.getYear(), firedPerson.getMonth(),
                                firedPerson.getDate(), firedPerson.getAddress());
                        people.put(user_name, newPerson);
                    }
                    Display.displayMessage("OK", "change_completed");
                }
            } else {
                Display.displayMessage("ERROR", "username_not_found");
            }
        }
    }

    /**
     * Method to appoint a new manager to a one service
     * @param service_name the name of the service
     * @param user_name the name of the manager to be hired
     */
    void appointManager(String service_name, String user_name) {
        if (checkUserName(user_name) && checkServiceName(service_name)) {
            Person tempPerson = people.get(user_name);
            DeliveryService employer = services.get(service_name);
            if (tempPerson instanceof Pilot) {
                Display.displayMessage("ERROR", "pilot_cannot_become_manager");
            } else if (tempPerson instanceof Manager) {
                Display.displayMessage("ERROR", "manager_does_not_work_for_this_delivery_service");
            } else if (tempPerson instanceof Worker) {
                Worker tempWorker = (Worker) tempPerson;
                if (tempWorker.getEmployers().contains(employer)) {
                    if (tempWorker.getEmployers().size() > 1) {
                        Display.displayMessage("ERROR", "worker_has_multiple_employers_cannot_be_appointed");
                    } else {
                        Manager newManager = new Manager(tempWorker, employer);
                        people.put(user_name, newManager);
                        Display.displayMessage("OK", "change_completed");
                    }
                } else {
                    Display.displayMessage("ERROR", "worker_does_not_work_for_this_delivery_service");
                }
            }
        }
    }

    void trainPilot(String service_name, String user_name, String init_license, Integer init_experience) {
        if (init_experience == null || init_license == null || init_license.equals("")) {
            Display.displayMessage("ERROR", "invalid_arguments_entered");
        } else if (checkServiceName(service_name) && checkUserName(user_name)) {
            Person tempPerson = people.get(user_name);
            DeliveryService employer = services.get(service_name);
            if (tempPerson instanceof Manager) {
                Display.displayMessage("ERROR", "manager_cannot_train_to_be_pilot");
            } else if (tempPerson instanceof Pilot) {
                Display.displayMessage("ERROR", "pilot_already_trained");
            } else if (tempPerson instanceof Worker) {
                Worker tempWorker = (Worker) tempPerson;
                if (tempWorker.getEmployers().contains(employer)) {
                    if (employer.getManager() != null) {
                        Pilot newPilot = new Pilot(tempWorker, employer, init_license, init_experience);
                        people.put(user_name, newPilot);
                        Display.displayMessage("OK", "change_completed");
                    } else {
                        Display.displayMessage("ERROR", "delivery_service_does_not_have_a_manager");
                    }
                } else {
                    Display.displayMessage("ERROR", "worker_does_not_work_for_delivery_service");
                }
            }
        }
    }

    void appointPilot(String service_name, String user_name, Integer drone_tag) {
        if (checkServiceName(service_name) && checkUserName(user_name)) {
            Person tempPerson = people.get(user_name);
            DeliveryService employer = services.get(service_name);
            if (tempPerson instanceof Pilot) {
                Pilot appointedPilot = (Pilot) tempPerson;
                if (appointedPilot.getEmployers().contains(employer)) {
                    Drone drone = services.get(service_name).getDrones().get(drone_tag);
                    if (drone == null) {
                        Display.displayMessage("ERROR", "drone_does_not_exist");
                    } else if (drone instanceof LeaderDrone) {
                        ((LeaderDrone) drone).setPilot(appointedPilot);
                    } else if (drone instanceof FollowerDrone) {
                        ((FollowerDrone) drone).getLeaderDrone().getSwarm().remove(drone_tag);
                        drone = new LeaderDrone((FollowerDrone) drone, appointedPilot);
                        employer.getDrones().put(drone_tag, drone);
                        Display.displayMessage("OK", "change_completed");
                    } else {
                        drone = new LeaderDrone(drone, appointedPilot);
                        employer.getDrones().put(drone_tag, drone);
                        Display.displayMessage("OK", "change_completed");
                    }
                } else {
                    Display.displayMessage("ERROR", "pilot_does_not_work_for_delivery_service");
                }
            } else {
                Display.displayMessage("ERROR", "person_is_not_a_pilot");
            }
        }
    }

    /*
     * TODO: execute IFF the drone, individual or leader of swarm, has a pilot with valid license
     * TODO: do not fly drone if one drone in the swarm cannot make it to destination or back to base
     */

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

        // TODO Move flight eligibility checks into separate method
        if (services.containsKey(serviceName)) {
            DeliveryService service = services.get(serviceName);
            if (service.getDrones().containsKey(tag)) {
                movedDrone = service.getDrones().get(tag);
            }
        }

        // if the drone does not exist in the system, display an error message
        if (movedDrone == null) {
            Display.displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else {
            if (locations.containsKey(destination)) {
                destinationLocation = locations.get(destination);
            }
        }

        // if the destination location does not exist in the system, display an error message
        if (destinationLocation == null) {
           Display.displayMessage("ERROR","flight_destination_does_not_exist");
            return;
        }

        if (movedDrone instanceof FollowerDrone) {
            Display.displayMessage("ERROR", "drone_is_not_a_leader");
        } else if (movedDrone instanceof LeaderDrone) {
            LeaderDrone leadDrone = (LeaderDrone) movedDrone;
            if (leadDrone.getPilot() == null) {
                Display.displayMessage("ERROR", "drone_has_no_pilot");
            } else if (leadDrone.getPilot().getLicense() == null) {
                Display.displayMessage("ERROR", "pilot_has_no_license");
            }
            int distance = movedDrone.getCurrentLocation().calculateDistance(destinationLocation);
            int returnDistance = destinationLocation.calculateDistance(movedDrone.getHomeBase());
            if (distance > leadDrone.getFuel()) {
                Display.displayMessage("ERROR", "lead_drone_does_not_have_enough_fuel_to_reach_destination");
                return;
            } else if (distance + returnDistance > leadDrone.getFuel()) {
                Display.displayMessage("ERROR", "lead_drone_does_not_have_enough_fuel_to_return_to_home_base_from_destination");
                return;
            } else {
                for (Drone drone : leadDrone.getSwarm().values()) {
                    if (distance > drone.getFuel()) {
                        Display.displayMessage("ERROR", "drone_in_swarm_does_not_have_enough_fuel_to_reach_destination");
                        return;
                    } else if (distance + returnDistance > drone.getFuel()) {
                        Display.displayMessage("ERROR", "drone_in_swarm_does_not_have_enough_fuel_to_return_to_home_base_from_destination");
                        return;
                    }
                }

                if (destinationLocation.getSpacesLeft() == 0 || destinationLocation.getSpacesLeft() < leadDrone.getSwarm().size() + 1) {
                    Display.displayMessage("ERROR", "destination_location_does_not_have_space_for_swarm");
                    return;
                } else {
                    leadDrone.flyToDestination(destinationLocation);
                    for (Drone drone : leadDrone.getSwarm().values()) {
                        drone.flyToDestination(destinationLocation);
                    }
                    Display.displayMessage("OK", "change_completed");
                }
            }
        } else {
            Display.displayMessage("ERROR", "drone_does_not_have_a_pilot");
        }
    }

    void joinSwarm(String service_name, Integer lead_drone_tag, Integer swarm_drone_tag) {
        if (!services.containsKey(service_name)) {
            Display.displayMessage("ERROR","service_does_not_exist");
            return;
        }

        Drone leadDrone = services.get(service_name).getDrones().get(lead_drone_tag);
        Drone swarmDrone = services.get(service_name).getDrones().get(swarm_drone_tag);
        if (leadDrone == null) {
            Display.displayMessage("ERROR","lead_drone_does_not_exist");
            return;
        } else if (swarmDrone == null) {
            Display.displayMessage("ERROR","swarm_drone_does_not_exist");
            return;
        }

        if (leadDrone.getCurrentLocation() != swarmDrone.getCurrentLocation()) {
            Display.displayMessage("ERROR", "lead_and_swarm_drone_must_be_at_same_location");
        }

        if (swarmDrone instanceof LeaderDrone) {
            if (!((LeaderDrone) swarmDrone).getSwarm().isEmpty()) {
                Display.displayMessage("ERROR", "swarm_drone_already_has_a_swarm");
            } else {
                if (leadDrone instanceof LeaderDrone) {
                    ((LeaderDrone) swarmDrone).getPilot().getPilotedDrones().remove(swarm_drone_tag);
                    swarmDrone = new FollowerDrone((LeaderDrone) swarmDrone, (LeaderDrone) leadDrone);
                    services.get(service_name).getDrones().put(swarm_drone_tag, swarmDrone);
                    Display.displayMessage("OK", "change_completed");
                } else if (leadDrone instanceof FollowerDrone) {
                    Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
                } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                    Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
                }
            }
        } else if (swarmDrone instanceof FollowerDrone) {
            if (leadDrone instanceof LeaderDrone) {
                if (((FollowerDrone) swarmDrone).getLeaderDrone() != null) {
                    ((FollowerDrone) swarmDrone).getLeaderDrone().getSwarm().remove(swarm_drone_tag);
                }
                ((LeaderDrone) leadDrone).getSwarm().put(swarm_drone_tag, swarmDrone);
                Display.displayMessage("OK", "change_completed");
            } else if (leadDrone instanceof FollowerDrone) {
                Display.displayMessage("ERROR", "lead_drone_is_following_another_swarm");
            } else { // the passed in lead drone tag is just a normal drone, so it doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        } else { // the passed in swarm drone tag is just a normal drone, so it doesn't have a pilot
            if (leadDrone instanceof LeaderDrone) {
                swarmDrone = new FollowerDrone(swarmDrone, (LeaderDrone) leadDrone);
                services.get(service_name).getDrones().put(swarm_drone_tag, swarmDrone);
            } else { // the lead drone is also just a normal drone and doesn't have a pilot
                Display.displayMessage("ERROR", "lead_drone_does_not_have_pilot_to_lead_swarm");
            }
        }
    }

    void leaveSwarm(String service_name, Integer swarm_drone_tag) {
        if (!services.containsKey(service_name)) {
            Display.displayMessage("ERROR","service_does_not_exist");
            return;
        }
        Drone swarmDrone = services.get(service_name).getDrones().get(swarm_drone_tag);

        if (swarmDrone == null) {
            Display.displayMessage("ERROR", "swarm_drone_does_not_exist");
        } else if (swarmDrone instanceof LeaderDrone) {
            Display.displayMessage("ERROR", "drone_is_not_a_follower_drone");
        } else if (swarmDrone instanceof FollowerDrone) {
            ((FollowerDrone) swarmDrone).getLeaderDrone().getSwarm().remove(swarm_drone_tag);
            ((FollowerDrone) swarmDrone).setLeaderDrone(null);
            Display.displayMessage("OK", "change_completed");
        } else {
            Display.displayMessage("ERROR", "drone_is_not_a_follower_drone");
        }
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
            Display.displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else { // if the drone exists in the system, check if the ingredient exists in the system
            if (ingredients.containsKey(barcode)) {
                loadIngredient = ingredients.get(barcode);
            }
        }

        // if the ingredient does not exist in the system, display an error message
        if (loadIngredient == null) {
            Display.displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // checking if the drone is at the service's home base
        if (!loadDrone.getCurrentLocation().equals(loadDrone.getHomeBase())) {
            Display.displayMessage("ERROR","drone_not_located_at_home_base");
            return;
        }

        if (noWorkersExist()) {
            Display.displayMessage("ERROR","no_worker_present_to_load_ingredient");
            return;
        }

        // checking if the quantity and price of the ingredient are valid
        if (quantity <= 0) {
            Display.displayMessage("ERROR","quantity_must_be_greater_than_zero");
            return;
        } else if (unitPrice <= 0) {
            Display.displayMessage("ERROR","unit_price_must_be_greater_than_zero");
            return;
        }

        // load the ingredient and track quantity and price using a package if there is space to load it
        if (loadDrone.getRemainingCapacity() == 0) {
            Display.displayMessage("ERROR","no_capacity_left_to_load_more_packages");
        } else if (loadDrone.getRemainingCapacity() < quantity) {
            Display.displayMessage("ERROR","not_enough_capacity_to_hold_new_packages");
        } else {
            loadDrone.addToPayload(loadIngredient, barcode, quantity, unitPrice);
            Display.displayMessage("OK","change_completed");
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
            Display.displayMessage("ERROR", "drone_does_not_exist");
            return;
        } else if (petrol <= 0) {
            Display.displayMessage("ERROR", "petrol_must_be_greater_than_zero");
            return;
        }

        // if the drone is at the service's home base, fill the drone with fuel
        if (!loadFuelDrone.getCurrentLocation().equals(loadFuelDrone.getHomeBase())) {
            Display.displayMessage("ERROR", "drone_not_located_at_home_base");
            return;
        }

        if (noWorkersExist()) {
            Display.displayMessage("ERROR", "no_worker_present_to_load_fuel");
            return;
        }

        loadFuelDrone.loadDroneFuel(petrol);
        Display.displayMessage("OK", "change_completed");
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
            Display.displayMessage("ERROR", "restaurant_identifier_does_not_exist");
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
            Display.displayMessage("ERROR","drone_does_not_exist");
            return;
        }

        // checking if the ingredient exists in the system
        boolean ingredientExists = ingredients.containsKey(barcode);

        // if the ingredient does not exist in the system, display an error message
        if (!ingredientExists) {
            Display.displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // if the drone is not at the restaurant's location, display an error message
        if (!buyerDrone.getCurrentLocation().equals(buyerRestaurant.getLocation())) {
            Display.displayMessage("ERROR","drone_not_located_at_restaurant");
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
            Display.displayMessage("ERROR","ingredient_not_found_in_payload");
            return;
        } else if (quantity <= 0) {
            Display.displayMessage("ERROR","quantity_requested_must_be_greater_than_zero");
            return;
        }

        // completing the purchase if the drone has enough of the ingredient requested for purchase
        if (buyerDrone.getPayload().get(buyerIngredient).getQuantity().compareTo(quantity) < 0) {
            Display.displayMessage("ERROR","drone_does_not_have_enough_of_ingredient_requested");
            return;
        } else {
            buyerRestaurant.makePurchase(buyerDrone, buyerIngredient, quantity);
            buyerDrone.completePurchase(buyerIngredient, quantity);
        }
        Display.displayMessage("OK","change_completed");
    }

    void collectRevenue(String service_name) {
        // checking if the service exists in the system
        DeliveryService service = null;
        if (services.containsKey(service_name)) {
            service = services.get(service_name);
        }

        if (service == null) {
            Display.displayMessage("ERROR","service_does_not_exist");
            return;
        }

        service.collectDroneSales();
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
                    Display.displayIngredients(ingredients.values());
                } else if (tokens[0].equals("make_location")) {
                    makeLocation(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_locations")) {
                    Display.displayLocations(locations.values());
                } else if (tokens[0].equals("check_distance")) {
                    checkDistance(tokens[1], tokens[2]);
                } else if (tokens[0].equals("make_service")) {
                    makeDeliveryService(tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                } else if (tokens[0].equals("display_services")) {
                    Display.displayServices(services.values());
                } else if (tokens[0].equals("make_restaurant")) {
                    makeRestaurant(tokens[1], tokens[2]);
                } else if (tokens[0].equals("display_restaurants")) {
                    Display.displayRestaurants(restaurants.values());
                } else if (tokens[0].equals("make_drone")) {
                    makeDrone(tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
                            Integer.parseInt(tokens[4]));
                } else if (tokens[0].equals("display_drones")) {
                    Display.displayDrones(tokens[1], services);
                } else if (tokens[0].equals("display_all_drones")) {
                    Display.displayAllDrones(services.values());
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
                } else if (tokens[0].equals("make_person")) {
                    makePerson(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]),
                            Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), tokens[7]);
                } else if (tokens[0].equals("display_persons")) {
                    Display.displayPersons(people.values());
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
     * Method to check if the username exists
     * @param user_name the username to check
     * @return true if the username exists, false otherwise
     */
    boolean checkUserName(String user_name) {
        if (people.containsKey(user_name)) {
            return true;
        } else {
            Display.displayMessage("ERROR", "username_does_not_exist");
            return false;
        }
    }

    /**
     * Method to check if the service exists
     * @param service_name the service to check
     * @return true if the service exists, false otherwise
     */
    boolean checkServiceName(String service_name) {
        if (services.containsKey(service_name)) {
            return true;
        } else {
            Display.displayMessage("ERROR", "service_does_not_exist");
            return false;
        }
    }

    /**
     * @return A boolean that is true if there are no workers at home base, and false otherwise
     */
    boolean noWorkersExist() {
        return people.values().stream().findAny().filter((item) -> (item instanceof Worker)).isEmpty();
    }
}
