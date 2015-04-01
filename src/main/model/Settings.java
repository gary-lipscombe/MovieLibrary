package main.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glipscombe on 30/03/2015.
 */
public class Settings {

    public static final String SETTINGS_FILE = "settings.ini";

    public static final String DEFAULT_DB_FILE = "movies.db";

    private static final String SETTING_DB_LOCATION = "movieDbFile";
    private static final String SETTING_MOVIE_LOCATION = "movieFolder";


    private static Settings settings;

    Map<String,String> settingsMap;


    private Settings() {
        this.settingsMap = new HashMap<>();
        setMovieDbFileName(System.getProperty("user.dir") + "/" + DEFAULT_DB_FILE);
    }

    public static Settings getInstance(){
        if(settings == null){
            Settings tmp = loadSettings();
            settings = (tmp == null)? new Settings(): tmp;
        }
        return settings;
    }

    public String getMovieDbFileName(){
        return settingsMap.get(SETTING_DB_LOCATION);
    }

    public void setMovieDbFileName(String filename){
        settingsMap.put(SETTING_DB_LOCATION,filename);
        saveSettings();
    }

    public String getMovieFolder(){
        return settingsMap.get(SETTING_MOVIE_LOCATION);
    }

    public void setMovieFolder(String movieFolder){
        settingsMap.put(SETTING_MOVIE_LOCATION,movieFolder);
        saveSettings();
    }

    private void saveSettings(){
        if(settings!=null) {
            Gson gson = new Gson();
            String json = gson.toJson(settings);
            try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.dir") + "/" + SETTINGS_FILE)) {
                printWriter.append(json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static Settings loadSettings(){
        Gson gson = new Gson();
        StringBuilder settingsString = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(System.getProperty("user.dir")+"/"+SETTINGS_FILE)).forEach(settingsString::append);
        } catch (IOException e) {
            return null;
        }
        return gson.fromJson(settingsString.toString(),Settings.class);
    }

}
