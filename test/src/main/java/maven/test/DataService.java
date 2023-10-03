package maven.test;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class DataService {
    private final Logger logger=Logger.getLogger(DataService.class.getName());
    private RestClient restClient;
    public static DataService dataService= new DataService();
    private String indexName;

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    private DataService() {
    }


    public void addIndex(String index) {
         indexName=index;

        try{
            Request createIndexRequest = new Request(
                    "PUT",
                    "/" + indexName
            );
            Response createIndexResponse = restClient.performRequest(createIndexRequest);
            int createIndexResponseStatus = createIndexResponse.getStatusLine().getStatusCode();
            if (createIndexResponseStatus == 200) {
                System.out.println("인덱스 생성 성공");
            }
            else{
                System.out.println("인덱스 생성 실패");
            }
        }catch (IOException e){
            logger.warning(e.toString());
        }


    }

    public void addData(String index, String docJson){
        try {
             indexName = index; // 색인할 인덱스 이름
            String documentJson = docJson; // 색인할 JSON 문서 데이터


            // 문서 색인 요청
            Request indexRequest = new Request(
                    "POST",
                    "/" + indexName + "/_doc" // 문서 ID를 자동으로 생성
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
        } catch (IOException e) {
            logger.warning("문서 색인 실패");
            logger.warning(e.toString());
        }

    }
    public void addFileData(String index, String file)  {
        indexName=index;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder jsonStringBuilder = new StringBuilder();

            // JSON 파일의 각 줄을 읽어와 StringBuilder에 추가
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            // JSON 문자열 생성
            String jsonString = jsonStringBuilder.toString();

            // 데이터 색인 요청 생성
            Request request = new Request(
                    "POST",
                    "/" + indexName + "/_doc"
            );
            request.setEntity(new NStringEntity(
                    jsonString,
                    ContentType.APPLICATION_JSON));

            // 데이터 색인 요청 실행
            Response response = restClient.performRequest(request);

            // 응답 확인
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            System.out.println("색인 결과: " + responseBody);

        }catch (Exception e){
            logger.warning(e.toString());
        }

    }

    public void deleteData(String index,String id){
        try {
            indexName = index; // 삭제할 인덱스 이름
            String documentId = id; // 삭제할 문서의 ID

            // 데이터 삭제 요청
            Request deleteRequest = new Request(
                    "DELETE",
                    "/" + indexName + "/_doc/" + documentId
            );

            // 데이터 삭제 요청 실행
            Response deleteResponse = restClient.performRequest(deleteRequest);

            // 데이터 삭제 결과 확인
            int deleteResponseStatus = deleteResponse.getStatusLine().getStatusCode();
            if (deleteResponseStatus == 200) {
                System.out.println("문서 삭제 성공");
            } else {
                System.err.println("문서 삭제 실패");
            }
        }catch (IOException e){
            logger.warning(e.toString());
        }

    }



    public void searchData(String index,String title,String search){
        try {
            indexName = index; // 검색할 인덱스 이름
            String searchQuery = "{\"query\":{\"match\":{\""+title+"\":\""+search+"\"}}}"; // 검색 쿼리

            // 데이터 검색 요청
            Request searchRequest = new Request(
                    "POST",
                    "/" + indexName + "/_search"
            );
            searchRequest.setJsonEntity(searchQuery);

            // 데이터 검색 요청 실행
            Response searchResponse = restClient.performRequest(searchRequest);

            // 검색 결과 확인
            String responseBody = EntityUtils.toString(searchResponse.getEntity());
            System.out.println("검색 결과:");
            System.out.println(responseBody);
        }catch (IOException e){
            logger.warning(e.toString());
        }

    }
    public  void searchRangeData(String index,String title, float x, float y){
        try {
            indexName = index; // 검색할 인덱스 이름
            String searchQuery = "{" + "\"query\": {" + "\"range\": {" +
                    "\""+title+"\": {" + "\"gte\": \""+x+"\"," + "\"lte\": \""+y+"\"" +
                    "}" + "}" + "}" + "}";
            // 데이터 검색 요청
            Request searchRequest = new Request(
                    "POST",
                    "/" + indexName + "/_search"
            );
            searchRequest.setJsonEntity(searchQuery);

            // 데이터 검색 요청 실행
            Response searchResponse = restClient.performRequest(searchRequest);

            // 검색 결과 확인
            String responseBody = EntityUtils.toString(searchResponse.getEntity());
            System.out.println("검색 결과:");
            System.out.println(responseBody);
        }catch (IOException e){
            logger.warning(e.toString());
        }

    }
    public void searchAll(String index){
        try {
            indexName = index; // 검색할 인덱스 이름
            String searchQuery = "{\"query\":{\"match_all\":{}}}"; // 검색 쿼리

            // 데이터 검색 요청
            Request searchRequest = new Request(
                    "POST",
                    "/" + indexName + "/_search"
            );
            searchRequest.setJsonEntity(searchQuery);

            // 데이터 검색 요청 실행
            Response searchResponse = restClient.performRequest(searchRequest);

            // 검색 결과 확인
            String responseBody = EntityUtils.toString(searchResponse.getEntity());
            System.out.println("검색 결과:");
            System.out.println(responseBody);
        }catch (IOException e){
            logger.warning(e.toString());
        }

    }

    public void getIndexList() {
        try {
            // 인덱스 목록을 가져오기 위한 요청
            Request request = new Request("GET", "/_cat/indices?v");
            Response response = restClient.performRequest(request);

            // 응답 내용을 문자열로 읽기
            String responseBody = EntityUtils.toString(response.getEntity());

            // 인덱스 목록 출력
            System.out.println(responseBody);
        } catch (Exception e) {
            logger.warning(e.toString());
        }
    }
    public void deleteIndex(String index){
        indexName=index;
        Request request= new Request("DELETE","/"+indexName);
        try {
                Response response=restClient.performRequest(request);
                logger.info("인덱스 삭제");
            }catch (Exception e){
            logger.warning(e.toString());
        }


    }
    public void mappingSetting(String index){
        indexName=index;
        Request request = new Request("PUT", "/" + indexName);

        String mappingJson = "{\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"crfSampleData\": {\n" +
                "        \"properties\": {\n" +
                "          \"albumine_0\": {\n" +
                "            \"type\": \"float\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
            try {
                request.setJsonEntity(mappingJson);
                Response response=restClient.performRequest(request);

            }catch (Exception e){
                logger.warning(e.toString());
            }


    }
}
