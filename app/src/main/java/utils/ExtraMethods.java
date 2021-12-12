package utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Attendance;
import okhttp3.internal.Util;


public class ExtraMethods {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static ArrayList<Attendance> attendanceArrayList = new ArrayList<>();


    public static ArrayList<Attendance> getAttendance(){

        return attendanceArrayList;
    }
    public static void setA(Attendance a)
    {
        attendanceArrayList.add(a);
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 min";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 hour";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if(diff < 7 * DAY_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        }else if (diff < 14 * DAY_MILLIS){
            return  " One Week ago";
        }else{
            return getDate(time, "dd/MM/yyyy hh:mm");
        }
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static String getTimeOnly(Timestamp timestamp){
        Date date = Calendar.getInstance().getTime();
//                timestamp.toDate();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }
//    public static byte[] compressImage(Context context,Uri resultUri) {
//
//        byte[] thumb_byte = null;
//
//
//        File thumbFilePath = new File(resultUri.getPath());
//        try {
//            Bitmap thumbBitmap = new Compressor(context)
//                    .setMaxWidth(200)
//                    .setMaxHeight(200)
//                    .setQuality(75)
//                    .compressToBitmap(thumbFilePath);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            thumb_byte = baos.toByteArray();
//
//
//        } catch (IOException e) {
//
//        }
//        return thumb_byte;
//    }

}

