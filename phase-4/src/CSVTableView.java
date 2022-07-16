import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * CSVTableView class to generate a table view from a CSV file.
 * @author Reetesh Sudhakar, Sebastian Jaskowski, Yash Gupta, Kunal Daga
 * @version 1.0
 */
public class CSVTableView extends TableView<String> {
    /**
     * Constructor to initialize the table view from a CSV file.
     * @param delimiter The delimiter used in the CSV file.
     * @param file The CSV file as a File object.
     * @throws IOException If the file is not found.
     */
    public CSVTableView(String delimiter, File file) throws IOException {

        // Get CSV file lines as List
        List<String> lines = Files.readAllLines(Paths.get(file.toURI()));

        // Get the header row
        String[] firstRow = lines.get(0).split(delimiter);

        // For each header/column, create TableColumn
        for (String columnName : firstRow) {
            TableColumn<String, String> column = new TableColumn<>(columnName);
            this.getColumns().add(column);

            column.setCellValueFactory(cellDataFeatures -> {
                String values = cellDataFeatures.getValue();
                String[] cells = values.split(delimiter);
                int columnIndex = cellDataFeatures.getTableView().getColumns().indexOf(cellDataFeatures.getTableColumn());
                if (columnIndex >= cells.length) {
                    return new SimpleStringProperty("");
                } else {
                    return new SimpleStringProperty(cells[columnIndex]);
                }
            });

            this.setItems(FXCollections.observableArrayList(lines));
            /*
            Remove header row, since it'll be added to the data at this point
            this only works if we're sure that our CSV file has a header.
            */
            this.getItems().remove(0);
        }
    }
}
