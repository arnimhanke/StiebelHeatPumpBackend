package de.hanke.arnim.heizung;

import de.hanke.arnim.TSTool.PeriodicTimeseries;
import de.hanke.arnim.TSTool.PeriodicTimeseriesValue;
import de.hanke.arnim.common.InfluxDBUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static de.hanke.arnim.Properties.*;
import static de.hanke.arnim.common.utils.DataCorrection.parseDataFromValueDtoToBigDecimal;

/**
 * Created by arnim on 12/24/17.
 */
public abstract class AbstractInfo {

    public String url;
    Map<String, BigDecimal> lastValues = new HashMap<>();

    public AbstractInfo() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract void getInformations(String content, Instant time);

    public String getInformation(String content, String tabelName, String key, String esType) {
        try {
            if (content.split(tabelName + "</th>").length == 2) {
                String table = content.split(tabelName + "</th>")[1];
                if (table.split(".*" + key + "</td>" + ".*").length > 1) {
                    String[] split = table.split(">" + key + "</td>");
                    return split[1].split("</td>")[0].split("\">")[1];
                } else {
                    // Type nicht in der Website vorhanden, Type scheint ausgeschaltet zu sein => muss trotzdem registriert werden, ist ja eine Änderung!
                    return "";
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(key + " - " + esType);
            return "";
        }
        return "";
    }

    /**
     * parses the given information to a numeric value and writes it to database
     *
     * @param information
     * @param time
     * @param esTypeIwWaermemengeVdHeizenTag
     */
    public void addValueToDB(String information, Instant time, String esTypeIwWaermemengeVdHeizenTag) {
        BigDecimal bigDecimal = parseDataFromValueDtoToBigDecimal(information);
        String index = esTypeIwWaermemengeVdHeizenTag.toLowerCase().replace("ü", "ue")
                .replace("ä", "ae")
                .replace("ö", "oe");
        if (lastValues.get(index) != null && lastValues.get(index).compareTo(bigDecimal) == 0) {
            return;
        }
        try (InfluxDBUtils influxDBUtils = new InfluxDBUtils(ADDRESS_INFLUXDB, USER_INFLUXDB, PASSWORD_INFLUXDB, DATABASE_RAW_DATA_INFLUXDB)) {

            PeriodicTimeseries tsDto = new PeriodicTimeseries();
            tsDto.setTsId(index);
            ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();
            values.add(new PeriodicTimeseriesValue(time, bigDecimal.doubleValue()));
            tsDto.setValues(values);
            influxDBUtils.insertTimeSeries(tsDto);
            lastValues.put(index, bigDecimal);
        }
        ;
    }

}
