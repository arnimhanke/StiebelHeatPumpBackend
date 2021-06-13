package de.hanke.arnim.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnim on 12/30/17.
 */
public class Constant {

    public static final String ES_INDEX_PREFIX = "heizungssuite_";

    // Information Waermepumpe
    public static final String ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG = "IW_WAERMEMENGE_VD_HEIZEN_TAG";
    public static final String ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME = "IW_WAERMEMENGE_VD_HEIZEN_SUMME";
    public static final String ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME = "IW_WAERMEMENGE_NHZ_HEIZEN_SUMME";
    public static final String ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG = "IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG";
    public static final String ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME = "IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME";
    public static final String ES_TYPE_IW_VD_HEIZEN = "IW_VD_HEIZEN";
    public static final String ES_TYPE_IW_VD_KUEHLEN = "IW_VD_KÜHLEN";
    public static final String ES_TYPE_IW_NHZ_1 = "IW_NHZ_1";
    public static final String ES_TYPE_IW_NHZ_2 = "IW_NHZ_2";
    public static final String ES_TYPE_IW_NHZ_1_DURCH_2 = "IW_NHZ_1_DURCH_2";
    public static final String ES_TYPE_IW_VERDICHTER = "IW_VERDICHTER";


    // Information Anlage
    public static final String ES_TYPE_IA_AUSSENTEMPERATUR = "IA_AUSSENTEMPERATUR";
    public static final String ES_TYPE_IA_ISTTEMPERATUR_HK_1 = "IA_ISTTEMPERATUR_HK_1";
    public static final String ES_TYPE_IA_SOLLTEMPERATUR_HK_1 = "IA_SOLLTEMPERATUR_HK_1";
    public static final String ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP = "IA_VORLAUFISTTEMPERATUR_WP";
    public static final String ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ = "IA_VORLAUFISTTEMPERATUR_NHZ";
    public static final String ES_TYPE_IA_RUECKLAUFISTTEMPERATUR = "IA_RÜCKLAUFISTTEMPERATUR";
    public static final String ES_TYPE_IA_PUFFERISTTEMPERATUR = "IA_PUFFERISTTEMPERATUR";
    public static final String ES_TYPE_IA_PUFFERSOLLTEMPERATUR = "IA_PUFFERSOLLTEMPERATUR";
    public static final String ES_TYPE_IA_HEIZUNGSDRUCK = "IA_HEIZUNGSDRUCK";
    public static final String ES_TYPE_IA_VOLUMENSTROM = "IA_VOLUMENSTROM";
    public static final String ES_TYPE_IA_ANLAGENFROST = "IA_ANLAGENFROST";
    public static final String ES_TYPE_IA_ISTTEMPERATUR_FEK = "IA_ISTTEMPERATUR_FEK";
    public static final String ES_TYPE_IA_SOLLTEMPERATUR_FEK = "IA_SOLLTEMPERATUR_FEK";
    public static final String ES_TYPE_IA_RAUMFEUCHTE = "IA_RAUMFEUCHTE";
    public static final String ES_TYPE_IA_TAUPUNKTTEMPERATUR = "IA_TAUPUNKTTEMPERATUR";
    public static final String ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE = "IA_SOLLTEMPERATUR_GEBLÄSE";
    public static final String ES_TYPE_IA_QUELLENTEMPERATUR = "IA_QUELLENTEMPERATUR";
    public static final String ES_TYPE_IA_QUELLENDRUCK = "IA_QUELLENDRUCK";
    public static final String ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE = "IA_ISTTEMPERATUR_GEBLÄSE";
    public static final String ES_TYPE_IA_ISTTEMPERATUR_FLAECHE = "IA_ISTTEMPERATUR_FLÄCHE";
    public static final String ES_TYPE_IA_SOLLTEMPERATUR_FLAECHE = "IA_SOLLTEMPERATUR_FLÄCHE";


