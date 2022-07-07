import java.util.TreeMap;

/**
 * Person class to represent a person in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Person implements Comparable<Person> {
    // collection of persons
    static TreeMap<String, Person> people = new TreeMap<>();

    // Object attributes
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Integer year;
    private final Integer month;
    private final Integer date;
    private final String address;

    /**
     * Constructor for the Person class.
     * @param username username of the person
     * @param firstName first name of the person
     * @param lastName last name of the person
     * @param year year of birth of the person
     * @param month month of birth of the person
     * @param date day of birth of the person
     * @param address address of the person
     */
    public Person(String username, String firstName, String lastName,
                  Integer year, Integer month, Integer date, String address) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
        this.month = month;
        this.date = date;
        this.address = address;
    }

    /**
     *
     * @param username Person's username
     * @param firstName Person's first name
     * @param lastName Person's last name
     * @param year Person's starting year
     * @param month Person's starting month
     * @param date Person's starting day
     * @param address Person's address
     */
    public static void makePerson(String username, String firstName, String lastName,
                                  Integer year, Integer month, Integer date, String address) {
        if (username == null || username.equals("")) {
            Main.displayMessage("ERROR","username_not_valid");
        } else if (people.containsKey(username)) {
            Main.displayMessage("ERROR","person_already_exists");
        } else if (firstName == null || firstName.equals("")) {
            Main.displayMessage("ERROR","first_name_not_valid");
        } else if (lastName == null || lastName.equals("")) {
            Main.displayMessage("ERROR","last_name_not_valid");
        } else if (year == null || year == 0) {
            Main.displayMessage("ERROR","year_not_valid");
        } else if (month == null || month == 0) {
            Main.displayMessage("ERROR","month_not_valid");
        } else if (date == null || date == 0) {
            Main.displayMessage("ERROR","date_not_valid");
        } else if (address == null || address.equals("")) {
            Main.displayMessage("ERROR","address_not_valid");
        } else {
            Person newPerson = new Person(username, firstName, lastName, year,
                    month, date, address);
            people.put(username, newPerson);
            Main.displayMessage("OK","person_created");
        }
    }

    /**
     * Method to check if the username exists
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public static boolean checkUserName(String username) {
        if (people.containsKey(username)) {
            return true;
        } else {
            Main.displayMessage("ERROR","user_name_does_not_exist");
            return false;
        }
    }

    /**
     * Method to compare two persons
     * @param other the person to compare to
     * @return 0 if the persons are the same, <0 if this person is less than the other,
     *         >0 if this person is greater than the other
     */
    @Override
    public int compareTo(Person other) {
        return this.username.compareTo(other.username);
    }

    /**
     * Method to display the person's information as a String
     * @return String representation of the person
     */
    @Override
    public String toString() {
        return String.format("userID: %s, name: %s, birth date: %d-%d-%d, address: %s", this.username,
                this.firstName + " " + this.lastName, this.year, this.month, this.date, this.address);
    }

    /**
     * Method to get the person's username
     * @return the person's username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Method to get the person's first name
     * @return the person's first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Method to get the person's last name
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Method to get the person's address
     * @return the person's address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Method to get the person's year of birth
     * @return the person's year of birth
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Method to get the person's month of birth
     * @return the person's month of birth
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Method to get the person's day of birth
     * @return the person's day of birth
     */
    public int getDate() {
        return this.date;
    }
}