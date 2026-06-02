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
    private static final LsyOrderDao orderDao = new LsyJsonOrderDaoImpl();
    private static final FoodDao foodDao = new MysqlFoodDaoImpl(); // 음식 데이터 DAO 추가
    //
    private static final Map<Integer, Command> commands = new HashMap<>();

    static {
        // 커맨드 초기화
        commands.put(1, new LoginCommand(memberDao, sc));
        commands.put(2, new SignupCommand(memberDao, sc));
        commands.put(3, new TimeChargeCommand(memberDao, sc));
        commands.put(4, new FoodOrderCommand(foodOrderController, sc));
        commands.put(5, new CartCommand(foodOrderController, sc));
        commands.put(6, new LsyOrderHistoryCommand(orderDao, sc));
        commands.put(9, new LogoutCommand());
         // 3~8번 기능은 추후 구현 예정 (현재는 가상 커맨드나 메시지 처리 가능)
    }

    public static void main(String[] args) {
        while (true) {
            // 사용자가 입력한 값 저장
            int choice = showMainMenu();
            
            if (choice == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            // 선택한 기능 command(로그인, 로그아웃 등 기능 함수로) 불러오기
            Command command = commands.get(choice);
            if (command != null) {
                // 로그인(1), 회원가입(2)을 제외한 모든 기능은 로그인 상태여야 함
                if (choice != 1 && choice != 2 && !SessionManager.isLoggedIn()) {
                    System.out.println("\n[알림] 로그인이 필요한 서비스입니다.");
                } else {
                    command.execute(); //받아온 함수(커멘드) 실행
                }
            } else if (choice >= 7 && choice <= 8) {
                System.out.println("해당 기능은 아직 구현되지 않았습니다.");
            } else {
                System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
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
            System.out.println("3. 시간 충전");
            System.out.println("4. 음식 주문");
            System.out.println("5. 장바구니");
            System.out.println("6. 주문내역 조회");
            System.out.println("9. 로그아웃");
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
