package Kiosk.domain;

public class Product {
    private String id;         // int -> String 변경 (R001 등)
    private String categoryId; // String category명 -> categoryId(C1 등)로 변경
    private String name;
    private int price;
    private int stock;
    private String description; // 새 필드 추가

    public Product(String id, String categoryId, String name, int price, int stock, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public String getId() { return id; }
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    public void setStock(int stock) { this.stock = stock; }
}