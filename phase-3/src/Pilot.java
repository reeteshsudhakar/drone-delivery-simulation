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

    public Pilot(Worker worker, DeliveryService employer, String license, Integer experience) {
        super(worker.getUsername(), worker.getFirstName(), worker.getLastName(), worker.getYear(), worker.getMonth(),
                worker.getDate(), worker.getAddress(), employer);
        this.license = license;
        this.experience = experience;
        this.pilotedDrones = new TreeMap<>();
    }

    public void changeEmployer(String serviceName, DeliveryService employer, String license, Integer experience) {
        this.getEmployers().clear();
        this.getEmployers().put(serviceName, employer);
        this.setLicense(license);
        this.setExperience(experience);
    }

    public boolean pilotingForAnotherService(DeliveryService service) {
        return !this.getEmployers().isEmpty() && !this.getEmployers().firstKey().equals(service.getName())
                && !this.getPilotedDrones().isEmpty();
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

    public void addSuccessfulTrip() { this.experience += 1; }

    public TreeMap<Integer, Drone> getPilotedDrones() { return pilotedDrones; }

    public String getLicense() { return license; }

    public void setLicense(String license) { this.license = license; }

    public void setExperience(int experience) { this.experience = experience; }

}
