package Kiosk.command;

import Kiosk.controller.FoodOrderController;
import java.util.Scanner;

public class CartCommand implements Command {

    private final FoodOrderController foodOrderController;
    private final Scanner sc;

    public CartCommand(FoodOrderController foodOrderController, Scanner sc) {
        this.foodOrderController = foodOrderController;
        this.sc = sc;
    }

    @Override
    public void execute() {
        // 기존에 만들었던 장바구니 메인 루프를 호출
        foodOrderController.openCartMenu(sc);
    }
}