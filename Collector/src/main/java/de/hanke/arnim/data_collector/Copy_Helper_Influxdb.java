package de.hanke.arnim.data_collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.TSTool.PeriodicTimeseries;
import de.hanke.arnim.common.InfluxDBUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by arnim on 2/13/18.
 */
public class Copy_Helper_Influxdb {

    public static final String ADRESS_INFLUXDB_AACHEN = "http://localhost:8086";
    public static final String ADRESS_INFLUXDB_BRAUWEILER = "http://192.168.180.201:8086";
    public static final String USERNAME_INFLUXDB_AACHEN = "heatpump";
    public static final String USERNAME_INFLUXDB_BRAUWEILER = "heatpump";
    public static final String PASSWORD_INFLUXDB_AACHEN = "<PW>";
    public static final String PASSWORD_INFLUXDB_BRAUWEILER = "<PW>";
    public static final String DATABASENAME_INFLUXDB_AACHEN = "StiebelEltronHeatPumpRawDatas";
    public static final String DATABASENAME_INFLUXDB_BRAUWEILER = "StiebelEltronHeatPumpRawDatas";
    public static ObjectMapper mapper;
    public static List<String> indicies = new ArrayList<>();

    static {
        mapper = new ObjectMapper();
        // Following indicies are copyied
        indicies.add("da_heizkreispumpe");
        indicies.add("iw_verdichter");
        indicies.add("da_pufferladepumpe");
        indicies.add("iw_vd_heizen");
        indicies.add("iw_waermemenge_nhz_heizen_summe");
        indicies.add("ia_quellentemperatur");
        indicies.add("iw_vd_kuehlen");
        indicies.add("iw_nhz_2");
        indicies.add("ia_raumfeuchte");
        indicies.add("ia_ruecklaufisttemperatur");
        indicies.add("ia_anlagenfrost");
        indicies.add("ia_vorlaufisttemperatur_wp");
        indicies.add("ia_quellendruck");
        indicies.add("da_reststillstand");
        indicies.add("ia_vorlaufisttemperatur_nhz");
        indicies.add("ds_nebenversionsnummer");
        indicies.add("ia_isttemperatur_hk_1");


        indicies.add("ds_ok");
        indicies.add("ia_volumenstrom");
        indicies.add("da_verdichterschuetz");
        indicies.add("da_verdichter");
        indicies.add("ia_isttemperatur_geblaese");
        indicies.add("iw_leistungsaufnahme_vd_heizen_tag");
        indicies.add("da_quellenpumpe");
        indicies.add("ia_isttemperatur_fek");
        indicies.add("da_heizen");
        indicies.add("ds_wpm_3i");
        indicies.add("ds_software");
        indicies.add("ds_revisionsnummer");
        indicies.add("iw_nhz_1_durch_2");
        indicies.add("ia_solltemperatur_fek");
        indicies.add("iw_waermemenge_vd_heizen_tag");
        indicies.add("ds_hauptversionsnummer");
        indicies.add("ia_solltemperatur_geblaese");
        indicies.add("ia_puffersolltemperatur");
        indicies.add("ds_sg_ready");
        indicies.add("iw_leistungsaufnahme_vd_heizen_summe");
        indicies.add("iw_waermemenge_vd_heizen_summe");
        indicies.add("iw_nhz_1");
        indicies.add("da_heizkreis_1_pumpe");
        indicies.add("ia_pufferisttemperatur");
        indicies.add("ia_taupunkttemperatur");
        indicies.add("ia_aussentemperatur");
        indicies.add("ia_solltemperatur_hk_1");
        indicies.add("ia_heizungsdruck");

        indicies.add("ia_solltemperatur_flaeche");
        indicies.add("ia_isttemperatur_flaeche");
    }

    /**
     * method for copying values from Database A to B
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        GregorianCalendar from = new GregorianCalendar(2021, Calendar.FEBRUARY, 1, 0, 0);
        GregorianCalendar to = new GregorianCalendar(2021, Calendar.MAY, 1, 0, 0);


        GregorianCalendar current = from;
        while (current.before(to)) {
            for (String id : indicies) {
                InfluxDBUtils influxDBAachen = new InfluxDBUtils(ADRESS_INFLUXDB_AACHEN, USERNAME_INFLUXDB_AACHEN, PASSWORD_INFLUXDB_AACHEN, DATABASENAME_INFLUXDB_AACHEN);
                InfluxDBUtils influxDBBrauweiler = new InfluxDBUtils(ADRESS_INFLUXDB_BRAUWEILER, USERNAME_INFLUXDB_BRAUWEILER, PASSWORD_INFLUXDB_BRAUWEILER, DATABASENAME_INFLUXDB_BRAUWEILER);


                // Kopie fuer das aktuelle bis
                GregorianCalendar currentUntil = GregorianCalendar.from(current.toZonedDateTime());
                currentUntil.add(Calendar.MONTH, 1);
                System.out.println("Laden der Daten vom Remote-Client fuer Zeitreihe " + id + " das Intervall " + current.toInstant().toString() + " - " + currentUntil.toInstant().toString());

                List<PeriodicTimeseries> timeSeries = influxDBAachen.getTimeSeries(id, current.toInstant(), currentUntil.toInstant());
                if (timeSeries.size() == 1) {
                    influxDBBrauweiler.insertTimeSeries(timeSeries.get(0));
                } else if (timeSeries.size() > 1) {
                    System.out.println("More then one timeseries found for " + id);
                } else {
                    System.out.println("None timeseries found for " + id);
                }
                influxDBAachen.close();
                influxDBBrauweiler.close();
                Thread.sleep(100);
            }
            // Ein Jahr drauf rechnen um das naechste Jahr zu laden
            current.add(Calendar.MONTH, 1);
        }
    }
}

