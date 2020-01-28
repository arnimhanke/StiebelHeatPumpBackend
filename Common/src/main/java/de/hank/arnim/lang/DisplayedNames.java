package de.hank.arnim.lang;

import java.util.LinkedHashMap;
import java.util.Map;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 1/4/18.
 */
public class DisplayedNames {

    public static final Map<String, String> MAP_ES_INDEX_TO_DISPLAYED_NAME = new LinkedHashMap<>();

    static {

        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_VERDICHTER.toLowerCase(), "VdS Tag");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_VERDICHTERSCHUETZ.toLowerCase(), "DA Verdichterschütz");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZKREIS_1_PUMPE.toLowerCase(), "DA Heizkreispumpe 1");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZEN.toLowerCase(), "DA Heizen");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZKREISPUMPE.toLowerCase(), "DA Heizkreispumpe");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_QUELLENPUMPE.toLowerCase(), "DA Quellenpumpe");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_PUFFERLADEPUMPE.toLowerCase(), "DA Pufferladepumpe");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DA_RESTSTILLSTAND.toLowerCase(), "DA Reststillstand");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_QUELLENTEMPERATUR.toLowerCase(), "IA Quellentemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_QUELLENDRUCK.toLowerCase(), "IA Quelleendruck");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_VOLUMENSTROM.toLowerCase(), "IA Volumenstrom");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_AUSSENTEMPERATUR.toLowerCase(), "IA Aussentemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_HEIZUNGSDRUCK.toLowerCase(), "IA Heizungsdruck");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_PUFFERSOLLTEMPERATUR.toLowerCase(), "IA Puffersolltemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_PUFFERISTTEMPERATUR.toLowerCase(), "IA Pufferisttemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP.toLowerCase(), "IA Vorlaufisttemperatur WP");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ.toLowerCase(), "IA Vorlaufisttemperatur NHZ");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_RUECKLAUFISTTEMPERATUR.toLowerCase(), "IA Rücklauftemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_HK_1.toLowerCase(), "IA Isttemperatur HK 1");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_HK_1.toLowerCase(), "IS Solltemperatur HK 1");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE.toLowerCase(), "IA Solltemperatur Gebläse");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE.toLowerCase(), "IA Isttemperatur Gebläse");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_FEK.toLowerCase(), "IS Isttemperatur FEK");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_FEK.toLowerCase(), "IA Solltemperatur FEK");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_RAUMFEUCHTE.toLowerCase(), "IA Raumfeutche");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_TAUPUNKTTEMPERATUR.toLowerCase(), "IA Taupunkttemperatur");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IA_ANLAGENFROST.toLowerCase(), "IA Anlagenfrost");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_VERDICHTER.toLowerCase(), "VdS Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG.toLowerCase(), "Hz Tag");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME.toLowerCase(), "Hz Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG.toLowerCase(), "El Tag");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME.toLowerCase(), "El Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_VD_HEIZEN.toLowerCase(), "Hz h Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_VD_KUEHLEN.toLowerCase(), "Küh h Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_1.toLowerCase(), "NHz h 1");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_2.toLowerCase(), "NHz h 2");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_1_DURCH_2.toLowerCase(), "NHz h 1+2");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME.toLowerCase(), "NHz Sum");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_WPM_3i.toLowerCase(), "DS WPM i3");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_SOFTWARE.toLowerCase(), "DS Software");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_SG_READY.toLowerCase(), "DS SG ready");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_HAUPTVERSIONSNUMMER.toLowerCase(), "DS Hauptversionsnummer");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_NEBENVERSIONSNUMMER.toLowerCase(), "DS Nebenversionsnummer");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_REVISIONSNUMMER.toLowerCase(), "DS Revisionsnummer");
        MAP_ES_INDEX_TO_DISPLAYED_NAME.put(ES_INDEX_PREFIX + ES_TYPE_DS_OK.toLowerCase(), "DS Ok");
    }
}
