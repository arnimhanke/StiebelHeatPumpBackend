package de.hanke.arnim;

import de.hanke.arnim.common.ElasticSearchUtils;

import java.time.Instant;

public class DeleteTest {

    public static void main(String[] args) {
        // Kontrolle ob das kopieren erfolgreich war

        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();
        Instant start = Instant.parse("2010-12-31T23:00:00.00Z");
        Instant end = Instant.now().plusSeconds(60 * 60 * 24);
        String key = "heizungssuite_ds_ok";
        try {
            elasticSearchUtils.deleteDataInInterval(key, start, end);
        } catch (Exception e) {
            System.out.println("Fehler beim Löschen der Daten von " + key + " fuer das Intervall " + start.toString() + " bis " + end);
            e.printStackTrace();
        }
    }

}
