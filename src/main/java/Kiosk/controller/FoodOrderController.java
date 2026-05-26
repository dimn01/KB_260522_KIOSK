package Kiosk.controller;

import Kiosk.domain.Category;
import Kiosk.domain.Product;
import Kiosk.domain.CartItem;

import Kiosk.service.FoodOrderService;

import java.util.List;
import java.util.Scanner;

public class FoodOrderController {

    private final FoodOrderService foodOrderService = new FoodOrderService();

    public void clearCart() { foodOrderService.resetCart(); }

    // [Menu4] 음식 주문 메뉴 화면
    public void openFoodOrderMenu(Scanner sc) {
        while (true) {
            System.out.println("\n======================================");
            System.out.println("             [ 음식 주문 ]");
            System.out.println("======================================");

            // Service를 통해 가상 DB에 등록된 모든 카테고리를 동적으로 가져옴
            List<Category> categories = foodOrderService.getAllCategories();
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            System.out.println("0. 메인 메뉴로 돌아가기");
            System.out.println("======================================");
            System.out.print("원하는 카테고리 번호를 입력하세요: ");

            int choice = sc.nextInt();
            if (choice == 0) break;

            if (choice > 0 && choice <= categories.size()) {
                Category selectedCategory = categories.get(choice - 1);
                // 선택된 카테고리의 고유 ID(C1 등)를 넘겨 세부 메뉴판을 띄움
                showCategoryMenu(sc, selectedCategory.getId(), selectedCategory.getName());
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 세부 메뉴판 출력 및 수량 담기
    private void showCategoryMenu(Scanner sc, String categoryId, String categoryName) {
        System.out.println("\n======================================");
        System.out.println("         [ " + categoryName + " 메뉴판 ]");
        System.out.println("======================================");

        // 카테고리 ID(C1)에 소속된 음식들만 매핑 서치
        List<Product> filtered = foodOrderService.getProductsByCategoryId(categoryId);

        if (filtered.isEmpty()) {
            System.out.println("   준비된 상품이 없습니다. (현재 재고 준비중)");
            System.out.println("0. 이전으로");
            System.out.println("======================================");
            sc.nextInt();
            return;
        }

        for (int i = 0; i < filtered.size(); i++) {
            Product p = filtered.get(i);
            // 설명(description) 컬럼 출력 연출 추가
            System.out.print((i + 1) + ". " + p.getName() + " (" + p.getPrice() + "원) - " + p.getDescription() + " | ");
            if (p.getStock() == 0) {
                System.out.println("[품절]");
            } else {
                System.out.println("재고: " + p.getStock() + "개");
            }
        }
        System.out.println("0. 이전으로");
        System.out.println("======================================");
        System.out.print("장바구니에 담을 메뉴의 번호 선택: ");
        int choice = sc.nextInt();

        if (choice == 0) return;

        if (choice > 0 && choice <= filtered.size()) {
            Product selected = filtered.get(choice - 1);
            System.out.print("수량을 입력하세요: ");
            int qty = sc.nextInt();

            try {
                foodOrderService.addFoodToCart(selected.getId(), qty);
                System.out.println("\n장바구니에 정상 등록되었습니다.");
            } catch (IllegalArgumentException e) {
                System.out.println("\n[오류] " + e.getMessage());
            }
        } else {
            System.out.println("잘못된 선택입니다.");
        }
    }

    // [Menu 5] 장바구니 리스트업 및 연산 (타입 안정성 보장 반영)
    public void openCartMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--------------------------------------");
            System.out.println("          [ 장바구니 내역 ]");
            System.out.println("--------------------------------------");

            List<CartItem> currentCart = foodOrderService.getCart();
            if (currentCart.isEmpty()) {
                System.out.println("         장바구니가 비어 있습니다.");
                System.out.println("--------------------------------------");
                System.out.println("0. 메인 메뉴로 돌아가기");
                System.out.println("--------------------------------------");
                System.out.print("번호를 선택하세요: ");
                if (sc.nextInt() == 0) break;
                continue;
            }

            int displayNo = 1;
            for (CartItem item : currentCart) {
                Product p = foodOrderService.getProductById(item.getProductId());
                int itemTotal = p.getPrice() * item.getQuantity();
                System.out.println(displayNo + ". " + p.getName() + " x " + item.getQuantity() + "개 : " + itemTotal + "원");
                displayNo++;
            }

            int totalPrice = foodOrderService.calculateCartTotal();
            System.out.println("--------------------------------------");
            System.out.println(" 총 결제 금액      :  " + totalPrice + "원");
            System.out.println("--------------------------------------");
            System.out.println("1. 최종 주문 및 대면 결제하기");
            System.out.println("2. 상품 수량 변경");
            System.out.println("3. 특정 상품 제거");
            System.out.println("0. 메인 메뉴로 돌아가기");
            System.out.println("======================================");
            System.out.print("원하는 기능의 번호를 입력하세요: ");

            int action = sc.nextInt();
            if (action == 0) break;

            if (action == 1) {
                if (handleCheckout(sc, totalPrice)) break;
            } else if (action == 2) {
                System.out.print("수량을 조정할 항목의 번호 입력: ");
                int editIdx = sc.nextInt();
                System.out.print("새로운 수량 입력: ");
                int newQty = sc.nextInt();
                try {
                    foodOrderService.updateCartItemQty(editIdx - 1, newQty);
                    System.out.println("수량이 수정되었습니다.");
                } catch (IllegalArgumentException e) {
                    System.out.println("\n[오류] " + e.getMessage());
                }
            } else if (action == 3) {
                System.out.print("삭제할 항목의 번호 입력: ");
                int removeIdx = sc.nextInt();
                try {
                    foodOrderService.removeCartItem(removeIdx - 1);
                    System.out.println("장바구니에서 제거되었습니다.");
                } catch (IllegalArgumentException e) {
                    System.out.println("\n[오류] " + e.getMessage());
                }
            }
        }
    }

    private boolean handleCheckout(Scanner sc, int currentViewPrice) {
        try {
            foodOrderService.preCheckoutValidate(currentViewPrice);

            System.out.println("\n======================================");
            System.out.println("         [ 최종 결제 수단 선택 ]");
            System.out.println("======================================");
            System.out.println(" 결제 금액: " + currentViewPrice + "원");
            System.out.println("1. 신용카드 (대면 단말기 결제)");
            System.out.println("2. 카카오페이 (원격 바코드 결제)");
            System.out.println("0. 결제 취소");
            System.out.println("======================================");
            System.out.print("번호 선택: ");
            int payChoice = sc.nextInt();

            if (payChoice == 1 || payChoice == 2) {
                String method = (payChoice == 1) ? "신용카드" : "카카오페이";
                foodOrderService.confirmPayment();
                System.out.println("\n[결제 최종 완료] 성공적으로 " + currentViewPrice + "원이 " + method + " 승인되었습니다.");
                System.out.println("[주문상태: 대기] 데이터가 주방 관제 시스템으로 전송되었습니다.");
                return true;
            }
            System.out.println("\n결제가 취소되었습니다.");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("\n[주문 반려] " + e.getMessage());
            return false;
        }
    }
}