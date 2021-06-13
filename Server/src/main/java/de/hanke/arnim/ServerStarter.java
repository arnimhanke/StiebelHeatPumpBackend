package de.hanke.arnim;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseries;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseriesValue;
import de.hanke.arnim.TimeSeriesToolSet.Raster;
import de.hanke.arnim.TimeSeriesToolSet.TimeseriesUnit;
import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.dtos.MonthViewDataDto;
import de.hanke.arnim.common.lang.DisplayedNames;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static de.hanke.arnim.common.Constant.ES_INDEX_PREFIX;
import static de.hanke.arnim.common.utils.DataCorrection.parseDataFromValueDtoToBigDecimal;
import static de.hanke.arnim.settings.DashBoard.INDICIES_FOR_DASHBOARD;
import static de.hanke.arnim.settings.DayView.INDICIES_FOR_DAYVIEW;
import static de.hanke.arnim.settings.MonthView.INDICIES_FOR_MONTHVIEW;
import static spark.Spark.get;

/**
 * Created by arnim on 12/24/17.
 */
public class ServerStarter {

    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
//        moveLastDatasToInfluxDB();
        startServer();
    }

    public static void startServer() {
        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils("192.168.178.78", "localhost", 9200, "http");
        get("/dashboard", (request, response) -> {
            System.out.println("Dashboard");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                // get last values from elastic
                Map<String, ValueDto> lastValueDtosForIndicies = new LinkedHashMap<>();
//                lastValueDtosForIndicies = elasticSearchUtils.getLastValueDtosForIndicies(INDICIES_FOR_DASHBOARD);

//                List<String> notFoundedValuesForIndexesInElasticSearchDB = INDICIES_FOR_DASHBOARD.stream().filter(s -> !lastValueDtosForIndicies.containsKey(s)).collect(Collectors.toList());
                for (String s : INDICIES_FOR_DASHBOARD) {

                    try (InfluxDBUtils influxDBUtils = new InfluxDBUtils(Properties.ADDRESS_INFLUXDB, Properties.USER_INFLUXDB, Properties.PASSWORD_INFLUXDB, Properties.DATABASE_RAW_DATA_INFLUXDB)) {
                        List<PeriodicTimeseries> lastValuesForIndex = influxDBUtils.getLastValuesForIndex(s.replace(ES_INDEX_PREFIX, "")
                                .toLowerCase()
                                .replace("ü", "ue")
                                .replace("ä", "ae")
                                .replace("ö", "oe"));
                        if (lastValuesForIndex.size() == 1) {
                            if (lastValuesForIndex.get(0).getValues().size() == 1) {
                                PeriodicTimeseriesValue periodicTimeseriesValue = lastValuesForIndex.get(0).getValues().get(0);
                                lastValueDtosForIndicies.put(s, new ValueDto("" + periodicTimeseriesValue.getValue(), periodicTimeseriesValue.getTime().toEpochMilli()));
                            }
                        }
                    }
                }

                Map<String, ValueDto> humanReadableNameToValues = new LinkedHashMap<>();
                for (String s : lastValueDtosForIndicies.keySet()) {
                    humanReadableNameToValues.put(DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME.getOrDefault(s.toLowerCase(), s), lastValueDtosForIndicies.get(s));
                }
                return mapper.writeValueAsString(humanReadableNameToValues);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        });

        get("/monthview", (request, response) -> {
            System.out.println("Monthview");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                System.out.println("Parse data");
                String fromAsString = request.queryParams("from");
                String toAsString = request.queryParams("to");
                Instant from = Instant.parse(fromAsString);
                Instant to = Instant.parse(toAsString);

                System.out.println("load Data");
                try (InfluxDBUtils influxDBUtils = new InfluxDBUtils("StiebelEltronHeatPumpCorrectedData");) {
                    Map<String, List<ValueDto>> valueDtosForIndiciesInInterval = new HashMap<>();
                    for (String s : INDICIES_FOR_MONTHVIEW) {
                        // Werte aus Influx lesen
                        List<PeriodicTimeseries> timeSeries = influxDBUtils.getTimeSeries(
                                s.replace(ES_INDEX_PREFIX, "")
                                        .replace("ü", "ue")
                                        .replace("ä", "ae")
                                        .replace("ö", "oe"), from, to);
                        if (timeSeries.size() == 1) {
                            List<ValueDto> values = new ArrayList<>();
                            PeriodicTimeseries periodicTimeseries = timeSeries.get(0);
                            for (PeriodicTimeseriesValue value : periodicTimeseries.getValues()) {
                                values.add(new ValueDto("" + value.getValue(), value.getTime().toEpochMilli()));
                            }
                            valueDtosForIndiciesInInterval.put(ES_INDEX_PREFIX + periodicTimeseries.getTsId(), values);
                        }
                    }
                    MonthViewDataDto dto = new MonthViewDataDto(valueDtosForIndiciesInInterval, DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME);
                    return mapper.writeValueAsString(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Fehler " + e.getMessage();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return "Fehler " + e.getMessage();
            }
        });

        get("/dayview", (request, response) -> {
            System.out.println("Dayview");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                String fromAsString = request.queryParams("from");
                String toAsString = request.queryParams("to");
                Instant from = Instant.parse(fromAsString);
                Instant to = Instant.parse(toAsString);

                Map<String, List<ValueDto>> lastValueDtosForIndicies = elasticSearchUtils.getDataFromIndexInInterval(INDICIES_FOR_DAYVIEW, from, to);
                MonthViewDataDto dto = new MonthViewDataDto(lastValueDtosForIndicies, DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME);
                return mapper.writeValueAsString(dto);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        });

//        Timer timerMoveData = new Timer();
//        timerMoveData.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    moveLastDatasToInfluxDB();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, 0, 1000 * 60 * 10); // Alle 10 Minuten
    }


    public static void moveLastDatasToInfluxDB() {

        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils("192.168.178.78", "localhost", 9200, "http");

        Instant start = Instant.now().minus(365, ChronoUnit.DAYS);
        Instant end = Instant.now().plusSeconds(60 * 60 * 24);
        ArrayList<String> allIndexes = new ArrayList<>();

        for (String allIndex : Constant.ALL_INDEXES) {
            allIndexes.add(ES_INDEX_PREFIX + allIndex.toLowerCase());
        }
        // Letzte Daten
        Map<String, List<ValueDto>> dataFromIndexInInterval = elasticSearchUtils.getDataFromIndexInInterval(allIndexes, start, end);

        for (Map.Entry<String, List<ValueDto>> stringListEntry : dataFromIndexInInterval.entrySet()) {
            String tsName = stringListEntry.getKey().replace("heizungssuite_", "")
                    .replace("ü", "ue")
                    .replace("ä", "ae")
                    .replace("ö", "oe");
            List<PeriodicTimeseriesValue> temp = new ArrayList<>();
//            System.out.println("Fuer den Index " + stringListEntry.getKey() + " sind " + stringListEntry.getValue().size() + " Werte gefunden worden");
            for (ValueDto valueDto : stringListEntry.getValue()) {
                temp.add(new PeriodicTimeseriesValue(Instant.ofEpochMilli(valueDto.getDate()), parseDataFromValueDtoToBigDecimal(valueDto.getValue()).doubleValue()));
            }

            if (stringListEntry.getValue().size() == 0) {
                continue;
            }

            // Das Werte-Intervall aus der DB extrahieren
            ValueDto valueDtoMin = stringListEntry.getValue().stream().min(Comparator.comparingLong(ValueDto::getDate)).get();
            ValueDto valueDtoMax = stringListEntry.getValue().stream().max(Comparator.comparingLong(ValueDto::getDate)).get();

            Instant minDateFromElasticsearch = Instant.ofEpochMilli(valueDtoMin.getDate());
            Instant maxDateFromElasticsearch = Instant.ofEpochMilli(valueDtoMax.getDate());

            try (InfluxDBUtils influxDBUtils = new InfluxDBUtils("StiebelEltronHeatPumpRawDatas");) {

                boolean b = influxDBUtils.insertTimeSeries(new PeriodicTimeseries(tsName, Raster.PT15S, TimeseriesUnit.mW, "StiebelEltronHeatPumpRawDatas", temp));
                if (!b) {
                    System.out.println("Insert nicht erfolgreich von " + tsName);
                } else {
                    List<PeriodicTimeseries> timeSeries = influxDBUtils.getTimeSeries(tsName, minDateFromElasticsearch, maxDateFromElasticsearch);
                    // Kontrolle ob das Kopieren erfolgreich war


                    long count = temp.stream().filter(periodicTimeseriesValue -> {
                        Instant parsedTime = periodicTimeseriesValue.getTime();
                        return parsedTime.isBefore(end)
                                && (parsedTime.isAfter(start)
                                    || parsedTime.compareTo(start) == 0);
                    }).count();


                    // Sind ueberhaupt Werte in dem relevanten Zeitraum vorhanden?
                    if (count == 0) {
                        System.out.println("Keine Werte im Zeitraum relevant fuer " + tsName);
                    } else if (timeSeries == null || timeSeries.size() == 0) {
                        System.out.println("InfluxDB hat keine Werte fuer " + tsName);
                    } else if (timeSeries.get(0).getValues().size() != temp.size()) {
                        System.out.println("Zu wenig Werte gefunden fuer " + tsName);
                    } else {
//                        System.out.println("Loeschen der Werte fuer " + tsName + " im Intervall " + minDateFromElasticsearch + " - " + maxDateFromElasticsearch);
                        try {
                            elasticSearchUtils.deleteDataInInterval(stringListEntry.getKey(), minDateFromElasticsearch, maxDateFromElasticsearch);
                        } catch (Exception e) {
                            System.out.println("Fehler beim Löschen der Daten von " + stringListEntry.getKey() + " fuer das Intervall " + start.toString() + " bis " + end);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
