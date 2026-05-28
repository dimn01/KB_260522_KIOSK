package Kiosk.domain;

public class LsyOrderItem {
    private int orderDetailId;
    private int orderId;
    private String foodId;
    private String name;
    private int quantity;
    private int price;

    public LsyOrderItem() {}

    public LsyOrderItem(int orderDetailId, int orderId, String foodId, String name, int quantity, int price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    @Override
    public String toString() {
        return "[" + foodId + "] " + name + " x" + quantity + " (" + (price * quantity) + "원)";
    }
}
