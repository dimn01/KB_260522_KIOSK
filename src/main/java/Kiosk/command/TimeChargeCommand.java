package Kiosk.command;

import Kiosk.SessionManager;
import Kiosk.dao.MemberDao;
import Kiosk.domain.Member;
import java.util.Scanner;

public class TimeChargeCommand implements Command {
    private final MemberDao memberDao;
    private final Scanner sc;

    public TimeChargeCommand(MemberDao memberDao, Scanner sc) {
        this.memberDao = memberDao;
        this.sc = sc;
    }

    @Override
    public void execute() {
        if (!SessionManager.isLoggedIn()) {
            System.out.println("로그인 상태가 아닙니다. 로그인을 먼저 해주세요.");
            return;
        }

        Member member = SessionManager.getLoggedInMember();
        System.out.println("\n[시간 충전]");
        System.out.println("현재 잔여 시간: " + member.getRemainingTime() + "분");
        System.out.println("-----------------------------------");
        System.out.println("1. 1시간 (1,000원)");
        System.out.println("2. 2시간 (2,000원)");
        System.out.println("3. 3시간 (3,000원)");
        System.out.println("4. 4시간 (4,000원)");
        System.out.println("5. 5시간 (5,000원)");
        System.out.println("6. 6시간 (6,000원)");
        System.out.println("0. 취소");
        System.out.println("-----------------------------------");
        System.out.print("충전할 시간을 선택하세요: ");

        try {
            int choice = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기

            if (choice == 0) {
                System.out.println("시간 충전을 취소합니다.");
                return;
            }

            if (choice >= 1 && choice <= 6) {
                int additionalMinutes = choice * 60;
                
                // 충전된 시간을 데이터에 반영
                memberDao.updateRemainingTime(member.getMemberId(), additionalMinutes);
                
                // 세션 정보 갱신 (DB에서 최신 정보를 다시 읽어옴)
                Member updatedMember = memberDao.getMemberById(member.getMemberId());
                SessionManager.login(updatedMember);
                
                System.out.println(choice + "시간이 충전되었습니다.");
                System.out.println("현재 총 잔여 시간: " + updatedMember.getRemainingTime() + "분");
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        } catch (Exception e) {
            System.out.println("숫자만 입력 가능합니다.");
            sc.nextLine(); // 버퍼 비우기
        }
    }
}
