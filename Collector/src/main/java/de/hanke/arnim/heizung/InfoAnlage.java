package de.hanke.arnim.heizung;

import de.hanke.arnim.common.Utils;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/23/17.
 */
public class InfoAnlage extends AbstractInfo {

    public static final String HEIZUNG_TABLE_KEY = "HEIZUNG";
    public static final String RAUMTEMPERATUR_TABLE_KEY = "RAUMTEMPERATUR";
    public static final String KÜHLEN_TABLE_KEY = "KÜHLEN";
    public static final String QUELLE_TABLE_KEY = "QUELLE";
    private static final String AUSSENTEMPERATUR = "AUSSENTEMPERATUR";
    private static final String ISTTEMPERATUR_HK_1 = "ISTTEMPERATUR HK 1";
    private static final String SOLLTEMPERATUR_HK_1 = "SOLLTEMPERATUR HK 1";
    private static final String VORLAUFISTTEMPERATUR_WP = "VORLAUFISTTEMPERATUR WP";
    private static final String VORLAUFISTTEMPERATUR_NHZ = "VORLAUFISTTEMPERATUR NHZ";
    private static final String RÜCKLAUFISTTEMPERATUR = "RÜCKLAUFISTTEMPERATUR";
    private static final String PUFFERISTTEMPERATUR = "PUFFERISTTEMPERATUR";
    private static final String PUFFERSOLLTEMPERATUR = "PUFFERSOLLTEMPERATUR";
    private static final String HEIZUNGSDRUCK = "HEIZUNGSDRUCK";
    private static final String VOLUMENSTROM = "VOLUMENSTROM";
    private static final String ANLAGENFROST = "ANLAGENFROST";
    private static final String ISTTEMPERATUR_FEK = "ISTTEMPERATUR FEK";
    private static final String SOLLTEMPERATUR_FEK = "SOLLTEMPERATUR FEK";
    private static final String RAUMFEUCHTE = "RAUMFEUCHTE";
    private static final String TAUPUNKTTEMPERATUR = "TAUPUNKTTEMPERATUR";
    private static final String ISTTEMPERATUR_GEBLÄSE = "ISTTEMPERATUR GEBLÄSE";
    private static final String SOLLTEMPERATUR_GEBLÄSE = "SOLLTEMPERATUR GEBLÄSE";
    private static final String QUELLENTEMPERATUR = "QUELLENTEMPERATUR";
    private static final String QUELLENDRUCK = "QUELLENDRUCK";

    public InfoAnlage() {
        super();
        setUrl(utils.ADDRESS_ISG + "/?s=1,0");
    }

    public void getInformations(String content, long time) {
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, AUSSENTEMPERATUR, time, ES_TYPE_IA_AUSSENTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, ISTTEMPERATUR_HK_1, time, ES_TYPE_IA_ISTTEMPERATUR_HK_1);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, SOLLTEMPERATUR_HK_1, time, ES_TYPE_IA_SOLLTEMPERATUR_HK_1);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_WP, time, ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_NHZ, time, ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, RÜCKLAUFISTTEMPERATUR, time, ES_TYPE_IA_RÜCKLAUFISTTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, PUFFERISTTEMPERATUR, time, ES_TYPE_IA_PUFFERISTTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, PUFFERSOLLTEMPERATUR, time, ES_TYPE_IA_PUFFERSOLLTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, HEIZUNGSDRUCK, time, ES_TYPE_IA_HEIZUNGSDRUCK);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, VOLUMENSTROM, time, ES_TYPE_IA_VOLUMENSTROM);
        utils.putValueForKeyInElasticSearch(content, HEIZUNG_TABLE_KEY, ANLAGENFROST, time, ES_TYPE_IA_ANLAGENFROST);
        utils.putValueForKeyInElasticSearch(content, RAUMTEMPERATUR_TABLE_KEY, ISTTEMPERATUR_FEK, time, ES_TYPE_IA_ISTTEMPERATUR_FEK);
        utils.putValueForKeyInElasticSearch(content, RAUMTEMPERATUR_TABLE_KEY, SOLLTEMPERATUR_FEK, time, ES_TYPE_IA_SOLLTEMPERATUR_FEK);
        utils.putValueForKeyInElasticSearch(content, RAUMTEMPERATUR_TABLE_KEY, RAUMFEUCHTE, time, ES_TYPE_IA_RAUMFEUCHTE);
        utils.putValueForKeyInElasticSearch(content, RAUMTEMPERATUR_TABLE_KEY, TAUPUNKTTEMPERATUR, time, ES_TYPE_IA_TAUPUNKTTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, KÜHLEN_TABLE_KEY, ISTTEMPERATUR_GEBLÄSE, time, ES_TYPE_IA_ISTTEMPERATUR_GEBLÄSE);
        utils.putValueForKeyInElasticSearch(content, KÜHLEN_TABLE_KEY, SOLLTEMPERATUR_GEBLÄSE, time, ES_TYPE_IA_SOLLTEMPERATUR_GEBLÄSE);
        utils.putValueForKeyInElasticSearch(content, QUELLE_TABLE_KEY, QUELLENTEMPERATUR, time, ES_TYPE_IA_QUELLENTEMPERATUR);
        utils.putValueForKeyInElasticSearch(content, QUELLE_TABLE_KEY, QUELLENDRUCK, time, ES_TYPE_IA_QUELLENDRUCK);
    }
}
