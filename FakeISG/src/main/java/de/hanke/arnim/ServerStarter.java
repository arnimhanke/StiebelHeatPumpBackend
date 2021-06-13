package de.hanke.arnim;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Spark;
import spark.utils.IOUtils;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by arnim on 12/24/17.
 */
public class ServerStarter {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        port(12345);
        get("/", (request, response) -> {
            String s = request.queryParams("s");
           switch(s) {
               case "2,0":
                   // Diagnose Anlage
                   String diagnoseAnlage = IOUtils.toString(Objects.requireNonNull(ServerStarter.class.getClassLoader().getResourceAsStream("ISG/Diagnose/Anlage.html")));
                   return diagnoseAnlage;
               case "2,1":
                   // Diagnose System
                   String diagnoseSystem = IOUtils.toString(Objects.requireNonNull(ServerStarter.class.getClassLoader().getResourceAsStream("ISG/Diagnose/System.html")));
                   return diagnoseSystem;
               case "1,0":
                   // Info Anlage
                   String infoAnlage = IOUtils.toString(Objects.requireNonNull(ServerStarter.class.getClassLoader().getResourceAsStream("ISG/Info/Anlage.html")));
                   return infoAnlage;
               case "1,1":
                   // Info Waermepumpe
                   String infoSystem = IOUtils.toString(Objects.requireNonNull(ServerStarter.class.getClassLoader().getResourceAsStream("ISG/Info/Waermepumpe.html")));
                   return infoSystem;
               default:
                   throw new IllegalArgumentException(s);
           }
        });
    }
}
