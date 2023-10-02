package maven.test.forTest;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.elasticsearch.client.RestClient;


import java.io.IOException;


public class Example {
    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        try {
            // 인덱스 생성 요청
            String indexName = "my_index"; // 새로 생성할 인덱스 이름

            Request createIndexRequest = new Request(
                    "PUT",
                    "/" + indexName
            );

            // 인덱스 생성 요청 실행
            Response createIndexResponse = restClient.performRequest(createIndexRequest);

            // 인덱스 생성 결과 확인
            int createIndexResponseStatus = createIndexResponse.getStatusLine().getStatusCode();
            if (createIndexResponseStatus == 200) {
                System.out.println("인덱스 생성 성공");

                // 문서 색인 요청 (동적 매핑을 사용하여 필드를 자동으로 매핑)
                String documentJson = "{\"title\":\"문서 제목\",\"content\":\"문서 내용\",\"timestamp\":\"2023-10-02\"}"; // 색인할 JSON 문서 데이터

                Request indexRequest = new Request(
                        "POST",
                        "/" + indexName + "/_doc/1"
                );
                indexRequest.setJsonEntity(documentJson);

                // 문서 색인 요청 실행
                Response indexResponse = restClient.performRequest(indexRequest);

                // 문서 색인 결과 확인
                int indexResponseStatus = indexResponse.getStatusLine().getStatusCode();
                if (indexResponseStatus == 201) {
                    System.out.println("문서 색인 성공");
                } else {
                    System.err.println("문서 색인 실패");
                }
            } else {
                System.err.println("인덱스 생성 실패");
            }
        } finally {
            // Elasticsearch 클라이언트 종료
            restClient.close();
        }
    }


            /*RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http") // Elasticsearch 호스트 및 포트 설정
            ).build();

            // Elasticsearch로 GET 요청 보내기
            Request request = new Request("GET", "/my_index/_doc/1");
            Response response = restClient.performRequest(request);

            // 응답 데이터 출력
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("응답 데이터: " + responseBody);

            // 클라이언트 종료
            restClient.close();*/

}
