import java.util.TreeMap;

/**
 * Pilot class to represent pilots who fly drones for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Pilot extends Worker {
    String license;
    int experience;
    TreeMap<Integer, Drone> pilotedDrones;

    public Pilot(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
                  Integer init_date, String init_address, DeliveryService init_employer, String init_license, int init_experience) {
        super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address, init_employer);
        this.license = init_license;
        this.experience = init_experience;
        this.pilotedDrones = new TreeMap<>();
    }

    public Pilot(Worker worker, DeliveryService init_employer, String init_license, int init_experience) {
        super(worker.getUsername(), worker.getFname(), worker.getLname(), worker.getYear(), worker.getMonth(),
                worker.getDate(), worker.getAddress(), init_employer);
        this.license = init_license;
        this.experience = init_experience;
        this.pilotedDrones = new TreeMap<>();
    }

    /**
     * @return The display string for this Pilot
     */
    public String toString() {
        if (pilotedDrones == null || pilotedDrones.isEmpty()) {
            return super.toString() + String.format("\nuser has a pilot's license (%s) with %d successful flight(s)",
                    this.license, this.experience);
        } else {
            StringBuilder droneString  = new StringBuilder("\nemployee is flying these drones: [ drone tags ");
            for (Drone drone : pilotedDrones.values()) {
                droneString.append(String.format("| %d ", drone.getTag()));
            }
            droneString.append("]");
            return super.toString() + String.format("\nuser has a pilot's license (%s) with %d successful flight(s)",
                    this.license, this.experience) + droneString;
        }
    }

    public TreeMap<Integer, Drone> getPilotedDrones() {
        return pilotedDrones;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) { this.license = license; }

    public void setExperience(int experience) { this.experience = experience; }

    public void addSuccessfulTrip() { this.experience += 1; }
}