    // Diagnose Anlage
    public static final String ES_TYPE_DA_HEIZKREIS_1_PUMPE = "DA_HEIZKREIS_1_PUMPE";
    public static final String ES_TYPE_DA_HEIZEN = "DA_HEIZEN";
    public static final String ES_TYPE_DA_VERDICHTER = "DA_VERDICHTER";
    public static final String ES_TYPE_DA_HEIZKREISPUMPE = "DA_HEIZKREISPUMPE";
    public static final String ES_TYPE_DA_QUELLENPUMPE = "DA_QUELLENPUMPE";
    public static final String ES_TYPE_DA_PUFFERLADEPUMPE = "DA_PUFFERLADEPUMPE";
    public static final String ES_TYPE_DA_VERDICHTERSCHUETZ = "DA_VERDICHTERSCHUETZ";
    public static final String ES_TYPE_DA_RESTSTILLSTAND = "DA_RESTSTILLSTAND";

    // Diagnose System
    public static final String ES_TYPE_DS_WPM_3i = "DS_WPM_3i";
    public static final String ES_TYPE_DS_SOFTWARE = "DS_SOFTWARE";
    public static final String ES_TYPE_DS_SG_READY = "DS_SG_READY";
    public static final String ES_TYPE_DS_HAUPTVERSIONSNUMMER = "DS_HAUPTVERSIONSNUMMER";
    public static final String ES_TYPE_DS_NEBENVERSIONSNUMMER = "DS_NEBENVERSIONSNUMMER";
    public static final String ES_TYPE_DS_REVISIONSNUMMER = "DS_REVISIONSNUMMER";
    public static final String ES_TYPE_DS_OK = "DS_OK";

    public final static String[] ALL_INDEXES = new String[]{ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG,
            ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME,
            ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME,
            ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG,
            ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME,
            ES_TYPE_IW_VD_HEIZEN,
            ES_TYPE_IW_VD_KUEHLEN,
            ES_TYPE_IW_NHZ_1,
            ES_TYPE_IW_NHZ_2,
            ES_TYPE_IW_NHZ_1_DURCH_2,
            ES_TYPE_IW_VERDICHTER,
            ES_TYPE_IA_AUSSENTEMPERATUR,
            ES_TYPE_IA_ISTTEMPERATUR_HK_1,
            ES_TYPE_IA_SOLLTEMPERATUR_HK_1,
            ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP,
            ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ,
            ES_TYPE_IA_RUECKLAUFISTTEMPERATUR,
            ES_TYPE_IA_PUFFERISTTEMPERATUR,
            ES_TYPE_IA_PUFFERSOLLTEMPERATUR,
            ES_TYPE_IA_HEIZUNGSDRUCK,
            ES_TYPE_IA_VOLUMENSTROM,
            ES_TYPE_IA_ANLAGENFROST,
            ES_TYPE_IA_ISTTEMPERATUR_FEK,
            ES_TYPE_IA_SOLLTEMPERATUR_FEK,
            ES_TYPE_IA_RAUMFEUCHTE,
            ES_TYPE_IA_TAUPUNKTTEMPERATUR,
            ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE,
            ES_TYPE_IA_QUELLENTEMPERATUR,
            ES_TYPE_IA_QUELLENDRUCK,
            ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE,
            ES_TYPE_IA_ISTTEMPERATUR_FLAECHE,
            ES_TYPE_IA_SOLLTEMPERATUR_FLAECHE,
            ES_TYPE_DA_HEIZKREIS_1_PUMPE,
            ES_TYPE_DA_HEIZEN,
            ES_TYPE_DA_VERDICHTER,
            ES_TYPE_DA_HEIZKREISPUMPE,
            ES_TYPE_DA_QUELLENPUMPE,
            ES_TYPE_DA_PUFFERLADEPUMPE,
            ES_TYPE_DA_VERDICHTERSCHUETZ,
            ES_TYPE_DA_RESTSTILLSTAND,
            ES_TYPE_DS_WPM_3i,
            ES_TYPE_DS_SOFTWARE,
            ES_TYPE_DS_SG_READY,
            ES_TYPE_DS_HAUPTVERSIONSNUMMER,
            ES_TYPE_DS_NEBENVERSIONSNUMMER,
            ES_TYPE_DS_REVISIONSNUMMER,
            ES_TYPE_DS_OK};

}
