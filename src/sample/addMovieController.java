package sample;

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
    TextField txtMovieDescription;
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

        Thread addMovThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String com = "1 " + txtCode.getText() + " " + txtMovieName.getText() +" " + txtLength.getText() +
                        " " + txtYear.getText() + " " + String.valueOf(sliderRating.getValue()) +
                        " " + txtImage.getText() + " " + txtMovieDescription.getText();
                String result = TCPClient.getInstance().commandToServer(com);
                System.out.println(result);
                String com2 ="";
                ArrayList<String> genres = new ArrayList<>(Arrays.asList(txtGenres.getText().split(",")));
                int size = genres.size();
                for(int i = 0; i<size; i++){
                    com2 = "4 " + txtCode.getText() + " " + genres.get(i);
                    result = TCPClient.getInstance().commandToServer(com2);
                    System.out.println(result);
                }


            }
        });

        btnAddMovie.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addMovThread.start();
            }
        });

        btnCancelMovie.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                ((Stage)btnAddMovie.getScene().getWindow()).close();
            }
        });

        sliderRating.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                lblRankNum.setText(Double.toString(newValue.doubleValue()).format("%.1f", newValue.doubleValue()));
            }
        });



    }
}
