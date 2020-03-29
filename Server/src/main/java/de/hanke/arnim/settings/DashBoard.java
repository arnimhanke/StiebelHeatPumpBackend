package de.hanke.arnim.settings;

import java.util.ArrayList;
import java.util.List;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/30/17.
 */
public class DashBoard {

    public static final List<String> INDICIES_FOR_DASHBOARD = new ArrayList<>();

    static {

        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_AUSSENTEMPERATUR.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_QUELLENTEMPERATUR.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_FEK.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_FEK.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_RAUMFEUCHTE.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_TAUPUNKTTEMPERATUR.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_PUFFERSOLLTEMPERATUR.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_PUFFERISTTEMPERATUR.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_HK_1.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_HK_1.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_RUECKLAUFISTTEMPERATUR.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_ANLAGENFROST.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_HEIZUNGSDRUCK.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE.toLowerCase());

        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_QUELLENDRUCK.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IA_VOLUMENSTROM.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_RESTSTILLSTAND.toLowerCase());

        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME.toLowerCase());

        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME.toLowerCase());


        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_VD_HEIZEN.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_VD_KUEHLEN.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_1.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_2.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_NHZ_1_DURCH_2.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_IW_VERDICHTER.toLowerCase()); // VdS Sum
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_VERDICHTER.toLowerCase());

        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_VERDICHTERSCHUETZ.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZKREIS_1_PUMPE.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZEN.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_HEIZKREISPUMPE.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_QUELLENPUMPE.toLowerCase());
        INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DA_PUFFERLADEPUMPE.toLowerCase());


        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_WPM_3i.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_SOFTWARE.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_SG_READY.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_HAUPTVERSIONSNUMMER.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_NEBENVERSIONSNUMMER.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_REVISIONSNUMMER.toLowerCase());
        // INDICIES_FOR_DASHBOARD.add(ES_INDEX_PREFIX + ES_TYPE_DS_OK.toLowerCase());
    }

    public static void main(String[] args) {
        for (String s : INDICIES_FOR_DASHBOARD) {
            System.out.print("\"" + s.replace(ES_INDEX_PREFIX, "") + "\",");
        }
    }
}
