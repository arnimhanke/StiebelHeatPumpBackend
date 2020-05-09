package de.hanke.arnim.heizung;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static de.hanke.arnim.common.Constant.*;
import static de.hanke.arnim.common.Constant.ES_TYPE_DA_RESTSTILLSTAND;
import static de.hanke.arnim.heizung.DiagnoseAnlage.*;

class DiagnoseAnlageTest {

    DiagnoseAnlage diagnoseAnlage = new DiagnoseAnlage();

    @Test
    void getInformations() throws IOException {
        String contentFromHTMLFile = ReadFromHTMLFile.getContentFromHTMLFile("STIEBEL_ELTRON_Reglersteuerung_DA.html");

        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, BETRIEBSSTATUS_TABLE_KEY, HEIZKREIS_1_PUMPE, ES_TYPE_DA_HEIZKREIS_1_PUMPE));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, BETRIEBSSTATUS_TABLE_KEY, HEIZEN, ES_TYPE_DA_HEIZEN));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, BETRIEBSSTATUS_TABLE_KEY, VERDICHTER, ES_TYPE_DA_VERDICHTER));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, STATUS_TABLE_KEY, HEIZKREISPUMPE, ES_TYPE_DA_HEIZKREISPUMPE));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, STATUS_TABLE_KEY, QUELLENPUMPE, ES_TYPE_DA_QUELLENPUMPE));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, STATUS_TABLE_KEY, PUFFERLADEPUMPE, ES_TYPE_DA_PUFFERLADEPUMPE));
        Assert.assertEquals("", diagnoseAnlage.getInformation(contentFromHTMLFile, STATUS_TABLE_KEY, VERDICHTERSCHUETZ, ES_TYPE_DA_VERDICHTERSCHUETZ));
        Assert.assertEquals("0 min", diagnoseAnlage.getInformation(contentFromHTMLFile, RESTSTILLSTAND_TABLE_KEY, RESTSTILLSTAND, ES_TYPE_DA_RESTSTILLSTAND));


    }
}