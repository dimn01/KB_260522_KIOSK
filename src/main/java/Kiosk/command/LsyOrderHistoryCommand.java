package Kiosk.command;

import Kiosk.SessionManager;
import Kiosk.dao.LsyOrderDao;
import Kiosk.domain.LsyOrder;
import java.util.List;

public class LsyOrderHistoryCommand implements Command {
    private final LsyOrderDao orderDao;

    public LsyOrderHistoryCommand(LsyOrderDao orderDao) {
        this.orderDao = orderDao;
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
    }
}
