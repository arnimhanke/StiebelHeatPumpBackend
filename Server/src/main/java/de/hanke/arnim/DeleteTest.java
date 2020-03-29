package de.hanke.arnim;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.*;
import de.hanke.arnim.TimeSeriesToolSet.PeriodicTimeseries;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

public class DeleteTest {

    public static void main(String[] args) throws IOException {
        // Kontrolle ob das kopieren erfolgreich war

        /*ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils();
        Instant start = Instant.parse("2010-12-31T23:00:00.00Z");
        Instant end = Instant.now().plusSeconds(60 * 60 * 24);
        String key = "heizungssuite_ds_ok";
        try {
            elasticSearchUtils.deleteDataInInterval(key, start, end);
        } catch (Exception e) {
            System.out.println("Fehler beim Löschen der Daten von " + key + " fuer das Intervall " + start.toString() + " bis " + end);
            e.printStackTrace();
        }*/

        String ids = "[\"ia_aussentemperatur\",\"ia_quellentemperatur\",\"ia_solltemperatur_fek\",\"ia_isttemperatur_fek\",\"ia_raumfeuchte\",\"ia_taupunkttemperatur\",\"ia_puffersolltemperatur\",\"ia_pufferisttemperatur\",\"ia_solltemperatur_hk_1\",\"ia_isttemperatur_hk_1\",\"ia_rücklaufisttemperatur\",\"ia_vorlaufisttemperatur_wp\",\"ia_vorlaufisttemperatur_nhz\",\"ia_anlagenfrost\",\"ia_heizungsdruck\",\"ia_solltemperatur_gebläse\",\"ia_isttemperatur_gebläse\",\"ia_quellendruck\",\"ia_volumenstrom\",\"da_reststillstand\",\"iw_waermemenge_vd_heizen_tag\",\"iw_waermemenge_vd_heizen_summe\",\"iw_leistungsaufnahme_vd_heizen_tag\",\"iw_leistungsaufnahme_vd_heizen_summe\",\"iw_vd_heizen\",\"iw_vd_kühlen\",\"iw_nhz_1\",\"iw_nhz_2\",\"iw_nhz_1_durch_2\",\"iw_waermemenge_nhz_heizen_summe\",\"iw_verdichter\",\"da_verdichter\",\"da_verdichterschuetz\",\"da_heizkreis_1_pumpe\",\"da_heizen\",\"da_heizkreispumpe\",\"da_quellenpumpe\",\"da_pufferladepumpe\"]";
        final String url = "http://localhost:4567/timeseries?ids=" + ids + "&from=2018-02-01T23:00:00.00Z&to=2018-03-01T23:00:00.00Z&raster=PT15S&dbName=StiebelEltronHeatPumpCorrectedData";

        RestTemplate template = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
        long start = System.currentTimeMillis();
        String forObject = template.getForObject(url, String.class);
        System.out.println((System.currentTimeMillis() - start) + " in ms");

        try {
//            PeriodicTimeseries[] deserialize = JsonIterator.deserialize(forObject, PeriodicTimeseries[].class);// will use reflection

            PeriodicTimeseries[] periodicTimeseries = objectMapper.readValue(forObject, PeriodicTimeseries[].class);

//            HashMap<String, PeriodicTimeseries> periodicTimeseries = objectMapper.readValue(forObject, new TypeReference<HashMap<String, PeriodicTimeseries>>() {
//            });


            //System.out.println(periodicTimeseries);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}