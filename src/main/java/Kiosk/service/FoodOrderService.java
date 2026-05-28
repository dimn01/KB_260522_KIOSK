package Kiosk.service;

import Kiosk.dao.FoodDao;
import Kiosk.dao.JsonFoodDaoImpl;
import Kiosk.domain.Category;
import Kiosk.domain.Food;
import Kiosk.domain.CartItem;

import Kiosk.repository.CartRepository;

import java.util.List;

public class FoodOrderService {

    private final FoodDao foodDao = new JsonFoodDaoImpl();
    private final CartRepository cartRepository = new CartRepository();
    private long lastOrderTime = 0;

    public List<Category> getAllCategories() {
        return foodDao.findAllCategories();
    }

    public List<Food> getProductsByCategoryId(String categoryId) {
        return foodDao.findByCategoryId(categoryId);
    }

    public List<CartItem> getCart() { return cartRepository.findAll(); }
    public Food getProductById(String id) { return foodDao.findById(id); }
    public void resetCart() { cartRepository.clear(); }

    public void addFoodToCart(String productId, int qty) {
        Food product = foodDao.findById(productId);

        if (product.getStock() == 0) {
            throw new IllegalArgumentException("품절된 상품입니다.");
        }
        if (qty > product.getStock()) {
            throw new IllegalArgumentException("잔여 재고를 초과합니다.");
        }

        CartItem existingItem = cartRepository.findByProductId(productId);
        if (existingItem != null) {
            if (existingItem.getQuantity() + qty > product.getStock()) {
                throw new IllegalArgumentException("장바구니 합계가 재고를 초과합니다.");
            }
            existingItem.setQuantity(existingItem.getQuantity() + qty);
        } else {
            cartRepository.save(new CartItem(productId, qty));
        }
    }

    public int calculateCartTotal() {
        int total = 0;
        for (CartItem item : cartRepository.findAll()) {
            Food p = foodDao.findById(item.getProductId());
            total += p.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void updateCartItemQty(int index, int newQty) {
        List<CartItem> cart = cartRepository.findAll();
        if (index < 0 || index >= cart.size()) throw new IllegalArgumentException("잘못된 선택.");

        CartItem item = cart.get(index);
        Food p = foodDao.findById(item.getProductId());

        if (newQty > p.getStock()) throw new IllegalArgumentException("재고 초과.");
        item.setQuantity(newQty);
    }

    public void removeCartItem(int index) {
        List<CartItem> cart = cartRepository.findAll();
        if (index < 0 || index >= cart.size()) throw new IllegalArgumentException("잘못된 선택.");
        cartRepository.deleteByIndex(index);
    }

    public void preCheckoutValidate(int currentViewPrice) {
        List<CartItem> cart = cartRepository.findAll();
        if (cart.isEmpty()) throw new IllegalArgumentException("장바구니가 비어있음.");

        long currentOrderTime = System.currentTimeMillis();
        if (currentOrderTime - lastOrderTime < 5000) throw new IllegalArgumentException("중복 주문 제한.");

        int dbVerifiedTotalAmount = 0;
        for (CartItem item : cart) {
            Food p = foodDao.findById(item.getProductId());
            dbVerifiedTotalAmount += p.getPrice() * item.getQuantity();
            if (item.getQuantity() > p.getStock()) throw new IllegalArgumentException("재고 부족 반려.");
        }
        if (dbVerifiedTotalAmount != currentViewPrice) throw new IllegalArgumentException("금액 위변조 위배.");
    }

    public void confirmPayment() {
        for (CartItem item : cartRepository.findAll()) {
            Food p = foodDao.findById(item.getProductId());
            if (p != null) {
                foodDao.updateFoodStock(p.getFoodId(), -item.getQuantity());
            }
        }
        lastOrderTime = System.currentTimeMillis();
        cartRepository.clear();
    }
}
