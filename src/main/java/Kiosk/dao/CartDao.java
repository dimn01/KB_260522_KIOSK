package Kiosk.dao;

import Kiosk.domain.CartItem;
import java.util.List;

public interface CartDao {
    List<CartItem> findAllByMemberId(String memberId);
    CartItem findByMemberIdAndFoodId(String memberId, String foodId);
    void save(CartItem cartItem);
    void updateQuantity(String cartId, int quantity);
    void deleteByMemberId(String memberId);
    void deleteByCartId(String cartId);
}
