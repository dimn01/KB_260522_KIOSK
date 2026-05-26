package Kiosk.dao;

import Kiosk.domain.Member;

// DAO(Data Access Object) 데이터를 어떻게 저장하고, 꺼내올지 정하는 역할

public interface MemberDao {
    // 회원가입
    void insertMember(Member member);

    // ID 가져오기
    Member getMemberById(String memberId);

    // 로그인 시간 업데이트
    void updateLastLoginDate(String memberId);
}
