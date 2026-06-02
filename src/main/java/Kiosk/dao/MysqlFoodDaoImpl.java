package Kiosk.dao;

import Kiosk.database.JDBCUtil;
import Kiosk.domain.Category;
import Kiosk.domain.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlFoodDaoImpl implements FoodDao {

    @Override
    public List<Food> findAll() {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT foodId, categoryId, name, price, stock, description FROM FOODS";
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            System.err.println("[MySQL DB 오류] Connection 객체가 null입니다.");
            return list;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Food(
                        rs.getString("foodId"),
                        rs.getString("categoryId"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[MySQL DB 오류] findAll 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Food findById(String foodId) {
        String sql = "SELECT foodId, categoryId, name, price, stock, description FROM FOODS WHERE foodId = ?";
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            System.err.println("[MySQL DB 오류] Connection 객체가 null입니다.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, foodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Food(
                            rs.getString("foodId"),
                            rs.getString("categoryId"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[MySQL DB 오류] findById 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Food> findByCategoryId(String categoryId) {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT foodId, categoryId, name, price, stock, description FROM FOODS WHERE categoryId = ?";
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            System.err.println("[MySQL DB 오류] Connection 객체가 null입니다.");
            return list;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Food(
                            rs.getString("foodId"),
                            rs.getString("categoryId"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MySQL DB 오류] findByCategoryId 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Category> findAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT categoryId, categoryName FROM CATEGORY";
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            System.err.println("[MySQL DB 오류] Connection 객체가 null입니다.");
            return list;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getString("categoryId"),
                        rs.getString("categoryName")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[MySQL DB 오류] findAllCategories 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateFoodStock(String foodId, int quantity) {
        String sql = "UPDATE FOODS SET stock = stock + ? WHERE foodId = ?";
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            System.err.println("[MySQL DB 오류] Connection 객체가 null입니다.");
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, foodId);
            pstmt.executeUpdate();
            System.out.println("[MySQL DB] 음식 재고 업데이트 완료: " + foodId + " (변경량: " + quantity + ")");
        } catch (SQLException e) {
            System.err.println("[MySQL DB 오류] updateFoodStock 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
