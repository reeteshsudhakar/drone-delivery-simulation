/**
 * Manager class to represent a person who runs a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Manager extends Worker {

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