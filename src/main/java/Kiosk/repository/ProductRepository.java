package Kiosk.repository;

import Kiosk.domain.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRepository {
    // 팀원 JSON의 foods 데이터 정밀 이식
    private static final List<Product> database = new ArrayList<>(Arrays.asList(
        new Product("R001", "C1", "신라면", 4000, 100, "기본 신라면"),
        new Product("R002", "C1", "진라면(매운맛)", 4000, 100, "칼칼한 진라면"),
        new Product("R003", "C1", "진라면(순한맛)", 4000, 100, "순한 진라면"),
        new Product("R004", "C1", "틈새라면", 4500, 80, "매운맛 끝판왕"),
        new Product("R005", "C1", "짜파게티", 4500, 80, "국물 없는 짜장라면"),
        new Product("R006", "C1", "너구리", 4500, 80, "통통한 면발"),
        new Product("R007", "C1", "참깨라면", 4000, 80, "고소한 참깨 계란"),
        new Product("R008", "C1", "안성탕면", 4000, 80, "구수한 국물"),
        new Product("R009", "C1", "불닭볶음면", 4500, 90, "매운 볶음면"),
        new Product("R010", "C1", "까르보불닭볶음면", 5000, 80, "매운 까르보나라")
    ));

    // categoryId로 필터링하도록 쿼리 로직 변경
    public List<Product> findByCategoryId(String categoryId) {
        List<Product> result = new ArrayList<>();
        for (Product p : database) {
            if (p.getCategoryId().equals(categoryId)) result.add(p);
        }
        return result;
    }

    public Product findById(String id) {
        for (Product p : database) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}