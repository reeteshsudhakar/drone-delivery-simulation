import java.util.TreeMap;

/**
 * Pilot class to represent pilots who fly drones for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Pilot extends Worker {
    private String license;
    private Integer experience;
    private TreeMap<Integer, Drone> pilotedDrones;

    /**
     * Constructor for the Pilot class.
     * @param worker worker to become a Pilot
     * @param employer employer of the Pilot
     * @param license license of the pilot
     * @param experience initial experience of the pilot
     */
    public Pilot(Worker worker, DeliveryService employer, String license, Integer experience) {
        super(worker.getUsername(), worker.getFirstName(), worker.getLastName(), worker.getYear(), worker.getMonth(),
                worker.getDate(), worker.getAddress(), employer);
        this.license = license;
        this.experience = experience;
        this.pilotedDrones = new TreeMap<>();
    }

    /**
     * Method to change the employer of the pilot
     * @param employer new employer of the pilot
     * @param license new license of the pilot
     * @param experience new experience of the pilot
     */
    public void changeEmployer(DeliveryService employer, String license, Integer experience) {
        this.getEmployers().clear();
        this.getEmployers().put(employer.getName(), employer);
        this.setLicense(license);
        this.setExperience(experience);
    }

    /**
     * Method to check if a drone is piloting for another service
     * @param service service to check if the drone is piloting for
     * @return true if the drone is piloting drones for another service, false otherwise
     */
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

    /**
     * Method to add a successful trip to the Pilot's experience
     */
    public void addSuccessfulTrip() { this.experience += 1; }

    /**
     * Getter for the drones the pilot is controlling
     * @return the drones the pilot is controlling
     */
    public TreeMap<Integer, Drone> getPilotedDrones() { return pilotedDrones; }

    /**
     * Getter for the license of a Pilot
     * @return the license of a Pilot
     */
    public String getLicense() { return license; }

    /**
     * Setter for the license of a Pilot
     * @param license new license of a Pilot
     */
    public void setLicense(String license) { this.license = license; }

    /**
     * Setter for the experience of a pilot
     * @param experience new experience of a pilot
     */
    public void setExperience(Integer experience) { this.experience = experience; }

}
