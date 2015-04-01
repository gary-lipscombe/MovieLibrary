package main.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gary on 1/04/2015.
 */
public class DateUtils {

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
