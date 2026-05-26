package Kiosk.domain;

import java.time.LocalDateTime;
import java.util.List;

public class LsyOrder {
    private String orderId;
    private String memberId;
    private LocalDateTime orderDate;
    private int totalPrice;
    private List<LsyOrderItem> items;

    public LsyOrder() {}

    public LsyOrder(String orderId, String memberId, LocalDateTime orderDate, int totalPrice, List<LsyOrderItem> items) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public List<LsyOrderItem> getItems() { return items; }
    public void setItems(List<LsyOrderItem> items) { this.items = items; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("주문 번호: ").append(orderId).append("\n");
        sb.append("주문 일시: ").append(orderDate).append("\n");
        sb.append("주문 내역:\n");
        for (LsyOrderItem item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        sb.append("총 합계: ").append(totalPrice).append("원");
        return sb.toString();
    }
}
