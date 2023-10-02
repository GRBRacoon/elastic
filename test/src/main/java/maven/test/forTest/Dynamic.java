package maven.test.forTest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Dynamic {
    public static void main(String[] args) {
        String filePath = "fixed-data.json"; // JSON 파일 경로


            try (FileReader reader = new FileReader(filePath)) {
                // JSON 파일을 파싱하여 JSONArray로 읽어옴
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

                // JSONArray의 요소를 반복하면서 처리
                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;

                    // 필요한 작업을 수행 (예: 출력)
                    System.out.println("JSON Object: " + jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
