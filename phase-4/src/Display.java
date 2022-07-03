import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// TODO: look into styling things using CSS (https://docs.oracle.com/javafx/2/css_tutorial/jfxpub-css_tutorial.htm)
/**
 * This class is used to display the messages from the program.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Display extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        // creating a grid to display the entities
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        addAsset(grid, 0, 0, "drone.png");
        addAsset(grid, 0, 1, "ingredient.png");
        addAsset(grid, 0, 2, "person.png");
        addAsset(grid, 0, 3, "restaurant.png");
        addAsset(grid, 0, 4, "service.png");


        // adding background image to the scene and showing the stage
        StackPane root = new StackPane();
        initializeWindow(root, grid, stage);
    }

    public void initializeWindow(StackPane root, GridPane grid, Stage stage) {
        // background image
        Image image = new Image("resources/background.jpeg");
        ImageView background = new ImageView(image);
        background.setFitHeight(background.getFitHeight());
        background.setFitWidth(background.getFitWidth());

        // Main Text of the window
        Text title = new Text("Drone Ingredient Delivery System");
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, 75));
        title.setFill(Color.BLACK);
        title.setStroke(Color.BLACK);
        title.setUnderline(true);
        title.setTranslateY(20);

        root.getChildren().addAll(background, title, grid);
        root.setAlignment(title, Pos.TOP_CENTER);
        stage.setScene(new Scene(root));
        stage.setTitle("Ingredient Delivery System");
        stage.setMinWidth(background.getFitWidth());
        stage.setMinHeight(background.getFitHeight());
        stage.setResizable(true);
        stage.show();
    }

    public void addAsset(GridPane grid, int row, int column, String fileName) {
        Image image = new Image("resources/" + fileName, 250, 250, true, true);
        ImageView imageView = new ImageView(image);
        Button button = new Button("Display");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(imageView, button);

        grid.add(vbox, column, row);
    }

    /**
     * Method to display all of the drones in the system.
     */
    public static void displayAllDrones() {
        // displaying all the drones in the system by iterating through the collection
        for (DeliveryService service : DeliveryService.services.values()) {
            System.out.printf("service name [%s] drones:%n", service.getName());
            for (Drone drone : service.getDrones().values()) {
                System.out.print(drone.toString());
            }
        }

        displayMessage("OK","display_completed");
    }

    /**
     * Method to display the delivery services in the system.
     */
    public static void displayServices() {
        // displaying all the delivery services in the system by iterating through the collection
        for (DeliveryService service : DeliveryService.services.values()) {
            System.out.println(service.toString());
        }
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the drones in the system attached to a specified service.
     * @param serviceName the name of the service the drone is assigned to
     */
    public static void displayDrones(String serviceName) {
        // displaying the drones in the system attached to the specified service
        if (DeliveryService.services.containsKey(serviceName)) {
            DeliveryService service = DeliveryService.services.get(serviceName);
            service.getDrones().forEach((k, v) -> System.out.print(v.toString()));
            displayMessage("OK","display_completed");
        } else {
            displayMessage("ERROR","service_does_not_exist");
        }
    }

    /**
     * Method to display the ingredients in the system.
     */
    public static void displayIngredients() {
        // displaying all the ingredients in the system by iterating through the collection
        for (Ingredient ingredient : Ingredient.ingredients.values()) {
            System.out.println(ingredient.toString());
        }
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the restaurants in the system.
     */
    public static void displayRestaurants() {
        // displaying all the restaurants in the system by iterating through the collection
        for (Restaurant restaurant : Restaurant.restaurants.values()) {
            System.out.println(restaurant.toString());
        }
        displayMessage("OK","display_completed");
    }

    /**
     * Method to display the locations in the system.
     */
    public static void displayLocations() {
        // displaying all the locations in the system by iterating through the collection
        for (Location location : Location.locations.values()) {
            System.out.println(location.toString());
        }
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display the people in our service
     */
    public static void displayPersons() {
        // displaying all the people in the system by iterating through the collection
        for (Person person : Person.people.values()) {
            System.out.println(person.toString());
        }
        Display.displayMessage("OK","display_completed");
    }

    /**
     * Method to display a message from the interface
     * @param status the status of the message
     * @param output the text to be displayed
     */
    public static void displayMessage(String status, String output) {
        System.out.println(status.toUpperCase() + ":" + output.toLowerCase());
    }
}
