package Kiosk.dao;

import Kiosk.database.JDBCUtil;
import Kiosk.domain.LsyOrder;
import Kiosk.domain.LsyOrderItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcOrderDaoImpl implements LsyOrderDao {

    @Override
    public void save(LsyOrder order) {
        String insertOrderSql = "INSERT INTO ORDERS (memberId, orderDate) VALUES (?, ?)";
        String insertDetailSql = "INSERT INTO ORDER_DETAILS (orderId, foodId, quantity, price) VALUES (?, ?, ?, ?)";
        String deleteCartSql = "DELETE FROM CART WHERE memberId = ?";
        
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) return;

        try {
            // 트랜잭션 시작 (자동 커밋 해제)
            conn.setAutoCommit(false);

            // 1. ORDERS 테이블 저장
            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, order.getMemberId());
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.executeUpdate();

                // 생성된 orderId 가져오기ㄴ
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);
                        order.setOrderId(orderId);

                        // 2. ORDER_DETAILS 테이블 저장
                        try (PreparedStatement detailPstmt = conn.prepareStatement(insertDetailSql)) {
                            for (LsyOrderItem item : order.getItems()) {
                                detailPstmt.setInt(1, orderId);
                                detailPstmt.setString(2, item.getFoodId());
                                detailPstmt.setInt(3, item.getQuantity());
                                detailPstmt.setInt(4, item.getPrice());
                                detailPstmt.addBatch();
                            }
                            detailPstmt.executeBatch();
                        }
                    }
                }
            }

            // 3. CART 테이블 비우기 (주문 확정 시 해당 사용자의 카트 삭제)
            try (PreparedStatement delPstmt = conn.prepareStatement(deleteCartSql)) {
                delPstmt.setString(1, order.getMemberId());
                delPstmt.executeUpdate();
            }

            // 모든 작업 성공 시 커밋
            conn.commit();
            System.out.println("[MySQL DB] 주문 저장 및 카트 삭제 완료: " + order.getOrderId());
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.err.println("[MySQL DB] 주문 저장 중 오류 발생, 롤백됨: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<LsyOrder> findByMemberId(String memberId) {
        List<LsyOrder> orders = new ArrayList<>();
        // ORDERS와 ORDER_DETAILS, FOODS를 조인하여 주문 내역 및 메뉴 이름 조회
        String sql = "SELECT o.orderId, o.memberId, o.orderDate, " +
                     "od.orderDetailId, od.foodId, od.quantity, od.price, f.name " +
                     "FROM ORDERS o " +
                     "JOIN ORDER_DETAILS od ON o.orderId = od.orderId " +
                     "JOIN FOODS f ON od.foodId = f.foodId " +
                     "WHERE o.memberId = ? " +
                     "ORDER BY o.orderDate DESC";

        Connection conn = JDBCUtil.getConnection();
        if (conn == null) return orders;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                LsyOrder currentOrder = null;
                int lastOrderId = -1;

                while (rs.next()) {
                    int orderId = rs.getInt("orderId");
                    
                    if (orderId != lastOrderId) {
                        currentOrder = new LsyOrder();
                        currentOrder.setOrderId(orderId);
                        currentOrder.setMemberId(rs.getString("memberId"));
                        currentOrder.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
                        currentOrder.setItems(new ArrayList<>());
                        currentOrder.setTotalPrice(0);
                        orders.add(currentOrder);
                        lastOrderId = orderId;
                    }

                    LsyOrderItem item = new LsyOrderItem(
                        rs.getInt("orderDetailId"),
                        orderId,
                        rs.getString("foodId"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("price")
                    );
                    
                    currentOrder.getItems().add(item);
                    currentOrder.setTotalPrice(currentOrder.getTotalPrice() + (item.getPrice() * item.getQuantity()));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MySQL DB] 주문 내역 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }
}
