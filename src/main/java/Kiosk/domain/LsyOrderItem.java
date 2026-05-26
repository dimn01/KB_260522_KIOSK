package Kiosk.domain;

public class LsyOrderItem {
    private String foodId; // DB 연결을 위해 foodId 추가
    private String name;
    private int quantity;
    private int price;

    public LsyOrderItem() {}

    public LsyOrderItem(String foodId, String name, int quantity, int price) {
        this.foodId = foodId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

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
