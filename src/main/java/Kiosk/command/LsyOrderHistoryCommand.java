package Kiosk.command;

import Kiosk.SessionManager;
import Kiosk.dao.LsyOrderDao;
import Kiosk.domain.LsyOrder;
import java.util.List;
import java.util.Scanner;

public class LsyOrderHistoryCommand implements Command {
    private final LsyOrderDao orderDao;
    private final Scanner sc;

    public LsyOrderHistoryCommand(LsyOrderDao orderDao, Scanner sc) {
        this.orderDao = orderDao;
        this.sc = sc;
    }

    @Override
    public void execute() {
        if (!SessionManager.isLoggedIn()) {
            System.out.println("\n[오류] 로그인이 필요한 서비스입니다.");
            return;
        }

        String memberId = SessionManager.getLoggedInMember().getMemberId();
        List<LsyOrder> history = orderDao.findByMemberId(memberId);

        System.out.println("\n--------------------------------------");
        System.out.println("          [ 나의 주문 내역 ]");
        System.out.println("--------------------------------------");

        if (history.isEmpty()) {
            System.out.println("         주문 내역이 없습니다.");
        } else {
            for (LsyOrder order : history) {
                System.out.println("[주문 번호: " + order.getOrderId() + "] " + order.getOrderDate());
                if (order.getItems() != null) {
                    for (var item : order.getItems()) {
                        System.out.println(" - " + item.getName() + " x " + item.getQuantity() + "개 : " + (item.getPrice() * item.getQuantity()) + "원");
                    }
                }
                System.out.println(" 총 결제 금액 : " + order.getTotalPrice() + "원");
                System.out.println("--------------------------------------");
            }
        }

        while (true) {
            System.out.println("0. 메인 메뉴로 돌아가기");
            System.out.println("======================================");
            System.out.print("원하는 기능의 번호를 입력하세요: ");

            String input = sc.nextLine();
            if ("0".equals(input)) {
                break;
            } else {
                System.out.println("잘못된 입력입니다. 0번을 입력해주세요.");
            }
        }
    }
}
