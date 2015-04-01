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

    public static String formatTime(long time){

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
