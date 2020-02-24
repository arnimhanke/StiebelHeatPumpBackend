package de.hanke.arnim;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.dtos.MonthViewDataDto;
import de.hanke.arnim.common.dtos.PeriodicTimeseries;
import de.hanke.arnim.common.dtos.PeriodicTimeseriesValue;
import de.hanke.arnim.common.dtos.Raster;
import de.hanke.arnim.common.lang.DisplayedNames;

import java.time.Instant;
import java.util.*;

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
        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();
        get("/dashboard", (request, response) -> {
            System.out.println("Dashboard");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                Map<String, ValueDto> lastValueDtosForIndicies = elasticSearchUtils.getLastValueDtosForIndicies(INDICIES_FOR_DASHBOARD);
                Map<String, ValueDto> humanReadableNameToValues = new LinkedHashMap<>();
                for (String s : lastValueDtosForIndicies.keySet()) {
                    humanReadableNameToValues.put(DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME.getOrDefault((ES_INDEX_PREFIX + s).toLowerCase(), s), lastValueDtosForIndicies.get(s));
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

        Timer timerMoveData = new Timer();
        timerMoveData.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    moveLastDatasToInfluxDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1000 * 60 * 10); // Alle 10 Minuten
    }


    public static void moveLastDatasToInfluxDB() {

        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();

        Instant start = Instant.now().minusSeconds(60 * 60 * 24 * 2);
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

                boolean b = influxDBUtils.insertTimeSeries(new PeriodicTimeseries(tsName, Raster.PT15S, temp));
                if (!b) {
                    System.out.println("Insert nicht erfolgreich von " + tsName);
                } else {
                    List<PeriodicTimeseries> timeSeries = influxDBUtils.getTimeSeries(tsName, minDateFromElasticsearch, maxDateFromElasticsearch);
                    // Kontrolle ob das kopieren erfolgreich war


                    long count = temp.stream().filter(periodicTimeseriesValue -> periodicTimeseriesValue.getTime().isBefore(end) && (periodicTimeseriesValue.getTime().isAfter(start) || periodicTimeseriesValue.getTime().compareTo(start) == 0)).count();


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
