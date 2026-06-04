package Kiosk;

import Kiosk.domain.Member;

//로그인 상태 구현을 위한 세션 함수
public class SessionManager {
    private static Member loggedInMember;

    public static void login(Member member) {
        loggedInMember = member;
    }

    public static void logout() {
        loggedInMember = null;
    }

    public static Member getLoggedInMember() {
        return loggedInMember;
    }

    public static boolean isLoggedIn() {
        return loggedInMember != null;
    }
}
