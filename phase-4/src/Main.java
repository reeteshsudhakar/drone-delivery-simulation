import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
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

/*
TODO: code cleanup
TODO: write more test cases to show the full system functionality
TODO: size up the popups
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
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setAlignment(buttons, Pos.CENTER);
        Scene mainScene = new Scene(root);

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

        Text text = new Text("Enter Arguments Manually Here:");
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
            showArgumentAlert();
        });

        Popup commands = makeTableViewPopup(new File("src/resources/arguments.csv"));
        Button showCommands = makeTableViewPopupButton(commands);
        Button showArguments = makeArgumentsButton();

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.getChildren().addAll(showArguments, textField, submit, showCommands);

        input.getChildren().addAll(text, hbox);
        return input;
    }

    public Popup makeTableViewPopup(File file) throws IOException {
        CSVTableView table = new CSVTableView(",", file);
        Popup popup = new Popup();
        popup.getContent().add(table);
        return popup;
    }

    public Button makeTableViewPopupButton(Popup popup) {
        Button button = new Button("     View\nCommands");
        button.setAlignment(Pos.CENTER);
        button.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
        button.setPrefSize(125, 50);
        button.setOnAction(e -> {
            if (button.getText().equals("     View\nCommands")) {
                popup.show(primaryStage);
                button.setText("     Hide\nCommands");
            } else {
                popup.hide();
                button.setText("     View\nCommands");
            }
        });
        return button;
    }

    public Button makeArgumentsButton() {
        Popup popup = makeArgumentsPopup();
        Button button = new Button("Arguments");
        button.setAlignment(Pos.CENTER);
        button.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        button.setPrefSize(125, 50);
        button.setOnAction(e -> popup.show(primaryStage));

        return button;
    }

    public Popup makeArgumentsPopup() {
        Popup popup = new Popup();

        StackPane popupRoot = new StackPane();
        popupRoot.setStyle("-fx-background-color: white; -fx-padding: 5px");
        popupRoot.setPrefWidth(popup.getWidth());
        popupRoot.setPrefHeight(popup.getHeight());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinWidth(800);
        scrollPane.setMaxWidth(800);
        scrollPane.setMinHeight(600);
        scrollPane.setMaxHeight(600);
        scrollPane.setStyle("-fx-padding: 5px");


        VBox popupBox = new VBox();
        popupBox.setStyle("-fx-background-color: white; -fx-spacing: 5px");

        HBox closeContainer = new HBox();
        closeContainer.setAlignment(Pos.CENTER_LEFT);
        Button closePopup = new Button("Close");
        closePopup.setOnAction(e -> popup.hide());
        closeContainer.getChildren().add(closePopup);
        popupBox.getChildren().addAll(closeContainer);

        VBox makeIngredientContainer = makeIngredientForm();
        VBox makeLocationContainer = makeLocationForm();
        VBox checkDistanceContainer = checkDistanceForm();
        VBox makeServiceContainer = makeServiceForm();
        VBox makeRestaurantContainer = makeRestaurantForm();
        VBox makeDroneContainer = makeDroneForm();
        VBox displayDronesContainer = DisplayDronesForm();
        VBox flyDroneContainer = flyDroneForm();
        VBox loadIngredientContainer = loadIngredientForm();
        VBox loadFuelContainer = loadFuelForm();
        VBox purchaseIngredientContainer = purchaseIngredientForm();
        VBox makePersonContainer = makePersonForm();
        VBox hireWorkerContainer = hireWorkerForm();
        VBox fireWorkerContainer = fireWorkerForm();
        VBox appointManagerContainer = appointManagerForm();
        VBox trainPilotContainer = trainPilotForm();
        VBox appointPilotContainer = appointPilotForm();
        VBox joinSwarmContainer = joinSwarmForm();
        VBox leaveSwarmContainer = leaveSwarmForm();
        VBox collectRevenueContainer = collectRevenueForm();

        popupBox.getChildren().addAll(makeIngredientContainer, makeLocationContainer, checkDistanceContainer, makeServiceContainer, makeRestaurantContainer, makeDroneContainer, displayDronesContainer, flyDroneContainer, loadIngredientContainer, loadFuelContainer, purchaseIngredientContainer, makePersonContainer, hireWorkerContainer, fireWorkerContainer, appointManagerContainer, trainPilotContainer, appointPilotContainer, joinSwarmContainer, leaveSwarmContainer, collectRevenueContainer);
        scrollPane.setContent(popupBox);
        popup.getContent().add(scrollPane);

        return popup;
    }

    public static VBox makeIngredientForm() {
        VBox makeIngredientContainer = new VBox();
        makeIngredientContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makeIngredientTitleContainer = new HBox();
        Text makeIngredientLabel = new Text("make_ingredient");
        makeIngredientTitleContainer.getChildren().addAll(makeIngredientLabel);
        makeIngredientTitleContainer.setAlignment(Pos.CENTER);

        HBox makeIngredientArguments = new HBox();
        makeIngredientArguments.setStyle("-fx-spacing: 3px");
        makeIngredientArguments.setMinWidth(800);
        makeIngredientArguments.setAlignment(Pos.CENTER);
        TextField makeIngredientBarcode = new TextField();
        makeIngredientBarcode.setPromptText("Barcode");
        TextField makeIngredientName = new TextField();
        makeIngredientName.setPromptText("Name");
        TextField makeIngredientWeight = new TextField();
        makeIngredientWeight.setPromptText("Weight");
        Button makeIngredientButton = new Button("Make Ingredient");

        makeIngredientButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_ingredient," + makeIngredientBarcode.getText() + "," + makeIngredientName.getText() + "," + Integer.parseInt(makeIngredientWeight.getText()));
            makeIngredientBarcode.clear();
            makeIngredientName.clear();
            makeIngredientWeight.clear();
            showArgumentAlert();
        });

        makeIngredientArguments.getChildren().addAll(makeIngredientBarcode, makeIngredientName, makeIngredientWeight, makeIngredientButton);
        makeIngredientContainer.getChildren().addAll(makeIngredientTitleContainer, makeIngredientArguments);

        return makeIngredientContainer;
    }

    public static VBox makeLocationForm() {
        VBox makeLocationContainer = new VBox();
        makeLocationContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makeLocationTitleContainer = new HBox();
        Text makeLocationLabel = new Text("make_location");
        makeLocationTitleContainer.getChildren().addAll(makeLocationLabel);
        makeLocationTitleContainer.setAlignment(Pos.CENTER);

        HBox makeLocationArguments = new HBox();
        makeLocationArguments.setStyle("-fx-spacing: 3px");
        makeLocationArguments.setMinWidth(800);
        makeLocationArguments.setAlignment(Pos.CENTER);
        TextField makeLocationName = new TextField();
        makeLocationName.setPromptText("Name");
        TextField makeLocationXCoordinate = new TextField();
        makeLocationXCoordinate.setPromptText("X Coordinate");
        TextField makeLocationYCoordinate = new TextField();
        makeLocationYCoordinate.setPromptText("Y Coordinate");
        TextField makeLocationSpaceLimit = new TextField();
        makeLocationSpaceLimit.setPromptText("Y Coordinate");
        Button makeLocationButton = new Button("Make Location");

        makeLocationButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_location," + makeLocationName.getText() + "," + Integer.parseInt(makeLocationXCoordinate.getText()) + "," + Integer.parseInt(makeLocationYCoordinate.getText()) + "," + Integer.parseInt(makeLocationSpaceLimit.getText()));
            makeLocationName.clear();
            makeLocationXCoordinate.clear();
            makeLocationYCoordinate.clear();
            makeLocationSpaceLimit.clear();
            showArgumentAlert();
        });

        makeLocationArguments.getChildren().addAll(makeLocationName, makeLocationXCoordinate, makeLocationYCoordinate, makeLocationSpaceLimit, makeLocationButton);
        makeLocationContainer.getChildren().addAll(makeLocationTitleContainer, makeLocationArguments);

        return makeLocationContainer;
    }

    public static VBox checkDistanceForm() {
        VBox checkDistanceContainer = new VBox();
        checkDistanceContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox checkDistanceTitleContainer = new HBox();
        Text checkDistanceLabel = new Text("check_distance");
        checkDistanceTitleContainer.getChildren().addAll(checkDistanceLabel);
        checkDistanceTitleContainer.setAlignment(Pos.CENTER);

        HBox checkLocationArguments = new HBox();
        checkLocationArguments.setStyle("-fx-spacing: 3px");
        checkLocationArguments.setMinWidth(800);
        checkLocationArguments.setAlignment(Pos.CENTER);
        TextField checkDistanceStartPoint = new TextField();
        checkDistanceStartPoint.setPromptText("Start Point");
        TextField checkDistanceEndPoint = new TextField();
        checkDistanceEndPoint.setPromptText("Destination");
        Button checkDistanceButton = new Button("Check Distance");

        checkDistanceButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("check_distance," + checkDistanceStartPoint.getText() + "," + checkDistanceEndPoint.getText());
            checkDistanceStartPoint.clear();
            checkDistanceEndPoint.clear();
            showArgumentAlert();
        });

        checkLocationArguments.getChildren().addAll(checkDistanceStartPoint, checkDistanceEndPoint, checkDistanceButton);
        checkDistanceContainer.getChildren().addAll(checkDistanceTitleContainer, checkLocationArguments);

        return checkDistanceContainer;
    }

    public static VBox makeServiceForm() {
        VBox makeServiceContainer = new VBox();
        makeServiceContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makeServiceTitleContainer = new HBox();
        Text makeServiceTitleLabel = new Text("make_service");
        makeServiceTitleContainer.getChildren().addAll(makeServiceTitleLabel);
        makeServiceTitleContainer.setAlignment(Pos.CENTER);

        HBox makeServiceArguments = new HBox();
        makeServiceArguments.setStyle("-fx-spacing: 3px");
        makeServiceArguments.setMinWidth(800);
        makeServiceArguments.setAlignment(Pos.CENTER);
        TextField makeServiceName = new TextField();
        makeServiceName.setPromptText("Name");
        TextField makeServiceRevenue = new TextField();
        makeServiceRevenue.setPromptText("Revenue");
        TextField makeServiceLocatedAt = new TextField();
        makeServiceLocatedAt.setPromptText("Located At");
        Button makeServiceButton = new Button("Make Service");

        makeServiceButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_service," + "," + makeServiceName.getText() + "," + Integer.parseInt(makeServiceRevenue.getText()) + "," + makeServiceLocatedAt.getText());
            makeServiceName.clear();
            makeServiceRevenue.clear();
            makeServiceLocatedAt.clear();
            showArgumentAlert();
        });

        makeServiceArguments.getChildren().addAll(makeServiceName, makeServiceRevenue, makeServiceLocatedAt, makeServiceButton);
        makeServiceContainer.getChildren().addAll(makeServiceTitleContainer, makeServiceArguments);

        return makeServiceContainer;
    }

    public static VBox makeRestaurantForm() {
        VBox makeRestaurantContainer = new VBox();
        makeRestaurantContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makeRestaurantTitleContainer = new HBox();
        Text makeRestaurantTitleLabel = new Text("make_restaurant");
        makeRestaurantTitleContainer.getChildren().addAll(makeRestaurantTitleLabel);
        makeRestaurantTitleContainer.setAlignment(Pos.CENTER);

        HBox makeRestaurantArguments = new HBox();
        makeRestaurantArguments.setStyle("-fx-spacing: 3px");
        makeRestaurantArguments.setMinWidth(800);
        makeRestaurantArguments.setAlignment(Pos.CENTER);
        TextField makeRestaurantName = new TextField();
        makeRestaurantName.setPromptText("Name");
        TextField makeRestaurantLocatedAt = new TextField();
        makeRestaurantLocatedAt.setPromptText("Located At");
        Button makeRestaurantButton = new Button("Make Service");

        makeRestaurantButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_restaurant" + "," + makeRestaurantName.getText() + "," + makeRestaurantLocatedAt.getText());
            makeRestaurantName.clear();
            makeRestaurantLocatedAt.clear();
            showArgumentAlert();
        });

        makeRestaurantArguments.getChildren().addAll(makeRestaurantName, makeRestaurantLocatedAt, makeRestaurantButton);
        makeRestaurantContainer.getChildren().addAll(makeRestaurantTitleContainer, makeRestaurantArguments);

        return makeRestaurantContainer;
    }

    public static VBox makeDroneForm() {
        VBox makeDroneContainer = new VBox();
        makeDroneContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makeDroneTitleContainer = new HBox();
        Text makeDroneTitleLabel = new Text("make_drone");
        makeDroneTitleContainer.getChildren().addAll(makeDroneTitleLabel);
        makeDroneTitleContainer.setAlignment(Pos.CENTER);

        HBox makeDroneArguments = new HBox();
        makeDroneArguments.setStyle("-fx-spacing: 3px");
        makeDroneArguments.setMinWidth(800);
        makeDroneArguments.setAlignment(Pos.CENTER);
        TextField makeDroneServiceName = new TextField();
        makeDroneServiceName.setPromptText("Service Name");
        TextField makeDroneTag = new TextField();
        makeDroneTag.setPromptText("Tag");
        TextField makeDroneCapacity = new TextField();
        makeDroneCapacity.setPromptText("Capacity");
        TextField makeDroneFuel = new TextField();
        makeDroneFuel.setPromptText("Fuel");
        Button makeDroneButton = new Button("Make Drone");

        makeDroneButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_drone," + makeDroneServiceName.getText() + "," + Integer.parseInt(makeDroneTag.getText()) + "," + Integer.parseInt(makeDroneCapacity.getText()) + "," + Integer.parseInt(makeDroneFuel.getText()));
            makeDroneServiceName.clear();
            makeDroneTag.clear();
            makeDroneCapacity.clear();
            makeDroneFuel.clear();
            showArgumentAlert();
        });

        makeDroneArguments.getChildren().addAll(makeDroneServiceName, makeDroneTag, makeDroneCapacity, makeDroneFuel, makeDroneButton);
        makeDroneContainer.getChildren().addAll(makeDroneTitleContainer, makeDroneArguments);

        return makeDroneContainer;
    }

    public static VBox DisplayDronesForm() {
        VBox displayDronesContainer = new VBox();
        displayDronesContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox displayDronesTitleContainer = new HBox();
        Text displayDronesTitleLabel = new Text("display_drones");
        displayDronesTitleContainer.getChildren().addAll(displayDronesTitleLabel);
        displayDronesTitleContainer.setAlignment(Pos.CENTER);

        HBox displayDronesArguments = new HBox();
        displayDronesArguments.setStyle("-fx-spacing: 3px");
        displayDronesArguments.setMinWidth(800);
        displayDronesArguments.setAlignment(Pos.CENTER);
        ComboBox<String> displayDronesComboBox = new ComboBox<>();
        displayDronesComboBox.setPromptText("Select Service");
        Button displayDronesButton = new Button("Display Drones");
        if (DeliveryService.services.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                displayDronesComboBox.getItems().add(service.getName());
            }
        } else {
            displayDronesComboBox.setDisable(true);
            displayDronesButton.setDisable(true);
        }

        displayDronesButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("display_drones," + displayDronesComboBox.getValue());
            displayDronesComboBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        displayDronesArguments.getChildren().addAll(displayDronesComboBox, displayDronesButton);
        displayDronesContainer.getChildren().addAll(displayDronesTitleContainer, displayDronesArguments);

        return displayDronesContainer;
    }

    public static VBox flyDroneForm() {
        VBox flyDroneContainer = new VBox();
        flyDroneContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox flyDroneTitleContainer = new HBox();
        Text flyDroneTitleLabel = new Text("fly_drone");
        flyDroneTitleContainer.getChildren().addAll(flyDroneTitleLabel);
        flyDroneTitleContainer.setAlignment(Pos.CENTER);

        HBox flyDroneArguments = new HBox();
        flyDroneArguments.setStyle("-fx-spacing: 3px");
        flyDroneArguments.setMinWidth(800);
        flyDroneArguments.setAlignment(Pos.CENTER);
        ComboBox<String> flyDroneServicesBox = new ComboBox<>();
        flyDroneServicesBox.setPromptText("Select Service");
        TextField flyDroneTag = new TextField();
        flyDroneTag.setPromptText("Tag");
        ComboBox<String> flyDroneLocationsBox = new ComboBox<>();
        flyDroneLocationsBox.setPromptText("Select Location");
        Button flyDroneButton = new Button("Fly Drone");
        if (DeliveryService.services.size() > 0 && Location.locations.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                flyDroneServicesBox.getItems().add(service.getName());
            }

            for (Location location : Location.locations.values()) {
                flyDroneLocationsBox.getItems().add(location.getName());
            }
        } else {
            flyDroneServicesBox.setDisable(true);
            flyDroneTag.setDisable(true);
            flyDroneLocationsBox.setDisable(true);
            flyDroneButton.setDisable(true);
        }

        flyDroneButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("fly_drone," + flyDroneServicesBox.getValue() + "," + Integer.parseInt(flyDroneTag.getText()) + "," + flyDroneLocationsBox.getValue());
            flyDroneServicesBox.getSelectionModel().clearSelection();
            flyDroneTag.clear();
            flyDroneLocationsBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        flyDroneArguments.getChildren().addAll(flyDroneServicesBox, flyDroneTag, flyDroneLocationsBox, flyDroneButton);
        flyDroneContainer.getChildren().addAll(flyDroneTitleContainer, flyDroneArguments);

        return flyDroneContainer;
    }

    public static VBox loadIngredientForm() {
        VBox loadIngredientContainer = new VBox();
        loadIngredientContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox loadIngredientTitleContainer = new HBox();
        Text loadIngredientTitleLabel = new Text("load_ingredient");
        loadIngredientTitleContainer.getChildren().addAll(loadIngredientTitleLabel);
        loadIngredientTitleContainer.setAlignment(Pos.CENTER);

        HBox loadIngredientArguments = new HBox();
        loadIngredientArguments.setStyle("-fx-spacing: 3px");
        loadIngredientArguments.setMinWidth(800);
        loadIngredientArguments.setAlignment(Pos.CENTER);
        ComboBox<String> loadIngredientServicesBox = new ComboBox<>();
        loadIngredientServicesBox.setPromptText("Select Service");
        TextField flyDroneTag = new TextField();
        flyDroneTag.setPromptText("Tag");
        ComboBox<String> loadIngredientBarcodesBox = new ComboBox<>();
        TextField loadIngredientQuantity = new TextField();
        flyDroneTag.setPromptText("Quantity");
        TextField loadIngredientUnitPrice = new TextField();
        flyDroneTag.setPromptText("Unit Price");
        loadIngredientBarcodesBox.setPromptText("Select Location");
        Button flyDroneButton = new Button("Fly Drone");
        if (DeliveryService.services.size() > 0 && Ingredient.ingredients.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                loadIngredientServicesBox.getItems().add(service.getName());
            }

            for (Ingredient ingredient : Ingredient.ingredients.values()) {
                loadIngredientBarcodesBox.getItems().add(ingredient.getBarcode());
            }
        } else {
            loadIngredientServicesBox.setDisable(true);
            flyDroneTag.setDisable(true);
            loadIngredientBarcodesBox.setDisable(true);
            loadIngredientQuantity.setDisable(true);
            loadIngredientUnitPrice.setDisable(true);
            flyDroneButton.setDisable(true);
        }

        flyDroneButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("load_ingredient," + loadIngredientServicesBox.getValue() + "," + Integer.parseInt(flyDroneTag.getText()) + "," + loadIngredientBarcodesBox.getValue() + "," + Integer.parseInt(loadIngredientQuantity.getText()) + "," + Integer.parseInt(loadIngredientUnitPrice.getText()));
            loadIngredientServicesBox.getSelectionModel().clearSelection();
            flyDroneTag.clear();
            loadIngredientBarcodesBox.getSelectionModel().clearSelection();
            loadIngredientQuantity.clear();
            loadIngredientUnitPrice.clear();
            showArgumentAlert();
        });

        loadIngredientArguments.getChildren().addAll(loadIngredientServicesBox, flyDroneTag, loadIngredientBarcodesBox, loadIngredientQuantity, loadIngredientUnitPrice, flyDroneButton);
        loadIngredientContainer.getChildren().addAll(loadIngredientTitleContainer, loadIngredientArguments);

        return loadIngredientContainer;

    }

    public static VBox loadFuelForm() {
        VBox loadFuelContainer = new VBox();
        loadFuelContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox loadFuelTitleContainer = new HBox();
        Text loadFuelTitleLabel = new Text("load_fuel");
        loadFuelTitleContainer.getChildren().addAll(loadFuelTitleLabel);
        loadFuelTitleContainer.setAlignment(Pos.CENTER);

        HBox loadFuelArguments = new HBox();
        loadFuelArguments.setStyle("-fx-spacing: 3px");
        loadFuelArguments.setMinWidth(800);
        loadFuelArguments.setAlignment(Pos.CENTER);
        ComboBox<String> loadFuelServicesBox = new ComboBox<>();
        loadFuelServicesBox.setPromptText("Select Service");
        TextField loadFuelDroneTag = new TextField();
        loadFuelDroneTag.setPromptText("Tag");
        TextField loadFuelPetrol = new TextField();
        loadFuelPetrol.setPromptText("Petrol");
        Button flyDroneButton = new Button("Fly Drone");
        if (DeliveryService.services.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                loadFuelServicesBox.getItems().add(service.getName());
            }
        } else {
            loadFuelServicesBox.setDisable(true);
            loadFuelDroneTag.setDisable(true);
            flyDroneButton.setDisable(true);
        }

        flyDroneButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("load_fuel," + loadFuelServicesBox.getValue() + "," + Integer.parseInt(loadFuelDroneTag.getText()) + "," + Integer.parseInt(loadFuelPetrol.getText()));
            loadFuelServicesBox.getSelectionModel().clearSelection();
            loadFuelDroneTag.clear();
            loadFuelPetrol.clear();
            showArgumentAlert();
        });

        loadFuelArguments.getChildren().addAll(loadFuelServicesBox, loadFuelDroneTag, loadFuelPetrol, flyDroneButton);
        loadFuelContainer.getChildren().addAll(loadFuelTitleContainer, loadFuelArguments);

        return loadFuelContainer;
    }

    public static VBox purchaseIngredientForm() {
        VBox purchaseIngredientContainer = new VBox();
        purchaseIngredientContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox purchaseIngredientTitleContainer = new HBox();
        Text purchaseIngredientTitleLabel = new Text("purchase_ingredient");
        purchaseIngredientTitleContainer.getChildren().addAll(purchaseIngredientTitleLabel);
        purchaseIngredientTitleContainer.setAlignment(Pos.CENTER);

        HBox purhcaseIngredientArguments = new HBox();
        purhcaseIngredientArguments.setStyle("-fx-spacing: 3px");
        purhcaseIngredientArguments.setMinWidth(800);
        purhcaseIngredientArguments.setAlignment(Pos.CENTER);
        ComboBox<String> purchaseIngredientRestaurantBox = new ComboBox<>();
        purchaseIngredientRestaurantBox.setPromptText("Select Restaurant");
        ComboBox<String> purchaseIngredientServicesBox = new ComboBox<>();
        purchaseIngredientServicesBox.setPromptText("Select Service");
        TextField purchaseIngredientTag = new TextField();
        purchaseIngredientTag.setPromptText("Tag");
        ComboBox<String> purchaseIngredientBarcodesBox = new ComboBox<>();
        purchaseIngredientBarcodesBox.setPromptText("Select Barcode");
        TextField purchaseIngredientQuantity = new TextField();
        purchaseIngredientQuantity.setPromptText("Quantity");
        Button purchaseIngredientButton = new Button("Purchase Ingredient");
        if (Ingredient.ingredients.size() > 0 && DeliveryService.services.size() > 0 && Restaurant.restaurants.size() > 0) {
            for (Restaurant restaurant : Restaurant.restaurants.values()) {
                purchaseIngredientRestaurantBox.getItems().add(restaurant.getName());
            }
            for (DeliveryService service : DeliveryService.services.values()) {
                purchaseIngredientServicesBox.getItems().add(service.getName());
            }
            for (Ingredient ingredient : Ingredient.ingredients.values()) {
                purchaseIngredientBarcodesBox.getItems().add(ingredient.getBarcode());
            }
        } else {
            purchaseIngredientRestaurantBox.setDisable(true);
            purchaseIngredientServicesBox.setDisable(true);
            purchaseIngredientTag.setDisable(true);
            purchaseIngredientBarcodesBox.setDisable(true);
            purchaseIngredientQuantity.setDisable(true);
            purchaseIngredientButton.setDisable(true);
        }

        purchaseIngredientButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("purchase_ingredient," + purchaseIngredientRestaurantBox.getValue() + "," + purchaseIngredientServicesBox.getValue() + "," + Integer.parseInt(purchaseIngredientTag.getText()) + "," + purchaseIngredientBarcodesBox.getValue() + "," + Integer.parseInt(purchaseIngredientQuantity.getText()));
            purchaseIngredientRestaurantBox.getSelectionModel().clearSelection();
            purchaseIngredientServicesBox.getSelectionModel().clearSelection();
            purchaseIngredientTag.clear();
            purchaseIngredientBarcodesBox.getSelectionModel().clearSelection();
            purchaseIngredientQuantity.clear();
            showArgumentAlert();
        });

        purhcaseIngredientArguments.getChildren().addAll(purchaseIngredientRestaurantBox, purchaseIngredientServicesBox, purchaseIngredientTag, purchaseIngredientBarcodesBox, purchaseIngredientQuantity, purchaseIngredientButton);
        purchaseIngredientContainer.getChildren().addAll(purchaseIngredientTitleContainer, purhcaseIngredientArguments);

        return purchaseIngredientContainer;
    }

    public static VBox makePersonForm() {
        VBox makePersonContainer = new VBox();
        makePersonContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox makePersonTitleContainer = new HBox();
        Text makePersonTitleLabel = new Text("make_person");
        makePersonTitleContainer.getChildren().addAll(makePersonTitleLabel);
        makePersonTitleContainer.setAlignment(Pos.CENTER);

        HBox makePersonArguments = new HBox();
        makePersonArguments.setStyle("-fx-spacing: 3px");
        makePersonArguments.setMinWidth(800);
        makePersonArguments.setAlignment(Pos.CENTER);
        TextField makePersonUserName = new TextField();
        makePersonUserName.setPromptText("Username");
        TextField makePersonFirstName = new TextField();
        makePersonFirstName.setPromptText("First Name");
        TextField makePersonLastName = new TextField();
        makePersonLastName.setPromptText("Last Name");
        TextField makePersonYear = new TextField();
        makePersonYear.setPromptText("Year of Birth");
        TextField makePersonMonth = new TextField();
        makePersonMonth.setPromptText("Month of Birth");
        TextField makePersonDay = new TextField();
        makePersonDay.setPromptText("Day of Birth");
        TextField makePersonAddress = new TextField();
        makePersonAddress.setPromptText("Address");
        Button makePersonButton = new Button("Make Person");

        makePersonButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("make_person," + makePersonUserName.getText() + "," + makePersonFirstName.getText() + "," + makePersonLastName.getText() + "," + Integer.parseInt(makePersonYear.getText()) + "," + Integer.parseInt(makePersonMonth.getText()) + "," + Integer.parseInt(makePersonDay.getText()) + "," + makePersonAddress.getText());
            makePersonUserName.clear();
            makePersonFirstName.clear();
            makePersonLastName.clear();
            makePersonYear.clear();
            makePersonMonth.clear();
            makePersonDay.clear();
            makePersonAddress.clear();
            showArgumentAlert();
        });

        makePersonArguments.getChildren().addAll(makePersonUserName, makePersonFirstName, makePersonLastName, makePersonYear, makePersonMonth, makePersonDay, makePersonAddress, makePersonButton);
        makePersonContainer.getChildren().addAll(makePersonTitleContainer, makePersonArguments);

        return makePersonContainer;
    }

    public static VBox hireWorkerForm() {
        VBox hireWorkerContainer = new VBox();
        hireWorkerContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox hireWorkerTitleContainer = new HBox();
        Text hireWorkerTitleLabel = new Text("hire_worker");
        hireWorkerTitleContainer.getChildren().addAll(hireWorkerTitleLabel);
        hireWorkerTitleContainer.setAlignment(Pos.CENTER);

        HBox hireWorkerArguments = new HBox();
        hireWorkerArguments.setStyle("-fx-spacing: 3px");
        hireWorkerArguments.setMinWidth(800);
        hireWorkerArguments.setAlignment(Pos.CENTER);
        ComboBox<String> hireWorkerServicesBox = new ComboBox<>();
        hireWorkerServicesBox.setPromptText("Select Service");
        ComboBox<String> hireWorkerUsernamesBox = new ComboBox<>();
        hireWorkerUsernamesBox.setPromptText("Select Username");
        Button hireWorkerButton = new Button("Hire Worker");
        if (DeliveryService.services.size() > 0 && Person.people.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                hireWorkerServicesBox.getItems().add(service.getName());
            }
            for (Person person : Person.people.values()) {
                hireWorkerUsernamesBox.getItems().add(person.getUsername());
            }
        } else {
            hireWorkerServicesBox.setDisable(true);
            hireWorkerUsernamesBox.setDisable(true);
            hireWorkerButton.setDisable(true);
        }

        hireWorkerButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("hire_worker," + hireWorkerServicesBox.getValue() + "," + hireWorkerUsernamesBox.getValue());
            hireWorkerServicesBox.getSelectionModel().clearSelection();
            hireWorkerUsernamesBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        hireWorkerArguments.getChildren().addAll(hireWorkerServicesBox, hireWorkerUsernamesBox, hireWorkerButton);
        hireWorkerContainer.getChildren().addAll(hireWorkerTitleContainer, hireWorkerArguments);

        return hireWorkerContainer;
    }

    public static VBox fireWorkerForm() {
        VBox fireWorkerContainer = new VBox();
        fireWorkerContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox fireWorkerTitleContainer = new HBox();
        Text hireWorkerTitleLabel = new Text("fire_worker");
        fireWorkerTitleContainer.getChildren().addAll(hireWorkerTitleLabel);
        fireWorkerTitleContainer.setAlignment(Pos.CENTER);

        HBox fireWorkerArguments = new HBox();
        fireWorkerArguments.setStyle("-fx-spacing: 3px");
        fireWorkerArguments.setMinWidth(800);
        fireWorkerArguments.setAlignment(Pos.CENTER);
        ComboBox<String> fireWorkerServicesBox = new ComboBox<>();
        fireWorkerServicesBox.setPromptText("Select Service");
        ComboBox<String> fireWorkerUsernamesBox = new ComboBox<>();
        fireWorkerUsernamesBox.setPromptText("Select Username");
        Button hireWorkerButton = new Button("Fire Worker");
        if (DeliveryService.services.size() > 0 && Person.people.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                fireWorkerServicesBox.getItems().add(service.getName());
            }
            for (Person person : Person.people.values()) {
                fireWorkerUsernamesBox.getItems().add(person.getUsername());
            }
        } else {
            fireWorkerServicesBox.setDisable(true);
            fireWorkerUsernamesBox.setDisable(true);
            hireWorkerButton.setDisable(true);
        }

        hireWorkerButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("fire_worker," + fireWorkerServicesBox.getValue() + "," + fireWorkerUsernamesBox.getValue());
            fireWorkerServicesBox.getSelectionModel().clearSelection();
            fireWorkerUsernamesBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        fireWorkerArguments.getChildren().addAll(fireWorkerServicesBox, fireWorkerUsernamesBox, hireWorkerButton);
        fireWorkerContainer.getChildren().addAll(fireWorkerTitleContainer, fireWorkerArguments);

        return fireWorkerContainer;
    }

    public static VBox appointManagerForm() {
        VBox appointManagerContainer = new VBox();
        appointManagerContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox appointManagerTitleContainer = new HBox();
        Text appointManagerTitleLabel = new Text("appoint_manager");
        appointManagerTitleContainer.getChildren().addAll(appointManagerTitleLabel);
        appointManagerTitleContainer.setAlignment(Pos.CENTER);

        HBox appointManagerArguments  = new HBox();
        appointManagerArguments.setStyle("-fx-spacing: 3px");
        appointManagerArguments.setMinWidth(800);
        appointManagerArguments.setAlignment(Pos.CENTER);
        ComboBox<String> appointManagerServicesBox = new ComboBox<>();
        appointManagerServicesBox.setPromptText("Select Service");
        ComboBox<String> appointManagerUsernamesBox = new ComboBox<>();
        appointManagerUsernamesBox.setPromptText("Select Username");
        Button hireWorkerButton = new Button("Hire Worker");
        if (DeliveryService.services.size() > 0 && Person.people.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                appointManagerServicesBox.getItems().add(service.getName());
            }
            for (Person person : Person.people.values()) {
                appointManagerUsernamesBox.getItems().add(person.getUsername());
            }
        } else {
            appointManagerServicesBox.setDisable(true);
            appointManagerUsernamesBox.setDisable(true);
            hireWorkerButton.setDisable(true);
        }

        hireWorkerButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("appoint_manager," + appointManagerServicesBox.getValue() + "," + appointManagerUsernamesBox.getValue());
            appointManagerServicesBox.getSelectionModel().clearSelection();
            appointManagerUsernamesBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        appointManagerArguments.getChildren().addAll(appointManagerServicesBox, appointManagerUsernamesBox, hireWorkerButton);
        appointManagerContainer.getChildren().addAll(appointManagerTitleContainer, appointManagerArguments);

        return appointManagerContainer;
    }

    public static VBox trainPilotForm() {
        VBox trainPilotContainer = new VBox();
        trainPilotContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox trainPilotTitleContainer = new HBox();
        Text trainPilotTitleLabel = new Text("train_pilot");
        trainPilotTitleContainer.getChildren().addAll(trainPilotTitleLabel);
        trainPilotTitleContainer.setAlignment(Pos.CENTER);

        HBox trainPilotArguments = new HBox();
        trainPilotArguments.setStyle("-fx-spacing: 3px");
        trainPilotArguments.setMinWidth(800);
        trainPilotArguments.setAlignment(Pos.CENTER);
        ComboBox<String> trainPilotServicesBox = new ComboBox<>();
        trainPilotServicesBox.setPromptText("Select Service");
        ComboBox<String> trainPilotUsernamesBox = new ComboBox<>();
        trainPilotUsernamesBox.setPromptText("Select Username");
        TextField trainPilotLicense = new TextField();
        trainPilotLicense.setPromptText("License");
        TextField trainPilotExperience = new TextField();
        trainPilotExperience.setPromptText("Experience");
        Button trainPilotButton = new Button("Train Pilot");
        if (DeliveryService.services.size() > 0 && Person.people.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                trainPilotServicesBox.getItems().add(service.getName());
            }
            for (Person person : Person.people.values()) {
                trainPilotUsernamesBox.getItems().add(person.getUsername());
            }
        } else {
            trainPilotServicesBox.setDisable(true);
            trainPilotUsernamesBox.setDisable(true);
            trainPilotLicense.setDisable(true);
            trainPilotExperience.setDisable(true);
            trainPilotButton.setDisable(true);
        }

        trainPilotButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("train_pilot," + trainPilotServicesBox.getValue() + "," + trainPilotUsernamesBox.getValue() + "," + trainPilotLicense.getText() + "," + trainPilotExperience.getText());
            trainPilotServicesBox.getSelectionModel().clearSelection();
            trainPilotUsernamesBox.getSelectionModel().clearSelection();
            trainPilotLicense.clear();
            trainPilotExperience.clear();
            showArgumentAlert();
        });

        trainPilotArguments.getChildren().addAll(trainPilotServicesBox, trainPilotUsernamesBox, trainPilotLicense, trainPilotExperience, trainPilotButton);
        trainPilotContainer.getChildren().addAll(trainPilotTitleContainer, trainPilotArguments);

        return trainPilotContainer;
    }

    public static VBox appointPilotForm() {
        VBox appointPilotContainer = new VBox();
        appointPilotContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox appointPilotTitleContainer = new HBox();
        Text appointPilotTitleLabel = new Text("appoint_pilot");
        appointPilotTitleContainer.getChildren().addAll(appointPilotTitleLabel);
        appointPilotTitleContainer.setAlignment(Pos.CENTER);

        HBox appointPilotArguments = new HBox();
        appointPilotArguments.setStyle("-fx-spacing: 3px");
        appointPilotArguments.setMinWidth(800);
        appointPilotArguments.setAlignment(Pos.CENTER);
        ComboBox<String> appointPilotServicesBox = new ComboBox<>();
        appointPilotServicesBox.setPromptText("Select Service");
        ComboBox<String> appointPilotUsernamesBox = new ComboBox<>();
        appointPilotUsernamesBox.setPromptText("Select Username");
        TextField appointPilotDroneTag = new TextField();
        appointPilotDroneTag.setPromptText("Drone Tag");
        Button appointPilotButton = new Button("Appoint Pilot");
        if (DeliveryService.services.size() > 0 && Person.people.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                appointPilotServicesBox.getItems().add(service.getName());
            }
            for (Person person : Person.people.values()) {
                appointPilotUsernamesBox.getItems().add(person.getUsername());
            }
        } else {
            appointPilotServicesBox.setDisable(true);
            appointPilotUsernamesBox.setDisable(true);
            appointPilotDroneTag.setDisable(true);
            appointPilotButton.setDisable(true);
        }

        appointPilotButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("appoint_pilot," + appointPilotServicesBox.getValue() + "," + appointPilotUsernamesBox.getValue() + "," + Integer.parseInt(appointPilotDroneTag.getText()));
            appointPilotServicesBox.getSelectionModel().clearSelection();
            appointPilotUsernamesBox.getSelectionModel().clearSelection();
            appointPilotDroneTag.clear();
            showArgumentAlert();
        });

        appointPilotArguments.getChildren().addAll(appointPilotServicesBox, appointPilotUsernamesBox, appointPilotDroneTag, appointPilotButton);
        appointPilotContainer.getChildren().addAll(appointPilotTitleContainer, appointPilotArguments);

        return appointPilotContainer;
    }

    public static VBox joinSwarmForm() {
        VBox joinSwarmContainer = new VBox();
        joinSwarmContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox joinSwarmTitleContainer = new HBox();
        Text joinSwarmTitleLabel = new Text("join_swarm");
        joinSwarmTitleContainer.getChildren().addAll(joinSwarmTitleLabel);
        joinSwarmTitleContainer.setAlignment(Pos.CENTER);

        HBox joinSwarmArguments = new HBox();
        joinSwarmArguments.setStyle("-fx-spacing: 3px");
        joinSwarmArguments.setMinWidth(800);
        joinSwarmArguments.setAlignment(Pos.CENTER);
        ComboBox<String> joinSwarmServicesBox = new ComboBox<>();
        joinSwarmServicesBox.setPromptText("Select Service");
        TextField appointPilotLeadDroneTag = new TextField();
        appointPilotLeadDroneTag.setPromptText("Lead Drone Tag");
        TextField appointPilotSwarmDroneTag = new TextField();
        appointPilotSwarmDroneTag.setPromptText("Swarm Drone Tag");
        Button joinSwarmButton = new Button("Join Swarm");
        if (DeliveryService.services.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                joinSwarmServicesBox.getItems().add(service.getName());
            }
        } else {
            joinSwarmServicesBox.setDisable(true);
            appointPilotLeadDroneTag.setDisable(true);
            appointPilotSwarmDroneTag.setDisable(true);
            joinSwarmButton.setDisable(true);
        }

        joinSwarmButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("join_swarm," + joinSwarmServicesBox.getValue() + "," + Integer.parseInt(appointPilotLeadDroneTag.getText()) + "," + Integer.parseInt(appointPilotSwarmDroneTag.getText()));
            joinSwarmServicesBox.getSelectionModel().clearSelection();
            appointPilotLeadDroneTag.clear();
            appointPilotSwarmDroneTag.clear();
            showArgumentAlert();
        });

        joinSwarmArguments.getChildren().addAll(joinSwarmServicesBox, appointPilotLeadDroneTag, appointPilotSwarmDroneTag, joinSwarmButton);
        joinSwarmContainer.getChildren().addAll(joinSwarmTitleContainer, joinSwarmArguments);

        return joinSwarmContainer;
    }

    public static VBox leaveSwarmForm() {
        VBox leaveSwarmContainer = new VBox();
        leaveSwarmContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox leaveSwarmTitleContainer = new HBox();
        Text leaveSwarmTitleLabel = new Text("leave_swarm");
        leaveSwarmTitleContainer.getChildren().addAll(leaveSwarmTitleLabel);
        leaveSwarmTitleContainer.setAlignment(Pos.CENTER);

        HBox leaveSwarmArguments = new HBox();
        leaveSwarmArguments.setStyle("-fx-spacing: 3px");
        leaveSwarmArguments.setMinWidth(800);
        leaveSwarmArguments.setAlignment(Pos.CENTER);
        ComboBox<String> leaveSwarmServicesBox = new ComboBox<>();
        leaveSwarmServicesBox.setPromptText("Select Service");
        TextField leaveSwarmDroneTag = new TextField();
        leaveSwarmDroneTag.setPromptText("Swarm Drone Tag");
        Button joinSwarmButton = new Button("Leave Swarm");
        if (DeliveryService.services.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                leaveSwarmServicesBox.getItems().add(service.getName());
            }
        } else {
            leaveSwarmServicesBox.setDisable(true);
            leaveSwarmDroneTag.setDisable(true);
            joinSwarmButton.setDisable(true);
        }

        joinSwarmButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("leave_swarm," + leaveSwarmServicesBox.getValue() + "," + Integer.parseInt(leaveSwarmDroneTag.getText()));
            leaveSwarmServicesBox.getSelectionModel().clearSelection();
            leaveSwarmDroneTag.clear();
            showArgumentAlert();
        });

        leaveSwarmArguments.getChildren().addAll(leaveSwarmServicesBox, leaveSwarmDroneTag, joinSwarmButton);
        leaveSwarmContainer.getChildren().addAll(leaveSwarmTitleContainer, leaveSwarmArguments);

        return leaveSwarmContainer;
    }

    public static VBox collectRevenueForm() {
        VBox collectRevenueContainer = new VBox();
        collectRevenueContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox collectRevenueTitleContainer = new HBox();
        Text collectRevenueTitleLabel = new Text("collect_revenue");
        collectRevenueTitleContainer.getChildren().addAll(collectRevenueTitleLabel);
        collectRevenueTitleContainer.setAlignment(Pos.CENTER);

        HBox collectRevenueArguments = new HBox();
        collectRevenueArguments.setStyle("-fx-spacing: 3px");
        collectRevenueArguments.setMinWidth(800);
        collectRevenueArguments.setAlignment(Pos.CENTER);
        ComboBox<String> leaveSwarmServicesBox = new ComboBox<>();
        leaveSwarmServicesBox.setPromptText("Select Service");
        Button leaveSwarmButton = new Button("Collect Revenue");
        if (DeliveryService.services.size() > 0) {
            for (DeliveryService service : DeliveryService.services.values()) {
                leaveSwarmServicesBox.getItems().add(service.getName());
            }
        } else {
            leaveSwarmServicesBox.setDisable(true);
            leaveSwarmButton.setDisable(true);
        }

        leaveSwarmButton.setOnAction(e -> {
            InterfaceLoop.commandLoop("collect_revenue," + leaveSwarmServicesBox.getValue());
            leaveSwarmServicesBox.getSelectionModel().clearSelection();
            showArgumentAlert();
        });

        collectRevenueArguments.getChildren().addAll(leaveSwarmServicesBox, leaveSwarmButton);
        collectRevenueContainer.getChildren().addAll(collectRevenueTitleContainer, collectRevenueArguments);

        return collectRevenueContainer;
    }

    public static void showArgumentAlert() {
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
            case "DISPLAY": {
                break;
            }
        }
    }
}
