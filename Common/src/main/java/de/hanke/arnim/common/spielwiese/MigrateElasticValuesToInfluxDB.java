package de.hanke.arnim.common.spielwiese;

import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.dtos.PeriodicTimeseries;
import de.hanke.arnim.common.dtos.PeriodicTimeseriesValue;
import de.hanke.arnim.common.dtos.Raster;
import de.hanke.arnim.common.utils.DataCorrection;
import de.hanke.arnim.common.utils.SeriesActions;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MigrateElasticValuesToInfluxDB {

    public static void main(String[] args) throws InterruptedException {

        Instant start = Instant.parse("2017-12-30T23:00:00.00Z");
        Instant end = Instant.parse("2020-01-31T23:00:00.00Z");

        ArrayList<String> allIndexes = new ArrayList<>();

        for (String allIndex : Constant.ALL_INDEXES) {
            allIndexes.add(allIndex.toLowerCase());
        }

        for (String index : allIndexes) {
            InfluxDBUtils influxDBUtils = new InfluxDBUtils("StiebelEltronHeatPumpCorrectedData");
            InfluxDBUtils influxDBUtilsRaw = new InfluxDBUtils("StiebelEltronHeatPumpRawDatas");

            System.out.println("Starte mit Index " + index);
            long startPerformance = System.currentTimeMillis();
            List<PeriodicTimeseries> timeSeries = influxDBUtilsRaw.getTimeSeries(index
                    .replace("ü", "ue")
                    .replace("ä", "ae")
                    .replace("ö", "oe"), start, end);

            if (timeSeries.size() == 1) {

                Map<Long, PeriodicTimeseriesValue> map = new HashMap<>();
                List<PeriodicTimeseriesValue> valuess = timeSeries.get(0).getValues();
                for (int i = 0; i < valuess.size(); i++) {
                    PeriodicTimeseriesValue val = valuess.get(i);
                    map.put(val.time.toEpochMilli(), val);
                }

                PeriodicTimeseries periodicTimeseries = timeSeries.get(0);
                List<PeriodicTimeseriesValue> valuesOfPeriodicTS = periodicTimeseries.getValues();
                List<ValueDto> valueDtos = new ArrayList<>();
                for (PeriodicTimeseriesValue valuesOfPeriodicT : valuesOfPeriodicTS) {
                    valueDtos.add(new ValueDto("" + valuesOfPeriodicT.getValue(), valuesOfPeriodicT.getTime().toEpochMilli()));
                }
                HashMap<String, List<ValueDto>> data = new HashMap<>();
                data.put(Constant.ES_INDEX_PREFIX + timeSeries.get(0).getTsId(), valueDtos);

                // Erst Luecken fuellen und dann korrigieren
                Map<String, List<ValueDto>> stringListMap = new DataCorrection().fixUpSeries(data, start, end, 15 * 1000);

                for (String tsName : stringListMap.keySet()) {
                    List<ValueDto> valuesFromElastic = stringListMap.get(tsName);
                    List<PeriodicTimeseriesValue> temp = new ArrayList<>();
                    String ts = tsName.replace(Constant.ES_INDEX_PREFIX, "")
                            .replace("ü", "ue")
                            .replace("ä", "ae")
                            .replace("ö", "oe");

                    Map<Long, ValueDto> distinctedList = new HashMap<>();
                    valuesFromElastic.forEach(valueDto -> distinctedList.put(valueDto.getDate(), valueDto));
                    System.out.println(tsName + " - " + valuesFromElastic.size());
                    // Alle 100 Werte schreiben
                    for (int i = 1; i <= valuesFromElastic.size(); i++) {
                        temp.add(new PeriodicTimeseriesValue(Instant.ofEpochMilli(valuesFromElastic.get(i - 1).getDate()),
                                Double.parseDouble(valuesFromElastic.get(i - 1).getValue())));
                        if (i % 100 == 0) {
                            PeriodicTimeseries tsDto = new PeriodicTimeseries(ts, Raster.PT15S, new ArrayList<>(temp));
                            influxDBUtils.insertTimeSeries(tsDto);
                            temp = new ArrayList<>();
                        }
                    }
                    PeriodicTimeseries tsDto = new PeriodicTimeseries(ts, Raster.PT15S, temp);
                    influxDBUtils.insertTimeSeries(tsDto);
                    List<PeriodicTimeseries> timeSeries1 = influxDBUtils.getTimeSeries(tsName.replace("heizungssuite_", ""),
                            Instant.ofEpochMilli(valuesFromElastic.get(0).getDate()),
                            Instant.ofEpochMilli(valuesFromElastic.get(valuesFromElastic.size() - 1).getDate()));

                    if (timeSeries1.get(0).getValues().size() != distinctedList.size()) {
                        throw new IllegalArgumentException("Fuer die Zeitreihe " + tsName + " stimmt die Anzahl der geschriebenen Daten nicht ueberein\n gelesen: " + valuesFromElastic.size() + " - geschrieben " + timeSeries1.get(0).getValues().size());
                    }
                }
                System.out.println("Finished mit Index " + index + " nach " + (System.currentTimeMillis() - startPerformance) / 1000);
            } else {
                System.err.println("Falsche Anzahl gefunden " + timeSeries.size());
            }


            Thread.sleep(2000);
            influxDBUtils.close();
            influxDBUtilsRaw.close();
            Thread.sleep(2000);
        }
    }
}
