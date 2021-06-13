package de.hanke.arnim.heizung;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.hanke.arnim.common.Constant.*;
import static de.hanke.arnim.heizung.InfoAnlage.*;

class InfoAnlageTest {

    InfoAnlage infoAnlage = new InfoAnlage();

    @Test
    void getInformations() throws IOException {
        String contentFromHTMLFile = ReadFromHTMLFile.getContentFromHTMLFile("STIEBEL_ELTRON_Reglersteuerung_IA.html");

        Assert.assertEquals("-24,7 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, AUSSENTEMPERATUR, ES_TYPE_IA_AUSSENTEMPERATUR));
        Assert.assertEquals("20,9 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, ISTTEMPERATUR_HK_1, ES_TYPE_IA_ISTTEMPERATUR_HK_1));
        Assert.assertEquals("17,7 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, SOLLTEMPERATUR_HK_1, ES_TYPE_IA_SOLLTEMPERATUR_HK_1));
        Assert.assertEquals("19,9 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_WP, ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP));
        Assert.assertEquals("20,1 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_NHZ, ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ));
        Assert.assertEquals("19,8 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, RUECKLAUFISTTEMPERATUR, ES_TYPE_IA_RUECKLAUFISTTEMPERATUR));
        Assert.assertEquals("20,9 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, PUFFERISTTEMPERATUR, ES_TYPE_IA_PUFFERISTTEMPERATUR));
        Assert.assertEquals("17,8 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, PUFFERSOLLTEMPERATUR, ES_TYPE_IA_PUFFERSOLLTEMPERATUR));
        Assert.assertEquals("1,83 bar", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, HEIZUNGSDRUCK, ES_TYPE_IA_HEIZUNGSDRUCK));
        Assert.assertEquals("0,00 l/min", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, VOLUMENSTROM, ES_TYPE_IA_VOLUMENSTROM));
        Assert.assertEquals("4,0 °C", infoAnlage.getInformation(contentFromHTMLFile, HEIZUNG_TABLE_KEY, ANLAGENFROST, ES_TYPE_IA_ANLAGENFROST));
        Assert.assertEquals("24,1 °C", infoAnlage.getInformation(contentFromHTMLFile, RAUMTEMPERATUR_TABLE_KEY, ISTTEMPERATUR_FEK, ES_TYPE_IA_ISTTEMPERATUR_FEK));
        Assert.assertEquals("20,4 °C", infoAnlage.getInformation(contentFromHTMLFile, RAUMTEMPERATUR_TABLE_KEY, SOLLTEMPERATUR_FEK, ES_TYPE_IA_SOLLTEMPERATUR_FEK));
        Assert.assertEquals("41,6 %", infoAnlage.getInformation(contentFromHTMLFile, RAUMTEMPERATUR_TABLE_KEY, RAUMFEUCHTE, ES_TYPE_IA_RAUMFEUCHTE));
        Assert.assertEquals("10,2 °C", infoAnlage.getInformation(contentFromHTMLFile, RAUMTEMPERATUR_TABLE_KEY, TAUPUNKTTEMPERATUR, ES_TYPE_IA_TAUPUNKTTEMPERATUR));
//        Assert.assertEquals("", infoAnlage.getInformation(contentFromHTMLFile, KÜHLEN_TABLE_KEY, ISTTEMPERATUR_GEBLÄSE, ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE));
//        Assert.assertEquals("", infoAnlage.getInformation(contentFromHTMLFile, KÜHLEN_TABLE_KEY, SOLLTEMPERATUR_GEBLÄSE, ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE));
        Assert.assertEquals("17,5 °C", infoAnlage.getInformation(contentFromHTMLFile, QUELLE_TABLE_KEY, QUELLENTEMPERATUR, ES_TYPE_IA_QUELLENTEMPERATUR));
        Assert.assertEquals("0,74 bar", infoAnlage.getInformation(contentFromHTMLFile, QUELLE_TABLE_KEY, QUELLENDRUCK, ES_TYPE_IA_QUELLENDRUCK));

    }
}