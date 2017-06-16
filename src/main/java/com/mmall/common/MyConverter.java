package com.mmall.common;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Justin on 2017/6/16.
 */
public class MyConverter implements Converter<String,Date>{
    @Override
    public Date convert(String source) {
        SimpleDateFormat format=  new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
