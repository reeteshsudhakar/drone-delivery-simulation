/**
 * Pilot class to represent pilots who fly drones for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Pilot extends Person {
    public Pilot(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
                  Integer init_date, String init_address, DeliveryService init_employer) {
        super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address);
    }

    public Pilot(Person person) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
    }
}
