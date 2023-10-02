package maven.test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.util.Scanner;

import static maven.test.DataService.dataService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        menu();



    }


    public static void menu(){
        ElasticSearch elasticSearch= new ElasticSearch();
        dataService.setRestClient(elasticSearch.getRestClient());
        String index;
        Scanner scanner=new Scanner(System.in);
        while(true) {
            System.out.println("원하는 작업 선택");
            System.out.println("데이터 추가 : 1");
            System.out.println("데이터 삭제 : 2");
            System.out.println("데이터 검색 : 3");
            System.out.println("인덱스 관리 : 4");

            System.out.println("나가기 : 6");
            int choice = scanner.nextInt();
            if(choice==6)
                break;
            else if(choice==1) {
                while (true) {
                    System.out.print("대상 인덱스 입력:");
                    index = scanner.next();
                    System.out.println("데이터 입력");
                    System.out.println("파일 입력 : 1");
                    System.out.println("직접 입력 : 2");
                    System.out.println("매핑 :3");
                    String data;
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        System.out.print("파일 주소를 입력하세요 :");
                        data = scanner.next();
                        System.out.println(data);
                        dataService.addFileData(index, data);
                        break;
                    } else if (choice == 2) {
                        System.out.print("데이터를 직접 입력하세요 : ");
                        data = scanner.next();
                        dataService.addData(index, data);
                        break;
                    }else if(choice==3){
                        dataService.mappingSetting(index);
                    }
                    else {
                        System.out.println("잘못된 입력");
                    }
                }
            }
            else if(choice==2) {
                System.out.print("대상 인덱스 입력 :");
                index = scanner.next();
                System.out.print("삭제 대상 ID 입력 :");
                String id = scanner.next();
                dataService.deleteData(index, id);
            }
            else if(choice==3) {
                while (true) {
                    System.out.print("대상 인덱스 입력 :");
                    index = scanner.next();
                    System.out.println("검색 방식 선택 ");
                    System.out.println("범위 검색 : 1");
                    System.out.println("일반 검색 : 2");
                    System.out.println("전체 출력 : 3");
                    choice = scanner.nextInt();
                    if (choice == 1) {
                        System.out.println("찾으려는 범주 입력 :");
                        String title = scanner.next();
                        System.out.println("범위를 입력하세요");
                        System.out.print("시작 : ");
                        int x = scanner.nextInt();
                        System.out.print("끝 : ");
                        int y = scanner.nextInt();
                        dataService.searchRangeData(index, title, x, y);
                        break;
                    } else if (choice == 2) {
                        System.out.println("찾으려는 범주 입력 :");
                        String title = scanner.next();
                        System.out.println("키워드 입력 :");
                        String data = scanner.next();
                        dataService.searchData(index, title, data);
                        break;
                    }else if (choice==3){
                        dataService.searchAll(index);
                        break;
                    }
                    else {
                        System.out.println("잘못된 입력");
                    }
                }
            }
            else if(choice==4){
                System.out.println("작업 선택");
                System.out.println("인덱스 추가 : 1");
                System.out.println("인덱스 삭제 : 2");
                System.out.println("인덱스 목록 : 3");
                choice=scanner.nextInt();
                if(choice==1){
                    System.out.print("인덱스 이름 입력 : ");
                    index = scanner.next();
                    dataService.addIndex(index);
                }
                else if(choice==2){
                    System.out.println("삭제 인덱스 입력 :" );
                    index=scanner.next();
                    dataService.deleteIndex(index);
                }
                else if(choice==3){
                    dataService.getIndexList();
                }
                else
                    System.out.println("잘못된 입력");
            }
            else{
                System.out.println("잘못된 입력");
            }
        }
        scanner.close();
    }
}
