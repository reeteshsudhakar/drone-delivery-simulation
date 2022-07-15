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
    public static void displayMessage(String info, String output) {
        Main.status = info;
        Main.message = output;
    }

    public static void displayAllDrones() {
        Popup dronePopup = createPopup();
        VBox popupBox = createPopupBox("Drones");

        for (DeliveryService service : DeliveryService.services.values()) {
            populateDronePopup(service, popupBox);
        }

        validateDronePopup(dronePopup, popupBox);
    }

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

    private static Popup createPopup() {
        Popup popup = new Popup();
        popup.setOpacity(1);
        popup.setAutoHide(true);
        return popup;
    }

    private static VBox createPopupBox(String title) {
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        HBox titleBox = makePopupTitle(title);
        popupBox.getChildren().add(titleBox);

        return popupBox;
    }
}
