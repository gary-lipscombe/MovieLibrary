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

    public static final String DEFAULT_DB_FILE = "movie.db";

    private static final String SETTING_DB_LOCATION = "movieDbFile";
    private static final String SETTING_MOVIE_LOCATION = "movieFolder";


    private static Settings settings;

    Map<String,String> settingsMap;


    private Settings() {
        this.settingsMap = new HashMap<String,String>();
        saveSettings();
    }

    public static Settings getInstance(){
        if(settings == null){
            Settings tmp = loadSettings();
            settings = (tmp == null)? new Settings(): tmp;
        }
        return settings;
    }

    public Map<String, String> getSettingsMap() {
        return settingsMap;
    }

    public void setSettingsMap(Map<String, String> settingsMap) {
        this.settingsMap = settingsMap;
    }

    public String getMovieDbFileName(){
        return settingsMap.get(SETTING_DB_LOCATION);
    }

    public void setMovieDbFileName(String filename){
        settingsMap.put(SETTING_DB_LOCATION,filename);
        saveSettings();
    }

    public void saveSettings(){

        Gson gson = new Gson();
        String json = gson.toJson(settings);
        try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.dir")+"/"+SETTINGS_FILE)) {
            printWriter.append(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Settings loadSettings(){
        Gson gson = new Gson();
        StringBuilder settingsString = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(System.getProperty("user.dir")+"/"+SETTINGS_FILE)).forEach(settingsString::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(settingsString.toString(),Settings.class);
    }

}
