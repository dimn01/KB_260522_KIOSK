import java.util.Scanner;

public class PcRoomSystem {
    
    // 1. 화면 지우기 메서드
    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 30; i++) System.out.println();
        }
    }

    // 2. 메시지 확인을 위한 잠깐 멈춤(1.5초) 메서드
    public static void delay() {
        try {
            Thread.sleep(1500); // 1500밀리초 = 1.5초 대기
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // 프로그램 제어를 위한 가상 상태 변수
        boolean isLoggedIn = false;
        boolean hasSeat = false;
        int remainingTime = 0; // 분 단위
        
        while (true) {
            // [1단계: 초기 화면 - 비로그인 상태]
            if (!isLoggedIn) {
                clearScreen(); // 화면 초기화
                System.out.println("======================================");
                System.out.println("         NEXUS PC방 관리 시스템");
                System.out.println("======================================");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("3. 프로그램 종료");
                System.out.println("======================================");
                System.out.print("원하는 기능의 번호를 입력하세요: ");
                
                int menu = sc.nextInt();
                if (menu == 1) {
                    System.out.println("\n✅ [로그인 성공! (테스트용으로 남은시간 0분 설정)]");
                    isLoggedIn = true; 
                    delay(); // 메시지 읽을 시간 1.5초 부여
                } else if (menu == 2) {
                    System.out.println("\n📝 [회원가입 진행: id, pw, 이름, 나이를 입력받아 DB에 삽입합니다.]");
                    delay();
                } else if (menu == 3) {
                    System.out.println("\n🛑 프로그램을 종료합니다.");
                    break;
                }
            } 
            // [2단계: 대기실 화면 - 로그인 성공 후 / 자리선택 전]
            else if (isLoggedIn && !hasSeat) {
                clearScreen(); // 화면 초기화
                System.out.println("======================================");
                System.out.println("       [ 대기실 ] 회원님 환영합니다!");
                System.out.println("       (남은 시간: " + (remainingTime / 60) + "시간 " + (remainingTime % 60) + "분)");
                System.out.println("======================================");
                System.out.println("1. 시간 충전하기");
                System.out.println("2. 빈 좌석 확인 및 사용 시작");
                System.out.println("3. 로그아웃 (초기 화면으로)");
                System.out.println("======================================");
                System.out.print("원하는 기능의 번호를 입력하세요: ");
                
                int menu = sc.nextInt();
                if (menu == 1) {
                    System.out.print("\n충전할 시간(분 단위)을 입력하세요: ");
                    int charge = sc.nextInt();
                    remainingTime += charge;
                    System.out.println("\n🔋 [" + charge + "분 충전이 완료되었습니다.]");
                    delay();
                } else if (menu == 2) {
                    if (remainingTime <= 0) {
                        System.out.println("\n❌ [경고] 충전된 시간이 없습니다! 먼저 시간을 충전해주세요.");
                        delay();
                    } else {
                        System.out.println("\n🖥️ [좌석 선택 완료! PC 이용을 시작합니다.]");
                        hasSeat = true;
                        delay();
                    }
                } else if (menu == 3) {
                    System.out.println("\n👋 [로그아웃 되었습니다.]");
                    isLoggedIn = false;
                    delay();
                }
            } 
            // [3단계: PC 이용 중 메인 화면 - 좌석 선택 완료]
            else if (isLoggedIn && hasSeat) {
                clearScreen(); // 화면 초기화
                System.out.println("======================================");
                System.out.println("       [ 05번 좌석 ] PC 이용 중...");
                System.out.println("       (남은 시간: " + (remainingTime / 60) + "시간 " + (remainingTime % 60) + "분)");
                System.out.println("======================================");
                System.out.println("1. 마이페이지 (내 정보 조회)");
                System.out.println("2. 음식 주문하기");
                System.out.println("3. 주문 내역 확인");
                System.out.println("4. PC 이용 종료 (사용 완료 및 좌석 비우기)");
                System.out.println("======================================");
                System.out.print("원하는 기능의 번호를 입력하세요: ");
                
                int menu = sc.nextInt();
                if (menu == 1) {
                    System.out.println("\n---------- [ 마이페이지 ] ----------");
                    System.out.println("ID: user01 | 이름: 홍길동 | 나이: 25세");
                    System.out.println("남은시간: " + remainingTime + "분");
                    System.out.println("----------------------------------");
                    System.out.println("\n[확인하셨으면 0번을 누르고 엔터를 치세요]");
                    sc.nextInt(); // 사용자가 확인할 때까지 대기
                } else if (menu == 2) {
                    // [4단계: 먹거리 주문 화면 (서브루프 진입)]
                    while (true) {
                        clearScreen(); // 서브 메뉴 진입 시 화면 초기화
                        System.out.println("======================================");
                        System.out.println("             [ 먹거리 주문 ]");
                        System.out.println("======================================");
                        System.out.println("1. 식사류 조회 (라면, 볶음밥 등)");
                        System.out.println("2. 음료류 조회 (콜라, 아이스커피 등)");
                        System.out.println("3. 장바구니 보기 및 결제");
                        System.out.println("0. 이전 메뉴로 돌아가기 (PC 메인으로)");
                        System.out.println("======================================");
                        System.out.print("원하는 기능의 번호를 입력하세요: ");
                        
                        int foodMenu = sc.nextInt();
                        if (foodMenu == 1) {
                            System.out.println("\n🍜 [식사류 목록을 DB에서 가져와 출력하고, 선택 시 장바구니에 담습니다.]");
                            delay();
                        } else if (foodMenu == 2) {
                            System.out.println("\n🥤 [음료류 목록을 DB에서 가져와 출력하고, 선택 시 장바구니에 담습니다.]");
                            delay();
                        } else if (foodMenu == 3) {
                            clearScreen(); // 결제 화면 전환 시 화면 초기화
                            System.out.println("--------------------------------------");
                            System.out.println("          [ 장바구니 내역 ]");
                            System.out.println("--------------------------------------");
                            System.out.println(" * 신라면  x 2개  :  6,000원");
                            System.out.println(" * 코카콜라 x 1개  :  2,000원");
                            System.out.println("--------------------------------------");
                            System.out.println(" 총 결제 금액      :  8,000원");
                            System.out.println("--------------------------------------");
                            System.out.println("1. 결제하기 (남은시간/포인트 차감)");
                            System.out.println("0. 취소하고 먹거리 메뉴로 돌아가기");
                            System.out.println("======================================");
                            System.out.print("원하는 기능의 번호를 입력하세요: ");
                            
                            int paySelect = sc.nextInt();
                            
                            if (paySelect == 1) {
                                System.out.println("\n💳 [결제 완료] 8,000원이 차감되었습니다.");
                                System.out.println("🔔 [알림] 주문이 주방으로 전송되었습니다. PC 메인화면으로 이동합니다.");
                                delay(); // 결제 완료 메시지 읽을 시간 부여
                                break; 
                            } else {
                                System.out.println("\n↩️ 결제를 취소했습니다. 먹거리 주문 메뉴로 돌아갑니다.");
                                delay();
                            }
                        } else if (foodMenu == 0) {
                            System.out.println("\n↩️ [PC 메인 화면으로 돌아갑니다.]");
                            delay();
                            break; 
                        }
                    }
                } else if (menu == 3) {
                    System.out.println("\n🧾 [DB에서 현재 회원의 주문 상세 내역을 JOIN하여 출력합니다.]");
                    System.out.println("\n[확인하셨으면 0번을 누르고 엔터를 치세요]");
                    sc.nextInt(); // 사용자가 확인할 때까지 대기
                } else if (menu == 4) {
                    System.out.println("\n🛑 [PC 이용을 종료합니다. 좌석을 비우고 초기 화면으로 이동합니다.]");
                    hasSeat = false;
                    isLoggedIn = false; 
                    delay();
                }
            }
        }
        sc.close();
    }
}