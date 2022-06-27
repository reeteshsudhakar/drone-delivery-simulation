import java.util.TreeMap;

/**
 * Delivery Service class to handle the delivery of ingredients to restaurants.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class DeliveryService implements Comparable <DeliveryService> {
    // Object attributes
    private String name;
    private Integer revenue;
    private Location locatedAt;
    private TreeMap<Integer, Drone> drones;
    private Manager manager = null;

    /**
     * Constructor for DeliveryService class.
     * @param init_name name of the Delivery Service.
     * @param init_revenue initial revenue of the Delivery Service.
     * @param location location of the Delivery Service.
     */
    public DeliveryService(String init_name, Integer init_revenue, Location location) {
        this.name = init_name;
        this.revenue = init_revenue;
        this.locatedAt = location;
        this.drones = new TreeMap<>();
    }

    /**
     * Getter for the name of the Delivery Service.
     * @return name of the Delivery Service.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the location of the Delivery Service.
     * @return location of the Delivery Service.
     */
    public Location getLocation() {
        return this.locatedAt;
    }

    /**
     * Getter for the drones under a delivery service.
     * @return collection of drones associated with a delivery service.
     */
    public TreeMap<Integer, Drone> getDrones() {
        return this.drones;
    }

    /**
     * Method to collect the sales of a drone from a delivery service.
     */
    public void collectDroneSales() {
        for (Drone drone : this.getDrones().values()) {
            this.revenue += drone.getSales();
            drone.clearSales();
        }
    }

    /**
     * Override to string method to print the info about the Delivery Service.
     * @return string representation of the Delivery Service.
     */
    @Override
    public String toString() {
        return String.format("Name: %s, Revenue: $%d, Location: %s", this.name, this.revenue, this.locatedAt.getName());
    }

    /**
     * Override compareTo method to compare the Delivery Services for sorting in their TreeMap.
     * @param other Delivery Service to compare with.
     * @return integer value representing the comparison.
     */
    @Override
    public int compareTo(DeliveryService other) {
        return this.name.compareTo(other.name);
    }

    public static void makeDeliveryService(String name, Integer revenue, String locatedAt,
                                           TreeMap<String, Location> locations,
                                           TreeMap<String, DeliveryService> services) {
        // checking if the service already exists
        if (services.containsKey(name)) {
            Display.displayMessage("ERROR", "service_already_exists");
            return;
        }

        // checking if the revenue is valid (positive) and whether the passed in arguments are valid
        if (revenue < 0) {
            Display.displayMessage("ERROR", "service_revenue_must_be_greater_than_or_equal_to_zero");
            return;
        } else if (name == null || name.equals("")) {
            Display.displayMessage("ERROR", "service_name_must_not_be_empty");
            return;
        } else if (locatedAt == null || locatedAt.equals("")) {
            Display.displayMessage("ERROR", "service_located_at_must_not_be_empty");
            return;
        }

        // creating the service and adding it to the collection IF the location exists in the system
        // if the location does not exist in the system, display an error message
        if (locations.containsKey(locatedAt)) {
            DeliveryService newService = new DeliveryService(name, revenue, locations.get(locatedAt));
            services.put(name, newService);
            Display.displayMessage("OK","service_created");
        } else {
            Display.displayMessage("ERROR", "location_identifier_does_not_exist");
        }

    }

    public static void hireWorker(String user_name, String service_name, Person tempPerson, DeliveryService employer, TreeMap<String, Person> people) {
        // Replaces Person object in TreeMap iff they are not a Manager or Pilot
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_a_service");
        } else if (tempPerson instanceof Pilot) {
            Pilot tempPilot = (Pilot) tempPerson;
            if (tempPilot.pilotedDrones.size() > 0) {
                Display.displayMessage("ERROR", "employee_is_piloting_drones_for_a_service");
            } else {
                tempPilot.getEmployers().clear();
                tempPilot.addEmployer(employer);
                Display.displayMessage("OK","new_employee_has_been_hired");
            }
        } else {
            Worker hiredWorker;
            if (tempPerson instanceof Worker) {
                hiredWorker = (Worker) tempPerson;
                if (hiredWorker.getEmployers().containsKey(service_name)) {
                    Display.displayMessage("ERROR", "employee_already_works_for_service");
                    return;
                }
                hiredWorker.addEmployer(employer);
            } else {
                // Object retrieved from TreeMap is a person, so a Worker object needs to be deep copied
                hiredWorker = new Worker(tempPerson, employer);
                people.put(user_name, hiredWorker);
            }
            Display.displayMessage("OK", "new_employee_has_been_hired");
        }
    }

    public static void fireWorker(String user_name, String service_name, TreeMap<String, DeliveryService> services, TreeMap<String, Person> people, Person firedPerson) {
        // Fires a worker iff they are a worker and if they work for the delivery service provided
        if (firedPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_a_service");
        } else if (firedPerson instanceof Pilot && !((Pilot) firedPerson).getPilotedDrones().isEmpty()) {
            Display.displayMessage("ERROR","employee_is_piloting_drones_for_service");
        } else if (firedPerson instanceof Pilot) {
            ((Pilot) firedPerson).getEmployers().remove(service_name);
            Display.displayMessage("OK", "employee_has_been_fired");
        } else if (firedPerson instanceof Worker) { //If person is a Worker, removes employer from list of employers if they exist
            Worker firedWorker = (Worker) firedPerson;
            DeliveryService employer = services.get(service_name);
            if (!firedWorker.getEmployers().containsValue(employer)) {
                Display.displayMessage("ERROR", "employee_does_not_work_for_service");
                return;
            }
            firedWorker.removeEmployer(employer);
            if (firedWorker.getEmployers().isEmpty()) {
                Person newPerson = new Person(firedPerson.getUsername(), firedPerson.getFname(),
                        firedPerson.getLname(), firedPerson.getYear(), firedPerson.getMonth(),
                        firedPerson.getDate(), firedPerson.getAddress());
                people.put(user_name, newPerson);
            }
            Display.displayMessage("OK", "employee_has_been_fired");
        } else {
            Display.displayMessage("ERROR","person_is_currently_unemployed");
        }
    }

    public static void appointManager(String user_name, String service_name, Person tempPerson, DeliveryService employer, TreeMap<String, Person> people) {
        //Appoints a manager iff they are a worker at the delivery service
        if (tempPerson instanceof Pilot) {
            Display.displayMessage("ERROR", "pilot_cannot_become_manager");
            return;
        }
        if (tempPerson instanceof Manager && ((Manager) tempPerson).getEmployers().firstKey().equals(service_name)) {
            Display.displayMessage("ERROR","employee_is_already_managing_service");
            return;
        }
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_another_service");
            return;
        }
        if (tempPerson instanceof Worker) {
            Worker tempWorker = (Worker) tempPerson;
            if (tempWorker.getEmployers().containsValue(employer)) {
                // Worker can only work at one delivery service if they are to become a manager
                if (tempWorker.getEmployers().size() > 1) {
                    Display.displayMessage("ERROR", "employee_is_working_at_other_companies");
                    return;
                }
                Manager newManager = new Manager(tempWorker, employer);
                people.put(user_name, newManager);
                employer.setManager(newManager);
                Display.displayMessage("OK", "employee_has_been_appointed_manager");
            } else {
                Display.displayMessage("ERROR", "employee_does_not_work_for_this_delivery_service");
            }
        }
    }

    public static void trainPilot(String user_name, String service_name, Person tempPerson, DeliveryService employer, TreeMap<String, Person> people, String init_license, int init_experience) {
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_too_busy_managing");
        } else if (tempPerson instanceof Pilot) {
            Pilot tempPilot = (Pilot) tempPerson;
            if (!tempPilot.getEmployers().isEmpty() && !tempPilot.getEmployers().firstKey().equals(service_name) && !((Pilot) tempPerson).getPilotedDrones().isEmpty()) {
                Display.displayMessage("ERROR", "employee_is_already_piloting_drones_for_another_service");
            } else {
                tempPilot.changeEmployer(service_name, employer, init_license, init_experience);
                Display.displayMessage("OK","pilot_has_been_trained");
            }
        } else if (tempPerson instanceof Worker) {
            Worker tempWorker = (Worker) tempPerson;
            if (tempWorker.getEmployers().containsValue(employer)) {
                if (employer.getManager() != null) {
                    Pilot newPilot = new Pilot(tempWorker, employer, init_license, init_experience);
                    people.put(user_name, newPilot);
                    Display.displayMessage("OK", "pilot_has_been_trained");
                } else {
                    Display.displayMessage("ERROR", "delivery_service_does_not_have_a_manager");
                }
            } else {
                Display.displayMessage("ERROR", "employee_does_not_work_for_delivery_service");
            }
        } else {
            Display.displayMessage("ERROR","person_is_currently_unemployed");
        }
    }

    public static void appointPilot(String user_name, String service_name, Person tempPerson, DeliveryService employer, int drone_tag, TreeMap<String, DeliveryService> services) {
        if (tempPerson instanceof Pilot) {
            Pilot appointedPilot = (Pilot) tempPerson;
            if (appointedPilot.getEmployers().containsValue(employer)) {
                Drone drone = services.get(service_name).getDrones().get(drone_tag);
                if (drone == null) {
                    Display.displayMessage("ERROR", "drone_does_not_exist");
                } else if (drone.hasPilot()) {
                    if (drone.getPilot().getUsername().equals(user_name)) {
                        Display.displayMessage("ERROR","employee_has_already_been_appointed_pilot_for_this_drone");
                        return;
                    }
                    drone.getPilot().getPilotedDrones().remove(drone.getTag());
                    drone.assignPilot(appointedPilot);
                    employer.getDrones().put(drone_tag, drone);
                    appointedPilot.getPilotedDrones().put(drone_tag, drone);
                    Display.displayMessage("OK", "employee_has_been_appointed_pilot");
                } else if (drone.hasLeader()) {
                    drone.getLeader().getFollowers().remove(drone.getTag());
                    drone.assignPilot(appointedPilot);
                    appointedPilot.getPilotedDrones().put(drone_tag, drone);
                    Display.displayMessage("OK", "employee_has_been_appointed_pilot");
                } else {
                    drone.assignPilot(appointedPilot);
                    employer.getDrones().put(drone_tag, drone);
                    appointedPilot.getPilotedDrones().put(drone_tag, drone);
                    Display.displayMessage("OK", "employee_has_been_appointed_pilot");
                }
            } else {
                Display.displayMessage("ERROR", "pilot_does_not_work_for_delivery_service");
            }
        } else {
            Display.displayMessage("ERROR", "person_is_not_a_pilot");
        }
    }

    /**
     * This method collects all the revenue in a delivery service
     * @param service_name A valid service name
     * @param services The map of service names to delivery service objects
     */
    public static void collectRevenue(String service_name, TreeMap<String, DeliveryService> services) {
        if (service_name == null || services == null) {
            return;
        }
        DeliveryService service = services.get(service_name);
        service.collectDroneSales();
        Display.displayMessage("OK", "change_completed");
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}