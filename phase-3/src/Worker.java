import java.util.TreeMap;

/**
 * Worker class to represent workers for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Worker extends Person {

    private TreeMap<String, DeliveryService> employers = new TreeMap<>();

    public Worker(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
                  Integer init_date, String init_address, DeliveryService init_employer) {
       super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address);
       addEmployer(init_employer);
    }

    public Worker(Person person, DeliveryService init_employer) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
        addEmployer(init_employer);
    }

    public void addEmployer(DeliveryService init_employer) {
        // If this is a Manager, all employers are erased, and the current one is added
        if (this instanceof Manager) {
            this.employers = new TreeMap<>();
        }
        this.employers.put(init_employer.getName(), init_employer);
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
