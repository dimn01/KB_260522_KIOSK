package Kiosk;

import java.util.Scanner;

public class kioskController {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = showMainMenu();
        System.out.println("선택한 번호: " + choice);
        if (choice == 0) {
            return;
        }
        if(choice == 8) order_log.showOrderLog();
        
    }

    // 초기 화면 출력 및 선택
    public static int showMainMenu() {

        System.out.println("===================================");
        System.out.println("          PC방 키오스크 시스템         ");
        System.out.println("===================================");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 시간 충전");
        System.out.println("4. 음식 주문");
        System.out.println("5. 장바구니");
        System.out.println("6. 음식 조회");
        System.out.println("7. 음료 조회");
        System.out.println("8. 주문내역 조회");
        System.out.println("0. 종료");

        int choice = sc.nextInt();
        sc.nextLine(); // 버퍼 비우기

        return choice;
    }
}