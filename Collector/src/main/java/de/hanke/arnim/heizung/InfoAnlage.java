package de.hanke.arnim.heizung;

import de.hanke.arnim.Properties;

import java.time.Instant;

import static de.hanke.arnim.common.Constant.*;

/**
 * Created by arnim on 12/23/17.
 */
public class InfoAnlage extends AbstractInfo {

    public static final String HEIZUNG_TABLE_KEY = "HEIZUNG";
    public static final String RAUMTEMPERATUR_TABLE_KEY = "RAUMTEMPERATUR";
    public static final String KUEHLEN_TABLE_KEY = "K.*HLEN";
    public static final String QUELLE_TABLE_KEY = "QUELLE";
    public static final String AUSSENTEMPERATUR = "AUSSENTEMPERATUR";
    public static final String ISTTEMPERATUR_HK_1 = "ISTTEMPERATUR HK 1";
    public static final String SOLLTEMPERATUR_HK_1 = "SOLLTEMPERATUR HK 1";
    public static final String VORLAUFISTTEMPERATUR_WP = "VORLAUFISTTEMPERATUR WP";
    public static final String VORLAUFISTTEMPERATUR_NHZ = "VORLAUFISTTEMPERATUR NHZ";
    public static final String RUECKLAUFISTTEMPERATUR = "R.*CKLAUFISTTEMPERATUR";
    public static final String PUFFERISTTEMPERATUR = "PUFFERISTTEMPERATUR";
    public static final String PUFFERSOLLTEMPERATUR = "PUFFERSOLLTEMPERATUR";
    public static final String HEIZUNGSDRUCK = "HEIZUNGSDRUCK";
    public static final String VOLUMENSTROM = "VOLUMENSTROM";
    public static final String ANLAGENFROST = "ANLAGENFROST";
    public static final String ISTTEMPERATUR_FEK = "ISTTEMPERATUR FEK";
    public static final String SOLLTEMPERATUR_FEK = "SOLLTEMPERATUR FEK";
    public static final String RAUMFEUCHTE = "RAUMFEUCHTE";
    public static final String TAUPUNKTTEMPERATUR = "TAUPUNKTTEMPERATUR";
    public static final String IA_ISTTEMPERATUR_FLAECHE = "ISTTEMPERATUR FL.*CHE";
    public static final String IA_SOLLTEMPERATUR_FLAECHE = "SOLLTEMPERATUR FL.*CHE";
    public static final String ISTTEMPERATUR_GEBLAESE = "ISTTEMPERATUR GEBL.*SE";
    public static final String SOLLTEMPERATUR_GEBLAESE = "SOLLTEMPERATUR GEBL.*SE";
    public static final String QUELLENTEMPERATUR = "QUELLENTEMPERATUR";
    public static final String QUELLENDRUCK = "QUELLENDRUCK";

    public InfoAnlage() {
        super();
        setUrl(Properties.ADDRESS_ISG + "/?s=1,0");
    }

    public void getInformations(String content, Instant time) {
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, AUSSENTEMPERATUR, ES_TYPE_IA_AUSSENTEMPERATUR), time, ES_TYPE_IA_AUSSENTEMPERATUR);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, ISTTEMPERATUR_HK_1, ES_TYPE_IA_ISTTEMPERATUR_HK_1), time, ES_TYPE_IA_ISTTEMPERATUR_HK_1);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, SOLLTEMPERATUR_HK_1, ES_TYPE_IA_SOLLTEMPERATUR_HK_1), time, ES_TYPE_IA_SOLLTEMPERATUR_HK_1);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_WP, ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP), time, ES_TYPE_IA_VORLAUFISTTEMPERATUR_WP);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, VORLAUFISTTEMPERATUR_NHZ, ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ), time, ES_TYPE_IA_VORLAUFISTTEMPERATUR_NHZ);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, RUECKLAUFISTTEMPERATUR, ES_TYPE_IA_RUECKLAUFISTTEMPERATUR), time, ES_TYPE_IA_RUECKLAUFISTTEMPERATUR);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, PUFFERISTTEMPERATUR, ES_TYPE_IA_PUFFERISTTEMPERATUR), time, ES_TYPE_IA_PUFFERISTTEMPERATUR);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, PUFFERSOLLTEMPERATUR, ES_TYPE_IA_PUFFERSOLLTEMPERATUR), time, ES_TYPE_IA_PUFFERSOLLTEMPERATUR);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, HEIZUNGSDRUCK, ES_TYPE_IA_HEIZUNGSDRUCK), time, ES_TYPE_IA_HEIZUNGSDRUCK);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, VOLUMENSTROM, ES_TYPE_IA_VOLUMENSTROM), time, ES_TYPE_IA_VOLUMENSTROM);
        addValueToDB(super.getInformation(content, HEIZUNG_TABLE_KEY, ANLAGENFROST, ES_TYPE_IA_ANLAGENFROST), time, ES_TYPE_IA_ANLAGENFROST);
        addValueToDB(super.getInformation(content, RAUMTEMPERATUR_TABLE_KEY, ISTTEMPERATUR_FEK, ES_TYPE_IA_ISTTEMPERATUR_FEK), time, ES_TYPE_IA_ISTTEMPERATUR_FEK);
        addValueToDB(super.getInformation(content, RAUMTEMPERATUR_TABLE_KEY, SOLLTEMPERATUR_FEK, ES_TYPE_IA_SOLLTEMPERATUR_FEK), time, ES_TYPE_IA_SOLLTEMPERATUR_FEK);
        addValueToDB(super.getInformation(content, RAUMTEMPERATUR_TABLE_KEY, RAUMFEUCHTE, ES_TYPE_IA_RAUMFEUCHTE), time, ES_TYPE_IA_RAUMFEUCHTE);
        addValueToDB(super.getInformation(content, RAUMTEMPERATUR_TABLE_KEY, TAUPUNKTTEMPERATUR, ES_TYPE_IA_TAUPUNKTTEMPERATUR), time, ES_TYPE_IA_TAUPUNKTTEMPERATUR);
        addValueToDB(super.getInformation(content, KUEHLEN_TABLE_KEY, IA_ISTTEMPERATUR_FLAECHE, ES_TYPE_IA_ISTTEMPERATUR_FLAECHE), time, ES_TYPE_IA_ISTTEMPERATUR_FLAECHE);
        addValueToDB(super.getInformation(content, KUEHLEN_TABLE_KEY, IA_SOLLTEMPERATUR_FLAECHE, ES_TYPE_IA_SOLLTEMPERATUR_FLAECHE), time, ES_TYPE_IA_SOLLTEMPERATUR_FLAECHE);

        addValueToDB(super.getInformation(content, KUEHLEN_TABLE_KEY, ISTTEMPERATUR_GEBLAESE, ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE), time, ES_TYPE_IA_ISTTEMPERATUR_GEBLAESE);
        addValueToDB(super.getInformation(content, KUEHLEN_TABLE_KEY, SOLLTEMPERATUR_GEBLAESE, ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE), time, ES_TYPE_IA_SOLLTEMPERATUR_GEBLAESE);

        addValueToDB(super.getInformation(content, QUELLE_TABLE_KEY, QUELLENTEMPERATUR, ES_TYPE_IA_QUELLENTEMPERATUR), time, ES_TYPE_IA_QUELLENTEMPERATUR);
        addValueToDB(super.getInformation(content, QUELLE_TABLE_KEY, QUELLENDRUCK, ES_TYPE_IA_QUELLENDRUCK), time, ES_TYPE_IA_QUELLENDRUCK);
    }
}
