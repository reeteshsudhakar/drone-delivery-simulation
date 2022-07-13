import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        Popup dronePopup = new Popup();
        dronePopup.setOpacity(1);
        dronePopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        for (DeliveryService service : DeliveryService.services.values()) {
            populateDronePopup(service, popupBox);
        }

        checkDronesExist(dronePopup, popupBox);
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

        Popup dronePopup = new Popup();
        dronePopup.setOpacity(1);
        dronePopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        populateDronePopup(service, popupBox);
        dronePopup.getContent().add(popupBox);

        displayMessage("DISPLAY","display_in_progress");
        dronePopup.show(Main.primaryStage);
    }

    public static void displayIngredients() {
        Popup ingredientPopup = new Popup();
        ingredientPopup.setOpacity(1);
        ingredientPopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        if (!Ingredient.ingredients.isEmpty()) {
            for (Ingredient ingredient : Ingredient.ingredients.values()) {
                HBox holder = new HBox();
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER);
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

        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        ingredientPopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        ingredientPopup.show(Main.primaryStage);
    }

    public static void displayPeople() {
        Popup peoplePopup = new Popup();
        peoplePopup.setOpacity(1);
        peoplePopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        if (!Person.people.values().isEmpty()) {
            for (Person person : Person.people.values()) {
                HBox holder = new HBox();
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER);
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
        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        peoplePopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        peoplePopup.show(Main.primaryStage);
    }

    public static void displayRestaurants() {
        Popup restaurantPopup = new Popup();
        restaurantPopup.setOpacity(1);
        restaurantPopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        if (!Restaurant.restaurants.isEmpty()) {
            for (Restaurant restaurant : Restaurant.restaurants.values()) {
                HBox holder = new HBox();
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER);
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

        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        restaurantPopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        restaurantPopup.show(Main.primaryStage);

    }

    public static void displayServices() {
        Popup servicePopup = new Popup();
        servicePopup.setOpacity(1);
        servicePopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        if (!DeliveryService.services.isEmpty()) {
            for (DeliveryService service : DeliveryService.services.values()) {
                HBox holder = new HBox();
                holder.setSpacing(10);
                holder.setAlignment(Pos.CENTER);
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
        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        servicePopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        servicePopup.show(Main.primaryStage);
    }

    public static void displayLocations() {
        Popup locationPopup = new Popup();
        locationPopup.setOpacity(1);
        locationPopup.setAutoHide(true);
        VBox popupBox = new VBox();
        popupBox.setSpacing(10);
        popupBox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        if (!Location.locations.isEmpty()) {
            for (Location location : Location.locations.values()) {
                HBox holder = new HBox();
                holder.setSpacing(10);
//                holder.setAlignment(Pos.CENTER);
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

        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        locationPopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        locationPopup.show(Main.primaryStage);
    }

    private static void checkDronesExist(Popup dronePopup, VBox popupBox) {
        if (popupBox.getChildren().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Drones");
            alert.setContentText("No drones have been made yet. Use the make_drone command to do so!");
            alert.show();
            return;
        }

        ScrollPane scrollPane = new ScrollPane(popupBox);
        scrollPane.setMinWidth(500);
        scrollPane.setMaxWidth(500);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        dronePopup.getContent().add(scrollPane);

        displayMessage("DISPLAY","display_in_progress");
        dronePopup.show(Main.primaryStage);
    }

    private static void populateDronePopup(DeliveryService service, VBox popupBox) {
        for (Drone drone : service.getDrones().values()) {
            HBox holder = new HBox();
            holder.setSpacing(10);
            holder.setAlignment(Pos.CENTER);
            VBox info = new VBox();
            info.setAlignment(Pos.CENTER_LEFT);
            info.setSpacing(5);

            Text droneInfo = new Text(drone.toString());
            info.getChildren().add(droneInfo);
            ImageView image = new ImageView(new Image("images/drone.png", 50, 50, true, true));
            holder.getChildren().addAll(image, info);
            popupBox.getChildren().add(holder);
        }
    }
}
