package de.hanke.arnim.heizung;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/24/17.
 */
public class DiagnoseAnlage extends AbstractInfo {

    // Betriebsstatus
    public static final String BETRIEBSSTATUS_TABLE_KEY = "BETRIEBSSTATUS";
    public static final String HEIZKREIS_1_PUMPE = "HEIZKREIS 1 PUMPE";
    public static final String HEIZEN = "HEIZEN";
    public static final String VERDICHTER = "VERDICHTER";

    // Status
    public static final String STATUS_TABLE_KEY = "STATUS";
    public static final String HEIZKREISPUMPE = "HEIZKREISPUMPE";
    public static final String QUELLENPUMPE = "QUELLENPUMPE";
    public static final String PUFFERLADEPUMPE = "PUFFERLADEPUMPE";
    public static final String VERDICHTERSCHUETZ = "VERDICHTERSCHUETZ";

    // Reststillstand
    public static final String RESTSTILLSTAND_TABLE_KEY = "RESTSTILLSTAND";
    public static final String RESTSTILLSTAND = "RESTSTILLSTAND";

    public DiagnoseAnlage() {
        super();
        setUrl(elasticSearchUtils.ADDRESS_ISG + "/?s=2,0");
    }

    @Override
    public void getInformations(String content, long time) {
        elasticSearchUtils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZKREIS_1_PUMPE, time, ES_TYPE_DA_HEIZKREIS_1_PUMPE);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZEN, time, ES_TYPE_DA_HEIZEN);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, VERDICHTER, time, ES_TYPE_DA_VERDICHTER);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, HEIZKREISPUMPE, time, ES_TYPE_DA_HEIZKREISPUMPE);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, QUELLENPUMPE, time, ES_TYPE_DA_QUELLENPUMPE);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, PUFFERLADEPUMPE, time, ES_TYPE_DA_PUFFERLADEPUMPE);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, VERDICHTERSCHUETZ, time, ES_TYPE_DA_VERDICHTERSCHUETZ);
        elasticSearchUtils.putValueForKeyInElasticSearch(content, RESTSTILLSTAND_TABLE_KEY, RESTSTILLSTAND, time, ES_TYPE_DA_RESTSTILLSTAND);
    }
}