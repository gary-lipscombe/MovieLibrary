package main.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by glipscombe on 1/04/2015.
 */
public class IMDBInterface {

    private static final String OMDB_URL = "http://www.omdbapi.com/?";
    private static final String UTF8 = "UTF-8";
    private static final String RESPONSE_FORMAT = "&r=json";

    //JSON response tags
    private static final String GENRE = "Genre";
    private static final String POSTER = "Poster";
    private static final String IMDB_ID = "imdbID";

    private static JSONObject cacheObject;
    private static String lastSearch;

    public static String getGenre(String movieTitle){
        return getDetails(movieTitle).get(GENRE).toString();
    }

    public static String getPosterUrl(String movieTitle){
        return getDetails(movieTitle).get(POSTER).toString();
    }

    public static String getImdbID(String movieTitle){
        return getDetails(movieTitle).get(IMDB_ID).toString();
    }

    /**
     * Opens the imdb page for the selected movie in the default browser
     * @param imdbID
     */
    public static void goToIMDB(String imdbID){
        try {
            Desktop.getDesktop().browse(new URI("http://www.imdb.com/title/"+imdbID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getDetails(String movie){
        JSONObject jsonObject = null;
        if(movie.equals(lastSearch)&&cacheObject!=null){
            jsonObject = cacheObject;
        }else{
            jsonObject = loadMovieDetails(movie,jsonObject);
        }
        return jsonObject;
    }

    private static JSONObject loadMovieDetails(String movie, JSONObject jsonObject){
        try {
            URL url = new URL(OMDB_URL+"t="+ URLEncoder.encode(movie,UTF8)+RESPONSE_FORMAT);

            URLConnection connection = url.openConnection();

            InputStream response = connection.getInputStream();
            String json = new Scanner(response,UTF8).useDelimiter("\\A").next();
            jsonObject = (JSONObject)new JSONParser().parse(json);
            //json = jsonObject.get("Genre").toString();
            //System.out.println(json);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cacheObject = jsonObject;
        lastSearch = movie;
        return jsonObject;
    }

}
