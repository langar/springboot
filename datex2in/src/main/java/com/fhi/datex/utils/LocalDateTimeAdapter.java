package com.fhi.datex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The Class LocalDateTimeAdapter.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	/** The date format. */
	DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
	        .parseCaseInsensitive()
	        .append(DateTimeFormatter.ISO_LOCAL_DATE)
	        .optionalStart()
	        .appendLiteral('T')
	        .append(DateTimeFormatter.ISO_TIME)
	        .toFormatter();

    /**
     * Marshal.
     *
     * @param dateTime the date time
     * @return the string
     */
    @Override
    public String marshal(LocalDateTime dateTime) {
        return dateTime.format(dateFormat);
    }

    /**
     * Unmarshal.
     *
     * @param dateTime the date time
     * @return the local date time
     */
    @Override
    public LocalDateTime unmarshal(String dateTime) {
        return LocalDateTime.parse(dateTime, dateFormat);
    }

}
