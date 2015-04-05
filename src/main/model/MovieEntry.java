package main.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gary on 29/03/2015.
 */
public class MovieEntry {

    //Data from file
    String filePath;
    String fileName;
    String duration;
    Date dateModified;

    //Data from IMDB
    String title;
    String genre;
    String plot;

    boolean hasImdbData;


    public MovieEntry(String fileName, String filePath, String duration, Date dateModified){
        this.fileName = fileName;
        this.filePath = filePath;
        this.duration = duration;
        this.dateModified = dateModified;
        this.hasImdbData = false;
    }

    public boolean hasImdbData(){
        return hasImdbData;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDuration() {
        return duration;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setImdbData(String title,String genre, String plot){
        this.title = title;
        this.genre = genre;
        this. plot = plot;
        this.hasImdbData = true;
    }

    public Map<String,String> getImdbData(){
        Map<String,String> tmp = new HashMap<>();
        tmp.put("Title", title);
        tmp.put("Genre", genre);
        tmp.put("Plot", plot);
        return tmp;
    }
}
