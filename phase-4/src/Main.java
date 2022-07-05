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
import javafx.stage.Stage;

/**
 * Main class to run the interface for the ingredient purchasing system.
 *
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 2.0
 */
public class Main extends Application {
    public static String status;
    public static String message;

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

        addAsset(grid, 0, 0, "drone.png", "Display Drones");
        addAsset(grid, 0, 1, "ingredient.png", "Display Ingredients");
        addAsset(grid, 0, 2, "person.png", "Display People");
        addAsset(grid, 0, 3, "restaurant.png", "Display Restaurants");
        addAsset(grid, 0, 4, "service.png", "Display Services");


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

        VBox input = addArgumentInput();

        root.getChildren().addAll(background, title, grid, input);
        root.setAlignment(title, Pos.TOP_CENTER);
        stage.setScene(new Scene(root));
        stage.setTitle("Ingredient Delivery System");
        stage.setMinWidth(background.getFitWidth());
        stage.setMinHeight(background.getFitHeight());
        stage.setResizable(true);
        stage.show();
    }

    public void addAsset(GridPane grid, int row, int column, String fileName, String buttonName) {
        Image image = new Image("resources/" + fileName, 250, 250, true, true);
        ImageView imageView = new ImageView(image);
        Button button = new Button(buttonName);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(imageView, button);

        grid.add(vbox, column, row);
    }

    public VBox addArgumentInput() {
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
            if (status.equals("ERROR")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(status);
                alert.setHeaderText(message);
                alert.showAndWait();
            } else if (status.equals("OK")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(status);
                alert.setHeaderText(message);
                alert.showAndWait();
            } else if (status.equals("STOP")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(status);
                alert.setHeaderText(message);
                alert.showAndWait();
                System.exit(0);
            }
        });

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.getChildren().addAll(textField, submit);

        input.getChildren().addAll(text, hbox);
        return input;
    }

    public static void displayMessage(String output) {
        status = "OK";
        message = output;
    }

    public static void displayError(String output) {
        status = "ERROR";
        message = output;
    }

    public static void closeWindow(String output) {
        status = "STOP";
        message = output;
    }
}
