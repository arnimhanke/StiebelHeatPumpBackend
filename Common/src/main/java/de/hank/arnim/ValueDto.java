package de.hank.arnim;

import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * Created by arnim on 12/24/17.
 */
public class ValueDto {

    private String value;
    private long date;

    public ValueDto(String value, long date) {
        this.value = value;
        this.date = date;
    }

    public ValueDto() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        Date d = new Date(date);
        return LocalDateTime.fromDateFields(d).toString() + " - " + value;
    }
}
