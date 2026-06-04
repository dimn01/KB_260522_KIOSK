//package Kiosk.repository;
//
//import Kiosk.domain.Category;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class CategoryRepository {
//    // 팀원 JSON의 categories 데이터 이식
//    private static final List<Category> categories = new ArrayList<>(Arrays.asList(
//        new Category("C1", "라면류"),
//        new Category("C2", "덮밥/볶음밥류"),
//        new Category("C3", "튀김/분식류"),
//        new Category("C4", "버거/핫도그류"),
//        new Category("C5", "카페/음료류"),
//        new Category("C6", "스낵/과자류"),
//        new Category("C7", "디저트/베이커리"),
//        new Category("C8", "세트메뉴")
//    ));
//
//    public List<Category> findAll() {
//        return categories;
//    }
//
//    public Category findById(String id) {
//        for (Category c : categories) {
//            if (c.getId().equals(id)) return c;
//        }
//        return null;
//    }
//}