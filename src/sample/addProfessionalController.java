package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by roi on 08/01/16.
 */
public class addProfessionalController {
    int profession = 0;
    @FXML
    ListView lstItems;

    @FXML
    RadioButton radioBtnMale;

    @FXML
    RadioButton radioBtnFamale;

    @FXML
    Button btnCancleAddProfession;

    @FXML
    Button btnAddProfession;

    @FXML
    TextField txtProID;
    @FXML
    TextField txtAge;
    @FXML
    TextField txtProName;
    @FXML
    MenuButton mbtnProfession;
    @FXML
    MenuItem proDirector;
    @FXML
    MenuItem proActor;
    @FXML
    MenuItem proWriter;
    @FXML
    MenuItem proProducer;
    @FXML
    RadioButton radioMale;
    @FXML
    RadioButton radioFemale;
    @FXML
    TextField txtDescription;
    @FXML
    ToggleGroup gender;
    @FXML
    TextArea txtMoviesPro;

    class SendToServer extends Thread {
        String command;
        String message = null;
        SendToServer(String command) {
            this.command = command;
        }

        String getMessage() {
            return this.message;
        }

        @Override
        public void run() {
            synchronized (SendToServer.class) {
                try {
                    this.message = TCPClient.getInstance().commandToServer(command);
                } catch (Exception e) {
                    Platform.runLater(new errorMsg("Error!", "Can't communicate server.\nPlease connect to server"));
                }
            }
        }
    }

    @FXML
    public void initialize() {

        Thread addProThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {

                }catch (Exception e){
                    Platform.runLater(new errorMsg("Error!", "Can't communicate server.\nPlease connect to server."));
                }
            }
        });


        btnAddProfession.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((txtProID.getText().length() == 0) || (txtAge.getText().length() == 0) || (txtDescription.getText().length() == 0) ||
                        (String.valueOf(txtProName.getText()).length() == 0) || (txtDescription.getText().length() == 0)) {
                    errorMsg msg = new errorMsg("Error!", "Please enter valid input.");
                    msg.show();
                    return;
                }
                if (mbtnProfession.getText().equals(proDirector.getText())) {
                    profession = 0;
                } else if (mbtnProfession.getText().equals(proActor.getText())) {
                    profession = 1;
                } else if (mbtnProfession.getText().equals(proWriter.getText())) {
                    profession = 2;
                } else if (mbtnProfession.getText().equals(proProducer.getText())) {
                    profession = 3;
                }
                String commandToSend = "2 " + profession + " " + txtProID.getText() + " " + txtAge.getText() + " " +
                        txtDescription.getText() + " " + ((RadioButton) gender.getSelectedToggle()).getText() +
                        " " + txtProName.getText();
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
                    errorMsg msg = new errorMsg("Error!", "Professional is already exist.");
                    msg.show();
                    return;
                }
                ArrayList<String> movies = new ArrayList<>(Arrays.asList(txtMoviesPro.getText().split(",")));
                int size = movies.size();
                int counter = 0;
                StringBuilder strBldr = new StringBuilder("");
                for(int i = 0; i<size; i++){
                    if (movies.get(i).length() > 0) {
                        commandToSend = "3 " + movies.get(i) + " " + txtProID.getText();
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
                            if (counter != 1) {
                                strBldr.append(", " + movies.get(i) + "\n");
                            } else {
                                strBldr.append(movies.get(i) + "\n");
                            }
                        }
                    }
                }
                if (counter > 0) {
                    Platform.runLater(new succesMsg("Success!", "Professional added.\nexcept to movies:\n" + strBldr.toString() + "."));
                } else {
                    Platform.runLater(new succesMsg("Success!", "Professional added successfully"));
                }
                ((Stage)(btnAddProfession.getScene().getWindow())).close();

            }
        });

        btnCancleAddProfession.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage)(btnCancleAddProfession.getScene().getWindow())).close();
            }
        });

        proActor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mbtnProfession.setText(proActor.getText());
            }
        });

        proDirector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mbtnProfession.setText(proDirector.getText());
            }
        });
        proProducer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mbtnProfession.setText(proProducer.getText());
            }
        });
        proWriter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mbtnProfession.setText(proWriter.getText());
            }
        });


    }
}
