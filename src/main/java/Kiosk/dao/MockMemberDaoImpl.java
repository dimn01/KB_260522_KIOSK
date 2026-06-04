package Kiosk.dao;

import Kiosk.domain.Member;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Member 정보.
public class MockMemberDaoImpl implements MemberDao {
    // 키: 아이디 , value:  회원정보
    private static final Map<String, Member> db = new HashMap<>();

    // 회원가입
    @Override
    public void insertMember(Member member) {
        db.put(member.getMemberId(), member);
        System.out.println("[임시 MockDB] 회원 등록 완료: " + member.getMemberId());
    }


    @Override
    public Member getMemberById(String memberId) {
        return db.get(memberId);
    }

    @Override
    public void updateLastLoginDate(String memberId) {
        Member member = db.get(memberId);
        if (member != null) {
            member.setLastLoginDate(LocalDateTime.now());
            System.out.println("[임시 MockDB] 접속 일시 업데이트 완료: " + memberId);
        }
    }

    @Override
    public void updateRemainingTime(String memberId, int additionalTime) {
        Member member = db.get(memberId);
        if (member != null) {
            member.setRemainingTime(member.getRemainingTime() + additionalTime);
            System.out.println("[임시 MockDB] 잔여 시간 업데이트 완료: " + memberId + " (+ " + additionalTime + "분)");
        }
    }
}
