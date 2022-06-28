import java.util.TreeMap;

/**
 * Worker class to represent workers for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Worker extends Person {

    private TreeMap<String, DeliveryService> employers = new TreeMap<>();

    public Worker(String username, String firstName, String lastName, Integer year, Integer month,
                  Integer date, String address, DeliveryService employer) {
       super(username, firstName, lastName, year, month, date, address);
       addEmployer(employer);
    }

    public Worker(Person person, DeliveryService init_employer) {
        super(person.getUsername(), person.getFirstName(), person.getLastName(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
        addEmployer(init_employer);
    }

    public void addEmployer(DeliveryService employer) {
        // If this is a Manager, all employers are erased, and the current one is added
        if (this instanceof Manager) {
            this.employers = new TreeMap<>();
        }
        this.employers.put(employer.getName(), employer);
    }

    @Override
    public String toString() {
        if (!(this instanceof Manager)) {
            StringBuilder works_at = new StringBuilder("\nemployee is working at:");
            for (DeliveryService service : employers.values()) {
                works_at.append(String.format("\n&> %s", service.getName()));
            }
            return super.toString() + works_at;
        } else {
            return super.toString();
        }
    }

    public TreeMap<String, DeliveryService> getEmployers() {
        return employers;
    }

    public void removeEmployer(DeliveryService employer) {
        employers.remove(employer.getName());
    }
}
