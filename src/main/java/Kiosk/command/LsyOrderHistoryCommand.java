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

        System.out.println("\n===================================");
        System.out.println("          나의 주문 내역 조회 ");
        System.out.println("===================================");

        if (history.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
        } else {
            for (LsyOrder order : history) {
                System.out.println(order);
                System.out.println("-----------------------------------");
            }
        }
        System.out.println("===================================");
        System.out.println("[Enter] 키를 누르면 메인 메뉴로 돌아갑니다.");
        sc.nextLine();
    }
}
