package main.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.model.IMDBInterface;
import main.model.MovieEntry;
import main.utils.FileUtils;

/**
 * Created by Gary on 3/04/2015.
 */
public class DetailsController {

    @FXML private ImageView imgPoster;
    @FXML private Label txtTitle;
    @FXML private Label txtGenre;
    @FXML private Label txtPlot;

    private String movieName;

    public void setMovieEntry(MovieEntry movieEntry){
        movieName = FileUtils.getMovieFromFilename(movieEntry.getFileName());

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                IMDBInterface.search(movieName);
                Platform.runLater(()->load());
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }


    private void load(){
        if(IMDBInterface.requestSuccessful()){
            imgPoster.setImage(new Image(IMDBInterface.getPosterUrl(movieName)));
            txtTitle.setText(IMDBInterface.getTitle(movieName));
            txtGenre.setText(IMDBInterface.getGenre(movieName));
            txtPlot.setText(IMDBInterface.getPlot(movieName));
        }else{
            imgPoster.setImage(new Image(getClass().getResource("../resources/images/play.png").toString()));
            txtTitle.setText("");
            txtGenre.setText("");
            txtPlot.setText("");
        }
    }

}
