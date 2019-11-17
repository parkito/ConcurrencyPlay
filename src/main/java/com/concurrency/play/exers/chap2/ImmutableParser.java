package com.concurrency.play.exers.chap2;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class ImmutableParser {
    private DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    public synchronized String format(Date date) {
        LocalDate localDate = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return df.format(localDate);
    }

    public synchronized Date parse(String date) {
        TemporalAccessor temp = df.parse(date);
        LocalDateTime localDateTime = LocalDateTime.from(temp);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }
}
