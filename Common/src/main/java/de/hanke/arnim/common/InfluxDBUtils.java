package de.hanke.arnim.common;

import de.hanke.arnim.common.dtos.PeriodicTimeseries;
import de.hanke.arnim.common.dtos.PeriodicTimeseriesValue;
import de.hanke.arnim.common.dtos.Raster;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InfluxDBUtils {

    private final String DATABASEURL = "http://192.168.178.122:8086";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";


    private String dbName;
    private InfluxDB influxDB;


    public InfluxDBUtils() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        this.influxDB = InfluxDBFactory.connect(DATABASEURL, USERNAME, PASSWORD, okHttpClientBuilder);
        this.influxDB.setLogLevel(InfluxDB.LogLevel.FULL);
        influxDB.setRetentionPolicy("autogen");
        influxDB.enableBatch(500000, 100, TimeUnit.MILLISECONDS);
        this.dbName = "MyTestDB";
    }

    public static void main(String[] args) {
        InfluxDBUtils influxDBUtils = new InfluxDBUtils();
        String TS_ID = "test_1";
        // influxDBUtils.getTimeSeries("h2o_feet", Instant.parse("2007-12-03T10:15:30.00Z"), Instant.now());
        ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();
        // tageszeitreihe erstellen
        long startMillis = System.currentTimeMillis();
        Instant start = Instant.parse("2016-01-01T23:00:00.00Z");
        Instant current = Instant.from(start);
        Instant end = Instant.parse("2016-01-02T23:00:00.00Z");
        while(current.isBefore(end)) {
            PeriodicTimeseriesValue value = new PeriodicTimeseriesValue();
            value.setTime(current);
            value.setValue(10);
            values.add(value);

            current = current.plusSeconds(15); // 15 Minuten
        }
        System.out.println("Dauer für das Erstellen der Werte "+ (System.currentTimeMillis() - startMillis));

        startMillis = System.currentTimeMillis();
        influxDBUtils.insertTimeSeries(new PeriodicTimeseries(TS_ID, Raster.PT15S, values));
        System.out.println("Dauer für das Schreiben der Werte in die DB "+ (System.currentTimeMillis() - startMillis));

//        List<PeriodicTimeseries> timeSeries = influxDBUtils.getTimeSeries(TS_ID, start, end);
//        System.out.println(timeSeries.size());
        influxDBUtils.influxDB.close();
    }

    public boolean insertTimeSeries(PeriodicTimeseries tsDto) {// tsId, raster, list of values(timstamp, value)
        this.influxDB.setDatabase(this.dbName);
        try {
            BatchPoints batchPoints = BatchPoints
                    .database(this.dbName)
                    .retentionPolicy("autogen")
                    .build();

            tsDto.getValues().forEach(val -> {
                Point point = Point.measurement(tsDto.getTsId())
                        .time(val.getTime().toEpochMilli(), TimeUnit.MILLISECONDS)
                        .addField("value", val.getValue())
                        .build();

                //add the actual point to PointList
                influxDB.write(point);
                batchPoints.point(point);
            });

            // write all points to the database
//            influxDB.write(batchPoints);

            this.influxDB.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PeriodicTimeseries> getTimeSeries(String tsId, Instant from, Instant to) {
        List<PeriodicTimeseries> result = new ArrayList<>();
        QueryResult queryResult;

        // Auslesen der Zeitreihe mit dem Zeitinterval in ms (InfluxDB will irgendwie nicht das normale ISO-Format für Datum UND Uhrzeit annehmen)
        String query = "Select * from " + tsId + " where time >= " + from.toEpochMilli() * 1000 * 1000 + " AND time <= " + to.toEpochMilli() * 1000 * 1000;
        queryResult = this.influxDB.query(new Query(query, this.dbName));


        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        // Gesamtmenge an Ergebnissen
        for (QueryResult.Result queryResult2 : queryResult.getResults()) {

            // Einzelne Zeitreihen
            for (QueryResult.Series series : queryResult2.getSeries()) {
                List<String> columns = series.getColumns();
                ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();
                PeriodicTimeseries periodicTimeseries = new PeriodicTimeseries(series.getName(), Raster.PT15S, values);

                // Werte <=> namen
                for (List<Object> value : series.getValues()) {
                    PeriodicTimeseriesValue periodicTimeseriesValue = new PeriodicTimeseriesValue();

                    // Die Reihenfolge der Spaltennamen und Werte in den Listen ist die gleiche, daher das indirekte Mapping
                    for (int i = 0; i < columns.size(); i++) {
                        Object valueForColumn = value.get(i);
                        try {
                            // Leerzeichen durch Unterstrich ersetzen
                            if(!(valueForColumn instanceof String)) {
                                periodicTimeseriesValue.getClass().getDeclaredField(columns.get(i).replace(" ", "_")).set(periodicTimeseriesValue, valueForColumn);
                            } else {
                                periodicTimeseriesValue.getClass().getDeclaredField(columns.get(i).replace(" ", "_")).set(periodicTimeseriesValue, Instant.parse((String) valueForColumn));
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
//                            e.printStackTrace();
                        }
                    }
                    values.add(periodicTimeseriesValue);
                }
                result.add(periodicTimeseries);
            }
        }
        return result;
    }

}
