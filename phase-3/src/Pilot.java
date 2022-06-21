import java.util.ArrayList;

/**
 * Pilot class to represent pilots who fly drones for delivery services.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Pilot extends Person {
    String license;
    ArrayList<DeliveryService> employers;
    int experience;


    public Pilot(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month,
                  Integer init_date, String init_address, DeliveryService init_employer, String init_license, int init_experience) {
        super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address);
        this.employers.add(init_employer);
        this.license = init_license;
        this.experience = init_experience;
    }

    public Pilot(Person person, DeliveryService init_employer, String init_license, int init_experience) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress());
        this.employers.add(init_employer);
        this.license = init_license;
        this.experience = init_experience;
    }
}
