package de.hanke.arnim.DataCorrector;

import de.hanke.arnim.TimeSeriesToolSet.AggregationTypes;
import de.hanke.arnim.TimeSeriesToolSet.AggregationTypes.AggregationType;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseries;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseriesValue;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static de.hanke.arnim.TimeSeriesToolSet.AggregateFunctions.moveInstantToStartOfDay;

public class FixUpSeries {

    public static Map<String, PeriodicTimeseries> fixUpSeries(Map<String, PeriodicTimeseries> timeseriesMap) {

        List<Map.Entry<String, PeriodicTimeseries>> collect = timeseriesMap.entrySet()
                .parallelStream()
                .map(timeseriesEntry -> new AbstractMap.SimpleEntry<>(timeseriesEntry.getKey(), fixUpSeries(timeseriesEntry.getValue())))
                .collect(Collectors.toList());

        HashMap<String, PeriodicTimeseries> stringPeriodicTimeseriesHashMap = new HashMap<>();

        for (Map.Entry<String, PeriodicTimeseries> stringPeriodicTimeseriesEntry : collect) {
            stringPeriodicTimeseriesHashMap.put(stringPeriodicTimeseriesEntry.getKey(), stringPeriodicTimeseriesEntry.getValue());
        }

        return stringPeriodicTimeseriesHashMap;
    }

    public static PeriodicTimeseries fixUpSeries(PeriodicTimeseries periodicTimeseries) {

        AggregationType aggregationType = AggregationTypes.aggregationConfig.get("heizungssuite_" + periodicTimeseries.getTsId());

        switch (aggregationType) {
            case MAX:
                return periodicTimeseries;
            case COUNT:
                return periodicTimeseries;
            case AVERAGE:
                return periodicTimeseries;
            case DIV_VORTAG:
                return fixUpDivVortag(periodicTimeseries);
            case MAX_BEFOR_MINOR:
                return fixUpMaxBeforMinor(periodicTimeseries);
            default:
                return periodicTimeseries;
        }

    }

    private static PeriodicTimeseries fixUpDivVortag(PeriodicTimeseries periodicTimeseries) {
        if (periodicTimeseries.getValues().size() == 0) {
            return periodicTimeseries;
        }

        ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();

        for (int i = 0; i < periodicTimeseries.getValues().size(); i++) {
            PeriodicTimeseriesValue value = periodicTimeseries.getValues().get(i);
            values.add(new PeriodicTimeseriesValue(moveInstantToStartOfDay(value.getTime()), value.getValue()));
        }

        PeriodicTimeseries fixedPeriodicTimeseries = new PeriodicTimeseries(periodicTimeseries.getPeriodicTimeseriesHead(), values);
        return fixedPeriodicTimeseries;
    }

    private static PeriodicTimeseries fixUpMaxBeforMinor(PeriodicTimeseries periodicTimeseries) {
        if (periodicTimeseries.getValues().size() == 0) {
            return periodicTimeseries;
        }

        ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();

        PeriodicTimeseriesValue previousValue = periodicTimeseries.getValues().get(0);

        for (int i = 0; i < periodicTimeseries.getValues().size(); i++) {
            PeriodicTimeseriesValue value = periodicTimeseries.getValues().get(i);
            if (previousValue.getValue() - value.getValue() > 0.5) {
                // Den Messfehler von ISG korrigieren -.-
                value.setTime(moveInstantToStartOfDay(value.getTime()));
            }
            values.add(value);
            previousValue = value;
        }

        PeriodicTimeseries fixedPeriodicTimeseries = new PeriodicTimeseries(periodicTimeseries.getPeriodicTimeseriesHead(), values);
        return fixedPeriodicTimeseries;
    }

}
