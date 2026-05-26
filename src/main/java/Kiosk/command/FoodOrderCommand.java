package Kiosk.command;

import Kiosk.controller.FoodOrderController;

import java.util.Scanner;

public class FoodOrderCommand implements Command {

    private final FoodOrderController foodOrderController;
    private final Scanner sc;

    // 생성자를 통해 컨트롤러와 스캐너를 주입받음
    public FoodOrderCommand(FoodOrderController foodOrderController, Scanner sc) {
        this.foodOrderController = foodOrderController;
        this.sc = sc;
    }

    @Override
    public void execute() {
        // 기존에 만들었던 음식 주문 메인 루프를 호출
        foodOrderController.openFoodOrderMenu(sc);
    }
}