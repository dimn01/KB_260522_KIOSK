package Kiosk.domain;

import java.time.LocalDateTime;

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

    public Member() {}

    public Member(String memberId, String password, String email, String name, String phone, String address, LocalDateTime signupDate, LocalDateTime lastLoginDate) {
        this(memberId, password, email, name, phone, address, signupDate, lastLoginDate, 0);
    }

    public Member(String memberId, String password, String email, String name, String phone, String address, LocalDateTime signupDate, LocalDateTime lastLoginDate, int remainingTime) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.signupDate = signupDate;
        this.lastLoginDate = lastLoginDate;
        this.remainingTime = remainingTime;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDateTime getSignupDate() { return signupDate; }
    public void setSignupDate(LocalDateTime signupDate) { this.signupDate = signupDate; }
    public LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(LocalDateTime lastLoginDate) { this.lastLoginDate = lastLoginDate; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", remainingTime=" + remainingTime + "분" +
                '}';
    }
}
