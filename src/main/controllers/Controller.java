package main.controllers;

import com.xuggle.xuggler.IContainer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import main.model.MovieDatabase;
import main.model.MovieEntry;
import main.model.Settings;
import main.utils.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    @FXML private TextArea txtTv;
    @FXML private Label lblMovieCount;
    @FXML private Label lblSelectedMovie;
    @FXML private TableView<MovieEntry> tableMovies;
    @FXML private TextField txtFilter;
    @FXML private TabPane tabMovies;
    @FXML private TableColumn<MovieEntry,String> dateColumn;
    @FXML private ProgressIndicator progress;

    private ObservableList<MovieEntry> movieEntries;
    private MovieDatabase movieDatabase;

    private String tvFolderPath = "L:/TV Shows";
    private File tvFolder;

    private File movieFolder;
    private List<File> movieFiles;

    @FXML
    protected void initialize(){

        String movieFolderPath = Settings.getInstance().getMovieFolder();
        movieDatabase = MovieDatabase.getDbInstance();
        tableMovies.setItems(movieDatabase.getMovieEntries());
        movieEntries = tableMovies.getItems();
        if(movieFolderPath ==null){
            selectMovieFolder();
        }else {
            movieFolder = new File(movieFolderPath);
        }

        if(movieFolder!=null) {
            movieFiles = getMovieFiles(movieFolder);
            System.out.println(movieFiles);
            progress.setVisible(true);

            // loading the movie durations takes a while, so this is done in a separate thread
            Thread th = new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    System.out.println("starting load");
                    loadMovies();
                    System.out.println("load finished ");
                    return null;
                }
            });
            th.setDaemon(true);
            th.start();
            addFilter();
            addDateColumnFormat();
        }


        // start of the tv section
//        tvFolder = new File(tvFolderPath);
//        String fileList = "";
//        for (String name: tvFolder.list()){
//            fileList += name + "\n";
//        }
//        txtTv.setText(fileList);

    }

    /**
     * On first run make the user pick the movie folder, then save it to the settings
     */
    private void selectMovieFolder(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Movie Folder");
        movieFolder = chooser.showDialog(null);
        if(movieFolder!=null){
            Settings.getInstance().setMovieFolder(movieFolder.getAbsolutePath());
        }
    }

    /**
     * stores the filename, file path, duration and date modified of each video file
     */
    private void loadMovies() {
        for (File file : movieFiles) {
            System.out.println("loading file");
            Date lastModified = new Date(file.lastModified());
            MovieEntry movieEntry = movieDatabase.getMovieByFileName(file.getName());

            if(!movieDatabase.isMovieInDB(file.getName()) || movieEntry.getDateModified().before(lastModified)) {
                System.out.println("adding new file ");
                IContainer container = IContainer.make();
                int result = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
                if (result >= 0) {
                    movieEntry = new MovieEntry(file.getName(), file.getAbsolutePath(), DateUtils.formatTime(container.getDuration()), lastModified);
                    movieDatabase.addMovieEntry(movieEntry);
                }
                container.close();
            }
        }
        tabMovies.setDisable(false);
        txtFilter.setDisable(false);
        Platform.runLater(() -> {
            lblMovieCount.setText("Number of Movies: " + movieDatabase.getMovieEntries().size());
            progress.setVisible(false);

        });
    }

    /**
     * Adds the filter function to the list from the text field in the toolbar
     */
    private void addFilter(){
        FilteredList<MovieEntry> filteredList = new FilteredList<>(movieEntries, p -> true);

        txtFilter.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(movieEntry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return movieEntry.getFileName().toLowerCase().contains(lowerCaseFilter);
            });
            lblMovieCount.setText("Number of Movies: " + tableMovies.getItems().size());

        }));

        SortedList<MovieEntry> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableMovies.comparatorProperty());
        tableMovies.setItems(sortedList);
    }

    /**
     * format the date column
     */
    private void addDateColumnFormat(){
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(DateUtils.formatDate(param.getValue().getDateModified())));
    }

    /**
     * randomly pick a movie and display the filename
     * @param event
     */
    @FXML
    protected void onPickPressed(ActionEvent event){
        if(!movieEntries.isEmpty()) {
            int index = (int) (Math.random() * movieEntries.size());
            lblSelectedMovie.setText(movieEntries.get(index).getFileName());
        }
    }

    /**
     * at some point this will open the video in the
     * default media player
     * @param event
     */
    @FXML
    protected void onTableCellClicked(MouseEvent event){
        if(event.getClickCount()==2){
            String file = tableMovies.getSelectionModel().getSelectedItem().getFilePath();
//            try {
//                Desktop.getDesktop().open(new File(file));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * Recursively traverse the given folder adding each movie file to a list and returning the list
     * @param folder
     * @return
     */
    private List<File> getMovieFiles(File folder){
        List<File> tmp = new ArrayList<>();
        if(folder.isFile()){
            if(isVideoFile(folder)) {
                tmp.add(folder);
            }
        }else{
            for(File file: folder.listFiles()){
                tmp.addAll(getMovieFiles(file));
            }
        }
        return tmp;
    }

    /**
     * checks if the given file is a video
     * @param video
     * @return
     */
    private boolean isVideoFile(File video){
        String extension = video.getName().substring(video.getName().lastIndexOf(".")+1);
        String regex = "avi|mp4|mkv";
        return extension.matches(regex);
    }



}
