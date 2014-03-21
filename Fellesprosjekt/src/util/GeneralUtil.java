package util;

import model.Appointment;
import model.Packet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 11:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralUtil {
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateToString(Date d){
        return sdf.format(d);
    }

    public static Date stringToDate(String s){
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * Reads file from filesystem.
     * @param filename
     * @return
     */
    public static ArrayList<String> readFile(String filename){
        ArrayList<String> results = new ArrayList<String>();
        try{
            List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(".","Fellesprosjekt","/res/",filename), Charset.forName("UTF-8"));
            for (String line : lines){
                results.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return results;
    }

}
