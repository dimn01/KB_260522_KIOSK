package Kiosk.domain;

public class Category {
    private String categoryId;
    private String categoryName;

    public Category() {}

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getId() { return categoryId; }
    public void setId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
