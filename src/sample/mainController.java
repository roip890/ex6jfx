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
    Button btnAdd;

    @FXML
    MenuItem miAddProfessional;

    @FXML
    MenuItem miAddMovie;

    @FXML
    MenuItem miAllProfessional;

    @FXML
    MenuItem miAllMovie;

    @FXML
    ListView lstItems;

    @FXML
    Button btnSearch;

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
        lstItems.getItems().add("xyz Inception 148 2010 8.8 SciFi,Action " + url +" This is the description\n" +
                "1 Ellen Page Ariadne Super-Star\n" +
                "1 Leonardo Dicaprio 42");


        lstItems.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new MovieListRow();
            }
        });



// handle exception

        btnSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String answer;
                String id = mainTextField.getText();
                StringBuilder commandToSend = new StringBuilder("");
                String searchOpt = btnMnuSearch.getText();
                if (searchOpt.equals(searchMoviesByProfessional.getText())) {
                    commandToSend.append("9 ");
                    commandToSend.append(id);
                    answer = TCPClient.getInstance().commandToServer(commandToSend.toString());
                    ArrayList<String> arr = new ArrayList<String>();
                    
                    showProfessionals(new ArrayList<String>().addAll(answer.split("~~-/SEPARATOR/-~~\n")));
                } else if (searchOpt.equals(searchMovieById.getText())) {
                    commandToSend.append("7 ");
                    commandToSend.append(id);
                } else if (searchOpt.equals(searchProfessionalsByMovie.getText())) {
                    commandToSend.append("6 ");
                    commandToSend.append(id);
                }
                if (answer.equals("Success")) {
                    succesMsg msg = new succesMsg("Success", "");
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
                    TCPClient.getInstance().commandToServer("13");
                    addAllMovToView();
                } catch (Exception e) {
                    errorMsg err = new errorMsg("Error!", "You are not connected.\nPlease connect to the server!");
                    err.show();
                }
            }
        });

        miAllProfessional.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String lst = TCPClient.getInstance().commandToServer("14");
                addAllProToView(lst);
            }
        });

        imgLogo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                login con = new login();
                con.show();

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




    //Add all professionals retrieved from server to listView
    protected void addAllProToView(String lst) {
        lstItems.getItems().clear();
        String[] cur = null;
        try {
            if(!lst.isEmpty()) {
                cur = lst.split("~~-/SEPARATOR/-~~\n");
                lstItems.getItems().addAll(cur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add all movies retrieved from server to listView
    protected void addAllMovToView(){
        /*
        lstItems.getItems().clear();
        String result = null, cur = null;
        try {
            while (TCPClient.getInstance(null,0).getStdin().ready()) {
                cur = TCPClient.getInstance(null,0).getStdin().readLine();
                if(cur.isEmpty()){
                    lstItems.getItems().add(result);
                    result = null;
                    continue;
                }
                result += cur;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        */
    }
}