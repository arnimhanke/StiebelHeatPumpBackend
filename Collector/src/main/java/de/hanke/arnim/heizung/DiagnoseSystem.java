package de.hanke.arnim.heizung;

import de.hanke.arnim.common.Utils;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/24/17.
 */
public class DiagnoseSystem extends AbstractInfo {


    // Regler
    public static final String REGLER_TABLE_KEY = "REGLER";
    public static final String WPM_3i = "WPM 3i";
    public static final String SOFTWARE = "SOFTWARE";

    // ISG
    public static final String ISG_TABLE_KEY = "ISG";
    public static final String SG_READY = "SG-READY";
    public static final String HAUPTVERSIONSNUMMER = "HAUPTVERSIONSNUMMER";
    public static final String NEBENVERSIONSNUMMER = "NEBENVERSIONSNUMMER";
    public static final String REVISIONSNUMMER = "REVISIONSNUMMER";

    // CAN Status
    public static final String CAN_STATUS_TABLE_KEY = "CAN Status";
    public static final String OK = "OK";


    public DiagnoseSystem() {
        super();
        setUrl(utils.ADDRESS_ISG + "/?s=2,1");
    }

    @Override
    public void getInformations(String content, long time) {
        utils.putValueForKeyInElasticSearch(content, REGLER_TABLE_KEY, WPM_3i, time, ES_TYPE_DS_WPM_3i);
        utils.putValueForKeyInElasticSearch(content, REGLER_TABLE_KEY, SOFTWARE, time, ES_TYPE_DS_SOFTWARE);
        utils.putValueForKeyInElasticSearch(content, ISG_TABLE_KEY, SG_READY, time, ES_TYPE_DS_SG_READY);
        utils.putValueForKeyInElasticSearch(content, ISG_TABLE_KEY, HAUPTVERSIONSNUMMER, time, ES_TYPE_DS_HAUPTVERSIONSNUMMER);
        utils.putValueForKeyInElasticSearch(content, ISG_TABLE_KEY, NEBENVERSIONSNUMMER, time, ES_TYPE_DS_NEBENVERSIONSNUMMER);
        utils.putValueForKeyInElasticSearch(content, ISG_TABLE_KEY, REVISIONSNUMMER, time, ES_TYPE_DS_REVISIONSNUMMER);
        utils.putValueForKeyInElasticSearch(content, CAN_STATUS_TABLE_KEY, OK, time, ES_TYPE_DS_OK);
    }
}
