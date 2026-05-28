package Kiosk.domain;

public class Food {
    private String foodId;         // int -> String 변경 (R001 등)
    private String categoryId; // String category명 -> categoryId(C1 등)로 변경
    private String name;
    private int price;
    private int stock;
    private String description; // 새 필드 추가

    public Food(String foodId, String categoryId, String name, int price, int stock, String description) {
        this.foodId = foodId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Food() {}

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Food{" +
                "foodId='" + foodId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}