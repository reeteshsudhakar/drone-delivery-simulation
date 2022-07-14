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
     * Method to check if the username exists
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public static boolean checkUserName(String username) {
        if (people.containsKey(username)) {
            return true;
        } else {
            Display.displayMessage("ERROR", "user_name_does_not_exist");
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