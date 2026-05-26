package Kiosk.repository;

import Kiosk.domain.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static final List<CartItem> cartDatabase = new ArrayList<>();

    public List<CartItem> findAll() { return cartDatabase; }

    // int -> String 타입 서치 구조 변경
    public CartItem findByProductId(String productId) {
        for (CartItem item : cartDatabase) {
            if (item.getProductId().equals(productId)) return item;
        }
        return null;
    }

    public void save(CartItem item) { cartDatabase.add(item); }
    public void deleteByIndex(int index) { cartDatabase.remove(index); }
    public void clear() { cartDatabase.clear(); }
}