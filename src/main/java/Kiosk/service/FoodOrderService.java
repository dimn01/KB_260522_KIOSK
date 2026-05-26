package Kiosk.service;

import Kiosk.domain.Category;
import Kiosk.domain.Product;
import Kiosk.domain.CartItem;

import Kiosk.repository.CategoryRepository;
import Kiosk.repository.ProductRepository;
import Kiosk.repository.CartRepository;

import java.util.List;

public class FoodOrderService {

    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final CartRepository cartRepository = new CartRepository();
    private long lastOrderTime = 0;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<CartItem> getCart() { return cartRepository.findAll(); }
    public Product getProductById(String id) { return productRepository.findById(id); }
    public void resetCart() { cartRepository.clear(); }

    public void addFoodToCart(String productId, int qty) {
        Product product = productRepository.findById(productId);

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
            Product p = productRepository.findById(item.getProductId());
            total += p.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void updateCartItemQty(int index, int newQty) {
        List<CartItem> cart = cartRepository.findAll();
        if (index < 0 || index >= cart.size()) throw new IllegalArgumentException("잘못된 선택.");

        CartItem item = cart.get(index);
        Product p = productRepository.findById(item.getProductId());

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
            Product p = productRepository.findById(item.getProductId());
            dbVerifiedTotalAmount += p.getPrice() * item.getQuantity();
            if (item.getQuantity() > p.getStock()) throw new IllegalArgumentException("재고 부족 반려.");
        }
        if (dbVerifiedTotalAmount != currentViewPrice) throw new IllegalArgumentException("금액 위변조 위배.");
    }

    public void confirmPayment() {
        for (CartItem item : cartRepository.findAll()) {
            Product p = productRepository.findById(item.getProductId());
            if (p != null) p.setStock(p.getStock() - item.getQuantity());
        }
        lastOrderTime = System.currentTimeMillis();
        cartRepository.clear();
    }
}