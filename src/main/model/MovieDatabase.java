package main.model;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by glipscombe on 30/03/2015.
 */
public class MovieDatabase {

    ObservableList<MovieEntry> movieEntries;

    private static MovieDatabase movieDatabase;

    private MovieDatabase(){
        this.movieEntries = FXCollections.observableArrayList();
    }

    public static MovieDatabase getDbInstance(){
        if(movieDatabase==null){
            MovieDatabase tmp = importDatabase();
            movieDatabase = (tmp == null)? new MovieDatabase(): tmp;
        }
        return movieDatabase;
    }

    public void addMovieEntry(MovieEntry movieEntry){
        if(!isMovieInDB(movieEntry.fileName)) {
            this.movieEntries.add(movieEntry);
            saveDatabase();
        }
    }

    public ObservableList<MovieEntry> getMovieEntries() {
        return movieEntries;
    }

    public void saveDatabase(){

        Gson gson = new Gson();
        String json = gson.toJson(this);
        try (PrintWriter printWriter = new PrintWriter(Settings.getInstance().getMovieDbFileName())) {
            printWriter.append(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static MovieDatabase importDatabase(){
        Gson gson = new Gson();
        StringBuilder movieDbString = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(Settings.getInstance().getMovieDbFileName())).forEach(movieDbString::append);
        } catch (IOException e) {
            return null;
        }

        MovieDatabase tmp = new MovieDatabase();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject)jsonParser.parse(movieDbString.toString());
            JSONArray jsonArray = (JSONArray)jsonParser.parse(jsonObject.get("movieEntries").toString());
            for (Object aJsonArray : jsonArray) {

                tmp.addMovieEntry(gson.fromJson(aJsonArray.toString(), MovieEntry.class));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public MovieEntry getMovieByFileName(String filename){
        for(MovieEntry entry:movieEntries){
            if(entry.getFileName().equals(filename)){
                return entry;
            }
        }
        return null;
    }

    public boolean isMovieInDB(String filename) {
        return getMovieByFileName(filename) != null;
    }
}
