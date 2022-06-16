/**
 * Main class to run the interface for the ingredient purchasing system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class Main {

    /**
     * Main method to run the program.
     *
     * @param args command line arguments for the program.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Restaurant Supply Express System!");
        InterfaceLoop simulator = new InterfaceLoop();
        simulator.commandLoop();
    }
}
