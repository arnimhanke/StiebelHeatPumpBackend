package de.hank.arnim;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestClient;
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
import java.util.stream.Collectors;

public class Utils {

    public static final boolean cacheEnabled = false;
    public static final ObjectMapper mapper = new ObjectMapper();
    private static final Map<String, ValueDto> lastValues = new HashMap<>();
    private static final Cache cache = new Cache();
    public static String ADDRESS_ELASTICSEARCH;
    public static int PORT_ELASTICSEARCH;
    public static String PROTOCOL_ELASTICSEARCH;
    public static String ADDRESS_ISG;
    static Properties properties;

    static {

        properties = new Properties();
        try {
            properties.load(Utils.class.getResourceAsStream("de/hank/arnim/system.properties"));
            ADDRESS_ELASTICSEARCH = properties.getProperty("ADRESS_ELASTICSEARCH");
            PORT_ELASTICSEARCH = Integer.parseInt(properties.getProperty("PORT_ELASTICSEARCH"));
            PROTOCOL_ELASTICSEARCH = properties.getProperty("PROTOCOL_ELASTICSEARCH");
            ADDRESS_ISG = properties.getProperty("ADDRESS_ISG");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RestHighLevelClient generateESRestClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(ADDRESS_ELASTICSEARCH, PORT_ELASTICSEARCH, PROTOCOL_ELASTICSEARCH)));
    }

    public static Map<String, List<ValueDto>> getDataFromIndexInInterval(List<String> indicies, Instant from, Instant to) {
        Map<String, List<ValueDto>> ret = new LinkedHashMap<>();
        List<AbstractMap.SimpleEntry<String, List<ValueDto>>> collection_of_data = indicies.parallelStream().map(index -> {
            if (cacheEnabled) {
                // Cache abfragen
                Cache.CacheResult cacheResult = cache.getValueDtosInMap(index, from.toEpochMilli(), to.toEpochMilli());
                if (cacheResult.getMissingIntervals().size() == 0) {
                    System.out.println("Daten aus dem Cache abfragen");
                    return new HashMap.SimpleEntry<>(index, cacheResult.getFoundValues());
                } else {
                    System.out.println("Teil der Daten aus der Datenbank laden");
                    List<ValueDto> allValues = new ArrayList<>();
                    cacheResult.getMissingIntervals().forEach(longLongPair -> {
                        allValues.addAll(loadDataFromDatabase(longLongPair.getObjA(), longLongPair.getObjB(), index));
                    });
                    cacheResult.getFoundValues().addAll(allValues);
                    // Alle Ergebnisse sortieren
                    cacheResult.getFoundValues().sort(Comparator.comparing(ValueDto::getDate));
                    // Cache füllen
                    allValues.forEach(valueDto -> cache.putValueInMap(index, valueDto, from.toEpochMilli(), to.toEpochMilli()));

                    return new HashMap.SimpleEntry<>(index, allValues);
                }
            } else {
                List<ValueDto> valueDtos = loadDataFromDatabase(from.toEpochMilli(), to.toEpochMilli(), index);
                valueDtos.sort(Comparator.comparing(ValueDto::getDate));
                System.out.println(index);
                return new HashMap.SimpleEntry<>(index, valueDtos);
            }
        }).collect(Collectors.toList());
        collection_of_data.forEach(stringListSimpleEntry -> {
            ret.put(stringListSimpleEntry.getKey(), stringListSimpleEntry.getValue());
        });
        return ret;
    }

    private static List<ValueDto> loadDataFromDatabase(long from, long to, String index) {
        List<ValueDto> allSearchHits = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        searchRequest.scroll(scroll);
        searchSourceBuilder.query(QueryBuilders.rangeQuery("date").gte(from).lte(to));
        try (RestHighLevelClient client = generateESRestClient()) {
            SearchResponse searchResponse = client.search(searchRequest);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            for (SearchHit searchHit : searchHits) {
                // add ValueDtos to list
                allSearchHits.add(mapper.readValue(searchHit.getSourceAsString(), ValueDto.class));
            }

            while (searchHits.length > 0) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = client.searchScroll(scrollRequest);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();

                for (SearchHit searchHit : searchHits) {
                    // add ValueDtos to list
                    allSearchHits.add(mapper.readValue(searchHit.getSourceAsString(), ValueDto.class));
                }
            }

            // If the first ValueDto laies after the given from-Instant, we have to ask for the last change in the database!
            if (allSearchHits.size() > 0 && allSearchHits.get(0).getDate() > from || allSearchHits.size() == 0) {
                SearchRequest searchRequestForLastValue = new SearchRequest(index);
                SearchSourceBuilder searchSourceBuilderForLastValue = new SearchSourceBuilder();
                searchSourceBuilderForLastValue.size(1);

                searchSourceBuilderForLastValue.query(QueryBuilders.rangeQuery("date").lte(from));
                searchSourceBuilderForLastValue.sort("date", SortOrder.DESC);

                searchRequestForLastValue.source(searchSourceBuilderForLastValue);


                SearchResponse search = client.search(searchRequestForLastValue);
                SearchHits hitsForLastValue = search.getHits();
                for (SearchHit searchHitForLastValue : hitsForLastValue) {
                    // add ValueDtos to list
                    allSearchHits.add(mapper.readValue(searchHitForLastValue.getSourceAsString(), ValueDto.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allSearchHits;
    }

    public static Map<String, ValueDto> getLastValueDtosForIndicies(List<String> indicies) {
        Map<String, ValueDto> ret = new LinkedHashMap<>();

        indicies.forEach(index -> {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.sort("date", SortOrder.DESC);
            searchSourceBuilder.size(1);
            searchRequest.source(searchSourceBuilder);
            try {
                RestHighLevelClient client = generateESRestClient();
                SearchResponse search = client.search(searchRequest);
                SearchHit[] hits = search.getHits().getHits();
                if (hits.length == 1) {
                    ValueDto valueDto = mapper.readValue(hits[0].getSourceAsString(), ValueDto.class);
                    ret.put(hits[0].getType(), valueDto);
                } else {
                    throw new IOException("Mehr als ein Ergebnis gefunden, obwohl die auf eine Länge von eins eingegrenzt wurde.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ret;
    }


    public static void putValueForKeyInElasticSearch(String content, String tabelName, String key, long time, String esType) {
        try {
            if (content.contains(tabelName + "</th>")) {
                String table = content.split(tabelName + "</th>")[1];
                if (table.contains(key + "</td>")) {
                    String[] split = table.split(">" + key + "</td>");
                    String ret = split[1].split("</td>")[0].split("\">")[1];
                    putValueIntoElasticsearch(ret, time, esType);
                } else {
                    // Type nicht in der Website vorhanden, Type scheint ausgeschaltet zu sein => muss trotzdem registriert werden, ist ja eine Änderung!
                    putValueIntoElasticsearch("", time, esType);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println(key + " - " + esType);
        }
    }

    public static boolean putValueIntoElasticsearch(String value, long time, String esType) {
        RestHighLevelClient client = generateESRestClient();
        try {
            ValueDto lastValueDtoForId = lastValues.get(esType);
            if (lastValueDtoForId == null || !lastValueDtoForId.getValue().equals(value)) {
                ValueDto valueDto = new ValueDto(value, time);
                IndexRequest source = new IndexRequest("heizungssuite_" + esType.toLowerCase(), esType, esType + "-" + time).source(mapper.writeValueAsString(valueDto), XContentType.JSON);
                IndexResponse index = client.index(source);
                lastValues.put(esType, valueDto);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}
