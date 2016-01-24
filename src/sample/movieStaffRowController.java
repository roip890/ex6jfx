package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.ArrayList;

/**
 * Created by roi on 14/01/16.
 */
public class movieStaffRowController {
    String type;
    String names;
    public movieStaffRowController(ArrayList<String> staffType) {
        this.type = staffType.get(1);
        boolean isFirst = true;
        StringBuilder strBldr = new StringBuilder("");
        for (int i = 2; i < staffType.size(); ++i) {
            if (isFirst) {
                strBldr.append(staffType.get(i));
                isFirst = false;
            } else {
                strBldr.append(", " + staffType.get(i));
            }
        }
        this.names = strBldr.toString();
    }

    @FXML
    Label lblStaffTypeOfMovie;

    @FXML
    Label lblStaffNamesOfMovie;

    @FXML
    public void initialize() {
        lblStaffTypeOfMovie.setText(this.type);
        lblStaffNamesOfMovie.setText(this.names);
    }
}
