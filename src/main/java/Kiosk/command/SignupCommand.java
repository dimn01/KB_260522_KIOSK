package Kiosk.command;

import Kiosk.dao.MemberDao;
import Kiosk.domain.Member;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SignupCommand implements Command {
    private final MemberDao memberDao;
    private final Scanner sc;

    public SignupCommand(MemberDao memberDao, Scanner sc) {
        this.memberDao = memberDao;
        this.sc = sc;
    }

    @Override
    public void execute() {
        System.out.println("\n[회원가입]");
        System.out.print("아이디: ");
        String id = sc.nextLine();

        if (memberDao.getMemberById(id) != null) {
            System.out.println("이미 존재하는 아이디입니다.");
            return;
        }

        System.out.print("비밀번호: ");
        String pw = sc.nextLine();
        System.out.print("이메일: ");
        String email = sc.nextLine();
        System.out.print("이름: ");
        String name = sc.nextLine();
        System.out.print("전화번호: ");
        String phone = sc.nextLine();
        System.out.print("주소: ");
        String address = sc.nextLine();

        Member newMember = new Member(id, pw, email, name, phone, address, LocalDateTime.now(), null, 0);
        memberDao.insertMember(newMember);
        System.out.println("회원가입이 완료되었습니다!");
    }
}
