package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class mainController {
    private TCPClient client;

    public TCPClient getClient() {
        return client;
    }


    /*
    class MyThread extends Thread {
        @Override
        public void run() {
            System.out.print("before");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
            System.out.print("after");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    btnLogin.setText("Done");
                }
            });
        }
    }*/
    @FXML
    ImageView imgLogo;

    @FXML
    TextField mainTextField;

    @FXML
    MenuButton btnMnuSearch;

    @FXML
    MenuItem searchMoviesByProfessional;

    @FXML
    MenuItem searchMovieById;

    @FXML
    MenuItem searchProfessionalsByMovie;

    @FXML
    Button btnAdd;

    @FXML
    Button btnSearch;

    @FXML
    Button btnRemove;

    @FXML
    ListView lstItems;

    @FXML
    MenuItem miRemoveProfessional;

    @FXML
    MenuItem miRemoveMovie;

    @FXML
    MenuItem miAddProfessional;

    @FXML
    MenuItem miAddMovie;

    @FXML
    MenuItem miAllProfessional;

    @FXML
    MenuItem miAllMovie;

    @FXML
    void initialize() {
        searchMoviesByProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnMnuSearch.setText(searchMoviesByProfessional.getText());
            }
        });

        searchMovieById.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnMnuSearch.setText(searchMovieById.getText());
            }
        });

        searchProfessionalsByMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnMnuSearch.setText(searchProfessionalsByMovie.getText());
            }
        });


        String url = "http://ia.media-imdb.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg";
        lstItems.getItems().add("xyz Inception 148 2010 8.8 SciFi,Action " + url + " This is the description\n" +
                "1 Ellen Page Ariadne Super-Star\n" +
                "1 Leonardo Dicaprio 42");


        lstItems.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new MovieListRow();
            }
        });


// handle exception

        imgLogo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                login con = new login();
                con.show();

            }
        });

        btnSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StringBuilder commandToSend = new StringBuilder("");
                String searchOpt = btnMnuSearch.getText();
                if (searchOpt.equals(searchMoviesByProfessional.getText())) {
                    commandToSend.append("9 ");
                    ArrayList<String> arr = new ArrayList<String>();
                } else if (searchOpt.equals(searchMovieById.getText())) {
                    commandToSend.append("7 ");
                } else if (searchOpt.equals(searchProfessionalsByMovie.getText())) {
                    commandToSend.append("6 ");
                }
                String id = mainTextField.getText();
                commandToSend.append(id);
                try {
                    String answer = TCPClient.getInstance().commandToServer(commandToSend.toString());
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(answer.split("~~-/SEPARATOR/-~~")));
                    if (searchOpt.equals(searchMoviesByProfessional.getText())) {
                        showProfessionals(list);
                    } else if (searchOpt.equals(searchMovieById.getText())) {
                        showProfessionals(list);
                    } else if (searchOpt.equals(searchProfessionalsByMovie.getText())) {
                        showProfessionals(list);
                    }
                } catch (Exception e) {
                    errorMsg msg = new errorMsg("Error!", "Can't communicate server.\nPlease connect to server");
                    msg.show();
                }
            }
        });

        miRemoveMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    StringBuilder commandToSend = new StringBuilder("");
                    commandToSend.append("11 ");
                    String id = mainTextField.getText();
                    commandToSend.append(id);
                    String answer = TCPClient.getInstance().commandToServer(commandToSend.toString());
                    if (answer.contains("Success")) {
                        succesMsg msg = new succesMsg("Success!", "Movie removed");
                        msg.show();
                    } else {
                        errorMsg msg = new errorMsg("Error!", "Can't remove movie");
                        msg.show();
                    }
                } catch (Exception e) {
                    errorMsg msg = new errorMsg("Error!", "Can't communicate server.\nPlease connect to server");
                    msg.show();
                }
            }
        });

        miRemoveProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    StringBuilder commandToSend = new StringBuilder("");
                    commandToSend.append("10 ");
                    String id = mainTextField.getText();
                    commandToSend.append(id);
                    String answer = TCPClient.getInstance().commandToServer(commandToSend.toString());
                    if (answer.contains("Success")) {
                        succesMsg msg = new succesMsg("Success!", "Professional removed");
                        msg.show();
                    } else {
                        errorMsg msg = new errorMsg("Error!", "Can't remove professional");
                        msg.show();
                        //mainTextField.setBackground();
                    }
                } catch (Exception e) {
                    errorMsg msg = new errorMsg("Error!", "Can't communicate server.\nPlease connect to server");
                    msg.show();
                }
            }
        });

        miAddProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addProfessional addPro = new addProfessional();
                addPro.show();
            }
        });

        miAddMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addMovie addMov = new addMovie();
                addMov.show();
            }
        });

        miAllMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String answer = TCPClient.getInstance().commandToServer("13");
                    ArrayList<String> moviesList = new ArrayList<String>(Arrays.asList(answer.split("~~-/SEPARATOR/-~~")));
                    showMovies(moviesList);
                } catch (Exception e) {
                    errorMsg err = new errorMsg("Error!", "You are not connected.\nPlease connect to the server!");
                    err.show();
                }
            }
        });

        miAllProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String answer = TCPClient.getInstance().commandToServer("14");
                    ArrayList<String> professionalsList = new ArrayList<String>(Arrays.asList(answer.split("~~-/SEPARATOR/-~~")));
                    showProfessionals(professionalsList);
                } catch (Exception e) {
                errorMsg err = new errorMsg("Error!", "You are not connected.\nPlease connect to the server!");
                err.show();
            }
            }
        });



        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnAdd.requestFocus();
            }
        });


    }

    public void showMovies(ArrayList<String> moviesList) {
        lstItems.getItems().clear();
        lstItems.getItems().addAll(moviesList);
        lstItems.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new MovieListRow();
            }
        });
        lstItems.refresh();
    }

    public void showProfessionals(ArrayList<String> professionalsList) {
        lstItems.getItems().clear();
        lstItems.getItems().addAll(professionalsList);
        lstItems.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ProfessionalListRow();
            }
        });
        lstItems.refresh();
    }
}