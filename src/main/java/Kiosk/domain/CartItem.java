package Kiosk.domain;

public class CartItem {
    private String cartId;
    private String memberId;
    private String foodId;
    private int quantity;

    public CartItem(String cartId, String memberId, String foodId, int quantity) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // 기존 호환성을 위한 Getter (Service 등에서 사용 중일 수 있음)
    public String getProductId() { return foodId; }
}
