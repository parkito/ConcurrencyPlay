package com.concurrency.play.exers.chap2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadConfined {

    private ThreadLocal<DateFormat> df = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd")
    );

    public String format(Date date) {
        return df.get().format(date);
    }

    public Date parse(String date) {
        try {
            return df.get().parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Can't parse the date");
        }
    }
}
