package Kiosk;

import java.util.Scanner;

public class order_log {
    static Scanner sc = new Scanner(System.in);

    public static void showOrderLog() {
        int currentPage = 1;
        int pageSize = 2; // 페이지당 주문 번호 2개

        // 하드코딩된 데이터 (실제 JDBC 연동 전 단계)
        // 주문번호, 상품명, 수량, 단가
        String[][] orders = {
            {"1", "신라면", "1", "4000"},
            {"1", "콜라", "2", "2000"},
            {"2", "치즈버거", "1", "5500"},
            {"3", "아이스 아메리카노", "1", "3000"},
            {"3", "감자튀김", "1", "2500"},
            {"4", "새우깡", "1", "1500"},
            {"5", "제로콜라", "1", "2000"}
        };

        // 주문번호별로 그룹화된 데이터 (실제 구현시에는 SQL의 GROUP BY나 로직 필요)
        // 여기서는 하드코딩이므로 주문번호 1, 2, 3, 4, 5가 있다고 가정
        int totalOrders = 5; 
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        while (true) {
            System.out.println("\n===================================");
            System.out.println("          [ 주문 내역 조회 ]          ");
            System.out.println("===================================");
            System.out.println("페이지: " + currentPage + " / " + totalPages);
            System.out.println("-----------------------------------");
            System.out.printf("%-8s | %-20s | %-5s | %-8s\n", "주문번호", "상품이름", "수량", "가격");
            System.out.println("------------------------------------------------------------");

            int startOrderNum = (currentPage - 1) * pageSize + 1;
            int endOrderNum = startOrderNum + pageSize - 1;

            for (int i = 1; i <= totalOrders; i++) {
                if (i >= startOrderNum && i <= endOrderNum) {
                    int orderTotal = 0;
                    boolean found = false;
                    for (String[] row : orders) {
                        if (Integer.parseInt(row[0]) == i) {
                            int quantity = Integer.parseInt(row[2]);
                            int price = Integer.parseInt(row[3]);
                            // 데이터 행 출력 포맷을 헤더와 일치시킴
                            System.out.printf("%-10s | %-22s | %-6s | %-8s\n", row[0], row[1], row[2], price);
                            orderTotal += (quantity * price);
                            found = true;
                        }
                    }
                    if (found) {
                        System.out.println("-> 주문번호 " + i + " 총 가격: " + orderTotal + "원");
                        System.out.println("------------------------------------------------------------");
                    }
                }
            }

            System.out.println("1. 이전 페이지  2. 다음 페이지  0. 돌아가기");
            System.out.print("입력: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                if (currentPage > 1) currentPage--;
                else System.out.println("첫 페이지입니다.");
            } else if (choice == 2) {
                if (currentPage < totalPages) currentPage++;
                else System.out.println("마지막 페이지입니다.");
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }
}

