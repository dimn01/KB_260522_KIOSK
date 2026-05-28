package Kiosk.dao;

import Kiosk.domain.Category;
import Kiosk.domain.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFoodDaoImpl implements FoodDao {
    private static final String FILE_PATH = "src/main/resources/foods.json";
    private final Gson gson;
    private Map<String, Food> foodDb = new HashMap<>();
    private List<Category> categoryList = new ArrayList<>();

    public JsonFoodDaoImpl() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadFoods();
    }

    private void loadFoods() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            FoodData data = gson.fromJson(reader, FoodData.class);
            if (data != null) {
                this.categoryList = data.category != null ? data.category : new ArrayList<>();
                if (data.foods != null) {
                    for (Food f : data.foods) {
                        foodDb.put(f.getFoodId(), f);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[JSON DB] 음식 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveFoods() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            FoodData data = new FoodData();
            data.category = this.categoryList;
            data.foods = new ArrayList<>(foodDb.values());
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("[JSON DB] 음식 데이터를 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public List<Food> findAll() {
        return new ArrayList<>(foodDb.values());
    }

    @Override
    public Food findById(String foodId) {
        return foodDb.get(foodId);
    }

    @Override
    public List<Food> findByCategoryId(String categoryId) {
        return foodDb.values().stream()
                .filter(f -> f.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryList;
    }

    @Override
    public void updateFoodStock(String foodId, int quantity) {
        Food food = foodDb.get(foodId);
        if (food != null) {
            food.setStock(food.getStock() + quantity);
            saveFoods();
            System.out.println("[JSON DB] 음식 재고 업데이트 완료: " + foodId + " (변경량: " + quantity + ")");
        }
    }

    private static class FoodData {
        List<Category> category;
        List<Food> foods;
    }
}
