package de.hanke.arnim.heizung;

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
        setUrl(elasticSearchUtils.ADDRESS_ISG + "/?s=2,1");
    }

    @Override
    public void getInformations(String content, long time) {
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, REGLER_TABLE_KEY, WPM_3i, ES_TYPE_DS_WPM_3i), time, ES_TYPE_DS_WPM_3i);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, REGLER_TABLE_KEY, SOFTWARE, ES_TYPE_DS_SOFTWARE), time, ES_TYPE_DS_SOFTWARE);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, ISG_TABLE_KEY, SG_READY, ES_TYPE_DS_SG_READY), time, ES_TYPE_DS_SG_READY);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, ISG_TABLE_KEY, HAUPTVERSIONSNUMMER, ES_TYPE_DS_HAUPTVERSIONSNUMMER), time, ES_TYPE_DS_HAUPTVERSIONSNUMMER);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, ISG_TABLE_KEY, NEBENVERSIONSNUMMER, ES_TYPE_DS_NEBENVERSIONSNUMMER), time, ES_TYPE_DS_NEBENVERSIONSNUMMER);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, ISG_TABLE_KEY, REVISIONSNUMMER, ES_TYPE_DS_REVISIONSNUMMER), time, ES_TYPE_DS_REVISIONSNUMMER);
        elasticSearchUtils.putValueIntoElasticsearch(super.getInformation(content, CAN_STATUS_TABLE_KEY, OK, ES_TYPE_DS_OK), time, ES_TYPE_DS_OK);
    }
}
