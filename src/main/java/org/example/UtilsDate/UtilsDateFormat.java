package org.example.UtilsDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class UtilsDateFormat {
    private final static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String formatDate(Date date){
        return sdfDate.format(date);
    }

    public static String formatTime(java.util.Date date){
        return sdfTime.format(date);
    }

    public static String formatDateTime(java.util.Date date){
        return formatDate(date) + " " + formatTime(date);
    }

    
    public static String formatYear(Date date){
        return new SimpleDateFormat("yyyy").format(date);
    }

    public static String formatMonthYear(Date date){
        return new SimpleDateFormat("MM/yyyy").format(date);
    }

    public static String formatCustom(String pattern, Date date){
        return new SimpleDateFormat(pattern).format(date);
    }
    
    // dateString chỉ nhận được dạng:
    // dd/MM/yyyy
    // dd/MM/yyyy HH:mm:ss
    // nha ae
    public static java.util.Date stringToDate(String dateString) throws ParseException{
        String dateTimeParts[] = dateString.split(" ");
        String toFormatString = "";

        if(dateTimeParts.length == 1){
            toFormatString = dateTimeParts[0] + " " + "00:00:00";
        } else
        if(dateTimeParts.length == 2){
            toFormatString = dateString;
        }

        return sdf.parse(toFormatString);
    }
}
