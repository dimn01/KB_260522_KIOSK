package Kiosk.command;

import Kiosk.SessionManager;

public class LogoutCommand implements Command {
    @Override
    public void execute() {
        if (!SessionManager.isLoggedIn()) {
            System.out.println("로그인 상태가 아닙니다.");
            return;
        }
        String name = SessionManager.getLoggedInMember().getName();
        SessionManager.logout();
        System.out.println(name + "님, 로그아웃되었습니다.");
    }
}
