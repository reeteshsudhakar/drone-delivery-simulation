import com.pixelduke.control.skin.FXSkins;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;


// https://stackoverflow.com/questions/31969372/how-do-i-create-a-scrollable-context-menu-in-javafx
// ^^ helpful link for scrollpane

/*
TODO: make the popups scrollable
TODO: code cleanup
TODO: write more test cases to show the full functionality of the system
TODO: style the popups (add a title, size them up)
TODO: try using CSS for styling (https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html)
 */

/**
 * Main class to run the interface for the ingredient purchasing system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Main extends Application {
    static String status;
    static String message;
    static Stage primaryStage;

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

        Button droneButton = addAsset(grid, 0, 0, "drone.png", "Display all Drones");
        Button ingredientButton = addAsset(grid, 0, 1, "ingredient.png", "Display Ingredients");
        Button peopleButton = addAsset(grid, 0, 2, "person.png", "Display People");
        Button restaurantButton = addAsset(grid, 0, 3, "restaurant.png", "Display Restaurants");
        Button serviceButton = addAsset(grid, 0, 4, "service.png", "Display Services");
        Button locationButton = addAsset(grid, 0, 5, "location.png", "Display Locations");

        droneButton.setOnAction(e -> Display.displayAllDrones());
        ingredientButton.setOnAction(e -> Display.displayIngredients());
        peopleButton.setOnAction(e -> Display.displayPeople());
        restaurantButton.setOnAction(e -> Display.displayRestaurants());
        serviceButton.setOnAction(e -> Display.displayServices());
        locationButton.setOnAction(e -> Display.displayLocations());

        // adding background image to the scene and showing the stage
        StackPane root = new StackPane();
        initializeWindow(root, grid);
    }

    public void initializeWindow(StackPane root, GridPane grid) throws IOException {
        // background image
        Image image = new Image("images/background.jpeg");
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
        mainScene.getStylesheets().add(FXSkins.getStylesheetURL());
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Ingredient Delivery System");
        primaryStage.setMinWidth(background.getFitWidth());
        primaryStage.setMinHeight(background.getFitHeight());
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public Button addAsset(GridPane grid, int row, int column, String fileName, String buttonName) {
        Image image = new Image("images/" + fileName, 200, 200, true, true);
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

        try (PrintWriter pw = new PrintWriter(new FileWriter("src/resources/commands.csv"))) {
            pw.println("Commands,Arguments");
            pw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        submit.setOnAction(e -> {
            String argument = textField.getText();
            try (PrintWriter pw = new PrintWriter(new FileWriter("src/resources/commands.csv", true))) {
                pw.println(argument);
                pw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
                case "DISPLAY":
                    break;
            }
        });

        Popup commands = makeTableViewPopup(new File("src/resources/arguments.csv"));
        Button showCommands = makeCommandsPopupButton(commands);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.getChildren().addAll(textField, submit, showCommands);

        input.getChildren().addAll(text, hbox);
        return input;
    }

    public Popup makeTableViewPopup(File file) throws IOException {
        CSVTableView table = new CSVTableView(",", file);
        Popup popup = new Popup();
        popup.getContent().add(table);
        return popup;
    }

    public Button makeCommandsPopupButton(Popup popup) {
        Button button = new Button("     View\nCommands");
        button.setAlignment(Pos.CENTER);
        button.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        button.setPrefSize(125, 50);
        button.setOnAction(e -> {
            if (button.getText().equals("     View\nCommands")) {
                popup.show(primaryStage);
                button.setText("    Hide\nCommands");
            } else {
                popup.hide();
                button.setText("    View\nCommands");
            }
        });
        return button;
    }
}
