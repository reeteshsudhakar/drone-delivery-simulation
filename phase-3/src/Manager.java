/**
 * Manager class to represent a person who runs a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Manager extends Person {

    private DeliveryService employer;

    public Manager(String init_username, String init_fname, String init_lname, Integer init_year,
                   Integer init_month, Integer init_date, String init_address, DeliveryService init_employer) {
        super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address);
        this.employer = init_employer;
    }

    public Manager(Person person, DeliveryService init_employer) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
        this.employer = init_employer;
    }

    @Override
    public String toString() {
        String manages = String.format("\nemployee is managing: %s", this.employer.getName());
        return super.toString() + manages;
    }
}