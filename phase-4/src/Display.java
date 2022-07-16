import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

/**
 * This class is used to display the messages from the program.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Display {
    /**
     * This method is used to send information to create alerts for the display.
     * @param info the information status to be displayed
     * @param output the specific message containing the information
     */
    public static void displayMessage(String info, String output) {
        Main.status = info;
        Main.message = output;
    }

    /**
     * This method is used to create and display a popup containing all services' drones.
     */
    public static void displayAllDrones() {
        Popup dronePopup = createPopup();
        VBox popupBox = createPopupBox("Drones");

        for (DeliveryService service : DeliveryService.services.values()) {
            populateDronePopup(service, popupBox);
        }

        validateDronePopup(dronePopup, popupBox);
    }

    /**
     * This method is used to create and display a popup containing a given services' drones.
     * @param serviceName the name of the service to be displayed
     */
    public static void displayDrones(String serviceName) {
        DeliveryService service = DeliveryService.services.get(serviceName);
        if (service == null) {
            displayMessage("ERROR","service_does_not_exist");
            return;
        }

        if (service.getDrones().size() == 0) {
            displayMessage("ERROR","The service has no drones. Use the make_drone command to create some!");
            return;
        }

        Popup dronePopup = createPopup();
        VBox popupBox = createPopupBox("Drones");

        populateDronePopup(service, popupBox);

        createScrollPane(dronePopup, popupBox);
    }

    /**
     * Method to display the ingredients created in the system
     */
    public static void displayIngredients() {
        Popup ingredientPopup = createPopup();
        VBox popupBox = createPopupBox("Ingredients");

        if (!Ingredient.ingredients.isEmpty()) {
            for (Ingredient ingredient : Ingredient.ingredients.values()) {
                HBox holder = new HBox();
                holder.setMinWidth(700);
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER_LEFT);
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text ingredientInfo = new Text(ingredient.toString());
                info.getChildren().addAll(ingredientInfo);
                ImageView image = new ImageView(new Image("images/ingredient.png", 50, 50, true, true));
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Ingredients");
            alert.setContentText("No ingredients have been made yet. Use the make_ingredient command to do so!");
            alert.show();
            return;
        }
        createScrollPane(ingredientPopup, popupBox);
    }

    /**
     * Method to display the people created in the system
     */
    public static void displayPeople() {
        Popup peoplePopup = createPopup();
        VBox popupBox = createPopupBox("People");

        if (!Person.people.values().isEmpty()) {
            for (Person person : Person.people.values()) {
                HBox holder = new HBox();
                holder.setMinWidth(700);
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER_LEFT);
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text personInfo = new Text(person.toString());
                info.getChildren().addAll(personInfo);
                ImageView image;
                if (person instanceof Pilot) {
                    image = new ImageView(new Image("images/pilot.png", 50, 50, true, true));
                } else if (person instanceof Manager) {
                    image = new ImageView(new Image("images/manager.png", 50, 50, true, true));
                } else if (person instanceof Worker) {
                    image = new ImageView(new Image("images/worker.png", 50, 50, true, true));
                } else {
                    image = new ImageView(new Image("images/person.png", 50, 50, true, true));
                }
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No People");
            alert.setContentText("No people have been made yet. Use the make_person command to do so!");
            alert.show();
            return;
        }
        createScrollPane(peoplePopup, popupBox);
    }

    /**
     * Method to display the restaurants created in the system
     */
    public static void displayRestaurants() {
        Popup restaurantPopup = createPopup();
        VBox popupBox = createPopupBox("Restaurants");

        if (!Restaurant.restaurants.isEmpty()) {
            for (Restaurant restaurant : Restaurant.restaurants.values()) {
                HBox holder = new HBox();
                holder.setMinWidth(700);
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER_LEFT);
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text locationInfo = new Text(restaurant.toString());
                info.getChildren().addAll(locationInfo);
                ImageView image = new ImageView(new Image("images/restaurant.png", 50, 50, true, true));
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Restaurants");
            alert.setContentText("No restaurants have been made yet. Use the make_restaurant command to do so!");
            alert.show();
            return;
        }

        createScrollPane(restaurantPopup, popupBox);

    }

    /**
     * Method to display the services created in the system
     */
    public static void displayServices() {
        Popup servicePopup = createPopup();
        VBox popupBox = createPopupBox("Services");

        if (!DeliveryService.services.isEmpty()) {
            for (DeliveryService service : DeliveryService.services.values()) {
                HBox holder = new HBox();
                holder.setMinWidth(700);
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER_LEFT);
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text locationInfo = new Text(service.toString());
                info.getChildren().addAll(locationInfo);
                ImageView image = new ImageView(new Image("images/service.png", 50, 50, true, true));
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Services");
            alert.setContentText("No services have been made yet. Use the make_service command to do so!");
            alert.show();
            return;
        }
        createScrollPane(servicePopup, popupBox);
    }

    /**
     * Method to display the locations created in the system
     */
    public static void displayLocations() {
        Popup locationPopup = createPopup();
        VBox popupBox = createPopupBox("Locations");

        if (!Location.locations.isEmpty()) {
            for (Location location : Location.locations.values()) {
                HBox holder = new HBox();
                holder.setMinWidth(700);
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER_LEFT);
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text locationInfo = new Text(location.toString());
                info.getChildren().addAll(locationInfo);
                ImageView image = new ImageView(new Image("images/location.png", 50, 50, true, true));
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Locations");
            alert.setContentText("No locations have been made yet. Use the make_location command to do so!");
            alert.show();
            return;
        }

        createScrollPane(locationPopup, popupBox);
    }

    /**
     * Method to check whether there are drones instantiated in the system
     * @param dronePopup the popup to display the drones in
     * @param popupBox the VBox containing the information about the drones
     */
    private static void validateDronePopup(Popup dronePopup, VBox popupBox) {
        if (popupBox.getChildren().size() == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Drones");
            alert.setContentText("No drones have been made yet. Use the make_drone command to do so!");
            alert.show();
            return;
        }

        createScrollPane(dronePopup, popupBox);
    }

    /**
     * Method to populate a VBox with drones in the system
     * @param service DeliveryService whose drones to display in the VBox
     * @param popupBox the VBox to populate with the drones
     */
    private static void populateDronePopup(DeliveryService service, VBox popupBox) {
        Text serviceName = new Text(service.getName());
        serviceName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        popupBox.getChildren().add(serviceName);

        for (Drone drone : service.getDrones().values()) {
            HBox holder = new HBox();
            holder.setMinWidth(700);
            holder.setSpacing(10);
            holder.setAlignment(Pos.CENTER_LEFT);
            VBox info = new VBox();
            info.setAlignment(Pos.CENTER_LEFT);
            info.setSpacing(5);

            Text droneInfo = new Text(drone.toString());
            info.getChildren().add(droneInfo);
            ImageView image;
            if (drone instanceof LeaderDrone) {
                image = new ImageView(new Image("images/leader-drone.png", 50, 50, true, true));
            } else {
                image = new ImageView(new Image("images/follower-drone.png", 50, 50, true, true));
            }
            holder.getChildren().addAll(image, info);
            popupBox.getChildren().add(holder);
        }
    }

    /**
     * Method to create and style a ScrollPane to house the popup
     * @param popup the popup to style
     * @param popupBox the VBox to house the popup
     */
    private static void createScrollPane(Popup popup, VBox popupBox) {
        StackPane background = new StackPane();
        background.setPrefWidth(popup.getWidth());
        background.setPrefHeight(popup.getHeight());
        background.setStyle("-fx-background-color: white;");
        background.getChildren().add(popupBox);
        ScrollPane scrollPane = new ScrollPane(background);

        scrollPane.setMinWidth(700);
        scrollPane.setMaxWidth(700);
        scrollPane.setMinHeight(500);
        scrollPane.setMaxHeight(500);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");

        popup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        popup.show(Main.primaryStage);
    }

    /**
     * Method to create an HBox to house the title of the popup
     * @param title the title of the popup
     * @return the HBox containing the title of the popup
     */
    private static HBox makePopupTitle(String title) {
        Text text = new Text(title);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        text.setFill(Color.BLACK);
        HBox titleBox = new HBox();
        titleBox.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(text);
        return titleBox;
    }

    /**
     * Method to create and style a Popup
     * @return the Popup created
     */
    private static Popup createPopup() {
        Popup popup = new Popup();
        popup.setOpacity(1);
        popup.setAutoHide(true);
        return popup;
    }

    /**
     * Method to create and style a VBox to house the popup info
     * @param title the title of the popup
     * @return the VBox containing the popup info
     */
    private static VBox createPopupBox(String title) {
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        HBox titleBox = makePopupTitle(title);
        popupBox.getChildren().add(titleBox);

        return popupBox;
    }
}
