package de.hanke.arnim;

import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseries;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseriesValue;
import de.hanke.arnim.TimeSeriesToolSet.Raster;
import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static de.hanke.arnim.common.utils.DataCorrection.parseDataFromValueDtoToBigDecimal;

public class Spielwiese {

    public static void main(String[] args) {

        String x = "8,5 ��C";
        System.out.println(x);

//        ElasticSearchUtils elasticSearchUtils9201 = new ElasticSearchUtils();
//        elasticSearchUtils9201.PORT_ELASTICSEARCH = 9201;
//
//        ElasticSearchUtils elasticSearchUtils9202 = new ElasticSearchUtils();
//        elasticSearchUtils9202.PORT_ELASTICSEARCH = 9202;
//
//        ElasticSearchUtils elasticSearchUtils9203 = new ElasticSearchUtils();
//        elasticSearchUtils9203.PORT_ELASTICSEARCH = 9203;
//
//        ArrayList<String> allIndexes = new ArrayList<>();
//
//        for (String allIndex : Constant.ALL_INDEXES) {
//            allIndexes.add(Constant.ES_INDEX_PREFIX + allIndex.toLowerCase());
//        }
//
//        List<ElasticSearchUtils> utils = new ArrayList<>();
//        utils.add(elasticSearchUtils9201);
//        utils.add(elasticSearchUtils9202);
//        utils.add(elasticSearchUtils9203);
//
//        utils.parallelStream().forEach(elasticSearchUtils -> moveData(elasticSearchUtils, allIndexes));
    }

    private static void moveData(ElasticSearchUtils elasticSearchUtils, ArrayList<String> allIndexes) {
        Instant start = Instant.parse("2010-12-30T23:00:00.00Z");
        Instant end = Instant.parse("2025-08-31T23:00:00.00Z");

        for (String index : allIndexes) {

            InfluxDBUtils influxDBUtils = new InfluxDBUtils("StiebelEltronHeatPumpRawDatas");
            System.out.println("Starte mit Index " + index);
            long startPerformance = System.currentTimeMillis();
            Map<String, List<ValueDto>> dataFromIndexInInterval = elasticSearchUtils.getDataFromIndexInInterval(Collections.singletonList(index), start, end);

            for (String tsName : dataFromIndexInInterval.keySet()) {

                ArrayList<PeriodicTimeseriesValue> values = new ArrayList<>();
                List<ValueDto> valuesFromElastic = dataFromIndexInInterval.get(tsName);
                for (ValueDto valueDto : valuesFromElastic) {
                    values.add(new PeriodicTimeseriesValue(Instant.ofEpochMilli(valueDto.getDate()), parseDataFromValueDtoToBigDecimal(valueDto.getValue()).doubleValue()));
                }
                PeriodicTimeseries tsDto = new PeriodicTimeseries(tsName.replace(Constant.ES_INDEX_PREFIX, "")
                        .replace("ü", "ue")
                        .replace("ä", "ae")
                        .replace("ö", "oe"), Raster.PT15S, values);
                influxDBUtils.insertTimeSeries(tsDto);
            }
            System.out.println("Finished mit Index " + index + " nach " + (System.currentTimeMillis() - startPerformance) / 1000);
        }
    }
}
