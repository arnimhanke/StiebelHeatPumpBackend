package de.hanke.arnim.common.spielwiese;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hanke.arnim.common.ElasticSearchUtils;
import de.hanke.arnim.common.ValueDto;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.*;


/**
 * Created by arnim on 12/29/17.
 */
public class FilterElasticsearchSearchQuery {

    public static RestHighLevelClient client;
    public static ObjectMapper mapper;
    public ElasticSearchUtils elasticSearchUtils;
    public FilterElasticsearchSearchQuery() {
        this.elasticSearchUtils = new ElasticSearchUtils("192.168.178.78", "localhost", 9200, "http");
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticSearchUtils.ADDRESS_ELASTICSEARCH, 9200, "http")));
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        FilterElasticsearchSearchQuery bla = new FilterElasticsearchSearchQuery();
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
        Map<String, List<ValueDto>> heizungssuite_iw_nhz_1 = bla.elasticSearchUtils.getDataFromIndexInInterval(Collections.singletonList("heizungssuite_iw_nhz_1"), from.toInstant(), to.toInstant());

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
