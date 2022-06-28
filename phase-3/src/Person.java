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
    private final int year;
    private final int month;
    private final int date;
    private final String address;

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
            Display.displayMessage("ERROR", "username_not_valid");
        } else if (people.containsKey(username)) {
            Display.displayMessage("ERROR", "person_already_exists");
        } else if (firstName == null || firstName.equals("")) {
            Display.displayMessage("ERROR", "first_name_not_valid");
        } else if (lastName == null || lastName.equals("")) {
            Display.displayMessage("ERROR", "last_name_not_valid");
        } else if (year == null || year == 0) {
            Display.displayMessage("ERROR", "year_not_valid");
        } else if (month == null || month == 0) {
            Display.displayMessage("ERROR", "month_not_valid");
        } else if (date == null || date == 0) {
            Display.displayMessage("ERROR", "date_not_valid");
        } else if (address == null || address.equals("")) {
            Display.displayMessage("ERROR", "address_not_valid");
        } else {
            Person newPerson = new Person(username, firstName, lastName, year,
                    month, date, address);
            people.put(username, newPerson);
            Display.displayMessage("OK", "person_created");
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
            Display.displayMessage("ERROR", "user_name_does_not_exist");
            return false;
        }
    }

    @Override
    public int compareTo(Person other) {
        return this.getUsername().compareTo(other.getUsername());
    }

    @Override
    public String toString() {
        return String.format("userID: %s, name: %s, birth date: %d-%d-%d, address: %s", this.username,
                this.firstName + " " + this.lastName, this.year, this.month, this.date, this.address);
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDate() {
        return this.date;
    }
}