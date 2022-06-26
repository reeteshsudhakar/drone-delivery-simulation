/**
 * Manager class to represent a person who runs a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Manager extends Worker {

    public Manager(String init_username, String init_fname, String init_lname, Integer init_year,
                   Integer init_month, Integer init_date, String init_address, DeliveryService init_employer) {
        super(init_username, init_fname, init_lname, init_year, init_month, init_date, init_address, init_employer);
    }

    public Manager(Worker worker, DeliveryService init_employer) {
        super(worker.getUsername(), worker.getFname(), worker.getLname(), worker.getYear(),
                worker.getMonth(), worker.getDate(), worker.getAddress(), init_employer);
    }

    @Override
    public String toString() {
        String manages = String.format("\nemployee is managing: %s", this.getEmployers().firstEntry().getValue().getName());
        return super.toString() + manages;
    }
}