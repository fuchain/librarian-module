package com.fptedu.common.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private final static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");

    public static Double roundMark(Double mark, int scale){
        BigDecimal bd = new BigDecimal(mark);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static String getCurrentDate1(){
        String format = sdf1.format(Calendar.getInstance().getTime());
        return format;
    }

    public static String getCurrentDate2(){
        String format = sdf2.format(Calendar.getInstance().getTime());
        return format;
    }

    public static String getDateFormatType2(Timestamp date){
        String format = sdf2.format(date);
        return format;
    }

    public static String getDateFormatType1(Timestamp date){
        String format = sdf1.format(date);
        return format;
    }

}
