package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Created by roi on 11/01/16.
 */
public class succesMsgController {
    String tag;
    String msg;

    public succesMsgController(String tag, String msg) {
        this.msg = msg;
        this.tag = tag;
    }

    @FXML
    Label titleSuccess;

    @FXML
    Label lblSuccess;

    @FXML
    Button btnSuccess;

    @FXML
    void initialize(){
        titleSuccess.setText(this.tag);
        lblSuccess.setText(this.msg);
        btnSuccess.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage)(btnSuccess.getScene().getWindow())).close();
            }
        });
    }
}
