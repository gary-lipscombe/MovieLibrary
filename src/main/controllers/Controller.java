package main.controllers;

import com.xuggle.xuggler.IContainer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import main.model.MovieEntry;
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

    ObservableList<MovieEntry> movieEntries;

    private  String tvFolderPath = "L:/TV Shows";
    private File tvFolder;

    private String movieFolderPath = "L:/Movies/";
    private File movieFolder;




    private List<String> allMovies;
    private List<String> allMoviePaths;

    private List<File> movieFiles;
    private List<Date> allMovieDateModified;

    @FXML
    protected void initialize(){
        movieEntries = tableMovies.getItems();
        movieFolder = new File(movieFolderPath);
        tvFolder = new File(tvFolderPath);

//        allMovies = getFilesFromFolder(movieFolder);
//        allMoviePaths = getPathsFromFolder(movieFolder);

        movieFiles = getMovieFiles(movieFolder);


        Thread th = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loadMovies();
                return null;
            }
        });
        th.setDaemon(true);
        th.start();

        String fileList = "";
        for (String name: tvFolder.list()){
            fileList += name + "\n";
        }

        addFilter();
        addColumnFormat();
        txtTv.setText(fileList);

    }

    private void loadMovies() {
        for (File file : movieFiles) {

            IContainer container = IContainer.make();
            int result = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
            if (result < 0) {

            } else {
                movieEntries.add(new MovieEntry(file.getName(), file.getAbsolutePath(), formatTime(container.getDuration()), new Date(file.lastModified())));
            }
            container.close();
        }


        tabMovies.setDisable(false);
        txtFilter.setDisable(false);
        Platform.runLater(() -> lblMovieCount.setText("Number of Movies: " + movieEntries.size()));
    }


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

        SortedList<MovieEntry> sortedList = new SortedList<MovieEntry>(filteredList);
        sortedList.comparatorProperty().bind(tableMovies.comparatorProperty());
        tableMovies.setItems(sortedList);
    }

    private void addColumnFormat(){
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MovieEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MovieEntry, String> param) {
                return new SimpleStringProperty(DateUtils.formatDate(param.getValue().getDateModified()));
            }
        });
    }

    @FXML
    protected void onPickPressed(ActionEvent event){
        int index = (int)(Math.random()*movieFolder.list().length);
        lblSelectedMovie.setText(movieFolder.list()[index]);
    }

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

    private boolean isVideoFile(File video){
        String extension = video.getName().substring(video.getName().lastIndexOf(".")+1);
        String regex = "avi|mp4|mkv";
        return extension.matches(regex);
    }

    private String formatTime(long time){

        //time/=1000;
        time/=1000000;
        long second = time%60;
        time/=60;
        long minute = time%60;
        time/=60;
        long hour = time;

        return ((hour<10)?("0"+hour):hour)+
                ":"+((minute<10)?("0"+minute):minute)+
                ":"+((second<10)?("0"+second):second);
    }

}
