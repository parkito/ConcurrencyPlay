package com.concurrency.play.exers.chap2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectConfined {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public synchronized String format(Date date) {
        return df.format(date);
    }

    public synchronized Date parse(String date) {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Can't parse the date");
        }
    }
}
