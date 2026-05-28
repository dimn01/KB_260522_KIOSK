package Kiosk.dao;

import Kiosk.domain.CartItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonCartDaoImpl implements CartDao {
    private static final String FILE_PATH = "src/main/resources/carts.json";
    private final Gson gson;
    private List<CartItem> cartItems = new ArrayList<>();

    public JsonCartDaoImpl() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadCarts();
    }

    private void loadCarts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<CartItem>>() {}.getType();
            List<CartItem> loadedItems = gson.fromJson(reader, listType);
            if (loadedItems != null) {
                this.cartItems = loadedItems;
            }
        } catch (JsonSyntaxException e) {
            System.err.println("[JSON CART] JSON 형식이 올바르지 않아 초기화합니다. (Expected Array)");
            this.cartItems = new ArrayList<>();
            saveCarts(); // 잘못된 파일 구조를 []로 덮어씌움
        } catch (IOException e) {
            System.err.println("[JSON CART] 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveCarts() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(cartItems, writer);
        } catch (IOException e) {
            System.err.println("[JSON CART] 데이터를 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public List<CartItem> findAllByMemberId(String memberId) {
        return cartItems.stream()
                .filter(item -> item.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public CartItem findByMemberIdAndFoodId(String memberId, String foodId) {
        return cartItems.stream()
                .filter(item -> item.getMemberId().equals(memberId) && item.getFoodId().equals(foodId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(CartItem cartItem) {
        // cartId 자동 생성 (간단히 현재시간 기반)
        if (cartItem.getCartId() == null) {
            cartItem.setCartId("CART_" + System.currentTimeMillis());
        }
        cartItems.add(cartItem);
        saveCarts();
    }

    @Override
    public void updateQuantity(String cartId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getCartId().equals(cartId)) {
                item.setQuantity(quantity);
                break;
            }
        }
        saveCarts();
    }

    @Override
    public void deleteByMemberId(String memberId) {
        cartItems.removeIf(item -> item.getMemberId().equals(memberId));
        saveCarts();
    }

    @Override
    public void deleteByCartId(String cartId) {
        cartItems.removeIf(item -> item.getCartId().equals(cartId));
        saveCarts();
    }
}
