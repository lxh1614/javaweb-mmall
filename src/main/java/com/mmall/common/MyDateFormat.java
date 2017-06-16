package com.mmall.common;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Justin on 2017/6/16.
 */
public class MyDateFormat implements Formatter<Date> {
    @Override
    public Date parse(String s, Locale locale) throws ParseException {
        SimpleDateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
       return format.parse(s);
    }

    @Override
    public String print(Date date, Locale locale) {
        return null;
    }
}
