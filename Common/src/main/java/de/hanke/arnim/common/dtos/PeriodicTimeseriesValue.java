package de.hanke.arnim.common.dtos;

import java.time.Instant;

public class PeriodicTimeseriesValues {

    private Instant timestamp = null;

    private double value;

    public PeriodicTimeseriesValues(Instant timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
