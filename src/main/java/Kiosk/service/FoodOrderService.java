package Kiosk.service;

import Kiosk.SessionManager;
import Kiosk.dao.FoodDao;
import Kiosk.dao.MysqlFoodDaoImpl;
import Kiosk.dao.CartDao;
import Kiosk.dao.JsonCartDaoImpl;
import Kiosk.domain.Category;
import Kiosk.domain.Food;
import Kiosk.domain.CartItem;

import java.util.List;

public class FoodOrderService {

    private final FoodDao foodDao = new MysqlFoodDaoImpl();
    private final CartDao cartDao = new JsonCartDaoImpl();
    private long lastOrderTime = 0;

    public List<Category> getAllCategories() {
        return foodDao.findAllCategories();
    }

    public List<Food> getProductsByCategoryId(String categoryId) {
        return foodDao.findByCategoryId(categoryId);
    }

    public List<CartItem> getCart() {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        return cartDao.findAllByMemberId(memberId);
    }

    public Food getProductById(String id) { return foodDao.findById(id); }

    public void resetCart() {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        cartDao.deleteByMemberId(memberId);
    }

    public void addFoodToCart(String productId, int qty) {
        Food product = foodDao.findById(productId);
        String memberId = SessionManager.getLoggedInMember().getMemberId();

        if (product.getStock() == 0) {
            throw new IllegalArgumentException("품절된 상품입니다.");
        }
        if (qty > product.getStock()) {
            throw new IllegalArgumentException("잔여 재고를 초과합니다.");
        }

        CartItem existingItem = cartDao.findByMemberIdAndFoodId(memberId, productId);
        if (existingItem != null) {
            if (existingItem.getQuantity() + qty > product.getStock()) {
                throw new IllegalArgumentException("장바구니 합계가 재고를 초과합니다.");
            }
            cartDao.updateQuantity(existingItem.getCartId(), existingItem.getQuantity() + qty);
        } else {
            cartDao.save(new CartItem(null, memberId, productId, qty));
        }
    }

    public int calculateCartTotal() {
        int total = 0;
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        for (CartItem item : cartDao.findAllByMemberId(memberId)) {
            Food p = foodDao.findById(item.getFoodId());
            total += p.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void updateCartItemQty(int index, int newQty) {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        List<CartItem> cart = cartDao.findAllByMemberId(memberId);
        if (index < 0 || index >= cart.size()) throw new IllegalArgumentException("잘못된 선택.");

        CartItem item = cart.get(index);
        Food p = foodDao.findById(item.getFoodId());

        if (newQty > p.getStock()) throw new IllegalArgumentException("재고 초과.");
        cartDao.updateQuantity(item.getCartId(), newQty);
    }

    public void removeCartItem(int index) {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        List<CartItem> cart = cartDao.findAllByMemberId(memberId);
        if (index < 0 || index >= cart.size()) throw new IllegalArgumentException("잘못된 선택.");
        cartDao.deleteByCartId(cart.get(index).getCartId());
    }

    public void preCheckoutValidate(int currentViewPrice) {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        List<CartItem> cart = cartDao.findAllByMemberId(memberId);
        if (cart.isEmpty()) throw new IllegalArgumentException("장바구니가 비어있음.");

        long currentOrderTime = System.currentTimeMillis();
        if (currentOrderTime - lastOrderTime < 5000) throw new IllegalArgumentException("중복 주문 제한.");

        int dbVerifiedTotalAmount = 0;
        for (CartItem item : cart) {
            Food p = foodDao.findById(item.getFoodId());
            dbVerifiedTotalAmount += p.getPrice() * item.getQuantity();
            if (item.getQuantity() > p.getStock()) throw new IllegalArgumentException("재고 부족 반려.");
        }
        if (dbVerifiedTotalAmount != currentViewPrice) throw new IllegalArgumentException("금액 위변조 위배.");
    }

    public void confirmPayment() {
        String memberId = SessionManager.getLoggedInMember().getMemberId();
        List<CartItem> cart = cartDao.findAllByMemberId(memberId);
        for (CartItem item : cart) {
            Food p = foodDao.findById(item.getFoodId());
            if (p != null) {
                foodDao.updateFoodStock(p.getFoodId(), -item.getQuantity());
            }
        }
        lastOrderTime = System.currentTimeMillis();
        cartDao.deleteByMemberId(memberId);
    }
}
