package Kiosk.dao;

import Kiosk.domain.Member;


public interface MemberDao {
    // 회원가입
    void insertMember(Member member);

    // ID 가져오기
    Member getMemberById(String memberId);

    // 로그인 시간 업데이트
    void updateLastLoginDate(String memberId);
}
