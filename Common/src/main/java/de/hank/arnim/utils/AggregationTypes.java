package de.hank.arnim.utils;

import java.util.HashMap;
import java.util.Map;

public class AggregationTypes {

    enum AggregationType {
        COUNT,
        AVERAGE,
        MAX,
        MAX_BEFOR_MINOR,
        DIV_VORTAG
    }

    public static final Map<String, AggregationType> aggregationConfig = new HashMap<>();

    static {
        aggregationConfig.put("heizungssuite_da_heizen", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_heizkreis_1_pumpe", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_heizkreispumpe", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_pufferladepumpe", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_quellenpumpe", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_reststillstand", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_da_verdichter", AggregationType.COUNT);
        aggregationConfig.put("heizungssuite_da_verdichterschuetz", AggregationType.COUNT);

        aggregationConfig.put("heizungssuite_ia_anlagenfrost", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_aussentemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_heizungsdruck", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_isttemperatur_fek", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_isttemperatur_gebläse", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_isttemperatur_hk_1", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_pufferisttemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_puffersolltemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_quellendruck", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_quellentemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_raumfeuchte", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_rücklaufisttemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_solltemperatur_fek", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_solltemperatur_gebläse", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_solltemperatur_hk_1", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_taupunkttemperatur", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_volumenstrom", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_vorlaufisttemperatur_nhz", AggregationType.AVERAGE);
        aggregationConfig.put("heizungssuite_ia_vorlaufisttemperatur_wp", AggregationType.AVERAGE);

        aggregationConfig.put("heizungssuite_iw_leistungsaufnahme_vd_heizen_summe", AggregationType.DIV_VORTAG);
        aggregationConfig.put("heizungssuite_iw_leistungsaufnahme_vd_heizen_tag", AggregationType.MAX_BEFOR_MINOR);
        aggregationConfig.put("heizungssuite_iw_nhz_1", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_nhz_1_durch_2", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_nhz_2", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_vd_heizen", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_vd_kühlen", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_verdichter", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_iw_waermemenge_nhz_heizen_summe", AggregationType.DIV_VORTAG);
        aggregationConfig.put("heizungssuite_iw_waermemenge_vd_heizen_summe", AggregationType.DIV_VORTAG);
        aggregationConfig.put("heizungssuite_iw_waermemenge_vd_heizen_tag", AggregationType.MAX_BEFOR_MINOR);

        aggregationConfig.put("heizungssuite_ds_hauptversionsnummer", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_nebenversionsnummer", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_ok", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_revisionsnummer", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_sg_ready", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_software", AggregationType.MAX);
        aggregationConfig.put("heizungssuite_ds_wpm_3i", AggregationType.MAX);
    }
}
