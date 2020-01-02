package de.hank.arnim.utils;

import de.hank.arnim.ValueDto;
import de.hank.arnim.utils.AggregationTypes.AggregationType;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static de.hank.arnim.utils.AggregationTypes.AggregationType.*;

public class DataCorrection {

    /**
     * Corrects the data from the database
     *
     * @param data Uncorrected data
     * @param start Start
     * @param end End
     * @param interval
     */
    public Map<String, List<ValueDto>> fixUpSeries(Map<String, List<ValueDto>> data, Instant start, Instant end, int interval) {

        Map<String, List<ValueDto>> stringValueDtoMap = new HashMap<>();

        for (Map.Entry<String, List<ValueDto>> stringValueDtoEntry : data.entrySet()) {
            List<ValueDto> retValues = new LinkedList<>();
            switch(AggregationTypes.aggregationConfig.get(stringValueDtoEntry.getKey())) {
                case COUNT:
                case AVERAGE:
                case MAX:
                    retValues = stringValueDtoEntry.getValue();
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

    /**
     * Corrects the data for the TS-Type "Max-Before-Minor"
     */
    public List<ValueDto> fixUpSeriesMaxBeforeMinor(List<ValueDto> data, Instant start, Instant end, String id, int interval) {
        System.out.println("fixUpSeriesMaxBeforeMinor " + id);

        // Ersten Wert ignorieren
        data.remove(0);

        // Nötige Variablen initialisieren
        List<ValueDto> ret = new LinkedList<>();
        int foundIntervals = 0;

        BigDecimal lastValue;
        try {
            lastValue = new BigDecimal(data.get(0).getValue());
        } catch (Exception e) {
            lastValue = BigDecimal.ZERO;
        }
        long lastDate = data.get(0).getDate();

        // Über alle Werte iterieren, auf das richtige Datum mappen, falls nötig.
        for (int i = 0; i < data.size(); i++) {

            // Werte die vor dem Start liegen nicht beachten
            if (Instant.ofEpochMilli(data.get(i).getDate()).isBefore(start)) {
                continue;
            }

            // Parsen des Wertes
            BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(data.get(i).getValue());

            // Differenz zwischen den letzten Werten ermitteln
            ret.add(new ValueDto("" + parsedValue.doubleValue(),  data.get(i).getDate()));
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
        Instant date = start.minus(interval, ChronoUnit.MILLIS);

        // Nötige Variablen initialisieren
        List<ValueDto> ret = new ArrayList<>();
        BigDecimal lastValue = parseDataFromValueDtoToBigDecimal(data.get(0).getValue());

        // Über die Werte iterieren und die Werte auf den Vortag datieren
        for (int i = 0; i < data.size(); i++) {
            BigDecimal parsedValue = parseDataFromValueDtoToBigDecimal(data.get(i).getValue());

            // Falls der Wert vor dem repräsentiertbaren Start liegt, wird dieser nicht extrahiert
            Instant instant = Instant.ofEpochMilli(data.get(i).getDate());
            Instant instant1 = Instant.ofEpochMilli(
                    LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                            .toLocalDate().atStartOfDay().plus(interval, ChronoUnit.MILLIS)
                            .toEpochSecond(ZoneOffset.UTC) * 1000);

            if (!(instant1.isAfter(representalStart) || instant1.equals(representalStart))) {
                lastValue = parsedValue;
                continue;
            }

            // Wenn der gelesene Wert ungleich dem vorherigen ist, wird der aktuelle Wert extrahiert
            if (lastValue.compareTo(parsedValue) != 0) {
                ret.add(new ValueDto("" + lastValue.doubleValue(), date.toEpochMilli()));
                date.plus(interval,  ChronoUnit.MILLIS);
                lastValue = parsedValue;
            }
        }
        return ret;
    }

    private static BigDecimal parseDataFromValueDtoToBigDecimal(String dataAsString) {
        try {
            String fixedString = dataAsString.replace(",", ".").replace("kWh", "").replace("MWh", "").trim();
            return new BigDecimal(fixedString);
        } catch(Exception e) {
            return BigDecimal.ZERO;
        }
    }

}
