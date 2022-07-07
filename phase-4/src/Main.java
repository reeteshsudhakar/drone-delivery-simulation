import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

// TODO: change the way that the main pane for buttons is made so that they actually work

/**
 * Main class to run the interface for the ingredient purchasing system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Main extends Application {
    private static String status;
    private static String message;
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // creating a grid to display the entities
        primaryStage = stage;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        Button droneButton = addAsset(grid, 0, 0, "drone.png", "Display Drones");
        Button ingredientButton = addAsset(grid, 0, 1, "ingredient.png", "Display Ingredients");
        Button peopleButton = addAsset(grid, 0, 2, "person.png", "Display People");
        Button restaurantButton = addAsset(grid, 0, 3, "restaurant.png", "Display Restaurants");
        Button serviceButton = addAsset(grid, 0, 4, "service.png", "Display Services");

        ingredientButton.setOnAction(e -> displayIngredients());

        // adding background image to the scene and showing the stage
        StackPane root = new StackPane();
        initializeWindow(root, grid);
    }

    public void initializeWindow(StackPane root, GridPane grid) throws IOException {
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

        VBox input = addArgumentInput();
        VBox buttons = new VBox();
        buttons.setSpacing(150);
        buttons.setAlignment(Pos.CENTER);
        buttons.setTranslateY(100);
        buttons.getChildren().addAll(grid, input);

        root.getChildren().addAll(background, title, buttons);
        root.setAlignment(title, Pos.TOP_CENTER);
        root.setAlignment(buttons, Pos.CENTER);
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Ingredient Delivery System");
        primaryStage.setMinWidth(background.getFitWidth());
        primaryStage.setMinHeight(background.getFitHeight());
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public Button addAsset(GridPane grid, int row, int column, String fileName, String buttonName) {
        Image image = new Image("resources/" + fileName, 250, 250, true, true);
        ImageView imageView = new ImageView(image);
        Button button = new Button(buttonName);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(imageView, button);

        grid.add(vbox, column, row);
        return button;
    }

    public VBox addArgumentInput() throws IOException {
        VBox input = new VBox();
        input.setAlignment(Pos.BOTTOM_CENTER);
        input.setTranslateY(-20);
        input.setSpacing(5);

        Text text = new Text("Enter Argument Here");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        text.setFill(Color.BLACK);
        text.setStroke(Color.BLACK);

        TextField textField = new TextField();
        textField.setPromptText("argument");
        textField.setMaxWidth(1000);
        textField.setMinWidth(1000);
        textField.setMaxHeight(50);
        textField.setMinHeight(50);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        textField.setAlignment(Pos.CENTER_LEFT);

        Button submit = new Button();
        submit.setText("Submit");
        submit.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        submit.setPrefSize(100, 50);

        submit.setOnAction(e -> {
            String argument = textField.getText();
            InterfaceLoop.commandLoop(argument);
            textField.clear();
            switch (status) {
                case "ERROR": {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(status);
                    alert.setHeaderText(message);
                    alert.showAndWait();
                    break;
                }
                case "OK": {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(status);
                    alert.setHeaderText(message);
                    alert.showAndWait();
                    break;
                }
                case "STOP": {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(status);
                    alert.setHeaderText(message);
                    alert.showAndWait();
                    System.exit(0);
                }
            }
        });

        Popup arguments = makeArgumentsPopup(new File("src/resources/arguments.csv"));
        Button showArguments = makeArgumentsPopupButton(arguments);
        showArguments.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        showArguments.setPrefSize(125, 50);
        showArguments.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.getChildren().addAll(textField, submit, showArguments);

        input.getChildren().addAll(text, hbox);
        return input;
    }

    public static void displayMessage(String info, String output) {
        status = info;
        message = output;
    }

    public Popup makeArgumentsPopup(File file) throws IOException {
        CSVTableView table = new CSVTableView(",", file);
        Popup popup = new Popup();
        popup.getContent().add(table);
        return popup;
    }

    public Button makeArgumentsPopupButton(Popup popup) {
        Button button = new Button("Show Args");
        button.setOnAction(e -> {
            if (button.getText().equals("Show Args")) {
                popup.show(primaryStage, primaryStage.getWidth()/2 - popup.getWidth()/2,
                        primaryStage.getHeight()/2 - popup.getHeight()/2);
                button.setText("Hide Args");
            } else {
                popup.hide();
                button.setText("Show Args");
            }
        });
        return button;
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
                VBox info = new VBox();
                info.setAlignment(Pos.CENTER_LEFT);
                info.setSpacing(5);
                Text name = new Text("Name: " + ingredient.getName());
                Text barcode = new Text("Barcode: " + ingredient.getBarcode());
                Text weight = new Text("Weight: " + ingredient.getWeight().toString());
                info.getChildren().addAll(name, barcode, weight);
                ImageView image = new ImageView(new Image("resources/ingredient.png", 50, 50, true, true));
                holder.getChildren().addAll(image, info);
                popupBox.getChildren().add(holder);
            }
            ingredientPopup.getContent().add(popupBox);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No Ingredients");
            alert.setContentText("No ingredients have been made yet. Use the make_ingredient command to do so!");
            alert.show();
            return;
        }

        ingredientPopup.show(primaryStage, primaryStage.getWidth()/2 - ingredientPopup.getWidth()/2,
                primaryStage.getHeight()/2 - ingredientPopup.getHeight()/2);
    }
}
