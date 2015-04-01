package main.model;

import java.util.Date;

/**
 * Created by Gary on 29/03/2015.
 */
public class MovieEntry {

    String filePath;
    String fileName;
    String duration;
    Date dateModified;

    public MovieEntry(String fileName, String filePath, String duration, Date dateModified){
        this.fileName = fileName;
        this.filePath = filePath;
        this.duration = duration;
        this.dateModified = dateModified;
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


}
