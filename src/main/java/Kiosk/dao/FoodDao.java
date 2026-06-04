package Kiosk.dao;

import Kiosk.domain.Category;
import Kiosk.domain.Food;

import java.util.List;

public interface FoodDao {
    List<Food> findAll();
    Food findById(String foodId);
    List<Food> findByCategoryId(String categoryId);
    List<Category> findAllCategories();
    void updateFoodStock(String foodId, int quantity);
}
