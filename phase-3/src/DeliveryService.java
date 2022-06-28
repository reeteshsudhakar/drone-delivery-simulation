import java.util.TreeMap;

/**
 * Delivery Service class to handle the delivery of ingredients to restaurants.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class DeliveryService implements Comparable <DeliveryService> {
    // collection of delivery services
    static TreeMap<String, DeliveryService> services = new TreeMap<>();

    // Object attributes
    private final String name;
    private Integer revenue;
    private final Location locatedAt;
    private final TreeMap<Integer, Drone> drones;
    private Manager manager = null;

    /**
     * Constructor for DeliveryService class.
     * @param name name of the Delivery Service.
     * @param revenue initial revenue of the Delivery Service.
     * @param location location of the Delivery Service.
     */
    public DeliveryService(String name, Integer revenue, Location location) {
        this.name = name;
        this.revenue = revenue;
        this.locatedAt = location;
        this.drones = new TreeMap<>();
    }

    public static void makeDeliveryService(String name, Integer revenue, String locatedAt) {
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
            Display.displayMessage("ERROR", "service_location_must_not_be_empty");
            return;
        }

        // creating the service and adding it to the collection IF the location exists in the system
        // if the location does not exist in the system, display an error message
        if (Location.locations.containsKey(locatedAt)) {
            DeliveryService newService = new DeliveryService(name, revenue, Location.locations.get(locatedAt));
            services.put(name, newService);
            Display.displayMessage("OK","delivery_service_created");
        } else {
            Display.displayMessage("ERROR", "location_identifier_does_not_exist");
        }

    }

    public void hireWorker(String username) {
        Person tempPerson = Person.people.get(username);
        // Replaces Person object in TreeMap iff they are not a Manager or Pilot
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_a_service");
        } else if (tempPerson instanceof Pilot) {
            Pilot tempPilot = (Pilot) tempPerson;
            if (tempPilot.pilotedDrones.size() > 0) {
                Display.displayMessage("ERROR", "employee_is_piloting_drones_for_a_service");
            } else {
                tempPilot.getEmployers().clear();
                tempPilot.addEmployer(this);
                Display.displayMessage("OK","new_employee_has_been_hired");
            }
        } else {
            Worker hiredWorker;
            if (tempPerson instanceof Worker) {
                hiredWorker = (Worker) tempPerson;
                if (hiredWorker.getEmployers().containsKey(this.getName())) {
                    Display.displayMessage("ERROR", "employee_already_works_for_service");
                    return;
                }
                hiredWorker.addEmployer(this);
            } else {
                // Object retrieved from TreeMap is a person, so a Worker object needs to be deep copied
                hiredWorker = new Worker(tempPerson, this);
                Person.people.put(username, hiredWorker);
            }
            Display.displayMessage("OK", "new_employee_has_been_hired");
        }
    }

    public void fireWorker(String username) {
        // Fires a worker iff they are a worker and if they work for the delivery service provided
        Person firedPerson = Person.people.get(username);
        if (firedPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_a_service");
        } else if (firedPerson instanceof Pilot && !((Pilot) firedPerson).getPilotedDrones().isEmpty()) {
            Display.displayMessage("ERROR","employee_is_piloting_drones_for_service");
        } else if (firedPerson instanceof Pilot) {
            ((Pilot) firedPerson).getEmployers().remove(this.getName());
            Display.displayMessage("OK", "employee_has_been_fired");
        } else if (firedPerson instanceof Worker) { //If person is a Worker, removes employer from list of employers if they exist
            Worker firedWorker = (Worker) firedPerson;
            if (!firedWorker.getEmployers().containsValue(this)) {
                Display.displayMessage("ERROR", "employee_does_not_work_for_service");
                return;
            }
            firedWorker.removeEmployer(this);
            if (firedWorker.getEmployers().isEmpty()) {
                Person newPerson = new Person(firedPerson.getUsername(), firedPerson.getFirstName(),
                        firedPerson.getLastName(), firedPerson.getYear(), firedPerson.getMonth(),
                        firedPerson.getDate(), firedPerson.getAddress());
                Person.people.put(username, newPerson);
            }
            Display.displayMessage("OK", "employee_has_been_fired");
        } else {
            Display.displayMessage("ERROR","person_is_currently_unemployed");
        }
    }

    public void appointManager(String username, Person tempPerson) {
        //Appoints a manager iff they are a worker at the delivery service
        if (tempPerson instanceof Pilot) {
            Display.displayMessage("ERROR", "pilot_cannot_become_manager");
            return;
        }
        if (tempPerson instanceof Manager && ((Manager) tempPerson).getEmployers().firstKey().equals(this.name)) {
            Display.displayMessage("ERROR","employee_is_already_managing_service");
            return;
        }
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_managing_another_service");
            return;
        }
        if (tempPerson instanceof Worker) {
            Worker tempWorker = (Worker) tempPerson;
            if (tempWorker.getEmployers().containsValue(this)) {
                // Worker can only work at one delivery service if they are to become a manager
                if (tempWorker.getEmployers().size() > 1) {
                    Display.displayMessage("ERROR", "employee_is_working_at_other_companies");
                    return;
                }
                Manager newManager = new Manager(tempWorker, this);
                Person.people.put(username, newManager);
                this.setManager(newManager);
                Display.displayMessage("OK", "employee_has_been_appointed_manager");
            } else {
                Display.displayMessage("ERROR", "employee_does_not_work_for_this_delivery_service");
            }
        }
    }

    public void trainPilot(String username, Person tempPerson, String license, int experience) {
        if (tempPerson instanceof Manager) {
            Display.displayMessage("ERROR", "employee_is_too_busy_managing");
        } else if (tempPerson instanceof Pilot) {
            Pilot tempPilot = (Pilot) tempPerson;
            if (tempPilot.pilotingForAnotherService(this)) {
                Display.displayMessage("ERROR", "employee_is_already_piloting_drones_for_another_service");
            } else {
                tempPilot.changeEmployer(this.getName(), this, license, experience);
                Display.displayMessage("OK","pilot_has_been_trained");
            }
        } else if (tempPerson instanceof Worker) {
            Worker tempWorker = (Worker) tempPerson;
            if (tempWorker.getEmployers().containsKey(this.getName())) {
                if (this.getManager() != null) {
                    Pilot newPilot = new Pilot(tempWorker, this, license, experience);
                    Person.people.put(username, newPilot);
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

    public void appointPilot(String username, String serviceName, Person tempPerson, Integer droneTag) {
        if (tempPerson instanceof Pilot) {
            Pilot appointedPilot = (Pilot) tempPerson;
            if (appointedPilot.getEmployers().containsValue(this)) {
                Drone drone = services.get(serviceName).getDrone(droneTag);
                if (drone == null) {
                    Display.displayMessage("ERROR", "drone_does_not_exist");
                } else if (drone.hasPilot()) {
                    if (drone.getPilot().getUsername().equals(username)) {
                        Display.displayMessage("ERROR","employee_has_already_been_appointed_pilot_for_this_drone");
                        return;
                    }
                    drone.switchPilot(appointedPilot);
                    Display.displayMessage("OK", "employee_has_been_appointed_pilot");
                } else if (drone.hasLeader()) {
                    drone.becomeLeader(appointedPilot);
                    Display.displayMessage("OK", "employee_has_been_appointed_pilot");
                } else {
                    drone.assignPilot(appointedPilot);
                    this.getDrones().put(droneTag, drone);
                    appointedPilot.getPilotedDrones().put(droneTag, drone);
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
     * @param serviceName A valid service name
     */
    public void collectRevenue(String serviceName) {
        if (serviceName == null || services == null) {
            return;
        }
        DeliveryService service = services.get(serviceName);
        for (Drone drone : service.getDrones().values()) {
            service.revenue += drone.getSales();
            drone.clearSales();
        }
        Display.displayMessage("OK", "change_completed");
    }

    /**
     * @return A boolean that is true if there are no workers at home base, and false otherwise
     */
    public boolean noWorkersExist() {
        return Person.people.values().stream().findAny().filter((item) -> (item instanceof Worker))
                .filter((item) -> (((Worker) item).getEmployers().containsKey(this.getName()))).isEmpty();
    }

    /**
     * Method to check if the service exists
     * @param serviceName the service to check
     * @return true if the service exists, false otherwise
     */
    public static boolean checkServiceName(String serviceName) {
        if (services.containsKey(serviceName)) {
            return true;
        } else {
            Display.displayMessage("ERROR", "service_does_not_exist");
            return false;
        }
    }

    /**
     * Override to string method to print the info about the Delivery Service.
     * @return string representation of the Delivery Service.
     */
    @Override
    public String toString() {
        return String.format("name: %s, revenue: $%d, location: %s", this.name, this.revenue, this.locatedAt.getName());
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

    public boolean hasDrone(int tag) {
        return drones.containsKey(tag);
    }

    public Drone getDrone(int tag) {
        return drones.get(tag);
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) { this.manager = manager; }

    public static Drone findDrone(String service_name, int drone_tag) {
        return services.get(service_name).getDrone(drone_tag);
    }
}