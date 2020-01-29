package de.hanke.arnim.common.spielwiese;

import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.dtos.PeriodicTimeseries;
import de.hanke.arnim.common.dtos.PeriodicTimeseriesValue;
import de.hanke.arnim.common.dtos.Raster;
import de.hanke.arnim.common.utils.SeriesActions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MigrateElasticValuesToInfluxDB {

    public static void main(String[] args) {
        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();


        Instant start = Instant.parse("2017-12-30T23:00:00.00Z");
        Instant end = Instant.parse("2019-08-31T23:00:00.00Z");

        ArrayList<String> allIndexes = new ArrayList<>();

        for (String allIndex : Constant.ALL_INDEXES) {
            allIndexes.add(Constant.ES_INDEX_PREFIX + allIndex.toLowerCase());
        }

        for (String index : allIndexes.subList(0, 10)) {

            //String index = Constant.ES_TYPE_IW_VD_HEIZEN;
            InfluxDBUtils influxDBUtils = new InfluxDBUtils("StiebelEltronHeatPump");
            System.out.println("Starte mit Index " + index);
            long startPerformance = System.currentTimeMillis();
            Map<String, List<ValueDto>> dataFromIndexInInterval = elasticSearchUtils.getDataFromIndexInInterval(Collections.singletonList(index), start, end);
            //Map<String, List<ValueDto>> dataFromIndexInIntervalToLowerCase = new HashMap<>();
            //dataFromIndexInInterval.keySet().forEach(s -> dataFromIndexInIntervalToLowerCase.put(Constant.ES_INDEX_PREFIX + s.toLowerCase(), dataFromIndexInInterval.get(s)));

            SeriesActions seriesActions = new SeriesActions();
            Map<String, List<ValueDto>> stringListMap = seriesActions.preparingDataForFurtherUse(dataFromIndexInInterval, start, end, Raster.PT15S);
            for (String tsName : stringListMap.keySet()) {

                ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();
                List<ValueDto> valuesFromElastic = stringListMap.get(tsName);
                for (ValueDto valueDto : valuesFromElastic) {
                    values.add(new PeriodicTimeseriesValue(Instant.ofEpochMilli(valueDto.getDate()), Double.parseDouble(valueDto.getValue())));
                }
                PeriodicTimeseries tsDto = new PeriodicTimeseries(tsName, Raster.PT15S, values);
                influxDBUtils.insertTimeSeries(tsDto);
            }
            System.out.println("Finished mit Index " + index + " nach " + (System.currentTimeMillis() - startPerformance) / 1000);
        }
    }
}
