package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by roi on 11/01/16.
 */
public class addMovieController {

    @FXML
    TextField txtCode;
    @FXML
    TextField txtMovieName;
    @FXML
    TextField txtLength;
    @FXML
    TextField txtDescription;
    @FXML
    TextField txtYear;
    @FXML
    TextField txtImage;
    @FXML
    Button btnAddMovie;
    @FXML
    Slider sliderRating;
    @FXML
    Label lblRankNum;
    @FXML
    TextArea txtGenres;
    @FXML
    Button btnCancelMovie;

    @FXML
    void initialize(){

        sliderRating.setMin(0.0);
        sliderRating.setMax(10.0);
        sliderRating.setBlockIncrement(0.1);
        sliderRating.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lblRankNum.setText(Double.toString(newValue.doubleValue()).format("%.1f", newValue.doubleValue()));
            }
        });

        btnAddMovie.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((txtCode.getText().length() == 0) || (txtMovieName.getText().length() == 0) || (txtLength.getText().length() == 0) ||
                        (txtYear.getText().length() == 0) || (txtImage.getText().length() == 0) || (txtDescription.getText().length() == 0)) {
                    errorMsg msg = new errorMsg("Error!", "Please enter valid input.");
                    msg.show();
                    return;
                }
                String commandToSend = "1 " + txtCode.getText() + " " + txtMovieName.getText() +" " + txtLength.getText() +
                        " " + txtYear.getText() + " " + String.valueOf(sliderRating.getValue()) +
                        " " + txtImage.getText() + " " + txtDescription.getText();
                SendToServer sendCommand = new SendToServer(commandToSend);
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if (message == null) {
                    return;
                }
                if (message.contains("Failure")) {
                    errorMsg msg = new errorMsg("Error!", "Movie is already exist.");
                    msg.show();
                    return;
                }
                ArrayList<String> genres = new ArrayList<>(Arrays.asList(txtGenres.getText().split(",")));
                int size = genres.size();
                int counter = 0;
                StringBuilder strBldr = new StringBuilder("");
                for(int i = 0; i<size; i++){
                    if (genres.get(i).length() > 0) {
                        commandToSend = "4 " + txtCode.getText() + " " + genres.get(i);
                        sendCommand = new SendToServer(commandToSend);
                        sendCommand.start();
                        try {
                            sendCommand.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        message = sendCommand.getMessage();
                        if (message == null) {
                            continue;
                        }
                        if (!message.contains("Success")) {
                            counter++;
                            if (counter == 1) {
                                strBldr.append(genres.get(i) + "\n");
                            } else {
                                strBldr.append(", " + genres.get(i) + "\n");
                            }
                        }
                    }
                }
                if (counter > 0) {
                    Platform.runLater(new succesMsg("Success!", "Movie added.\nwithout genres:\n" + strBldr.toString() + "."));
                } else {
                    Platform.runLater(new succesMsg("Success!", "Movie added successfully"));
                }
                ((Stage)(btnAddMovie.getScene().getWindow())).close();
            }
        });

        btnCancelMovie.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                ((Stage)btnAddMovie.getScene().getWindow()).close();
            }
        });
    }
}