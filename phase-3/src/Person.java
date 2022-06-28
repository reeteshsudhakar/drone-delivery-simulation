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
    private final String fname;
    private final String lname;
    private final int year;
    private final int month;
    private final int date;
    private final String address;

    public Person(String init_username, String init_fname, String init_lname,
                  Integer init_year, Integer init_month, Integer init_date, String init_address) {
        this.username = init_username;
        this.fname = init_fname;
        this.lname = init_lname;
        this.year = init_year;
        this.month = init_month;
        this.date = init_date;
        this.address = init_address;
    }

    /**
     *
     * @param init_username Person's username
     * @param init_fname Person's first name
     * @param init_lname Person's last name
     * @param init_year Person's starting year
     * @param init_month Person's starting month
     * @param init_date Person's starting day
     * @param init_address Person's address
     */
    public static void makePerson(String init_username, String init_fname, String init_lname,
                                  Integer init_year, Integer init_month, Integer init_date, String init_address) {
        if (init_username == null || init_username.equals("")) {
            Display.displayMessage("ERROR", "username_not_valid");
        } else if (people.containsKey(init_username)) {
            Display.displayMessage("ERROR", "person_already_exists");
        } else if (init_fname == null || init_fname.equals("")) {
            Display.displayMessage("ERROR", "first_name_not_valid");
        } else if (init_lname == null || init_lname.equals("")) {
            Display.displayMessage("ERROR", "last_name_not_valid");
        } else if (init_year == null || init_year == 0) {
            Display.displayMessage("ERROR", "year_not_valid");
        } else if (init_month == null || init_month == 0) {
            Display.displayMessage("ERROR", "month_not_valid");
        } else if (init_date == null || init_date == 0) {
            Display.displayMessage("ERROR", "date_not_valid");
        } else if (init_address == null || init_address.equals("")) {
            Display.displayMessage("ERROR", "address_not_valid");
        } else {
            Person newPerson = new Person(init_username, init_fname, init_lname, init_year,
                    init_month, init_date, init_address);
            people.put(init_username, newPerson);
            Display.displayMessage("OK", "person_created");
        }
    }

    /**
     * Method to check if the username exists
     * @param user_name the username to check
     * @return true if the username exists, false otherwise
     */
    static boolean checkUserName(String user_name) {
        if (people.containsKey(user_name)) {
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
                this.fname + " " + this.lname, this.year, this.month, this.date, this.address);
    }

    public String getUsername() {
        return this.username;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return lname;
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