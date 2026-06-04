package Kiosk.dao;

import Kiosk.database.JDBCUtil; 
import Kiosk.domain.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCartDao implements CartDao {
    private static final String SQL_CART_SELECT_BY_MEMBER  = "SELECT * FROM CART WHERE memberId = ?";
    private static final String SQL_CART_SELECT_EXIST      = "SELECT * FROM CART WHERE memberId = ? AND foodId = ?";
    private static final String SQL_CART_INSERT            = "INSERT INTO CART (memberId, foodId, quantity) VALUES (?, ?, ?)";
    private static final String SQL_CART_UPDATE_QUANTITY   = "UPDATE CART SET quantity = ? WHERE cartId = ?";
    private static final String SQL_CART_DELETE_BY_MEMBER  = "DELETE FROM CART WHERE memberId = ?";
    private static final String SQL_CART_DELETE_BY_ID      = "DELETE FROM CART WHERE cartId = ?";

    @Override
    public List<CartItem> findAllByMemberId(String memberId) {
        List<CartItem> cartList = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_SELECT_BY_MEMBER)) {
            ps.setString(1, memberId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem(
                        String.valueOf(rs.getInt("cartId")),
                        rs.getString("memberId"),
                        rs.getString("foodId"),
                        rs.getInt("quantity")
                    );
                    cartList.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("장바구니 전체 조회 중 에러 발생: " + e.getMessage());
        }
        return cartList;
    }

    @Override
    public CartItem findByMemberIdAndFoodId(String memberId, String foodId) {
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_SELECT_EXIST)) {
            ps.setString(1, memberId);
            ps.setString(2, foodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CartItem(
                        String.valueOf(rs.getInt("cartId")),
                        rs.getString("memberId"),
                        rs.getString("foodId"),
                        rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("특정 장바구니 품목 조회 중 에러 발생: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void save(CartItem cartItem) {
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_INSERT)) {
            ps.setString(1, cartItem.getMemberId());
            ps.setString(2, cartItem.getFoodId());
            ps.setInt(3, cartItem.getQuantity());

            ps.executeUpdate();
            System.out.println("장바구니 추가 완료");
        } catch (SQLException e) {
            System.err.println("장바구니 저장 중 에러 발생: " + e.getMessage());
        }
    }

    @Override
    public void updateQuantity(String cartId, int quantity) {
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_UPDATE_QUANTITY)) {
            ps.setInt(1, quantity);
            ps.setInt(2, Integer.parseInt(cartId));

            ps.executeUpdate();
            System.out.println("장바구니 수량 수정 완료");
        } catch (SQLException e) {
            System.err.println("장바구니 수량 수정 중 에러 발생: " + e.getMessage());
        }
    }

    @Override
    public void deleteByMemberId(String memberId) {
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_DELETE_BY_MEMBER)) {
            ps.setString(1, memberId);

            ps.executeUpdate();
            System.out.println("해당 유저의 장바구니 전체 삭제 완료: " + memberId);
        } catch (SQLException e) {
            System.err.println("유저별 장바구니 삭제 중 에러 발생: " + e.getMessage());
        }
    }

    @Override
    public void deleteByCartId(String cartId) {
        Connection conn = JDBCUtil.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_CART_DELETE_BY_ID)) {
            ps.setInt(1, Integer.parseInt(cartId));

            ps.executeUpdate();
            System.out.println("장바구니 품목 개별 삭제 완료: " + cartId);
        } catch (SQLException e) {
            System.err.println("장바구니 품목 개별 삭제 중 에러 발생: " + e.getMessage());
        }
    }
}