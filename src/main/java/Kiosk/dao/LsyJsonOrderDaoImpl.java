package Kiosk.dao;

import Kiosk.domain.LsyOrder;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LsyJsonOrderDaoImpl implements LsyOrderDao {
    private static final String FILE_PATH = "src/main/resources/orders.json";
    private List<LsyOrder> orders = new ArrayList<>();
    private final Gson gson;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LsyJsonOrderDaoImpl() {
        // LocalDateTime 처리를 위한 Gson 설정
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(formatter)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(), formatter))
                .setPrettyPrinting()
                .create();
        loadOrders();
    }

    private void loadOrders() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<LsyOrder>>() {}.getType();
            List<LsyOrder> loadedOrders = gson.fromJson(reader, listType);
            if (loadedOrders != null) {
                this.orders = loadedOrders;
            }
        } catch (IOException e) {
            System.err.println("[LSY JSON] 주문 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveOrders() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(orders, writer);
        } catch (IOException e) {
            System.err.println("[LSY JSON] 주문 데이터를 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(LsyOrder order) {
        orders.add(order);
        saveOrders();
    }

    @Override
    public List<LsyOrder> findByMemberId(String memberId) {
        // 파일에서 실시간으로 필터링해서 반환
        return orders.stream()
                .filter(o -> o.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }
}
