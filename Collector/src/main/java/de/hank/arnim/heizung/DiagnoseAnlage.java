package de.hank.arnim.heizung;

import de.hank.arnim.Utils;

import static de.hank.arnim.Constant.*;
import static de.hank.arnim.Utils.putValueForKeyInElasticSearch;

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
        super(Utils.ADDRESS_ISG + "/?s=2,0");
    }

    @Override
    public void getInformations(String content, long time) {
        putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZKREIS_1_PUMPE, time, ES_TYPE_DA_HEIZKREIS_1_PUMPE);
        putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, HEIZEN, time, ES_TYPE_DA_HEIZEN);
        putValueForKeyInElasticSearch(content, BETRIEBSSTATUS_TABLE_KEY, VERDICHTER, time, ES_TYPE_DA_VERDICHTER);
        putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, HEIZKREISPUMPE, time, ES_TYPE_DA_HEIZKREISPUMPE);
        putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, QUELLENPUMPE, time, ES_TYPE_DA_QUELLENPUMPE);
        putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, PUFFERLADEPUMPE, time, ES_TYPE_DA_PUFFERLADEPUMPE);
        putValueForKeyInElasticSearch(content, STATUS_TABLE_KEY, VERDICHTERSCHUETZ, time, ES_TYPE_DA_VERDICHTERSCHUETZ);
        putValueForKeyInElasticSearch(content, RESTSTILLSTAND_TABLE_KEY, RESTSTILLSTAND, time, ES_TYPE_DA_RESTSTILLSTAND);
    }
}
