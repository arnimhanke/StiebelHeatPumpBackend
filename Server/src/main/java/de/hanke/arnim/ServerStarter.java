package de.hanke.arnim;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hank.arnim.Utils;
import de.hank.arnim.ValueDto;
import de.hank.arnim.dtos.MonthViewDataDto;
import de.hank.arnim.lang.DisplayedNames;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.hank.arnim.Constant.ES_INDEX_PREFIX;
import static de.hanke.arnim.settings.DashBoard.INDICIES_FOR_DASHBOARD;
import static de.hanke.arnim.settings.DayView.INDICIES_FOR_DAYVIEW;
import static de.hanke.arnim.settings.MonthView.INDICIES_FOR_MONTHVIEW;
import static spark.Spark.get;

/**
 * Created by arnim on 12/24/17.
 */
public class ServerStarter {

    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        get("/dashboard", (request, response) -> {
            System.out.println("Dashboard1");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                Map<String, ValueDto> lastValueDtosForIndicies = Utils.getLastValueDtosForIndicies(INDICIES_FOR_DASHBOARD);
                Map<String, ValueDto> humanReadableNameToValues = new LinkedHashMap<>();
                for (String s : lastValueDtosForIndicies.keySet()) {
                    humanReadableNameToValues.put(DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME.getOrDefault((ES_INDEX_PREFIX + s).toLowerCase(), s), lastValueDtosForIndicies.get(s));
                }
                return mapper.writeValueAsString(humanReadableNameToValues);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return "";
            }
        });

        get("/monthview", (request, response) -> {
            System.out.println("Monthview");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                String fromAsString = request.queryParams("from");
                String toAsString = request.queryParams("to");
                Instant from = Instant.parse(fromAsString);
                Instant to = Instant.parse(toAsString);

                Map<String, List<ValueDto>> lastValueDtosForIndicies = Utils.getDataFromIndexInInterval(INDICIES_FOR_MONTHVIEW, from, to);
                MonthViewDataDto dto = new MonthViewDataDto(lastValueDtosForIndicies, DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME);
                return mapper.writeValueAsString(dto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return "";
            }
        });

        get("/dayview", (request, response) -> {
            System.out.println("Dayview");
            response.header("Access-Control-Allow-Origin", "*");
            try {
                String fromAsString = request.queryParams("from");
                String toAsString = request.queryParams("to");
                Instant from = Instant.parse(fromAsString);
                Instant to = Instant.parse(toAsString);

                Map<String, List<ValueDto>> lastValueDtosForIndicies = Utils.getDataFromIndexInInterval(INDICIES_FOR_DAYVIEW, from, to);
                MonthViewDataDto dto = new MonthViewDataDto(lastValueDtosForIndicies, DisplayedNames.MAP_ES_INDEX_TO_DISPLAYED_NAME);
                return mapper.writeValueAsString(dto);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        });

//        path("/heizung", () -> {
//            get("/getQuelle", (request, response) -> {
//                try {
//                    List<ValueDto> values = getNewestDataForIndex("heizung_test_ia_quellentemperatur");
//                    response.header("Access-Control-Allow-Origin", "*");
//                    return mapper.writeValueAsString(values);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "";
//                }
//            });
//
//            get("/getQuellendruck", (request, response) -> {
//                try {
//                    List<ValueDto> values = getNewestDataForIndex("heizung_test_ia_quellendruck");
//                    response.header("Access-Control-Allow-Origin", "*");
//                    return mapper.writeValueAsString(values);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "";
//                }
//            });
//        });
    }

}
