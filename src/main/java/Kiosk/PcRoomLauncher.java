package Kiosk;

public class PcRoomLauncher {
    public static void main(String[] args) {
        // 자바 VM 레벨의 기본 인코딩 설정을 UTF-8로 강제 지정해 한글 깨짐 방지
        System.setProperty("file.encoding", "UTF-8");

        // PC방 감성 테마 Swing 래퍼 윈도우 생성
        PcRoomTerminalFrame frame = new PcRoomTerminalFrame();
        
        // 래퍼 창이 표시되면 백그라운드 스레드에서 기존 kioskController 실행
        frame.startApplication(() -> {
            try {
                kioskController.main(args);
            } catch (Exception e) {
                System.err.println("[오류] 키오스크 실행 중 문제가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.exit(0); // 기존 코드 수정 없이 백그라운드 스레드 종료 시 메인 윈도우 및 JVM 전체 종료
            }
        });
    }
}
