//package Kiosk.repository;
//
//import Kiosk.domain.Food;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class ProductRepository {
//    // 팀원 JSON의 foods 데이터 정밀 이식
//    private static final List<Food> database = new ArrayList<>(Arrays.asList(
//        new Food("R001", "C1", "신라면", 4000, 100, "기본 신라면"),
//        new Food("R002", "C1", "진라면(매운맛)", 4000, 100, "칼칼한 진라면"),
//        new Food("R003", "C1", "진라면(순한맛)", 4000, 100, "순한 진라면"),
//        new Food("R004", "C1", "틈새라면", 4500, 80, "매운맛 끝판왕"),
//        new Food("R005", "C1", "짜파게티", 4500, 80, "국물 없는 짜장라면"),
//        new Food("R006", "C1", "너구리", 4500, 80, "통통한 면발"),
//        new Food("R007", "C1", "참깨라면", 4000, 80, "고소한 참깨 계란"),
//        new Food("R008", "C1", "안성탕면", 4000, 80, "구수한 국물"),
//        new Food("R009", "C1", "불닭볶음면", 4500, 90, "매운 볶음면"),
//        new Food("R010", "C1", "까르보불닭볶음면", 5000, 80, "매운 까르보나라")
//    ));
//
//    // categoryId로 필터링하도록 쿼리 로직 변경
//    public List<Food> findByCategoryId(String categoryId) {
//        List<Food> result = new ArrayList<>();
//        for (Food p : database) {
//            if (p.getCategoryId().equals(categoryId)) result.add(p);
//        }
//        return result;
//    }
//
//    public Food findById(String id) {
//        for (Food p : database) {
//            if (p.getFoodId().equals(id)) return p;
//        }
//        return null;
//    }
//}