package de.hanke.arnim.heizung;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.hanke.arnim.common.Constant.*;
import static de.hanke.arnim.heizung.InfoWaermepumpe.*;

class InfoWaermepumpeTest {

    InfoWaermepumpe infoWaermepumpe = new InfoWaermepumpe();

    @Test
    void getInformations() throws IOException {

        String contentFromHTMLFile = ReadFromHTMLFile.getContentFromHTMLFile("STIEBEL_ELTRON_Reglersteuerung_IW.html");


        Assert.assertEquals("4,825 kWh", infoWaermepumpe.getInformation(contentFromHTMLFile, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_TAG, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_TAG));
        Assert.assertEquals("37,159 MWh", infoWaermepumpe.getInformation(contentFromHTMLFile, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_VD_HEIZEN_SUMME, ES_TYPE_IW_WAERMEMENGE_VD_HEIZEN_SUMME));
        Assert.assertEquals("0,000 MWh", infoWaermepumpe.getInformation(contentFromHTMLFile, WAERMEMENGE_TABLE_KEY, WAERMEMENGE_NHZ_HEIZEN_SUMME, ES_TYPE_IW_WAERMEMENGE_NHZ_HEIZEN_SUMME));
        Assert.assertEquals("2,619 kWh", infoWaermepumpe.getInformation(contentFromHTMLFile, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_TAG, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_TAG));
        Assert.assertEquals("8,032 MWh", infoWaermepumpe.getInformation(contentFromHTMLFile, LEISTUNGSAUFNAHME_TABLE_KEY, LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME, ES_TYPE_IW_LEISTUNGSAUFNAHME_VD_HEIZEN_SUMME));
        Assert.assertEquals("5085 h", infoWaermepumpe.getInformation(contentFromHTMLFile, LAUFZEIT_TABLE_KEY, VD_HEIZEN, ES_TYPE_IW_VD_HEIZEN));
        Assert.assertEquals("930 h", infoWaermepumpe.getInformation(contentFromHTMLFile, LAUFZEIT_TABLE_KEY, VD_KUEHLEN, ES_TYPE_IW_VD_KUEHLEN));
        Assert.assertEquals("9 h", infoWaermepumpe.getInformation(contentFromHTMLFile, LAUFZEIT_TABLE_KEY, NHZ_1, ES_TYPE_IW_NHZ_1));
        Assert.assertEquals("7 h", infoWaermepumpe.getInformation(contentFromHTMLFile, LAUFZEIT_TABLE_KEY, NHZ_2, ES_TYPE_IW_NHZ_2));
        Assert.assertEquals("33 h", infoWaermepumpe.getInformation(contentFromHTMLFile, LAUFZEIT_TABLE_KEY, NHZ_1_DURCH_2, ES_TYPE_IW_NHZ_1_DURCH_2));
        Assert.assertEquals("10017 ", infoWaermepumpe.getInformation(contentFromHTMLFile, STARTS_TABLE_KEY, VERDICHTER, ES_TYPE_IW_VERDICHTER));

    }
}