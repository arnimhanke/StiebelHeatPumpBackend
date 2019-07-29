package de.hanke.arnim.heizung;

import de.hanke.arnim.common.Utils;

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
        setUrl(utils.ADDRESS_ISG + "/?s=2,0");
    }

    @Override
    public void getInformations(String content, long time) {
        utils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZKREIS_1_PUMPE, time, ES_TYPE_DA_HEIZKREIS_1_PUMPE);
        utils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZEN, time, ES_TYPE_DA_HEIZEN);
        utils.putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, VERDICHTER, time, ES_TYPE_DA_VERDICHTER);
        utils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, HEIZKREISPUMPE, time, ES_TYPE_DA_HEIZKREISPUMPE);
        utils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, QUELLENPUMPE, time, ES_TYPE_DA_QUELLENPUMPE);
        utils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, PUFFERLADEPUMPE, time, ES_TYPE_DA_PUFFERLADEPUMPE);
        utils.putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, VERDICHTERSCHUETZ, time, ES_TYPE_DA_VERDICHTERSCHUETZ);
        utils.putValueForKeyInElasticSearch(content, RESTSTILLSTAND_TABLE_KEY, RESTSTILLSTAND, time, ES_TYPE_DA_RESTSTILLSTAND);
    }
}
