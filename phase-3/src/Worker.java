import java.util.TreeMap;
import java.util.ArrayList;

public class Worker extends Person {

    final private ArrayList<DeliveryService> employers = new ArrayList<>();

    public Worker(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
                  Integer init_date, String init_address, DeliveryService init_employer) {
       super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address);
       this.employers.add(init_employer);
    }

    public Worker(Person person, DeliveryService init_employer) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getYear(), person.getMonth(), person.getDate(), person.getAddress());
        this.employers.add(init_employer);
    }

    public ArrayList<DeliveryService> getEmployers() {
        return employers;
    }

    public void removeEmployer(DeliveryService employer) {
        employers.remove(employer);
    }
}
