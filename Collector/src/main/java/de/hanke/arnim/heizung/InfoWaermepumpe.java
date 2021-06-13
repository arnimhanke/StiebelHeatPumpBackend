package de.hanke.arnim.heizung;

import de.hanke.arnim.Properties;
import de.hanke.arnim.common.InfluxDBUtils;

import java.time.Instant;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/23/17.
 */
public class InfoWaermepumpe extends AbstractInfo {

    public static final String WAERMEMENGE_TABLE_KEY = "W.*RMEMENGE";
    public static final String WAERMEMENGE_VD_HEIZEN_TAG = "VD HEIZEN TAG";
    public static final String WAERMEMENGE_VD_HEIZEN_SUMME = "VD HEIZEN SUMME";
    public static final String WAERMEMENGE_NHZ_HEIZEN_SUMME = "NHZ HEIZEN SUMME";

    public static final String LEISTUNGSAUFNAHME_TABLE_KEY = "LEISTUNGSAUFNAHME";
    public static final String LEISTUNGSAUFNAHME_VD_HEIZEN_TAG = "VD HEIZEN TAG";
    public static final String LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME = "VD HEIZEN SUMME";

    public static final String LAUFZEIT_TABLE_KEY = "LAUFZEIT";
    public static final String VD_HEIZEN = "VD HEIZEN";
    public static final String VD_KUEHLEN = "VD K.*HLEN";
    public static final String NHZ_1 = "NHZ 1";
    public static final String NHZ_2 = "NHZ 2";
    public static final String NHZ_1_DURCH_2 = "NHZ 1/2";

    public static final String STARTS_TABLE_KEY = "STARTS";
    public static final String VERDICHTER = "VERDICHTER";


    public InfoWaermepumpe() {
        super();
        setUrl(Properties.ADDRESS_ISG + "/?s=1,1");
    }

    public void getInformations(String content, Instant time) {
        addValueToDB(super.getInformation(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_TAG, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG), time, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG);
        addValueToDB(super.getInformation(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_SUMME, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME), time, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME);
        addValueToDB(super.getInformation(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_NHZ_HEIZEN_SUMME, ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME), time, ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME);
        addValueToDB(super.getInformation(content, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_TAG, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG), time, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG);
        addValueToDB(super.getInformation(content, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME), time, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME);
        addValueToDB(super.getInformation(content, LAUFZEIT_TABLE_KEY, VD_HEIZEN, ES_TYPE_IW_VD_HEIZEN), time, ES_TYPE_IW_VD_HEIZEN);
        addValueToDB(super.getInformation(content, LAUFZEIT_TABLE_KEY, VD_KUEHLEN, ES_TYPE_IW_VD_KUEHLEN), time, ES_TYPE_IW_VD_KUEHLEN);
        addValueToDB(super.getInformation(content, LAUFZEIT_TABLE_KEY, NHZ_1, ES_TYPE_IW_NHZ_1), time, ES_TYPE_IW_NHZ_1);
        addValueToDB(super.getInformation(content, LAUFZEIT_TABLE_KEY, NHZ_2, ES_TYPE_IW_NHZ_2), time, ES_TYPE_IW_NHZ_2);
        addValueToDB(super.getInformation(content, LAUFZEIT_TABLE_KEY, NHZ_1_DURCH_2, ES_TYPE_IW_NHZ_1_DURCH_2), time, ES_TYPE_IW_NHZ_1_DURCH_2);
        addValueToDB(super.getInformation(content, STARTS_TABLE_KEY, VERDICHTER, ES_TYPE_IW_VERDICHTER), time, ES_TYPE_IW_VERDICHTER);
    }
}
