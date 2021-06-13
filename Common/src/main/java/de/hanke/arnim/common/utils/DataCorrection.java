package de.hanke.arnim.common.utils;

import de.hanke.arnim.common.ValueDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class DataCorrection {

    public static BigDecimal parseDataFromValueDtoToBigDecimal(String dataAsString) {
        try {
            String fixedString = dataAsString
                    .replace(",", ".")
                    .replace("kWh", "")
                    .replace("MWh", "")
                    .replace("h", "")
                    .replace("°C", "")
                    .replace("bar", "")
                    .replace("l/min", "")
                    .replace("%", "")
                    .replace("min", "")
                    .replaceAll("[^0-9.-]", "")
                    .trim();
            return new BigDecimal(fixedString);
        } catch (Exception e) {
            if(dataAsString == null) {
                return BigDecimal.ZERO;
            } else {
                if(dataAsString.isEmpty()) {
                    return BigDecimal.ZERO;
                } else {
                    return BigDecimal.ONE;
                }
            }
        }
    }

    /**
     * Corrects the data from the database
     *
     * @param data     Uncorrected data
     * @param start    Start
     * @param end      End
     * @param interval
     */
    public Map<String, List<ValueDto>> fixUpSeries(Map<String, List<ValueDto>> data, Instant start, Instant end, int interval) {

        Map<String, List<ValueDto>> stringValueDtoMap = new HashMap<>();

        for (Map.Entry<String, List<ValueDto>> stringValueDtoEntry : data.entrySet()) {
            List<ValueDto> retValues = new LinkedList<>();
            switch (AggregationTypes.aggregationConfig.get(stringValueDtoEntry.getKey())) {
                case COUNT:
                    retValues = fixUpSeriesCount(stringValueDtoEntry.getValue(), start, end, stringValueDtoEntry.getKey(), interval);
                    break;
                case AVERAGE:
                case MAX:
                    retValues = parseAllValues(stringValueDtoEntry.getValue());
                    break;
                case MAX_BEFOR_MINOR:
                    retValues = fixUpSeriesMaxBeforeMinor(stringValueDtoEntry.getValue(), start, end, stringValueDtoEntry.getKey(), interval);
                    break;
                case DIV_VORTAG:
                    retValues = fixUpSeriesDivVortag(stringValueDtoEntry.getValue(), start, end, stringValueDtoEntry.getKey(), interval);
                    break;
            }
            stringValueDtoMap.put(stringValueDtoEntry.getKey(), retValues);
        }
        return stringValueDtoMap;
    }

    private List<ValueDto> parseAllValues(List<ValueDto> value) {
        ArrayList<ValueDto> valueDtos = new ArrayList<>();
        for (ValueDto valueDto : value) {
            valueDtos.add(new ValueDto("" + parseDataFromValueDtoToBigDecimal(valueDto.getValue()), valueDto.getDate()));
        }
        return valueDtos;
    }

    private List<ValueDto> fixUpSeriesCount(List<ValueDto> value, Instant start, Instant end, String key, int interval) {
        List<ValueDto> ret = new ArrayList<>();
        for (ValueDto valueDto : value) {

            String reinterpretedData;
            if (valueDto.getValue() == null || valueDto.getValue().isEmpty() || valueDto.getValue().compareTo("0.0") == 0) {
                reinterpretedData = "0";
            } else {
                reinterpretedData = "1";
            }
            ret.add(new ValueDto(reinterpretedData, valueDto.getDate()));
        }
        return ret;
    }

    /**
     * Corrects the data for the TS-Type "Max-Before-Minor"
     */
    public List<ValueDto> fixUpSeriesMaxBeforeMinor(List<ValueDto> data, Instant start, Instant end, String id, int interval) {
        System.out.println("fixUpSeriesMaxBeforeMinor " + id);

        // Ersten Wert ignorieren
        data.remove(0);

        // Nötige Variablen initialisieren
        List<ValueDto> ret = new LinkedList<>();

        Instant lastParsedDate = Instant.ofEpochMilli(data.get(0).getDate());

        // Über alle Werte iterieren, auf das richtige Datum mappen, falls nötig.
        for (int i = 0; i < data.size(); i++) {

            // Werte die vor dem Start liegen nicht beachten
            if (Instant.ofEpochMilli(data.get(i).getDate()).isBefore(start)) {
                continue;
            }

            // Parsen des Wertes
            BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(data.get(i).getValue());
            if (ret.size() > 0) {
                BigDecimal retValMinusOne = parseDataFromValueDtoToBigDecimal(ret.get(ret.size() - 1).getValue());
                if (retValMinusOne.subtract(parsedValue).doubleValue() > 0.5) {
                    // Letzten Wert auf der Zeiztachse verschieben
                    ValueDto removed = ret.remove(ret.size() - 1);
                    Instant correctedInstantForFirstDay = LocalDateTime.ofInstant(lastParsedDate, ZoneId.systemDefault())
                            .toLocalDate().atStartOfDay().plus(1, ChronoUnit.DAYS).minus(interval, ChronoUnit.MILLIS)
                            .toInstant(ZoneId.systemDefault().getRules().getOffset(lastParsedDate));
                    ret.add(new ValueDto(removed.getValue(), correctedInstantForFirstDay.toEpochMilli()));


                    long correctedDateForNextDay = correctedInstantForFirstDay.plus(interval, ChronoUnit.MILLIS).toEpochMilli();
                    ret.add(new ValueDto("" + parsedValue.doubleValue(), correctedDateForNextDay));
                    lastParsedDate = Instant.ofEpochMilli(correctedDateForNextDay);

                } else {
                    ret.add(new ValueDto("" + parsedValue.doubleValue(), data.get(i).getDate()));
                    lastParsedDate = Instant.ofEpochMilli(data.get(i).getDate());
                }
            } else {
                ret.add(new ValueDto("" + parsedValue.doubleValue(), data.get(i).getDate()));
                lastParsedDate = Instant.ofEpochMilli(data.get(i).getDate());
            }
        }
        return ret;
    }

    /**
     * Corrects the data for the TS-Type "Div-Vortag"
     */
    public List<ValueDto> fixUpSeriesDivVortag(List<ValueDto> data, Instant start, Instant end, String id, int interval) {
        System.out.println("fixUpSeriesDivVortag " + id);

        // Representierbarer Start generieren (Fehlerrate des ISGs)
        Instant representalStart = start;
        Instant firstDate = Instant.ofEpochMilli(data.get(0).getDate());
        Instant date = firstDate.minus(1, ChronoUnit.DAYS);

        // Nötige Variablen initialisieren
        List<ValueDto> ret = new ArrayList<>();

        BigDecimal lastValue = parseDataFromValueDtoToBigDecimal(data.get(0).getValue());

        // Über die Werte iterieren und die Werte auf den Vortag datieren
        for (ValueDto datum : data.stream().filter(valueDto -> valueDto.getDate() >= start.toEpochMilli()).collect(Collectors.toList())) {

            if (datum.getValue() == null || datum.getValue().isEmpty()) {
                continue;
            }

            if (date.isAfter(end)) {
                break;
            }

            BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(datum.getValue());

            // Falls der Wert vor dem repräsentiertbaren Start liegt, wird dieser nicht extrahiert
            Instant instant = Instant.ofEpochMilli(datum.getDate());
            Instant instant1 = Instant.ofEpochMilli(
                    LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                            .toLocalDate().atStartOfDay()
                            .toEpochSecond(ZoneOffset.UTC) * 1000);

            if (!(instant1.isAfter(representalStart) || instant1.equals(representalStart))) {
                lastValue = parsedValue;
                continue;
            }

            // Wenn der gelesene Wert ungleich dem vorherigen ist, wird der aktuelle Wert extrahiert
            if (lastValue.compareTo(parsedValue) != 0) {
                ret.add(new ValueDto("" + parsedValue.doubleValue(), date.toEpochMilli()));
                date = date.plus(1, ChronoUnit.DAYS);
                lastValue = parsedValue;
            }
        }
        Optional<ValueDto> min = ret.stream().min(Comparator.comparingLong(ValueDto::getDate));
        if (min.isPresent()) {
            if (min.get().getDate() > start.toEpochMilli()) {
                // Auffüllen
                Optional<ValueDto> beforeFirstValidValue = data.stream().filter(valueDto -> valueDto.getDate() < start.toEpochMilli()).max(Comparator.comparingLong(ValueDto::getDate));
                if (beforeFirstValidValue.isPresent()) {
                    BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(beforeFirstValidValue.get().getValue());
                    for (Instant instant = date; instant.toEpochMilli() < (min.get().getDate()); instant = instant.plus(interval, ChronoUnit.MILLIS)) {
                        ret.add(new ValueDto("" + parsedValue.doubleValue(), instant.toEpochMilli()));
                    }
                }
            }
        } else {
            // Gar kein Wert geschrieben
            // => fuer das gesamte Intervall den letzten gefundenen Wert annehmen
            Optional<ValueDto> beforeFirstValidValue = data.stream().filter(valueDto -> valueDto.getDate() < start.toEpochMilli()).max(Comparator.comparingLong(ValueDto::getDate));
            if (beforeFirstValidValue.isPresent()) {
                BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(beforeFirstValidValue.get().getValue());
                for (Instant instant = date; instant.toEpochMilli() < (end.toEpochMilli()); instant = instant.plus(interval, ChronoUnit.MILLIS)) {
                    ret.add(new ValueDto("" + parsedValue.doubleValue(), instant.toEpochMilli()));
                }
            }
        }
        return ret;
    }

}
