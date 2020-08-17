package com.heima.travel.pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter implements Converter<String,Date> {
    @Nullable
    @Override
    public Date convert(String source) {
        Date date=null;
        //转换成日期
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
