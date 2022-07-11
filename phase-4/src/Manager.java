/**
 * Manager class to represent a person who runs a delivery service.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Manager extends Worker {

    /**
     * Constructor for Manager class.
     * @param worker worker to become a manager
     * @param employer employer of the worker
     */
    public Manager(Worker worker, DeliveryService employer) {
        super(worker.getUsername(), worker.getFirstName(), worker.getLastName(), worker.getYear(),
                worker.getMonth(), worker.getDate(), worker.getAddress(), employer);
    }

    /**
     * Method to display the information of the manager.
     * @return String containing the information of the manager
     */
    @Override
    public String toString() {
        String manages = String.format("\n\tEmployee is managing: %s",
                this.getEmployers().firstEntry().getValue().getName());
        return super.toString() + manages;
    }
}