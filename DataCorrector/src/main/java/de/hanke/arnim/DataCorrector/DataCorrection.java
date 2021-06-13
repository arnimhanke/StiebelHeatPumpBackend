package de.hanke.arnim.DataCorrector;

import de.hanke.arnim.TSTool.*;
import de.hanke.arnim.TSPersistence.influx.InfluxTimeseries;
import de.hanke.arnim.TSPersistence.influx.InfluxDBService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class DataCorrection {

    public static void main(String[] args) {

//        copyDataWrapper();

        correctDataFromLastDay();

//        Timer timerMoveData = new Timer();
//        timerMoveData.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, 1000 * 60 * 60 * 24); // Jeden Tag einmal

    }

    private static void correctDataFromLastDay() {
        String stiebelEltronHeatPumpRawDatasDatabase = "StiebelEltronHeatPumpRawDatasTest";
        String stiebelEltronHeatPumpCorrectedDatasDatabase = "StiebelEltronHeatPumpCorrectedDatasTest";

        Instant from = Instant.parse("2020-07-31T00:00:00.00Z");
        Instant to = Instant.parse("2020-12-11T00:00:00.00Z");

        correctDataForGiveIntervalAndDataBase(stiebelEltronHeatPumpRawDatasDatabase, stiebelEltronHeatPumpCorrectedDatasDatabase, from, to);

    }

    private static void copyDataWrapper() {
        String fromDatabase = "StiebelEltronHeatPumpRawDatas";
        String toDatabase = "StiebelEltronHeatPumpRawDatasTest";

        Instant overallStart = Instant.parse("2020-04-30T00:00:00.00Z");
        Instant overallEnd = Instant.parse("2021-01-01T00:00:00.00Z");

        for (Instant i = overallStart; i.isBefore(overallEnd); i = i.plus(365, ChronoUnit.DAYS)) {
            Instant to = i.plus(365, ChronoUnit.DAYS).plus(10, ChronoUnit.DAYS);
            copyDataForGiveIntervalAndDataBase(fromDatabase, toDatabase, i, to);
        }
    }

    private static void correctDataForGiveIntervalAndDataBase(String stiebelEltronHeatPumpRawDatasDatabase, String stiebelEltronHeatPumpCorrectedDatasDatabase, Instant fromGesamt, Instant toGesamt) {

        Instant lastFrom;
        for (lastFrom = fromGesamt; lastFrom.isBefore(toGesamt.minus(15, ChronoUnit.DAYS)); lastFrom = lastFrom.plus(15, ChronoUnit.DAYS)) {
            Instant fromCalculated = lastFrom.minus(10, ChronoUnit.HOURS);
            Instant toCalculcated = lastFrom.plus(15, ChronoUnit.DAYS);
            secondWrapper(stiebelEltronHeatPumpRawDatasDatabase, stiebelEltronHeatPumpCorrectedDatasDatabase, fromCalculated, toCalculcated);
        }
        secondWrapper(stiebelEltronHeatPumpRawDatasDatabase, stiebelEltronHeatPumpCorrectedDatasDatabase, lastFrom.minus(10, ChronoUnit.HOURS), toGesamt);
    }

    private static void secondWrapper(String stiebelEltronHeatPumpRawDatasDatabase, String stiebelEltronHeatPumpCorrectedDatasDatabase, Instant fromCalculated, Instant toCalculcated) {
        System.out.println(new Interval(fromCalculated, toCalculcated).toString());
        Map<String, PeriodicTimeseriesHead> tsIds = getListOfAllPeriodicTimeseriesHeads(stiebelEltronHeatPumpRawDatasDatabase);

        System.out.println("Start loading data");
        long startLoadingData = System.currentTimeMillis();

        Map<String, List<PeriodicTimeseriesValue>> timeseriesForInterval = InfluxDBService.getTimeseriesForInterval(new ArrayList<>(tsIds.keySet()), stiebelEltronHeatPumpRawDatasDatabase, fromCalculated, toCalculcated);

        System.out.println("Finished loading data " + (System.currentTimeMillis() - startLoadingData));

        System.out.println("Start for raster");
        long startForRaster = System.currentTimeMillis();

        List<InfluxTimeseries> timeseriesToSave = timeseriesForInterval.entrySet().parallelStream().map(entry -> {

            PeriodicTimeseriesHead periodicTimeseriesHead = tsIds.get(entry.getKey());

            PeriodicTimeseries periodicTimeseries = new PeriodicTimeseries(periodicTimeseriesHead, entry.getValue());

            PeriodicTimeseries correctedData = FixUpSeries.fixUpSeries(periodicTimeseries);

            return new InfluxTimeseries(entry.getKey(), stiebelEltronHeatPumpCorrectedDatasDatabase, correctedData.getValues());
        }).collect(Collectors.toList());

        System.out.println("Finished for raster " + (System.currentTimeMillis() - startForRaster));

        System.out.println("Start saving data");
        long startSavingData = System.currentTimeMillis();

        InfluxDBService.putTimeseriesValues(timeseriesToSave, stiebelEltronHeatPumpCorrectedDatasDatabase);


        System.out.println("Finished saving data " + (System.currentTimeMillis() - startSavingData));
    }

    private static void copyDataForGiveIntervalAndDataBase(String fromDatabase, String toDatabase, Instant from, Instant to) {
        Map<String, PeriodicTimeseriesHead> tsIds = getListOfAllPeriodicTimeseriesHeads(fromDatabase);

        System.out.println("Start loading data");
        long startLoadingData = System.currentTimeMillis();

        Map<String, List<PeriodicTimeseriesValue>> timeseriesForInterval = InfluxDBService.getTimeseriesForInterval(new ArrayList<>(tsIds.keySet()), fromDatabase, from, to);

        System.out.println("Finished loading data " + (System.currentTimeMillis() - startLoadingData));

        ArrayList<InfluxTimeseries> timeseriesToSave = new ArrayList<>();

        System.out.println("Start for raster");
        long startForRaster = System.currentTimeMillis();

        timeseriesForInterval.forEach((key, value) -> {
            timeseriesToSave.add(new InfluxTimeseries(key, toDatabase, value));
        });

        System.out.println("Finished for raster " + (System.currentTimeMillis() - startForRaster));

        System.out.println("Start saving data");
        long startSavingData = System.currentTimeMillis();

        InfluxDBService.putTimeseriesValues(timeseriesToSave, toDatabase);


        System.out.println("Finished saving data " + (System.currentTimeMillis() - startSavingData));
    }

    private static Map<String, PeriodicTimeseriesHead> getListOfAllPeriodicTimeseriesHeads(String stiebelEltronHeatPumpRawDatasDatabase) {
        Map<String, PeriodicTimeseriesHead> tsIds = new HashMap<>();

        tsIds.put("ia_raumfeuchte", new PeriodicTimeseriesHead("ia_raumfeuchte", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_nhz_1_durch_2", new PeriodicTimeseriesHead("iw_nhz_1_durch_2", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_waermemenge_vd_heizen_tag", new PeriodicTimeseriesHead("iw_waermemenge_vd_heizen_tag", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_vorlaufisttemperatur_wp", new PeriodicTimeseriesHead("ia_vorlaufisttemperatur_wp", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_pufferisttemperatur", new PeriodicTimeseriesHead("ia_pufferisttemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_reststillstand", new PeriodicTimeseriesHead("da_reststillstand", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_quellenpumpe", new PeriodicTimeseriesHead("da_quellenpumpe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_puffersolltemperatur", new PeriodicTimeseriesHead("ia_puffersolltemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_anlagenfrost", new PeriodicTimeseriesHead("ia_anlagenfrost", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_nhz_1", new PeriodicTimeseriesHead("iw_nhz_1", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_isttemperatur_fek", new PeriodicTimeseriesHead("ia_isttemperatur_fek", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_sg_ready", new PeriodicTimeseriesHead("ds_sg_ready", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_quellendruck", new PeriodicTimeseriesHead("ia_quellendruck", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_leistungsaufnahme_vd_heizen_summe", new PeriodicTimeseriesHead("iw_leistungsaufnahme_vd_heizen_summe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_ok", new PeriodicTimeseriesHead("ds_ok", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_vd_heizen", new PeriodicTimeseriesHead("iw_vd_heizen", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_solltemperatur_fek", new PeriodicTimeseriesHead("ia_solltemperatur_fek", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_software", new PeriodicTimeseriesHead("ds_software", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_volumenstrom", new PeriodicTimeseriesHead("ia_volumenstrom", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_isttemperatur_hk_1", new PeriodicTimeseriesHead("ia_isttemperatur_hk_1", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_waermemenge_vd_heizen_summe", new PeriodicTimeseriesHead("iw_waermemenge_vd_heizen_summe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_quellentemperatur", new PeriodicTimeseriesHead("ia_quellentemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_ruecklaufisttemperatur", new PeriodicTimeseriesHead("ia_ruecklaufisttemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_aussentemperatur", new PeriodicTimeseriesHead("ia_aussentemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_heizungsdruck", new PeriodicTimeseriesHead("ia_heizungsdruck", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_taupunkttemperatur", new PeriodicTimeseriesHead("ia_taupunkttemperatur", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_nhz_2", new PeriodicTimeseriesHead("iw_nhz_2", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_isttemperatur_geblaese", new PeriodicTimeseriesHead("ia_isttemperatur_geblaese", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_solltemperatur_geblaese", new PeriodicTimeseriesHead("ia_solltemperatur_geblaese", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_heizkreispumpe", new PeriodicTimeseriesHead("da_heizkreispumpe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_leistungsaufnahme_vd_heizen_tag", new PeriodicTimeseriesHead("iw_leistungsaufnahme_vd_heizen_tag", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_hauptversionsnummer", new PeriodicTimeseriesHead("ds_hauptversionsnummer", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_wpm_3i", new PeriodicTimeseriesHead("ds_wpm_3i", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_waermemenge_nhz_heizen_summe", new PeriodicTimeseriesHead("iw_waermemenge_nhz_heizen_summe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_heizen", new PeriodicTimeseriesHead("da_heizen", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_solltemperatur_hk_1", new PeriodicTimeseriesHead("ia_solltemperatur_hk_1", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_vd_kuehlen", new PeriodicTimeseriesHead("iw_vd_kuehlen", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_revisionsnummer", new PeriodicTimeseriesHead("ds_revisionsnummer", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ds_nebenversionsnummer", new PeriodicTimeseriesHead("ds_nebenversionsnummer", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("iw_verdichter", new PeriodicTimeseriesHead("iw_verdichter", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_verdichter", new PeriodicTimeseriesHead("da_verdichter", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_verdichterschuetz", new PeriodicTimeseriesHead("da_verdichterschuetz", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_heizkreis_1_pumpe", new PeriodicTimeseriesHead("da_heizkreis_1_pumpe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_vorlaufisttemperatur_nhz", new PeriodicTimeseriesHead("ia_vorlaufisttemperatur_nhz", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("da_pufferladepumpe", new PeriodicTimeseriesHead("da_pufferladepumpe", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_isttemperatur_flaeche", new PeriodicTimeseriesHead("ia_isttemperatur_flaeche", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        tsIds.put("ia_solltemperatur_flaeche", new PeriodicTimeseriesHead("ia_solltemperatur_flaeche", Raster.PT15S, TimeseriesUnit.mW, stiebelEltronHeatPumpRawDatasDatabase));
        return tsIds;
    }

}
