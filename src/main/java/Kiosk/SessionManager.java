package Kiosk;

import Kiosk.domain.Member;

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
