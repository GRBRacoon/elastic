package maven.test;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class ElasticSearch {
    private final RestClient restClient;

    public ElasticSearch() {
         restClient = RestClient.builder(
                new HttpHost("ssel.asuscomm.com", 9192, "http")
         ).build();

    }

    public RestClient getRestClient() {
        return restClient;
    }
}
