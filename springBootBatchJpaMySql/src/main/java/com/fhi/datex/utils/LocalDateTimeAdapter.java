package com.fhi.datex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
	        .parseCaseInsensitive()
	        .append(DateTimeFormatter.ISO_LOCAL_DATE)
	        .optionalStart()
	        .appendLiteral('T')
	        .append(DateTimeFormatter.ISO_TIME)
	        .toFormatter();

    @Override
    public String marshal(LocalDateTime dateTime) {
        return dateTime.format(dateFormat);
    }

    @Override
    public LocalDateTime unmarshal(String dateTime) {
        return LocalDateTime.parse(dateTime, dateFormat);
    }

}
