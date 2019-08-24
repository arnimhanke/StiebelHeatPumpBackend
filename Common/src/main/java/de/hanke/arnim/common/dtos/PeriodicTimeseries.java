package de.hanke.arnim.common.dtos;


import java.util.List;

public class PeriodicTimeseries {

    private String tsId = null;

    private Raster raster = null;

    private List<PeriodicTimeseriesValue> values = null;

    public PeriodicTimeseries(String tsId, Raster raster, List<PeriodicTimeseriesValue> values) {
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

    public List<PeriodicTimeseriesValue> getValues() {
        return values;
    }

    public void setValues(List<PeriodicTimeseriesValue> values) {
        this.values = values;
    }

    // private Interval gueltigkeitsInterval;

}
