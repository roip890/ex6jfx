package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;


/**
 * Created by tomericko on 14/01/16.
 */
public class loginController {

    /*private TCPClient tcpClient;

    public TCPClient getTcpClient() {
        return tcpClient;
    }*/

    @FXML
    Button btnConnect;

    @FXML
    TextField txtIP;

    @FXML
    TextField txtPort;

    @FXML
    void initialize(){

        btnConnect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    TCPClient.getInstance(txtIP.getText(),Integer.parseInt(txtPort.getText()));
                    if (TCPClient.getInstance() != null) {
                        ((Stage)(btnConnect.getScene().getWindow())).close();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    errorMsg msg = new errorMsg("Error", "Can't connect, invalid input.");
                    msg.show();
                }
            }
        });

    }


}
