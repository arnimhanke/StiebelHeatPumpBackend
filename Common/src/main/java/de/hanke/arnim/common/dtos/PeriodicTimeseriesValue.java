package de.hanke.arnim.common.dtos;

import java.time.Instant;

public class PeriodicTimeseriesValue {

    public Instant time = null;

    public double value;

    public PeriodicTimeseriesValue(Instant time, double value) {
        this.time = time;
        this.value = value;
    }

    public PeriodicTimeseriesValue() {

    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
