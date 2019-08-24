package de.hanke.arnim.common.dtos;


import java.util.List;

public class PeriodicTimeseries {

    private String tsId = null;

    private Raster raster = null;

    private List<PeriodicTimeseriesValues> values = null;

    public PeriodicTimeseries(String tsId, Raster raster, List<PeriodicTimeseriesValues> values) {
        this.tsId = tsId;
        this.raster = raster;
        this.values = values;
    }

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public Raster getRaster() {
        return raster;
    }

    public void setRaster(Raster raster) {
        this.raster = raster;
    }

    public List<PeriodicTimeseriesValues> getValues() {
        return values;
    }

    public void setValues(List<PeriodicTimeseriesValues> values) {
        this.values = values;
    }

    // private Interval gueltigkeitsInterval;

}
