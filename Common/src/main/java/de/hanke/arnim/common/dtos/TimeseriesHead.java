package de.hanke.arnim.common.dtos;

import de.hanke.arnim.TSTool.Raster;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "TIMESERIES_HEAD")
public class TimeseriesHead {

    @Column(name = "ID")
    private String ID;

    @Column(name = "RASTER")
    private Raster raster;

    @Column(name = "COMMIDITY")
    private Commodity commodity;

    public TimeseriesHead(String ID, Raster raster, Commodity commodity) {
        this.ID = ID;
        this.raster = raster;
        this.commodity = commodity;
    }

    public TimeseriesHead() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Raster getRaster() {
        return raster;
    }

    public void setRaster(Raster raster) {
        this.raster = raster;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }
}
