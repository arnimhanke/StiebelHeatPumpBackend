package de.hanke.arnim;

import de.hanke.arnim.heizung.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

public class CollectorStarter {

    public static List<AbstractInfo> informations;

    static {
        informations = new ArrayList<>();
        informations.add(new InfoWaermepumpe());
        informations.add(new InfoAnlage());
        informations.add(new DiagnoseAnlage());
        informations.add(new DiagnoseSystem());
    }

    public static void main(String[] args) {
        startTimer();
    }

    private static void startTimer() {
//        System.out.println(Properties.ADDRESS_ELASTICSEARCH);
//        System.out.println(Properties.ADDRESS_ELASTICSEARCH_LOCALHOST);
        System.out.println(Properties.ADDRESS_ISG);
        System.out.println(Properties.ADDRESS_INFLUXDB);
        System.out.println(Properties.DATABASE_RAW_DATA_INFLUXDB);
//        System.out.println(Properties.PORT_ELASTICSEARCH);
//        System.out.println(Properties.PROTOCOL_ELASTICSEARCH);
        try {
            Timer timerCollection = new Timer();
            timerCollection.schedule(new TimerTask() {
                @Override
                public void run() {
                    ZonedDateTime now = ZonedDateTime.now();
                    GregorianCalendar timestamp = GregorianCalendar.from(now);
                    int seconds = timestamp.get(GregorianCalendar.SECOND);
                    int secondsrounded = (seconds % 15) * 15;
                    GregorianCalendar clearedTimestamp = new GregorianCalendar(timestamp.get(GregorianCalendar.YEAR),
                            timestamp.get(GregorianCalendar.MONTH),
                            timestamp.get(GregorianCalendar.DAY_OF_MONTH),
                            timestamp.get(GregorianCalendar.HOUR_OF_DAY),
                            timestamp.get(GregorianCalendar.MINUTE),
                            secondsrounded);
                    getStiebelEltronData(clearedTimestamp.toInstant());
                }
            }, 0, 1000 * 15);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getStiebelEltronData(Instant time) {
        System.out.println("Get Stiebel-Eltron Data");
        long start = System.currentTimeMillis();
        informations.stream().parallel().forEach(abstractInfo -> {
            URLConnection connection = null;
            try {
                System.out.println("Trying to get ISG-Infos from url " + abstractInfo.url);
                connection = new URL(abstractInfo.url).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                System.out.println("Extract ISG-Infos from url " + abstractInfo.url);
                abstractInfo.getInformations(content, time);
            } catch (IOException e) {
                System.err.println("Fehler beim Abfragen der Daten vom ISG");
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("Dauer: " + (end - start));
    }

}


