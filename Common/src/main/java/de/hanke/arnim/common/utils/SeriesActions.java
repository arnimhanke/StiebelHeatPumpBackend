package de.hanke.arnim.common.utils;

import de.hanke.arnim.TimeSeriesToolSet.Raster;
import de.hanke.arnim.common.ValueDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeriesActions {

    private DataCorrection dataCorrection = new DataCorrection();

    /**
     * Preparing the data for the user
     *
     * @param data     Data from the server
     * @param start    start-timestamp
     * @param end      end-timestamp
     * @param interval Interval for the series
     */
    public Map<String, List<ValueDto>> preparingDataForFurtherUse(Map<String, List<ValueDto>> data, Instant start, Instant end, Raster interval) {
        replaceCommaDecimalWithPoint(data);

        int intervalAsNumber = 0;

        switch (interval) {
            case PT15S:
                intervalAsNumber = 15 * 1000;
                break;
            case PT1D:
                intervalAsNumber = 24 * 60 * 60 * 1000;
                break;
        }

        Map<String, List<ValueDto>> fixedValues = dataCorrection.fixUpSeries(data, start, end, intervalAsNumber);
        Map<String, List<ValueDto>> filledSeries = fillSeries(fixedValues, start, end.plus(intervalAsNumber, ChronoUnit.MILLIS));
        Map<String, List<ValueDto>> aggregateSeries = aggregateSeries(filledSeries, start, end, intervalAsNumber);
        return aggregateSeries;

    }

    public void replaceCommaDecimalWithPoint(Map<String, List<ValueDto>> data) {
        for (String key : data.keySet()) {
            List<ValueDto> vals = data.get(key);
            for (int i = 0; i < vals.size(); i++) {
                vals.get(i).setValue(vals.get(i).getValue().replace(',', '.'));
            }
        }
    }

    /**
     * Füllt die Zeitreihe auf und aggregiert diese auf den entsprechenden Zeitraum
     *
     * @param data  Not-Filled Data
     * @param start Start-TimeStamp
     * @param end   End-TimeStamp
     */
    public Map<String, List<ValueDto>> fillSeries(Map<String, List<ValueDto>> data, Instant start, Instant end) {
        Map<String, List<ValueDto>> ret = new HashMap<>();

        for (String key : data.keySet()) {
            if (data.get(key) != null) {
                List<ValueDto> retValues = new ArrayList<>();
                List<ValueDto> values = data.get(key);
                Instant date = Instant.ofEpochMilli(start.toEpochMilli());
                for (int i = 0; i < values.size(); i++) {
                    ValueDto actualValueDto = values.get(i);
                    // Der aktuelle Wert liegt nicht vor dem Startzeitpunkt
                    if (!Instant.ofEpochMilli(actualValueDto.getDate()).isBefore(start)) {
                        if (i == values.size() - 1) {
                            while (Instant.ofEpochMilli(end.toEpochMilli()).isAfter(date) || Instant.ofEpochMilli(end.toEpochMilli()).equals(date)) {
                                retValues.add(new ValueDto(actualValueDto.getValue().replace(',', '.'), date.toEpochMilli()));
                                date = date.plus(15, ChronoUnit.SECONDS);
                            }
                        } else {
                            ValueDto nextValueDto = values.get(i + 1);
                            while (Instant.ofEpochMilli(nextValueDto.getDate()).isAfter(date)) {
                                retValues.add(new ValueDto(actualValueDto.getValue().replace(',', '.'), date.toEpochMilli()));
                                date = date.plus(15, ChronoUnit.SECONDS);
                            }
                        }
                        // Der aktuelle Wert liegt vor dem Startzeitpunkt
                    } else {
                        if (i == values.size() - 1) {
                            while (Instant.ofEpochMilli(date.toEpochMilli()).isBefore(end) || Instant.ofEpochMilli(date.toEpochMilli()).equals(end)) {
                                retValues.add(new ValueDto(actualValueDto.getValue().replace(',', '.'), date.toEpochMilli()));
                                date = date.plus(15, ChronoUnit.SECONDS);
                            }
                        } else {
                            ValueDto nextValueDto = values.get(i + 1);
                            while (Instant.ofEpochMilli(date.toEpochMilli()).isBefore(Instant.ofEpochMilli(nextValueDto.getDate()))) {
                                retValues.add(new ValueDto(actualValueDto.getValue().replace(',', '.'), date.toEpochMilli()));
                                date = date.plus(15, ChronoUnit.SECONDS);
                            }
                        }

                    }
                }
                ret.put(key, retValues);
            }
        }
        return ret;
    }

    /**
     * Aggregates the Series for the given interval
     *
     * @param data     Not-aggregated Data
     * @param start    start-timestamp for the aggregation
     * @param end      end-timestamp for the aggregation
     * @param interval interval for the aggregation
     */
    public Map<String, List<ValueDto>> aggregateSeries(Map<String, List<ValueDto>> data, Instant start, Instant end, int interval) {
        /*if (interval == 15 * 1000) {
            return extractValuesForInterval(data, start, end);
        }*/
        Map<String, List<ValueDto>> ret = new HashMap<>();

        for (String key : data.keySet()) {
            if (data.get(key) != null) {
                List<ValueDto> values = data.get(key);
                List<ValueDto> retValues = aggregateSeiresForGivenAggregationType(AggregationTypes.aggregationConfig.get(key), values, start, end, interval, key);
                ret.put(key, retValues);
            }
        }

        return ret;
    }

    public Map<String, List<ValueDto>> extractValuesForInterval(Map<String, List<ValueDto>> valuesToExtract, Instant start, Instant end) {
        Map<String, List<ValueDto>> ret = new HashMap<>();
        long startAsEpocheMillis = start.toEpochMilli();
        long endAsEpocheMillis = end.toEpochMilli();
        for (String s : valuesToExtract.keySet()) {
            List<ValueDto> extractedValueDtos = valuesToExtract.get(s).stream().filter(valueDto -> valueDto.getDate() >= startAsEpocheMillis && valueDto.getDate() < endAsEpocheMillis).collect(Collectors.toList());
            ret.put(s, extractedValueDtos);
        }
        return  ret;
    }

    public List<ValueDto> aggregateSeiresForGivenAggregationType(AggregationTypes.AggregationType aggregationType, List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        if (data == null || data.size() == 0) {
            return new ArrayList<>();
        }

        if(interval == 15*1000) {
            return data;
        }

        switch (aggregationType) {
            case COUNT:
                return aggregateSeriesCount(data, start, end, interval, id);
            case AVERAGE:
                return aggregateSeriesAverage(data, start, end, interval, id);
            case MAX:
                return aggregateSeriesMax(data, start, end, interval, id);
            case MAX_BEFOR_MINOR:
                return aggregateSeriesMaxBeforeMinor(data, start, end, interval, id);
            case DIV_VORTAG:
                return aggregateSeriesDivVortag(data, start, end, interval, id);
            default:
                return data;

        }
    }

    /**
     * Für das gegebene Intervall werden die Veränderung von 'LEER' zu 'Enthält etwas' gezählt
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesCount(List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        System.out.println("aggregateSeriesCount" + id);

        List<ValueDto> ret = new ArrayList<>();
        long startAsMilliseconds = start.toEpochMilli();
        Instant date = Instant.ofEpochMilli(startAsMilliseconds);
        int foundIntervals = 0;
        int countForInterval = 0;
        int i = 0;
        String lastFoundStatus = data.get(i).getValue();
        while ((date.isBefore(end) || date.equals(end)) && data.size() > i) {
            if (startAsMilliseconds + (foundIntervals + 1) * interval == date.toEpochMilli()) {
                ret.add(new ValueDto("" + countForInterval, start.toEpochMilli() + (foundIntervals) * interval));
                foundIntervals++;
                countForInterval = 0;
                if (!data.get(i).getValue().equals("")) {
                    countForInterval++;
                    lastFoundStatus = data.get(i).getValue();
                }
            } else {
                if (!data.get(i).getValue().equals("") && !lastFoundStatus.equals(data.get(i).getValue())) {
                    countForInterval++;
                    lastFoundStatus = data.get(i).getValue();
                } else if (data.get(i).getValue().equals("")) {
                    lastFoundStatus = "";
                }
            }
            i++;
            date = date.plus(15, ChronoUnit.SECONDS);
        }

        return ret;
    }

    /**
     * Für das gegebene Intervall wird der Durchschnitt gebildet
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesAverage(List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        System.out.println("aggregateSeriesAverage" + id);

        List<ValueDto> ret = new ArrayList<>();
        long startAsMilliseconds = start.toEpochMilli();
        Instant date = Instant.ofEpochMilli(start.toEpochMilli());
        long sumForInterval = 0l;
        int i = 0;
        int foundIntervals = 0;
        while (date.isBefore(end) || date.equals(end)) {
            if (startAsMilliseconds + (foundIntervals + 1) * interval == date.toEpochMilli()) {
                double value = sumForInterval / (interval / 15l);
                ret.add(new ValueDto("" + value, start.toEpochMilli() + (foundIntervals) * interval));
                foundIntervals++;
                sumForInterval = 0;
                float parsedValue;
                try {
                    parsedValue = Float.parseFloat(data.get(i).getValue());
                } catch (NumberFormatException e) {
                    parsedValue = 0;
                }
                sumForInterval += parsedValue;
                i++;
                date.plus(15, ChronoUnit.SECONDS);
            } else {
                float parsedValue;
                try {
                    parsedValue = Float.parseFloat(data.get(i).getValue());
                } catch (NumberFormatException e) {
                    parsedValue = 0;
                }
                sumForInterval += parsedValue;
                i++;
                date.plus(15, ChronoUnit.SECONDS);
            }
        }
        return ret;
    }

    /**
     * Ermittelt das Maximum für eine Zeitreihe
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesMax(List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        System.out.println("aggregateSeriesMax" + id);

        List<ValueDto> ret = new ArrayList<>();
        long startAsMilliseconds = start.toEpochMilli();
        Instant date = Instant.ofEpochMilli(start.toEpochMilli());
        int foundIntervals = 0;
        float lastmax;
        try {
            lastmax = Float.parseFloat(data.get(0).getValue().replace("h", "").trim());
        } catch (NumberFormatException e) {
            lastmax = 0f;
        }
        int i = 0;
        while (date.isBefore(end) || date.equals(end)) {
            if (i < data.size()) {
                if (startAsMilliseconds + (foundIntervals + 1) * interval == date.toEpochMilli()) {
                    ret.add(new ValueDto("" + lastmax, start.toEpochMilli() + ((foundIntervals) * interval)));
                    foundIntervals++;
                    try {
                        lastmax = Float.parseFloat(data.get(i).getValue().replace("h", "").trim());
                    } catch (NumberFormatException e) {
                        lastmax = 0;
                    }
                    i++;
                    date = date.plus(15, ChronoUnit.SECONDS);
                } else {

                    float parsedValue;
                    try {
                        parsedValue = Float.parseFloat(data.get(i).getValue().replace("h", "").trim());
                    } catch (NumberFormatException e) {
                        parsedValue = 0;
                    }

                    if (lastmax < parsedValue) {
                        lastmax = parsedValue;
                    }
                    i++;
                    date = date.plus(15, ChronoUnit.SECONDS);
                }
            } else {
                break;
            }
        }

        return ret;
    }

    /**
     * Aggregiert die Zeitreihe; Taktik ist dass solange ein größerer Wert gesucht wird, bis der Wert signifikant abfällt
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesMaxBeforeMinor(List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        System.out.println("aggregateSeriesMaxBeforeMinor" + id);

        List<ValueDto> ret = new ArrayList<>();
        float lastMax = 0;
        float lastCommitedValue = 0;
        int aktInterval = 0;

        for (int i = 0; i < data.size(); i++) {
            // TODO: Das hier ist nicht wirklich safe, wenn es um <image ...> geht...
            float parsedValue = Float.parseFloat(data.get(i).getValue());
            if (Instant.ofEpochMilli(data.get(i).getDate()).isAfter(end)) {
                break;
            }
            if (aktInterval == interval) {
                // Ende des Intervalls erreicht => Übernahme des Wertes und Intervallsuche von neuem starten
                ret.add(new ValueDto("" + lastMax, data.get(i).getDate() - interval));
                lastCommitedValue = lastMax;
                aktInterval = 0;
                lastMax = 0;
            } else {
                if (lastMax < parsedValue && lastCommitedValue != parsedValue) {
                    lastMax = parsedValue;
                }
            }
            aktInterval += 15 * 1000;
        }
        return ret;
    }

    /**
     * Aggregiert die Zeitreihe; Taktik ist, dass über die Werte iteriert wird und bei jedem vollständigen Intervall der Wert genommen wird.
     *
     * @param data
     * @param start
     * @param end
     * @param interval
     * @param id
     */
    public List<ValueDto> aggregateSeriesDivVortag(List<ValueDto> data, Instant start, Instant end, int interval, String id) {
        System.out.println("aggregateSeriesDivVortag" + id);
        List<ValueDto> ret = new ArrayList<>();

        int aktInterval = 0;

        if (data.size() > 0) {
            ret.add(new ValueDto("" + data.get(0).getValue(), data.get(0).getDate()));
        }
        for (int i = 0; i < data.size(); i++) {
            // Sind wir am Ende angekommen?
            if (Instant.ofEpochMilli(data.get(i).getDate()).isAfter(end) || Instant.ofEpochMilli(data.get(i).getDate()).equals(end)) {
                break;
            }

            if (aktInterval == interval) {
                // Ende des Intervalls erreicht => Übernahme des Wertes und Intervallsuche von neuem starten
                ret.add(new ValueDto("" + data.get(i).getValue(), Instant.ofEpochMilli(data.get(i).getDate()).toEpochMilli()));
                aktInterval = 0;
            }
            aktInterval += 15 * 1000;
        }
        return ret;
    }


}
