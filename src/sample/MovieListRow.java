package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by roi on 13/01/16.
 */
public class MovieListRow extends ListCell<String>{

    @Override
    protected void updateItem(String movieItem, boolean empty) {
        setItem(movieItem);

        if (movieItem != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movieRow.fxml"));
            movieRowController controller = new movieRowController(movieItem);
            loader.setController(controller);

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException E) {

            }
            root.getStylesheets().add(getClass().getResource("movieRow.css").toExternalForm());
            setGraphic(root);
        }
}

}