package main.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gary on 3/04/2015.
 */
public class FileUtils {

    private static final String SPLIT_REGEX = "\\.|_|\\-| |\\[|\\]|\\(|\\)|\\{|\\}";
    private static final String EXTENSION_REGEX = "avi|mp4|mkv";
    private static final String INVALID_LOWERCASE_REGEX = "xvid|dvdrip|brrip|axxo|yify|dvdscr|x264|bluray|720p|1080p|r5|ac3" +
            "|eng|etrg|juggs|extratorrentrg|hdrip|aac|line|noir|1337x|full|vision|maxspeed|www|torentz|3xforum|ro" +
            "|proper|bugz|dvd|scr|lottery|bdrip|absurdity|gaz|unrated|amiable|hs|ws|webrip|edition|alliance|imagine" +
            "|fxg|evo|hdtv|biz|readnfo|wbz|pa|anarchy|720|jyk|3lton|extended|cocain|hd|r33m|bida|grimmo|ozlem|26k|rerip" +
            "|blackjesus|playxd|dl|sbt|muxed|lpd|mxmd|3lton|thcr|nosubs|hq|cm8|sumotorrent|larceny|telesync|h264|crys|bhrg" +
            "|cropped|ganool|stealthmaster|daremusic|thai|safcuk009|ts|r6|hellraz0r|gopanda|playnow|devashish22";

    private static final String INVALID_CASE_SENSE_REGEX = "NEW|INSPiRAL|RETAIL|NYDIC|FiRE|WEB|lOVE|READ|Hive|Rip|MRShanku|SaM|fixed";
    private static final String YEAR_REGEX = "(19|20)\\d\\d";

    public static String getMovieFromFilename(String filename) {

        List<String> tmp = Arrays.asList(filename.split(SPLIT_REGEX));
        System.out.println(tmp.toString());
        StringBuilder stringBuilder = new StringBuilder();
        for (String tmpString : tmp) {
            String lowercase = tmpString.toLowerCase();
            if (!(lowercase.length() == 0)
                    && !lowercase.matches(EXTENSION_REGEX)
                    && !lowercase.matches(INVALID_LOWERCASE_REGEX)
                    && (!(lowercase.matches(YEAR_REGEX) && tmp.indexOf(lowercase) > 0))
                    && !tmpString.matches(INVALID_CASE_SENSE_REGEX)) {

                stringBuilder.append(tmpString + " ");
            }
        }
        System.out.println(stringBuilder.toString());
        System.out.println();
        return stringBuilder.toString();
    }
}
