package de.hank.arnim.heizung;

import de.hank.arnim.common.Utils;

import static de.hank.arnim.common.Constant.*;
import static de.hank.arnim.common.Utils.putValueForKeyInElasticSearch;

/**
 * Created by arnim on 12/23/17.
 */
public class InfoWaermepumpe extends AbstractInfo {

    public static final String WAERMEMENGE_TABLE_KEY = "WÄRMEMENGE";
    public static final String WAERMEMENGE_VD_HEIZEN_TAG = "VD HEIZEN TAG";
    public static final String WAERMEMENGE_VD_HEIZEN_SUMME = "VD HEIZEN SUMME";
    public static final String WAERMEMENGE_NHZ_HEIZEN_SUMME = "NHZ HEIZEN SUMME";

    public static final String LEISTUNGSAUFNAHME_TABLE_KEY = "LEISTUNGSAUFNAHME";
    public static final String LEISTUNGSAUFNAHME_VD_HEIZEN_TAG = "VD HEIZEN TAG";
    public static final String LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME = "VD HEIZEN SUMME";

    public static final String LAUFZEIT_TABLE_KEY = "LAUFZEIT";
    public static final String VD_HEIZEN = "VD HEIZEN";
    public static final String VD_KÜHLEN = "VD KÜHLEN";
    public static final String NHZ_1 = "NHZ 1";
    public static final String NHZ_2 = "NHZ 2";
    public static final String NHZ_1_DURCH_2 = "NHZ 1/2";

    public static final String STARTS_TABLE_KEY = "STARTS";
    public static final String VERDICHTER = "VERDICHTER";

    public InfoWaermepumpe() {
        super(Utils.ADDRESS_ISG + "/?s=1,1");
    }

    public void getInformations(String content, long time) {
        putValueForKeyInElasticSearch(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_TAG, time, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG);
        putValueForKeyInElasticSearch(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_SUMME, time, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME);
        putValueForKeyInElasticSearch(content, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_NHZ_HEIZEN_SUMME, time, ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME);
        putValueForKeyInElasticSearch(content, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_TAG, time, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG);
        putValueForKeyInElasticSearch(content, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME, time, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME);
        putValueForKeyInElasticSearch(content, LAUFZEIT_TABLE_KEY, VD_HEIZEN, time, ES_TYPE_IW_VD_HEIZEN);
        putValueForKeyInElasticSearch(content, LAUFZEIT_TABLE_KEY, VD_KÜHLEN, time, ES_TYPE_IW_VD_KÜHLEN);
        putValueForKeyInElasticSearch(content, LAUFZEIT_TABLE_KEY, NHZ_1, time, ES_TYPE_IW_NHZ_1);
        putValueForKeyInElasticSearch(content, LAUFZEIT_TABLE_KEY, NHZ_2, time, ES_TYPE_IW_NHZ_2);
        putValueForKeyInElasticSearch(content, LAUFZEIT_TABLE_KEY, NHZ_1_DURCH_2, time, ES_TYPE_IW_NHZ_1_DURCH_2);
        putValueForKeyInElasticSearch(content, STARTS_TABLE_KEY, VERDICHTER, time, ES_TYPE_IW_VERDICHTER);
    }
}
