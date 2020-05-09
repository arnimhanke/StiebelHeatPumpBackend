package de.hanke.arnim.heizung;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.hanke.arnim.common.Constant.*;
import static de.hanke.arnim.heizung.DiagnoseSystem.*;

class DiagnoseSystemTest {

    DiagnoseSystem  diagnoseSystem = new DiagnoseSystem();

    @Test
    void getInformations() throws IOException {
        String contentFromHTMLFile = ReadFromHTMLFile.getContentFromHTMLFile("STIEBEL_ELTRON_Reglersteuerung_DS.html");


        Assert.assertEquals("<img height=\"15\" src=\"./STIEBEL ELTRON Reglersteuerung_DS_files/ste-symbol_an-97b765.png", diagnoseSystem.getInformation(contentFromHTMLFile, REGLER_TABLE_KEY, WPM_3i, ES_TYPE_DS_WPM_3i));
        Assert.assertEquals("5 ", diagnoseSystem.getInformation(contentFromHTMLFile, REGLER_TABLE_KEY, SOFTWARE, ES_TYPE_DS_SOFTWARE));
        Assert.assertEquals("<img height=\"15\" src=\"./STIEBEL ELTRON Reglersteuerung_DS_files/ste-symbol_an-97b765.png", diagnoseSystem.getInformation(contentFromHTMLFile, ISG_TABLE_KEY, SG_READY, ES_TYPE_DS_SG_READY));
        Assert.assertEquals("2 ", diagnoseSystem.getInformation(contentFromHTMLFile, ISG_TABLE_KEY, HAUPTVERSIONSNUMMER, ES_TYPE_DS_HAUPTVERSIONSNUMMER));
        Assert.assertEquals("5 ", diagnoseSystem.getInformation(contentFromHTMLFile, ISG_TABLE_KEY, NEBENVERSIONSNUMMER, ES_TYPE_DS_NEBENVERSIONSNUMMER));
        Assert.assertEquals("6 ", diagnoseSystem.getInformation(contentFromHTMLFile, ISG_TABLE_KEY, REVISIONSNUMMER, ES_TYPE_DS_REVISIONSNUMMER));
        Assert.assertEquals("<img height=\"15\" src=\"./STIEBEL ELTRON Reglersteuerung_DS_files/ste-symbol_an-97b765.png", diagnoseSystem.getInformation(contentFromHTMLFile, CAN_STATUS_TABLE_KEY, OK, ES_TYPE_DS_OK));

    }
}