package de.hank.arnim.common.spielwiese;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hank.arnim.common.Utils;
import de.hank.arnim.common.ValueDto;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.*;

import static de.hank.arnim.common.Utils.ADDRESS_ELASTICSEARCH;

/**
 * Created by arnim on 12/29/17.
 */
public class FilterElasticsearchSearchQuery {

    public static RestHighLevelClient client;
    public static ObjectMapper mapper;

    static {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(ADDRESS_ELASTICSEARCH, 9200, "http")));
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        GregorianCalendar from = new GregorianCalendar(2019, Calendar.MARCH, 1, 0, 0);
        GregorianCalendar to = new GregorianCalendar(2019, Calendar.MARCH, 31, 23, 0);
        List<ValueDto> values = new ArrayList<>();
        /*SearchRequest searchRequest = new SearchRequest("heizungssuite_iw_nhz_1");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(6000);
        long start = System.nanoTime();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("date").gte(from.toInstant().toEpochMilli()).lte(to.toInstant().toEpochMilli()));
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(1L));
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
*/
        Map<String, List<ValueDto>> heizungssuite_iw_nhz_1 = Utils.getDataFromIndexInInterval(Collections.singletonList("heizungssuite_iw_nhz_1"), from.toInstant(), to.toInstant());

/*        for (SearchHit hit : hits) {
            ValueDto valueDto = mapper.readValue(hit.getSourceAsString(), ValueDto.class);
            values.add(valueDto);
        }
  */
        String x = mapper.writeValueAsString(heizungssuite_iw_nhz_1.get("heizungssuite_iw_nhz_1"));
        long end = System.nanoTime();
        System.out.println(heizungssuite_iw_nhz_1.get("heizungssuite_iw_nhz_1").size());

        client.close();
    }

}
