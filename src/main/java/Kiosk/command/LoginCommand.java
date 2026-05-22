package Kiosk.command;

import Kiosk.SessionManager;
import Kiosk.dao.MemberDao;
import Kiosk.domain.Member;
import java.util.Scanner;

public class LoginCommand implements Command {
    private final MemberDao memberDao;
    private final Scanner sc;


    public LoginCommand(MemberDao memberDao, Scanner sc) {
        this.memberDao = memberDao;
        this.sc = sc;
    }

    @Override
    public void execute() {
        if (SessionManager.isLoggedIn()) {
            System.out.println("이미 로그인된 상태입니다: " + SessionManager.getLoggedInMember().getName());
            return;
        }

        System.out.println("\n[로그인]");
        System.out.print("아이디: ");
        String id = sc.nextLine();
        System.out.print("비밀번호: ");
        String pw = sc.nextLine();

        Member member = memberDao.getMemberById(id);
        if (member != null && member.getPassword().equals(pw)) {
            SessionManager.login(member);
            memberDao.updateLastLoginDate(id);
            System.out.println(member.getName() + "님, 환영합니다!");
        } else {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
