package com.example.fsmartphotoalbum.util;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Feng Zhaohao
 * Created on 2021/3/2
 */
public class StringUtil {


    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat(PATTERN, Locale.getDefault());

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String milliseconds2String(long milliseconds) {
        return format.format(new Date(milliseconds));
    }

    public static String byte2String(long b) {
        numberFormat.setMaximumFractionDigits(2);

        double kb = b / 1024f;
        if (kb < 1024) {
            return numberFormat.format(kb) + "KB";
        }

        double mb = kb / 1024f;
        return numberFormat.format(mb) + "MB";
    }

    public static String getLastPathSegment(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        String[] segments = content.split("/");
        if (segments.length > 0) {
            return segments[segments.length - 1];
        }
        return "";
    }

}
