import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        VBox vBox = new VBox();
        VBox rootButtons = new VBox();
        for (int i = 0; i < 20; i++)
        {
            Button item = new Button("item " + i);
            item.setOnAction(e -> System.out.println("clicked"));
            vBox.getChildren().add(item);
        }
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setMaxHeight(500);//Adjust max height of the popup here
        scrollPane.setMaxWidth(200);//Adjust max width of the popup here
        popup.getContent().add(scrollPane);

        Button openPopup = new Button("Open Popup");
        openPopup.setOnAction(e -> popup.show(primaryStage));

        Button addNewButton = new Button("Add New Button");
        addNewButton.setOnAction(e -> {
            Button item = new Button("item " + vBox.getChildren().size());
            item.setOnAction(e2 -> System.out.println("clicked"));
            vBox.getChildren().add(item);
        });

        rootButtons.getChildren().addAll(openPopup, addNewButton);

        StackPane stackPane = new StackPane(rootButtons);
        Scene scene = new Scene(stackPane, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
