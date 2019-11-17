package com.concurrency.play.exers.chap2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StackConfined {
    public String format(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public Date parse(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Can't parse the date");
        }
    }
}
