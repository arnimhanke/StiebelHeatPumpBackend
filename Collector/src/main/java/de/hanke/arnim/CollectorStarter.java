package de.hanke.arnim;

import de.hanke.arnim.common.Constant;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.InfluxDBUtils;
import de.hanke.arnim.common.ValueDto;
import de.hanke.arnim.common.dtos.PeriodicTimeseries;
import de.hanke.arnim.common.dtos.PeriodicTimeseriesValue;
import de.hanke.arnim.common.dtos.Raster;
import de.hanke.arnim.heizung.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

import static de.hanke.arnim.common.utils.DataCorrection.parseDataFromValueDtoToBigDecimal;

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
                    long time = clearedTimestamp.toInstant().toEpochMilli();
                    getStiebelEltronData(time);
                }
            }, 0, 1000 * 15);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getStiebelEltronData(long time) {
        long start = System.currentTimeMillis();
        informations.stream().parallel().forEach(abstractInfo -> {
            URLConnection connection = null;
            try {
                connection = new URL(abstractInfo.url).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                abstractInfo.getInformations(content, time);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("Dauer: " + (end - start));
    }

}


