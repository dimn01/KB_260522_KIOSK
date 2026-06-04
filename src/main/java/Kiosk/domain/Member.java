package Kiosk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String memberId;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String address;
    private LocalDateTime signupDate;
    private LocalDateTime lastLoginDate;
    private int remainingTime; // 잔여 시간 (분 단위)

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", remainingTime=" + remainingTime + "분" +
                '}';
    }
}
