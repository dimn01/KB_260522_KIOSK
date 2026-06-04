package Kiosk.dao;

import Kiosk.domain.LsyOrder;
import Kiosk.domain.LsyOrderItem;
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
    private static final String ORDERS_FILE = "src/main/resources/orders.json";
    private static final String DETAILS_FILE = "src/main/resources/order_details.json";
    private List<LsyOrder> orders = new ArrayList<>();
    private final Gson gson;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LsyJsonOrderDaoImpl() {
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
        File ordersFile = new File(ORDERS_FILE);
        File detailsFile = new File(DETAILS_FILE);
        
        if (!ordersFile.exists()) return;

        try (Reader orderReader = new FileReader(ordersFile)) {
            Type orderListType = new TypeToken<List<LsyOrder>>() {}.getType();
            List<LsyOrder> loadedOrders = gson.fromJson(orderReader, orderListType);
            
            if (loadedOrders != null) {
                this.orders = loadedOrders;
                
                // 상세 내역 로드 및 연결 (JOIN 시뮬레이션)
                if (detailsFile.exists()) {
                    try (Reader detailReader = new FileReader(detailsFile)) {
                        Type detailListType = new TypeToken<List<LsyOrderItem>>() {}.getType();
                        List<LsyOrderItem> allItems = gson.fromJson(detailReader, detailListType);
                        
                        if (allItems != null) {
                            for (LsyOrder order : orders) {
                                List<LsyOrderItem> orderItems = allItems.stream()
                                        .filter(item -> item.getOrderId() == order.getOrderId())
                                        .collect(Collectors.toList());
                                order.setItems(orderItems);
                                
                                // 총액 계산 (DB에 totalPrice가 없으므로 로드 시 계산)
                                int total = orderItems.stream()
                                        .mapToInt(item -> item.getPrice() * item.getQuantity())
                                        .sum();
                                order.setTotalPrice(total);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[LSY JSON] 주문 데이터를 읽어오는 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveOrders() {
        try (Writer orderWriter = new FileWriter(ORDERS_FILE);
             Writer detailWriter = new FileWriter(DETAILS_FILE)) {
            
            // 1. ORDERS 테이블 저장 (items 제외하고 저장하기 위해 별도 처리하거나, 
            // Transient 필드를 사용해야 하지만 여기서는 단순화를 위해 리스트를 가공)
            List<LsyOrder> ordersToSave = orders.stream().map(o -> {
                LsyOrder copy = new LsyOrder(o.getOrderId(), o.getMemberId(), o.getOrderDate(), 0, null);
                return copy;
            }).collect(Collectors.toList());
            gson.toJson(ordersToSave, orderWriter);

            // 2. ORDER_DETAILS 테이블 저장
            List<LsyOrderItem> allItems = orders.stream()
                    .filter(o -> o.getItems() != null)
                    .flatMap(o -> o.getItems().stream())
                    .collect(Collectors.toList());
            gson.toJson(allItems, detailWriter);
            
        } catch (IOException e) {
            System.err.println("[LSY JSON] 주문 데이터를 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void save(LsyOrder order) {
        // ID 생성 (AUTO_INCREMENT 시뮬레이션)
        int maxOrderId = orders.stream()
                .mapToInt(LsyOrder::getOrderId)
                .max()
                .orElse(0);
        order.setOrderId(maxOrderId + 1);

        // 상세 아이템 ID 생성 및 연결
        int detailIdStart = 1;
        // 기존 모든 아이템 중 최대 ID 찾기
        int maxDetailId = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToInt(LsyOrderItem::getOrderDetailId)
                .max()
                .orElse(0);
        
        if (order.getItems() != null) {
            for (LsyOrderItem item : order.getItems()) {
                item.setOrderId(order.getOrderId());
                item.setOrderDetailId(++maxDetailId);
            }
        }

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
