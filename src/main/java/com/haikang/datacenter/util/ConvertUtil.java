package com.haikang.datacenter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Filename:    ConvertUtil
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-24 15:14
 * Description:
 */

public class ConvertUtil {

    public static String getStringValue(Object obj) {

        String res = String.valueOf(obj);
        if (res == "null") {
            return "";
        }
        else {
            return res;
        }
    }

    public static String getStringFloatValue(Object obj, int n) {

        float number = getFloatValue(obj);

        String format = "%." + n + "f";

        String ret;
        try {
            ret = String.format(format, number);
        } catch (java.util.IllegalFormatException ex) {
            return "0";
        }

        return ret;
    }

    public static String getStringDoubleValue(Object obj, int n) {

        double number = getDoubleValue(obj);

        String format = "%." + n + "f";

        String ret;
        try {
            ret = String.format(format, number);
        } catch (java.util.IllegalFormatException ex) {
            return "0";
        }

        return ret;
    }

    public static long getLongValue(Object obj) {
        try {
            return Long.parseLong(getStringValue(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getIntValue(Object obj) {
        try {
            return Integer.parseInt(getStringValue(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static byte getByteValue(Object obj) {
        try {
            return Byte.parseByte(getStringValue(obj));
        } catch (NumberFormatException e) {
            return (byte)(getIntValue(obj));
        }
    }

    public static short getShortValue(Object obj) {
        try {
            return Short.parseShort(getStringValue(obj));
        } catch (NumberFormatException e) {
            return (short)(getIntValue(obj));
        }
    }

    public static float getFloatValue(Object obj) {
        try {
            return Float.parseFloat(getStringValue(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double getDoubleValue(Object obj) {
        try {
            return Double.parseDouble(getStringValue(obj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getStringDatetimeValue(Object obj) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(obj);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    public static String getStringDateValue(Object obj) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.format(obj);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void main(String args[]) {
        // Date aaa = ConvertUtil.StrToDate("2017-04-01 12:58:25");

        byte asd = getByteValue(128);
        asd = getByteValue(1);
        asd = getByteValue(0);
        asd = getByteValue(157);
        asd = getByteValue(255);
    }

    public static byte[] getBCD(Object obj,int len)
    {
        return null;
    }

    @SuppressWarnings("null")
    public static <T> String getStringfromList(List<T> vehicle_ids) {
        if (vehicle_ids == null || vehicle_ids.size() == 0) {
            return null;
        }
        String msg = new String();
        for (Iterator<T> it2 = vehicle_ids.iterator(); it2.hasNext();) {
            msg += (it2.next()).toString() + ",";
        }

        return msg;
    }

}

