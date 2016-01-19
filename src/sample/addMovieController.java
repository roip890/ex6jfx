package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * Created by roi on 11/01/16.
 */
public class addMovieController {

    @FXML
    Slider sliderRating;

    @FXML
    Label lblRankNum;
    @FXML
    void initialize(){

        sliderRating.setMin(0.0);
        sliderRating.setMax(10.0);
        sliderRating.setBlockIncrement(0.1);

        Thread addMovThread = new Thread(new Runnable() {
            @Override
            public void run() {

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
