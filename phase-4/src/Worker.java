import java.util.TreeMap;

/**
 * Worker class to represent workers for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Worker extends Person {

    // TreeMap of workers
    private TreeMap<String, DeliveryService> employers = new TreeMap<>();

    /**
     * Constructor for the worker class.
     * @param username username of the worker
     * @param firstName first name of the worker
     * @param lastName last name of the worker
     * @param year year of birth of the worker
     * @param month month of birth of the worker
     * @param date date of birth of the worker
     * @param address address of the worker
     * @param employer name of the employer of the worker
     */
    public Worker(String username, String firstName, String lastName, Integer year, Integer month,
                  Integer date, String address, DeliveryService employer) {
       super(username, firstName, lastName, year, month, date, address);
       addEmployer(employer);
    }

    /**
     * Constructor for the worker class.
     * @param person person to become a worker
     * @param employer name of the employer of the worker
     */
    public Worker(Person person, DeliveryService employer) {
        super(person.getUsername(), person.getFirstName(), person.getLastName(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
        addEmployer(employer);
    }

    /**
     * Adds an employer to the worker.
     * @param employer name of the employer of the worker to be added
     */
    public void addEmployer(DeliveryService employer) {
        // If this is a Manager, all employers are erased, and the current one is added
        if (this instanceof Manager) {
            this.employers = new TreeMap<>();
        }
        this.employers.put(employer.getName(), employer);
    }

    /**
     * Method to display the information of a worker as a String
     * @return String representation of the worker information
     */
    @Override
    public String toString() {
        if (!(this instanceof Manager)) {
            StringBuilder worksAt = new StringBuilder("\n\tEmployee is working at: ");
            for (DeliveryService service : employers.values()) {
                worksAt.append(String.format("\n\t\t&> %s", service.getName()));
            }
            return super.toString() + worksAt;
        } else {
            return super.toString();
        }
    }

    /**
     * Method to get the employers of a worker.
     * @return employers of the worker
     */
    public TreeMap<String, DeliveryService> getEmployers() {
        return employers;
    }

    /**
     * Method to remove an employer from the worker's employers.
     * @param employer employer to be removed
     */
    public void removeEmployer(DeliveryService employer) {
        employers.remove(employer.getName());
    }
}
