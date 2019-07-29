package de.hanke.arnim.data_collector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.common.Utils;
import de.hanke.arnim.common.ValueDto;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static de.hanke.arnim.common.Utils.*;

/**
 * Created by arnim on 2/13/18.
 */
public class Copy_Helper {

    public static Utils utils = new Utils();
    public static RestHighLevelClient remoteClient = utils.generateESRestClient();
    public static RestHighLevelClient localClient = utils.generateLocalESRestClient();
    public static ObjectMapper mapper;
    public static List<String> indicies = new ArrayList<>();

    static {
        mapper = new ObjectMapper();
        // Following indicies are copyied
//        indicies.add("heizungssuite_da_heizkreispumpe");
//        indicies.add("heizungssuite_iw_verdichter");
//        indicies.add("heizungssuite_da_pufferladepumpe");
//        indicies.add("heizungssuite_iw_vd_heizen");
//        indicies.add("heizungssuite_iw_waermemenge_nhz_heizen_summe");
//        indicies.add("heizungssuite_ia_quellentemperatur");
//        indicies.add("heizungssuite_iw_vd_kühlen");
//        indicies.add("heizungssuite_iw_nhz_2");
//        indicies.add("heizungssuite_ia_raumfeuchte");
        indicies.add("heizungssuite_ia_rücklaufisttemperatur");
        indicies.add("heizungssuite_ia_anlagenfrost");
        indicies.add("heizungssuite_ia_vorlaufisttemperatur_wp");
        indicies.add("heizungssuite_ia_quellendruck");
        indicies.add("heizungssuite_da_reststillstand");
        indicies.add("heizungssuite_ia_vorlaufisttemperatur_nhz");
        indicies.add("heizungssuite_ds_nebenversionsnummer");
        indicies.add("heizungssuite_ia_isttemperatur_hk_1");


        indicies.add("heizungssuite_ds_ok");
        indicies.add("heizungssuite_ia_volumenstrom");
        indicies.add("heizungssuite_da_verdichterschuetz");
        indicies.add("heizungssuite_da_verdichter");
        indicies.add("heizungssuite_ia_isttemperatur_gebläse");
        indicies.add("heizungssuite_iw_leistungsaufnahme_vd_heizen_tag");
        indicies.add("heizungssuite_da_quellenpumpe");
        indicies.add("heizungssuite_ia_isttemperatur_fek");
        indicies.add("heizungssuite_da_heizen");
        indicies.add("heizungssuite_ds_wpm_3i");
        indicies.add("heizungssuite_ds_software");
        indicies.add("heizungssuite_ds_revisionsnummer");
        indicies.add("heizungssuite_iw_nhz_1_durch_2");
        indicies.add("heizungssuite_ia_solltemperatur_fek");
        indicies.add("heizungssuite_iw_waermemenge_vd_heizen_tag");
        indicies.add("heizungssuite_ds_hauptversionsnummer");
        indicies.add("heizungssuite_ia_solltemperatur_gebläse");
        indicies.add("heizungssuite_ia_puffersolltemperatur");
        indicies.add("heizungssuite_ds_sg_ready");
        indicies.add("heizungssuite_iw_leistungsaufnahme_vd_heizen_summe");
        indicies.add("heizungssuite_iw_waermemenge_vd_heizen_summe");
        indicies.add("heizungssuite_iw_nhz_1");
        indicies.add("heizungssuite_da_heizkreis_1_pumpe");
        indicies.add("heizungssuite_ia_pufferisttemperatur");
        indicies.add("heizungssuite_ia_taupunkttemperatur");
        indicies.add("heizungssuite_ia_aussentemperatur");
        indicies.add("heizungssuite_ia_solltemperatur_hk_1");
        indicies.add("heizungssuite_ia_heizungsdruck");
    }

