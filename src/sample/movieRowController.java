package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by roi on 14/01/16.
 */
public class movieRowController {
    String movieCode;
    String movieName;
    int movieLength;
    int movieYear;
    double movieRank;
    String movieTypes;
    String imgUrl;
    String movieDescription;
    ArrayList<ArrayList<String>> staffTypes = new ArrayList<ArrayList<String>>();

    public movieRowController(String movie) {
        ArrayList<String> movieInfoAndStaff = new ArrayList<String>(Arrays.asList(movie.split("~~-/STAFF_SEPARATOR/-~~")));

        ArrayList<String> movieParams = new ArrayList<String>(Arrays.asList(movieInfoAndStaff.get(0).split(" ")));

        this.movieCode = movieParams.get(0);
        this.movieName = movieParams.get(1);
        this.movieLength = Integer.parseInt(movieParams.get(2));
        this.movieYear = Integer.parseInt(movieParams.get(3));
        this.movieRank = Double.parseDouble(movieParams.get(4));
        boolean isFirst = true;
        StringBuilder strBldr = new StringBuilder("");
        if(!movieParams.get(5).equals("~~-/NO-GENRES/-~~")) {
            ArrayList<String> movieTypesList = new ArrayList<String>(Arrays.asList(movieParams.get(5).split(",")));
            for (int i = 0; i < movieTypesList.size(); ++i) {
                if (isFirst) {
                    strBldr.append(movieTypesList.get(i));
                    isFirst = false;
                } else {
                    strBldr.append(" | " + movieTypesList.get(i));
                }
            }
        }
        this.movieTypes = strBldr.toString();
        this.imgUrl = movieParams.get(6);
        isFirst = true;
        StringBuilder strBldr2 = new StringBuilder("");
        for (int i = 7; i < movieParams.size(); ++i) {
            if (isFirst) {
                strBldr2.append(movieParams.get(i));
                isFirst = false;
            } else {
                strBldr2.append(" " + movieParams.get(i));
            }
        }
        this.movieDescription = strBldr2.toString();

        ArrayList<String> directors = new ArrayList<>();
        directors.add("0");
        directors.add("Directors:");
        this.staffTypes.add(directors);
        ArrayList<String> actors = new ArrayList<>();
        actors.add("1");
        actors.add("Actors:");
        this.staffTypes.add(actors);
        ArrayList<String> screenWriters = new ArrayList<>();
        screenWriters.add("2");
        screenWriters.add("Screen Writers:");
        this.staffTypes.add(screenWriters);
        ArrayList<String> producers = new ArrayList<>();
        producers.add("3");
        producers.add("Producers:");
        this.staffTypes.add(producers);
        ArrayList<String> professionalInfo;
        for (int i = 1; i < movieInfoAndStaff.size(); ++i) {
            professionalInfo = new ArrayList<String>(Arrays.asList(movieInfoAndStaff.get(i).split(" ")));
            for (int j = 0; j < this.staffTypes.size(); ++j) {
                if (professionalInfo.get(0).equals(this.staffTypes.get(j).get(0))) {
                        this.staffTypes.get(j).add(professionalInfo.get(1));
                    break;
                }
            }
        }
    }
    @FXML
    ImageView imgMovie;

    @FXML
    Label lblMovieTitle;

    @FXML
    Label lblMovieInfo;

    @FXML
    Label lblMovieGoodStars;

    @FXML
    Label lblMovieBadStars;

    @FXML
    Label lblMovieRank;

    @FXML
    Label lblMovieRankFrom;

    @FXML
    Label lblMovieDescrioption;

    @FXML
    ListView<ArrayList<String>> lstMovieStaff;

    class DrawImage extends Thread {

        ImageView imageView;
        String url;

        DrawImage(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        public void run() {
            Image img = new Image(imgUrl);
            imgMovie.setImage(img);
        }
    }

    @FXML
    public void initialize() {

        DrawImage drawImage = new DrawImage(this.imgMovie, this.imgUrl);
        drawImage.start();
        lblMovieTitle.setText("#" + this.movieCode + " - " + this.movieName + "(" + this.movieYear + ")");
        lblMovieInfo.setText(this.movieLength + " min - " + this.movieTypes);

        int goodStars = (int) this.movieRank;
        StringBuilder strBldr = new StringBuilder("");
        String strGoodString;
        for (int i = 0; i < goodStars; ++i) {
            strBldr.append("★");
        }
        strGoodString = strBldr.toString();
        lblMovieGoodStars.setText(strGoodString);

        int badStars = 10 - goodStars;
        StringBuilder strBldr2 = new StringBuilder("");
        String strBadString;
        for (int i = 0; i < badStars; ++i) {
            strBldr2.append("★");
        }
        strBadString = strBldr2.toString();
        lblMovieBadStars.setText(strBadString);

        lblMovieRank.setText(Double.toString(this.movieRank).format("%.1f", this.movieRank));

        lblMovieRankFrom.setText("/10");

        lblMovieDescrioption.setText(this.movieDescription);


        for (ArrayList<String> staffType: this.staffTypes) {
            if (staffType.size() > 2) {
                lstMovieStaff.getItems().add(staffType);
            }
        }

        lstMovieStaff.setCellFactory(new Callback<ListView<ArrayList<String>>, ListCell<ArrayList<String>>>() {
            @Override
            public ListCell call(ListView<ArrayList<String>> param) {
                return new MovieStaffListRow();
            }
        });
    }

}