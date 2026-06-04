package Kiosk;

import Kiosk.command.*;
import Kiosk.dao.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class kioskController {

    private static final Scanner sc = new Scanner(System.in);
    //
    // private static final MemberDao memberDao = new MockMemberDaoImpl(); // 기존 메모리 기반 저장소
    private static final FoodOrderController foodOrderController = new FoodOrderController();
    private static final MemberDao memberDao = new JdbcMemberDaoImpl(); // JSON 파일 기반 저장소로 변경
    private static final LsyOrderDao orderDao = new MysqlOrderDaoImpl();
    private static final FoodDao foodDao = new MysqlFoodDaoImpl(); // 음식 데이터 DAO 추가
    //
    private static final Map<Integer, Command> guestCommands = new HashMap<>();
    private static final Map<Integer, Command> memberCommands = new HashMap<>();

    static {
        // 비로그인 상태 커맨드 (1: 로그인, 2: 회원가입)
        guestCommands.put(1, new LoginCommand(memberDao, sc));
        guestCommands.put(2, new SignupCommand(memberDao, sc));

        // 로그인 상태 커맨드 (1~4: 주요 기능, 0: 로그아웃)
        memberCommands.put(1, new TimeChargeCommand(memberDao, sc));
        memberCommands.put(2, new FoodOrderCommand(foodOrderController, sc));
        memberCommands.put(3, new CartCommand(foodOrderController, sc));
        memberCommands.put(4, new LsyOrderHistoryCommand(orderDao, sc));
        memberCommands.put(0, new LogoutCommand());
    }

    public static void main(String[] args) {
        while (true) {
            // 사용자가 입력한 값 저장
            int choice = showMainMenu();

            if (SessionManager.isLoggedIn()) {
                // 로그인 상태 처리
                Command command = memberCommands.get(choice);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
                }
            } else {
                // 비로그인 상태 처리
                if (choice == 0) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }
                Command command = guestCommands.get(choice);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
                }
            }
        }
    }

    // 초기 화면 출력 및 선택
    public static int showMainMenu() {
        System.out.println("\n===================================");
        System.out.println("          PC방 키오스크 시스템         ");
        if (SessionManager.isLoggedIn()) {
            System.out.println(" [ 로그인 중: " + SessionManager.getLoggedInMember().getName() + " 님 ]");
            System.out.println("===================================");
            System.out.println("1. 시간 충전");
            System.out.println("2. 음식 주문");
            System.out.println("3. 장바구니");
            System.out.println("4. 주문내역 조회");
            System.out.println("0. 로그아웃");
        } else {
            System.out.println(" [ 로그인 필요 ]");
            System.out.println("===================================");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
        }
        System.out.println("===================================");
        System.out.print("선택: ");

        try {
            int choice = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기
            return choice;
        } catch (Exception e) {
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
            return -1;
        }
    }
}
