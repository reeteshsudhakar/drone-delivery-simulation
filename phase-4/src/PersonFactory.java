/**
 * PersonFactory class to switch between Person, Worker, Pilot, and the entities for the system.
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class PersonFactory {

    /**
     * Creates a Pilot object from a Worker
     * @param tempWorker Worker object to be converted to a Pilot
     * @param employer Employer object of the Worker
     * @param license License of the Pilot
     * @param experience Experience of the Pilot
     * @return Pilot object
     */
    public static Worker createPilot(Worker tempWorker, DeliveryService employer, String license, Integer experience) {
        return new Pilot(tempWorker, employer, license, experience);
    }

    /**
     * Creates a Worker object from a Person
     * @param worker Worker object to be converted to a Person
     * @return Person object
     */
    public static Person createPerson(Worker worker) {
        return PersonFactory.createPerson(worker.getUsername(), worker.getFirstName(),
                worker.getLastName(), worker.getYear(), worker.getMonth(),
                worker.getDate(), worker.getAddress());
    }

    /**
     * Method to check if a Person has valid inputs or not.
     * @param username Person's username
     * @param firstName Person's first name
     * @param lastName Person's last name
     * @param year Person's starting year
     * @param month Person's starting month
     * @param date Person's starting day
     * @param address Person's address
     */
    public static boolean creatingPersonValid(String username, String firstName, String lastName,
                                  Integer year, Integer month, Integer date, String address) {
        if (username == null || username.equals("")) {
            Display.displayMessage("ERROR", "username_not_valid");
            return false;
        } else if (Person.people.containsKey(username)) {
            Display.displayMessage("ERROR", "person_already_exists");
            return false;
        } else if (firstName == null || firstName.equals("")) {
            Display.displayMessage("ERROR", "first_name_not_valid");
            return false;
        } else if (lastName == null || lastName.equals("")) {
            Display.displayMessage("ERROR", "last_name_not_valid");
            return false;
        } else if (year == null || year == 0) {
            Display.displayMessage("ERROR", "year_not_valid");
            return false;
        } else if (month == null || month == 0) {
            Display.displayMessage("ERROR", "month_not_valid");
            return false;
        } else if (date == null || date == 0) {
            Display.displayMessage("ERROR", "date_not_valid");
            return false;
        } else if (address == null || address.equals("")) {
            Display.displayMessage("ERROR", "address_not_valid");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to create a Person from the inputs.
     * @param username Person's username
     * @param firstName Person's first name
     * @param lastName Person's last name
     * @param year Person's birth year
     * @param month Person's birth month
     * @param date Person's birth day
     * @param address Person's address
     * @return Person object
     */
    public static Person createPerson(String username, String firstName, String lastName,
                                      Integer year, Integer month, Integer date, String address) {
        Person newPerson = new Person(username, firstName, lastName, year, month, date, address);
        Person.people.put(username, newPerson);
        return newPerson;
    }

    /**
     * Constructor for the worker class.
     * @param person person to become a worker
     * @param employer name of the employer of the worker
     * @return Worker object
     */
    public static Worker createWorker(Person person, DeliveryService employer) {
        return new Worker(person.getUsername(), person.getFirstName(), person.getLastName(), person.getYear(), person.getMonth(),
                person.getDate(), person.getAddress(), employer);
    }
}
