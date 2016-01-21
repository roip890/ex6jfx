package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by roi on 14/01/16.
 */
public class ProfessionalListRow extends ListCell<String> {

    @Override
    protected void updateItem(String professionalItem, boolean empty) {
        super.updateItem(professionalItem,empty);
        if (professionalItem != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("professionalRow.fxml"));
            professionalRowController controller = new professionalRowController(professionalItem);
            loader.setController(controller);

            Parent root = null;
            try{
                root = loader.load();
            } catch (IOException E) {

            }
            root.getStylesheets().add(getClass().getResource("professionalRow.css").toExternalForm());
            setGraphic(root);
        }
    }

}