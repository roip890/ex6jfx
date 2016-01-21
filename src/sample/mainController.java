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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class mainController {

    static Semaphore mutex = new Semaphore(1, true);

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
    ListView<String> lstMovies;

    @FXML
    ListView<String> lstProfessionals;

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
    ImageView btnExit;

    class SendToServer extends Thread {
        String command;
        String message;
        SendToServer(String command) {
            this.command = command;
            this.message = "";
            /*this.message = "xyz Inception 148 2010 8.8 SciFi,Action " + "http://ia.media-imdb.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg" + " This is the description\n" +
                   "1 Ellen Page Ariadne Super-Star\n" +
                  "1 Leonardo Dicaprio 42";*/
        }

        String getMessage() {
            return this.message;
        }

        @Override
        public synchronized void start() {
            super.start();
            System.out.println("ppppp" + this.message);
        }

        @Override
        public void run() {
            synchronized (SendToServer.class) {
                try {
                    mutex.acquire(1);
                    try {
                        this.message = TCPClient.getInstance().commandToServer(command);
                    } catch (Exception e) {
                        Platform.runLater(new errorMsg("Error!", "Can't communicate server.\nPlease connect to server"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release(1);
                }

            }
        }
    }

    @FXML
    void initialize() {
        lstMovies.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell call(ListView<String> param) {
                return new MovieListRow();
            }
        });

        lstProfessionals.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell call(ListView<String> param) {
                return new ProfessionalListRow();
            }
        });

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


        //String url = "http://ia.media-imdb.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg";
        //lstItems.getItems().add("xyz Inception 148 2010 8.8 SciFi,Action " + url + " This is the description\n" +
         //       "1 Ellen Page Ariadne Super-Star\n" +
         //       "1 Leonardo Dicaprio 42");
        /*String a1 = "qwe wqeqwe 123 2000 4.220779220779221 ~~-/NO-GENRES/-~~ http://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX300.jpg dsadas";
        String a2 = "zxc xzcxzc 100 2001 1.103896103896104 ~~-/NO-GENRES/-~~ http://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX300.jpg bnmbnm";

        lstItems.getItems().add(a1);
        lstItems.getItems().add(a2);

        lstItems.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell call(ListView param) {
                return new MovieListRow();
            }
        });*/



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
                String id = mainTextField.getText();
                if (id.length() == 0) {
                    errorMsg msg = new errorMsg("Error!", "Please insert an id.");
                    msg.show();
                    return;
                }
                String type = "";
                StringBuilder commandToSend = new StringBuilder("");
                String searchOpt = btnMnuSearch.getText();
                if (searchOpt.equals(searchMoviesByProfessional.getText())) {
                    type = "Professional";
                    commandToSend.append("9 ");
                    ArrayList<String> arr = new ArrayList<String>();
                } else if (searchOpt.equals(searchMovieById.getText())) {
                    type = "Movie";
                    commandToSend.append("7 ");
                } else if (searchOpt.equals(searchProfessionalsByMovie.getText())) {
                    type = "Movie";
                    commandToSend.append("6 ");
                } else {
                    errorMsg msg = new errorMsg("Error!", "Please select an option.");
                    msg.show();
                    return;
                }
                commandToSend.append(id);
                SendToServer sendCommand = new SendToServer(commandToSend.toString());
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if ((message == null) || (message == ""))  {
                    return;
                }
                if (message.contains("Failure")) {
                    errorMsg msg = new errorMsg("Failure", type + " not found");
                    msg.show();
                } else {
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.split("~~-/SEPARATOR/-~~")));
                    if (searchOpt.equals(searchMoviesByProfessional.getText())) {
                        showMovies(list);
                    } else if (searchOpt.equals(searchMovieById.getText())) {
                        showMovies(list);
                    } else if (searchOpt.equals(searchProfessionalsByMovie.getText())) {
                        showProfessionals(list);
                    }
                }
            }
        });

        miRemoveMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder commandToSend = new StringBuilder("");
                commandToSend.append("11 ");
                String id = mainTextField.getText();
                commandToSend.append(id);
                SendToServer sendCommand = new SendToServer(commandToSend.toString());
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if ((message == null) || (message == null)) {
                    return;
                }
                if (message.contains("Success")) {
                    succesMsg msg = new succesMsg("Success!", "Movie removed");
                    msg.show();
                } else {
                    errorMsg msg = new errorMsg("Error!", "Can't remove movie");
                    msg.show();
                }
            }
        });

        miRemoveProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder commandToSend = new StringBuilder("");
                commandToSend.append("10 ");
                String id = mainTextField.getText();
                commandToSend.append(id);
                SendToServer sendCommand = new SendToServer(commandToSend.toString());
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if ((message == null) || (message == null)) {
                    return;
                }
                if (message.contains("Success")) {
                    Platform.runLater(new succesMsg("Success!", "Professional removed"));
                } else {
                    Platform.runLater(new errorMsg("Error!", "Can't remove professional"));
                    //mainTextField.setBackground();
                }
            }
        });

        miAddProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (TCPClient.isConstruct()) {
                    addProfessional addPro = new addProfessional();
                    addPro.show();
                } else {
                    errorMsg msg = new errorMsg("Error!", "You are not connected.\nPlease connect to the server!");
                    msg.show();
                }
            }
        });

        miAddMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (TCPClient.isConstruct()) {
                    addMovie addMov = new addMovie();
                    addMov.show();
                } else {
                    errorMsg msg = new errorMsg("Error!", "You are not connected.\nPlease connect to the server!");
                    msg.show();                }
            }
        });



        miAllMovie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SendToServer sendCommand = new SendToServer("13");
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if ((message == null) || (message == ""))  {
                    return;
                }
                ArrayList<String> moviesList = new ArrayList<String>(Arrays.asList(message.split("~~-/SEPARATOR/-~~")));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showMovies(moviesList);
                    }
                });
            }
        });

        miAllProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SendToServer sendCommand = new SendToServer("14");
                sendCommand.start();
                try {
                    sendCommand.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = sendCommand.getMessage();
                if ((message == null) || (message == "")){
                    return;
                }
                ArrayList<String> professionalsList = new ArrayList<String>(Arrays.asList(message.split("~~-/SEPARATOR/-~~")));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showProfessionals(professionalsList);
                    }
                });
            }
        });

        btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String answer = TCPClient.getInstance().commandToServer("-1");
                    ((Stage)btnExit.getScene().getWindow()).close();
                } catch (Exception e) {
                    ((Stage)btnExit.getScene().getWindow()).close();
                }
            }
        });
    }

    void showMovies(ArrayList<String> movieList) {
        lstMovies.getItems().removeAll(lstMovies.getItems());
        lstProfessionals.getItems().removeAll(lstProfessionals.getItems());
        lstMovies.getItems().addAll(movieList);
    }

    void showProfessionals(ArrayList<String> professionalsList) {
        lstMovies.getItems().removeAll(lstMovies.getItems());
        lstProfessionals.getItems().removeAll(lstProfessionals.getItems());
        lstProfessionals.getItems().addAll(professionalsList);
    }
}