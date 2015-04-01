package main.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by glipscombe on 30/03/2015.
 */
public class MovieDatabase {

    List<MovieEntry> movieEntries;

    public List<MovieEntry> getMovieEntries() {
        return movieEntries;
    }

    public void setMovieEntries(List<MovieEntry> movieEntries) {
        this.movieEntries = movieEntries;
    }

    static String file = "C:/users/glipscombe/desktop/test.txt";

    private static MovieDatabase movieDatabase;

    public static MovieDatabase getDbInstance(){
        if(movieDatabase==null){
            MovieDatabase tmp = importDatabase();
            movieDatabase = (tmp == null)? new MovieDatabase(): tmp;
        }
        return movieDatabase;
    }

    public void exportDatabase(){

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
            e.printStackTrace();
        }

        return gson.fromJson(movieDbString.toString(),MovieDatabase.class);
    }
    
}
