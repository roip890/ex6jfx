package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by roi on 11/01/16.
 */
public class errorMsg implements Runnable{
    String tag;
    String msg;

    public errorMsg(String tag, String msg) {
        this.tag = tag;
        this.msg = msg;
    }

    public void show() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("errorMsg.fxml"));
            errorMsgController controller = new errorMsgController(this.tag, this.msg);
            loader.setController(controller);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Error");
        Scene scene = new Scene(root, 250, 150);
        scene.getStylesheets().add(getClass().getResource("errorMsg.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();



    }

    @Override
    public void run() {
        show();
    }
}
