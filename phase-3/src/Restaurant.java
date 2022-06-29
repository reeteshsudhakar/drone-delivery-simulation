import java.util.TreeMap;

/**
 * Restaurant class to represent a restaurant purchasing ingredients in the system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Restaurant implements Comparable<Restaurant> {
    // collection of restaurants
    static TreeMap<String, Restaurant> restaurants = new TreeMap<>();

    // Object attributes
    private final String name;
    private final Location locatedAt;
    private Integer spending;

    /**
     * Constructor for Restaurant class.
     * @param name name of restaurant
     * @param location location of restaurant
     */
    public Restaurant(String name, Location location) {
        this.name = name;
        this.locatedAt = location;
        this.spending = 0;
    }

    /**
     * Method to complete the purchase for a restaurant
     * @param drone drone to purchase ingredients from
     * @param ingredient ingredient to purchase
     * @param quantity quantity of ingredient to purchase
     */
    public void makePurchase(Drone drone, Ingredient ingredient, Integer quantity) {
        this.addSpending(drone.getPayload().get(ingredient).getUnitPrice() * quantity);
    }

    /**
     * Method of creating a restaurant in the system.
     * @param name name of the restaurant
     * @param locatedAt location of the restaurant
     */
    public static void makeRestaurant(String name, String locatedAt) {
        // checking if the restaurant already exists
        if (restaurants.containsKey(name)) {
            Display.displayMessage("ERROR", "restaurant_already_exists");
            return;
        }

        // checking if the passed in location exists
        // if the location does not exist in the system, display an error message
        if (Location.locations.containsKey(locatedAt)) {
            Restaurant restaurant = new Restaurant(name, Location.locations.get(locatedAt));
            restaurants.put(name, restaurant);
            Display.displayMessage("OK","restaurant_created");
        } else {
            Display.displayMessage("ERROR", "location_identifier_does_not_exist");
        }
    }

    /**
     * Method for a restaurant to purchase ingredients from a drone in the system.
     * @param tag tag of the drone to purchase from
     * @param barcode barcode of the ingredient to be purchased
     * @param quantity quantity requested of the ingredient
     * @param serviceName name of the service to be purchased from
     */
    public void purchaseIngredient(Integer tag, String barcode, int quantity, String serviceName) {
        // checking if the drone exists in the system
        Drone buyerDrone;
        if (DeliveryService.checkServiceName(serviceName)) {
            DeliveryService service = DeliveryService.services.get(serviceName);
            if (service.hasDrone(tag)) {
                buyerDrone = service.getDrone(tag);
            } else {
                // if the drone does not exist in the system, display an error message
                Display.displayMessage("ERROR","drone_does_not_exist");
                return;
            }
        } else {
            return;
        }

        // checking if the ingredient exists in the system
        boolean ingredientExists = Ingredient.ingredients.containsKey(barcode);

        // if the ingredient does not exist in the system, display an error message
        if (!ingredientExists) {
            Display.displayMessage("ERROR","ingredient_identifier_does_not_exist");
            return;
        }

        // if the drone is not at the restaurant's location, display an error message
        if (!buyerDrone.getCurrentLocation().equals(this.getLocation())) {
            Display.displayMessage("ERROR","drone_not_located_at_restaurant");
            return;
        }

        // finding the ingredient in the drone's payload
        Ingredient buyerIngredient = null;
        for (Ingredient ingredient : buyerDrone.getPayload().keySet()) {
            if (ingredient.getBarcode().equals(barcode)) {
                buyerIngredient = ingredient;
                break;
            }
        }

        // if the ingredient is not found in the drone's payload, display an error message
        if (buyerIngredient == null) {
            Display.displayMessage("ERROR","ingredient_not_found_in_payload");
            return;
        } else if (quantity <= 0) {
            Display.displayMessage("ERROR","quantity_requested_must_be_greater_than_zero");
            return;
        }

        // completing the purchase if the drone has enough of the ingredient requested for purchase
        if (buyerDrone.getIngredientPayload(buyerIngredient, quantity) < 0) {
            Display.displayMessage("ERROR","drone_does_not_have_enough_of_ingredient_requested");
            return;
        } else {
            this.makePurchase(buyerDrone, buyerIngredient, quantity);
            buyerDrone.completePurchase(buyerIngredient, quantity);
        }
        Display.displayMessage("OK","change_completed");
    }


    /**
     * Override of the toString method to display the restaurant's information.
     * @return String representation of restaurant
     */
    @Override
    public String toString() {
        return String.format("name: %s, money_spent: $%d, location: %s", this.name,
                this.spending, this.locatedAt.getName());
    }

    /**
     * Override of the compareTo method to compare two restaurants.
     * @param other restaurant to compare to
     * @return Integer comparison of the two restaurants
     */
    @Override
    public int compareTo(Restaurant other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Getter for name.
     * @return name of restaurant
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for location.
     * @return location of restaurant
     */
    public Location getLocation() {
        return this.locatedAt;
    }

    /**
     * Setter for spending.
     * @param amount amount to increment spending by based on purchases made by restaurant
     */
    public void addSpending(Integer amount) {
        this.spending += amount;
    }
}
