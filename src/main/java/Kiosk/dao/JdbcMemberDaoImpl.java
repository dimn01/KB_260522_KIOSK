package Kiosk.dao;

import Kiosk.database.JDBCUtil;
import Kiosk.domain.Member;

import java.sql.*;
import java.time.LocalDateTime;

public class JdbcMemberDaoImpl implements MemberDao {
    Connection conn = JDBCUtil.getConnection();

    @Override
    public void insertMember(Member member) {
        String sql = "INSERT INTO MEMBERS (memberId, password, email, name, phone, address, signupDate, lastLoginDate, remainingTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getName());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress());

            if (member.getSignupDate() != null) {
                pstmt.setTimestamp(7, Timestamp.valueOf(member.getSignupDate()));
            } else {
                pstmt.setNull(7, java.sql.Types.TIMESTAMP);
            }

            if (member.getLastLoginDate() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(member.getLastLoginDate()));
            } else {
                pstmt.setNull(8, java.sql.Types.TIMESTAMP);
            }

            pstmt.setInt(9, member.getRemainingTime());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Member 객체로 매핑하는 함수
    private Member map(ResultSet rs) throws SQLException {
        return Member.builder()
                .memberId(rs.getString("memberId"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                // DATETIME 타입 처리 (Member의 필드가 LocalDateTime인 경우)
                .signupDate(rs.getTimestamp("signupDate") != null ? rs.getTimestamp("signupDate").toLocalDateTime() : null)
                .lastLoginDate(rs.getTimestamp("lastLoginDate") != null ? rs.getTimestamp("lastLoginDate").toLocalDateTime() : null)
                .remainingTime(rs.getInt("remainingTime"))
                .build();
    }

    @Override
    public Member getMemberById(String memberId) {
        String sql = "SELECT * FROM MEMBERS WHERE memberId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void updateLastLoginDate(String memberId) {
        String sql = "UPDATE MEMBERS SET lastLoginDate = ? WHERE memberId = ?";

        // 💡 수정됨
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRemainingTime(String memberId, int additionalTime) {
        String sql = "UPDATE MEMBERS SET remainingTime = remainingTime + ? WHERE memberId = ?";

        // 💡 수정됨
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, additionalTime);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