    /**
     * method for copying values from Database A to B
     */
    public static void main(String[] args) throws IOException {
        System.out.println(utils.ADDRESS_ELASTICSEARCH);
        indicies.forEach((id) -> {
            GregorianCalendar from = new GregorianCalendar(2019, Calendar.JANUARY, 1, 0, 0);
            GregorianCalendar to = new GregorianCalendar(2019, Calendar.JULY, 7, 0, 0);
            System.out.println("Laden der Daten vom Remote-Client");
            List<ValueDtoWithID> dataFromIndexInInterval = getDataFromIndexInInterval(Collections.singletonList(id), from.toInstant(), to.toInstant());
            System.out.println("Laden erfolgreich");
            final int[] i = {0};
            final boolean[] mustWriteLastEntries = {false};
            final BulkRequest[] request = {new BulkRequest()};
            dataFromIndexInInterval.forEach(valueDtoId -> {
                String esType = id.replace("heizungssuite_", "").toUpperCase();
                try {
                    mustWriteLastEntries[0] = true;
                    request[0].add(new IndexRequest("heizungssuite_" + esType.toLowerCase(), esType, valueDtoId.getId()).source(mapper.writeValueAsString(valueDtoId.getValueDto()), XContentType.JSON));

                    i[0]++;
                    // Alle 100 Einträge in die DB speichern
                    if(i[0] == 100) {
                        try {
                            System.out.println("Wieder 100 wegschreiben");
                            localClient.bulk(request[0]);
                            i[0] = 0;
                            mustWriteLastEntries[0] = false;
                        } catch (IOException e) {
                            System.out.println("Fehler beim Schreiben in die DB");
                            e.printStackTrace();
                        }
                        //neuen Request erstellen
                        request[0] = new BulkRequest();
                    }
                } catch (JsonProcessingException e) {
                    System.out.println("Fehler beim Mapping");
                    e.printStackTrace();
                }

            });
            System.out.println("Entries in DB gespeichert");
            if(mustWriteLastEntries[0]) {
                try {
                    localClient.bulk(request[0]);
                } catch (IOException e) {
                    System.out.println("Fehler beim Schreiben in die DB");
                    e.printStackTrace();
                }
            }
            System.out.println(id + " ---- fertig -----");
        });
        remoteClient.close();
        localClient.close();
    }

    /**
     * Load all values for the given indicies and the interval
     *
     * @param indicies to load data from
     * @param from     begin of the interval
     * @param to       end of the interval
     * @return List of loaded values
     */
    public static List<ValueDtoWithID> getDataFromIndexInInterval(List<String> indicies, Instant from, Instant to) {
        List<ValueDtoWithID> ret = new ArrayList<>();
        indicies.forEach(index -> {
            List<ValueDtoWithID> allSearchHits = new ArrayList<>();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(10000);
            searchSourceBuilder.sort("date", SortOrder.ASC);
            searchRequest.source(searchSourceBuilder);
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            searchRequest.scroll(scroll);
            searchSourceBuilder.query(QueryBuilders.rangeQuery("date").gte(from.toEpochMilli()).lte(to.toEpochMilli()));
            try {

                SearchResponse searchResponse = remoteClient.search(searchRequest);
                String scrollId = searchResponse.getScrollId();
                SearchHit[] searchHits = searchResponse.getHits().getHits();

                for (SearchHit searchHit : searchHits) {
                    // add ValueDtos to list
                    allSearchHits.add(new ValueDtoWithID(mapper.readValue(searchHit.getSourceAsString(), ValueDto.class), searchHit.getId()));
                }

                while (searchHits.length > 0) {
                    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                    scrollRequest.scroll(scroll);
                    searchResponse = remoteClient.searchScroll(scrollRequest);
                    scrollId = searchResponse.getScrollId();
                    searchHits = searchResponse.getHits().getHits();

                    for (SearchHit searchHit : searchHits) {
                        // add ValueDtos to list
                        allSearchHits.add(new ValueDtoWithID(mapper.readValue(searchHit.getSourceAsString(), ValueDto.class), searchHit.getId()));
                    }

                }

                // If the first ValueDto laies after the given from-Instant, we have to ask for the last change in the database!
                if (allSearchHits.size() > 0 && allSearchHits.get(0).getValueDto().getDate() > from.toEpochMilli() || allSearchHits.size() == 0) {
                    SearchRequest searchRequestForLastValue = new SearchRequest(index);
                    SearchSourceBuilder searchSourceBuilderForLastValue = new SearchSourceBuilder();
                    searchSourceBuilderForLastValue.size(1);
                    searchSourceBuilderForLastValue.sort("date", SortOrder.DESC);

                    searchSourceBuilderForLastValue.query(QueryBuilders.rangeQuery("date").lte(from.toEpochMilli()));

                    searchRequestForLastValue.source(searchSourceBuilderForLastValue);

                    SearchResponse search = remoteClient.search(searchRequestForLastValue);
                    SearchHits hitsForLastValue = search.getHits();
                    for (SearchHit searchHitForLastValue : hitsForLastValue) {
                        // add ValueDtos to list
                        allSearchHits.add(new ValueDtoWithID(mapper.readValue(searchHitForLastValue.getSourceAsString(), ValueDto.class), searchHitForLastValue.getId()));
                    }
                }
                allSearchHits.sort(Comparator.comparing(valueDtoWithID -> valueDtoWithID.getValueDto().getDate()));
                ret.addAll(allSearchHits);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // in the end, close the client and generate a new one
                try {
                    remoteClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                remoteClient = null;
                remoteClient = utils.generateESRestClient();
            }
        });
        return ret;
    }

}

